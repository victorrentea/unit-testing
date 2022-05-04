package victor.testing.tools;

import lombok.Data;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.tools.InjectRealObjectsMockitoExtension.Real;

@ExtendWith(MockitoExtension.class)
public class MockitoExtensionTest {
    @InjectMocks
    A a;
    @Mock
    B b;
    @Real
    C c = new C();
    
    @Test
    void test() {
        Assertions.assertThat(a.getB()).isNotNull();
        Assertions.assertThat(a.getC()).isNotNull();
    }


    @Data
    public static  class A {
        private final B b;
        private final C c;
    }
    public static class B {}
    public static class C {}
}
