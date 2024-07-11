//package victor.testing;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.web.client.RestTemplate;
//import scala.concurrent.impl.FutureConvertersImpl.CF;
//
//import java.time.DayOfWeek;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.Set;
//import java.util.UUID;
//
//import static java.time.DayOfWeek.SATURDAY;
//import static java.time.DayOfWeek.SUNDAY;
//
//@ExtendWith(MockitoExtension.class)
//public class MocksPuzzle {
//  interface D {
//    int g(String x);
//    void sendWarning(String message);
//  }
//  @Mock
//  D d;
//
//  int e1() { //when
//    return 1 + d.g("x");
//  }
//  void e3(String message) { // verify, captor; change param, da null, repeta x 2
//    Message customer = new Message();
//    customer.setText(message);
//    customer.setSentAt(LocalDateTime.now()); // is close by
//    messageRepo.save(customer);
//  }
//  void e4(String message) { // any, eq,  static mock
//    if (Set.of(SATURDAY, SUNDAY).contains(LocalDate.now().getDayOfWeek())) {
//      return; // verify(no interaction)
//    }
//    d.sendNotification(message, LocalDateTime.now());
//  }
//  void e4b(String city) { // any;
//    LocalTime time = d.fetchTime(new TimeRequest(city, "api-key"));
//    if (time.isAfter(LocalTime.parse("20:00")) || time.isBefore(LocalTime.parse("8:00")))
//      sendNotification(message);
//  }
//
//  String[] e5() { // ceva ce da REST calls
//    return new RestTemplate().getForObject(baseUrl+"/user/{}", UserDto.class);
//  }
//  void e6(String name) {
//    repo.find.... "SELECT * FROM customer WHERE name LIKE '%' || "+name+" || '%'"
//      // dynamic query
//      repo.findByNameContainsIgnoringCase(...)
//  }
//
//  void e7() {
//    WEB @validated, role, rute, dto
//  }
//
//  void e8() {
//    CF.runAsync(()->{});
//  }
//
//
//}
