package app.fisherman.service;

import app.exception.InvalidUsernameOrPasswordException;
import app.fisherman.model.Fisherman;
import app.fisherman.repository.FishermanRepository;
import app.web.dto.EditProfileRequest;
import app.web.dto.LoginRequest;
import app.web.dto.RegisterRequest;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class FishermanService {

  private final FishermanRepository fishermanRepository;
  private final PasswordEncoder passwordEncoder;

  public FishermanService(FishermanRepository fishermanRepository, PasswordEncoder passwordEncoder) {
    this.fishermanRepository = fishermanRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public void createNewFisherman(RegisterRequest registerRequest) {

    Fisherman fisherman = Fisherman.builder()
        .username(registerRequest.getUsername())
        .password(passwordEncoder.encode(registerRequest.getPassword()))
        .nickname(registerRequest.getNickname())
        .experience(registerRequest.getExperience())
        .createdOn(LocalDateTime.now())
        .updatedOn(LocalDateTime.now())
        .build();

    fishermanRepository.save(fisherman);
  }

  public Fisherman login(LoginRequest loginRequest) {

    Fisherman fisherman = fishermanRepository.findByUsername(loginRequest.getUsername())
        .orElseThrow(() -> new InvalidUsernameOrPasswordException("User with username %s does not exist".formatted(loginRequest.getUsername())));

    String rawPassword = loginRequest.getPassword();
    String encodedPassword = fisherman.getPassword();

    if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
      throw new InvalidUsernameOrPasswordException("Username or password are incorrect");
    }

    return fisherman;
  }

  public Fisherman getById(UUID fishermanId) {
    return fishermanRepository.findById(fishermanId).orElseThrow(() -> new RuntimeException("Player not found."));
  }

  public void update(UUID fishermanId, EditProfileRequest editProfileRequest) {

    Fisherman fisherman = getById(fishermanId);

    fisherman.setUsername(editProfileRequest.getUsername());
    fisherman.setNickname(editProfileRequest.getNickname());

    if (editProfileRequest.getPassword() != null &&
        !editProfileRequest.getPassword().isBlank()) {

      if (editProfileRequest.getPassword().length() < 4 ||
          editProfileRequest.getPassword().length() > 10) {
        throw new RuntimeException("Password must be between 4 and 10 characters!");
      }

      fisherman.setPassword(passwordEncoder.encode(editProfileRequest.getPassword()));
    }

    fisherman.setImageUrl(editProfileRequest.getImageUrl());
    fisherman.setUpdatedOn(LocalDateTime.now());

    fishermanRepository.save(fisherman);
  }
}
