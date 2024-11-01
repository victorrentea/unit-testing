package victor.testing.spring.service;

import org.springframework.stereotype.Component;

import java.security.Principal;

@Component // "testPrincipals"
public class TestPrincipals {
  public Principal bo$$() {
    return new Principal() {
      @Override
      public String getName() {
        return "BO$$";
      }
    };
  }
}
