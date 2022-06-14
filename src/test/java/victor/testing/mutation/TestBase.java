package victor.testing.mutation;

import org.junit.jupiter.api.BeforeEach;

import java.util.UUID;

public abstract class TestBase {
    protected String username;
    @BeforeEach
    final void beforeDinSuper() {
        username = UUID.randomUUID().toString();
        System.out.println("#sieu cu userul "  + username);
    }
}
