package app.exception;

import app.web.dto.LoginRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(InvalidUsernameOrPasswordException.class)
  public String handleInvalidUsernameOrPasswordException(InvalidUsernameOrPasswordException ex, Model model) {

    model.addAttribute("loginAttemptMessage", ex.getMessage());
    model.addAttribute("loginDto", new LoginRequest());

    return "login";
  }
}
