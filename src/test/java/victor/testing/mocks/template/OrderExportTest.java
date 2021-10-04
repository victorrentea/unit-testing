package victor.testing.mocks.template;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.time.Order;
import victor.testing.time.OrderRepo;

import java.util.List;

import static java.time.LocalDate.parse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderExportTest {
   @Mock
   OrderRepo orderRepo;

   @Mock
   EmailSender emailSender; // unused mock (aka 'dummy') = code smell

   @Spy // partial mock = code smell => missing abstraction
   @InjectMocks
   OrderExport orderExport;

   @Captor
   ArgumentCaptor<List<Object>> listCaptor;

   @Test
   void writeContent() {
      // [[HARD LEVEL]]
      Order order = new Order().setId(13L).setCreatedOn(parse("2021-09-01"));
      when(orderRepo.findByActiveTrue()).thenReturn(List.of(order));
      doNothing().when(orderExport).writeLine(anyList());

      orderExport.writeContent();

      verify(orderExport,atLeastOnce()).writeLine(listCaptor.capture());
      List<Object> line1 = listCaptor.getAllValues().get(1);
      assertThat(line1).containsExactly(13L, "01 Sep 2021");
   }
}