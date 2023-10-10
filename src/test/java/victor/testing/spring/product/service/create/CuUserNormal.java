package victor.testing.spring.product.service.create;

import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@WithMockUser(username = "jdoe", roles = {"USER"})
@Retention(RUNTIME) // stops javac from removing it at compilation
public @interface CuUserNormal {
}
