package app.fish.service;

import app.fish.model.Fish;
import app.fish.property.FishProperties;
import app.fish.property.FishProperties.FishesData;
import app.fish.repository.FishRepository;
import app.fisherman.model.Fisherman;
import app.fisherman.service.FishermanService;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class FishService {

  private final FishRepository fishRepository;
  private final FishProperties fishProperties;
  private final FishermanService fishermanService;
  private final List<FishesData> availableFishes = new ArrayList<>();

  public FishService(FishRepository fishRepository, FishProperties fishProperties, FishermanService fishermanService) {
    this.fishRepository = fishRepository;
    this.fishProperties = fishProperties;
    this.fishermanService = fishermanService;
  }

  @PostConstruct
  public void init() {
    availableFishes.addAll(fishProperties.getFishes());
  }

  public FishProperties.FishesData pickRandomFish() {
    if (availableFishes.isEmpty()) {
      return null;
    }
    int randomIndex = new Random().nextInt(availableFishes.size());
    return availableFishes.get(randomIndex);
  }

  public void takeFish(FishProperties.FishesData fishData, UUID fishermanId) {

    Fisherman fisherman = fishermanService.getById(fishermanId);

    Fish fish = Fish.builder()
        .species(fishData.getSpecies())
        .weight(fishData.getWeight())
        .type(fishData.getType())
        .imageURL(fishData.getImageUrl())
        .price(fishData.getPrice())
        .forSale(false)
        .owner(fisherman)
        .build();

    fisherman.getFishes().add(fish);
    fishRepository.save(fish);

    availableFishes.remove(fishData);
  }

  public List<FishProperties.FishesData> getAvailableFishes() {
    return Collections.unmodifiableList(availableFishes);
  }

  public void putFishForSale(UUID fishId, UUID fishermanId) {
    Fish fish = fishRepository.findById(fishId)
        .orElseThrow(() -> new RuntimeException("Fish not found"));

    if (!fish.getOwner().getId().equals(fishermanId)) {
      throw new RuntimeException("You cannot sell a fish that is not yours");
    }

    if (fish.isForSale()) {
      throw new RuntimeException("Fish is already offered for sale");
    }

    fish.setForSale(true);
    fishRepository.save(fish);
  }

  public List<Fish> getFishesForSale() {
    return fishRepository.findAll().stream()
        .filter(Fish::isForSale)
        .toList();
  }

  public void buyFish(UUID fishId, UUID buyerId) {
    Fish fish = fishRepository.findById(fishId)
        .orElseThrow(() -> new RuntimeException("Fish not found"));

    Fisherman buyer = fishermanService.getById(buyerId);
    Fisherman seller = fish.getOwner();

    if (seller.getId().equals(buyerId)) {
      throw new RuntimeException("You cannot buy your own fish");
    }

    seller.getFishes().remove(fish);

    fish.setOwner(buyer);
    fish.setForSale(false);
    buyer.getFishes().add(fish);

    fishRepository.save(fish);
  }
}
