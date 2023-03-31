package victor.testing.design.fixturecreep.strictstubs;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.quality.Strictness;

import java.util.List;

import static org.mockito.Mockito.*;

@Disabled("all are supposed to fail :)")
public class StrictStubsTest {

  @Nested
  @ExtendWith(MockitoExtension.class)
  class WithMockAnnotation {
    @Mock
    List<String> mockList;
    @Test
    void unusedStubbing() {
        when(mockList.get(0)).thenReturn("a");
    }
  }


  @Nested
  @ExtendWith(StrictMockitoExtension.class)
  class WithCustomExtension {
    List<String> mockList = mock(List.class);
    @Test
    void unusedStubbing() {
        when(mockList.get(0)).thenReturn("a");
    }
  }

  @Nested
  @ExtendWith(MockitoExtension.class)
  class WithMockito_v4_6 {
    List<String> mockList;

    @BeforeEach
    void setUp() {
      mockList = mock(List.class, withSettings().strictness(Strictness.STRICT_STUBS));
    }

    @Test
    void unusedStubbing() {
        when(mockList.get(0)).thenReturn("a");
    }
  }



  @Nested
  class WithMockSession {
    List<String> mockList;
    MockitoSession mockitoSession;

    @BeforeEach
    final void init() {
      mockitoSession = mockitoSession().strictness(Strictness.STRICT_STUBS).startMocking();
      mockList = mock(List.class); // .mock has to be called after MockitoSession.startMocking()
    }
    @AfterEach
    public void closeMockitoSession() {
        mockitoSession.finishMocking();
    }
    @Test
    void unusedStubbing() {
        when(mockList.get(0)).thenReturn("a");
    }
  }
}
