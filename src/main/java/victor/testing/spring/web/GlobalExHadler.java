package victor.testing.spring.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExHadler {

   @ExceptionHandler
   @ResponseStatus(HttpStatus.INSUFFICIENT_STORAGE)
   public String buba(IllegalStateException e) {
      return "buba";
   }
}
