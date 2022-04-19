package victor.testing.spring.hack;

import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;

@Service
public class StatefulSingleton {
   private int state = 1;

   public int getAndInc() {
      return state++;
   }

   @PreDestroy
   public void goodBye() {
      System.out.println("Say goodbye to "+ this);
   }
}

