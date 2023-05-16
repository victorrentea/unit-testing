package victor.testing.tricks;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

@SpringBootTest
public class ClockInSpringContextTest {
   @Autowired
   private CacheManager cacheManager;

   @BeforeEach
   public void initialize() {
      // Clear manually all caches
      cacheManager.getCacheNames().stream().map(cacheManager::getCache).forEach(Cache::clear);
   }

   @TestConfiguration
   public static class TestConfig {
      @Bean
      @Primary
      public Clock clockFixed() {
         return Clock.fixed(Instant.parse("2014-12-22T10:15:30.00Z"), ZoneId.systemDefault());
      }
   }


   // TODO Fixed Time
   // @TestConfiguration public static class ClockConfig {  @Bean  @Primary  public Clock fixedClock() {}}

   // TODO Variable Time
   // when(clock.instant()).thenAnswer(call -> currentTime.toInstant(ZoneId.systemDefault().getRules().getOffset(currentTime)));
   // when(clock.getZone()).thenReturn(ZoneId.systemDefault());
}
