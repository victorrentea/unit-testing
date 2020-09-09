package ro.victor.unittest.spring.fixture;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Data
@Entity
public class Post {
   @Id
   @GeneratedValue
   private Long id;
   private String title;
   private String body;
   @ManyToOne
   private User createdBy;
}
