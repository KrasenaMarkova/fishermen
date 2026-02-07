package app.web;

import app.fish.property.FishProperties;
import app.fish.service.FishService;
import jakarta.servlet.http.HttpSession;
import java.util.UUID;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FishController {

  private final FishService fishService;

  public FishController(FishService fishService) {
    this.fishService = fishService;
  }

  @GetMapping("/fishing-spot")
  public ModelAndView fishingSpot(HttpSession session) {

    FishProperties.FishesData randomFish = fishService.pickRandomFish();

    if (randomFish == null) {
      return new ModelAndView("redirect:/home"); // няма повече риби
    }

    ModelAndView modelAndView = new ModelAndView("fishing-spot");
    modelAndView.addObject("fish", randomFish);
    return modelAndView;
  }

  @PostMapping("/fishing-spot")
  public ModelAndView takeFish(@RequestParam String species, HttpSession session) {

    UUID fishermanId = (UUID) session.getAttribute("user_id");

    FishProperties.FishesData fishToTake = fishService.getAvailableFishes().stream()
        .filter(f -> f.getSpecies().equals(species))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Fish not available"));

    fishService.takeFish(fishToTake, fishermanId);
    return new ModelAndView("redirect:/home");
  }

  @PostMapping("/fish/for-sale")
  public ModelAndView putFishForSale(@RequestParam UUID fishId, HttpSession session) {

    UUID fishermanId = (UUID) session.getAttribute("user_id");

    fishService.putFishForSale(fishId, fishermanId);

    return new ModelAndView("redirect:/home");
  }

  @PostMapping("/fish/buy")
  public ModelAndView buyFish(@RequestParam UUID fishId, HttpSession session) {

    UUID buyerId = (UUID) session.getAttribute("user_id");

    fishService.buyFish(fishId, buyerId);

    return new ModelAndView("redirect:/home");
  }
}
