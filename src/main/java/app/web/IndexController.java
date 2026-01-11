package app.web;

import app.fish.model.Fish;
import app.fish.service.FishService;
import app.fisherman.enums.Experience;
import app.fisherman.model.Fisherman;
import app.fisherman.service.FishermanService;
import app.web.dto.LoginRequest;
import app.web.dto.RegisterRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

  private final FishService fishService;
  private final FishermanService fishermanService;

  public IndexController(FishService fishService, FishermanService fishermanService) {
    this.fishService = fishService;
    this.fishermanService = fishermanService;
  }

  @GetMapping
  public String index() {
    return "index";
  }

  @GetMapping("/register")
  public ModelAndView getRegisterPage() {

    ModelAndView modelAndView = new ModelAndView();
    modelAndView.setViewName("register");
    modelAndView.addObject("registerRequest", new RegisterRequest());
    modelAndView.addObject("experience", Experience.values());

    return modelAndView;
  }

  @PostMapping("/register")
  public ModelAndView register(@Valid RegisterRequest registerRequest, BindingResult bindingResult) {

    ModelAndView modelAndView = new ModelAndView();

    if (bindingResult.hasErrors()) {
      modelAndView.addObject("registerRequest", registerRequest);
      modelAndView.addObject("experience", Experience.values());
      modelAndView.setViewName("register"); // задаваме view
      return modelAndView;
    }

    fishermanService.createNewFisherman(registerRequest);

    return new ModelAndView("redirect:/login");
  }

  @GetMapping("/login")
  public ModelAndView getLoginPage() {

    ModelAndView modelAndView = new ModelAndView();
    modelAndView.setViewName("login");

    modelAndView.addObject("loginRequest", new LoginRequest());

    return modelAndView;
  }

  @PostMapping("/login")
  public ModelAndView login(LoginRequest loginRequest, HttpSession session) {

    Fisherman fisherman = fishermanService.login(loginRequest);
    session.setAttribute("user_id", fisherman.getId());

    return new ModelAndView("redirect:/home");
  }

  @GetMapping("/home")
  public ModelAndView getHomePage(HttpSession session) {

    UUID fishermanId = (UUID) session.getAttribute("user_id");
    Fisherman fisherman = fishermanService.getById(fishermanId);

    List<Fish> fishesForSale = fishService.getFishesForSale();

    ModelAndView modelAndView = new ModelAndView();
    modelAndView.setViewName("home");
    modelAndView.addObject("fisherman", fisherman);
    modelAndView.addObject("fishesForSale", fishesForSale);

    return modelAndView;
  }

  @GetMapping("/logout")
  public String logout(HttpSession session) {

    session.invalidate();
    return "redirect:/";
  }
}
