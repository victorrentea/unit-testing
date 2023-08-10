package victor.testing.spring.product.service.create;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@SpringBootTest
@Transactional
//@ExtendWith() // junit extensions
@Retention(RUNTIME) // stops javac from removing it at compilation
public @interface VictorsTest {

}
