package victor.testing.spring.web;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
   @ExceptionHandler(Exception.class)
   @ResponseStatus
   public String translate(Exception e) {
      return e.getMessage();
   }
}
