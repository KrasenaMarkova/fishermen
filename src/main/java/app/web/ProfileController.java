package app.web;

import app.fisherman.model.Fisherman;
import app.fisherman.service.FishermanService;
import app.web.dto.DtoMapper;
import app.web.dto.EditProfileRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ProfileController {

  private final FishermanService fishermanService;

  public ProfileController(FishermanService fishermanService) {
    this.fishermanService = fishermanService;
  }

  @GetMapping("/edit-profile")
  public ModelAndView profile(HttpSession session) {

    ModelAndView modelAndView = new ModelAndView("edit-profile");

    UUID fishermanId = (UUID) session.getAttribute("user_id");
    Fisherman fisherman = fishermanService.getById(fishermanId);

    EditProfileRequest editProfileRequest = DtoMapper.fromFisherman(fisherman);

    modelAndView.addObject("editProfileRequest", editProfileRequest);
    modelAndView.addObject("fisherman", fisherman);

    return modelAndView;
  }

  @PutMapping("/edit-profile")
  public ModelAndView updateProfile(@Valid @ModelAttribute("editProfileRequest") EditProfileRequest editProfileRequest,
      BindingResult bindingResult, HttpSession session) {

    if (bindingResult.hasErrors()) {
      ModelAndView modelAndView = new ModelAndView("edit-profile");
      modelAndView.addObject("editProfileRequest", editProfileRequest);

      UUID fishermanId = (UUID) session.getAttribute("user_id");
      Fisherman fisherman = fishermanService.getById(fishermanId);
      modelAndView.addObject("fisherman", fisherman);
      return modelAndView;
    }

    UUID fishermanId = (UUID) session.getAttribute("user_id");
    fishermanService.update(fishermanId, editProfileRequest);

    return new ModelAndView("redirect:/home");
  }
}
