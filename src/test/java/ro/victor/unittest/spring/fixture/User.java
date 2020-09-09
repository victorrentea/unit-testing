package ro.victor.unittest.spring.fixture;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data // in real life, don't on @Entities
public class User {
   @Id
   @GeneratedValue
   private Long id;
   private String username;
}
