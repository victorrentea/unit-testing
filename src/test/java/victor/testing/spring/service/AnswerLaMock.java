package victor.testing.spring.service;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.List;
import java.util.Random;

public class AnswerLaMock {
  public static void main(String[] args) {
    List listMock = Mockito.mock(List.class);

//    Mockito.when(listMock.size()).thenReturn(1000);
    Mockito.when(listMock.size()).thenAnswer(
        invocation -> new Random().nextInt());
    System.out.println(listMock.size());
    System.out.println(listMock.size());
    System.out.println(listMock.size());
    System.out.println(listMock.size());
  }
}
