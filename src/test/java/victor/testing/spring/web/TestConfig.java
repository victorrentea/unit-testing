//package victor.testing.spring.web;
//
//import org.mockito.Mockito;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//
//@Profile("mysql.real")
//@Retention(RetentionPolicy.RUNTIME)
//@interface MySqlReal {
//
//}
//
//@Configuration
//@Profile("setup1")
//public class TestConfig {
//   @Bean
//   public A mock1() {
//      return Mockito.mock(A.class);
//   }
//   @Bean
//   public B mock1() {
//      return Mockito.mock(B.class);
//   }
//   @Bean
//   public A mock1() {
//      return Mockito.mock(A.class);
//   }
//   @Bean
//   public A mock1() {
//      return Mockito.mock(A.class);
//   }
//}
////
////
////A B C
////
////
////A B
////B C
////C
//@Configuration
//@Profile("kafka.real")
//class KafkaConfig{
//
//}
//
//@ActiveProfiles({"kafka.real", "h2", "smth.else"})
//class TestClass1 {
//
//}
//@ActiveProfiles({"kafka.fake", "h2", "smth.else"})
//class TestClass2 {
//
//}
//@ActiveProfiles({"kafka.fake", "mysql", "smth.else"})
//class TestClass3 {
//
//}