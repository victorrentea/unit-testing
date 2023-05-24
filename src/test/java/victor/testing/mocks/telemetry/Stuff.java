package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class Stuff {
//  @Mock
//  String s
    /*= new String() {
    @Override
    public boolean startsWith(String prefix) {
      return true;
    }
  }*/;
//  @Test
//  void explore() {
//    when(s.startsWith(anyString())).thenReturn(true);
//
//    System.out.println(s.startsWith("コードを書く ジャバは強力だ オブジェクト指向"));
//  }
  @Mock
  StapaneFraeruInsista mock;
  @Test
  void explore() {
    when(mock.mockThis()).thenReturn(false);

    System.out.println(mock.mockThis());
  }

}

final class StapaneFraeruInsista {
  // add 'mockito-inline' dependency to your project
  // to unlock hard-core shit: mocking statics, finals,
  public boolean mockThis() {
    return true;
  }
}