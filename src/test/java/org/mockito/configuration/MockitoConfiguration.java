package org.mockito.configuration;

import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// configures mockito to expo
public class MockitoConfiguration extends DefaultMockitoConfiguration{
   @Override
   public Answer<Object> getDefaultAnswer() {
      return new ReturnsEmptyValues() {
         @Override
         public Object answer(InvocationOnMock invocation) {
            if (Mono.class.isAssignableFrom(invocation.getMethod().getReturnType())) {
               return Mono.error(new RuntimeException("DEFAULT EMIT ERROR"));
            }
            if (Flux.class.isAssignableFrom(invocation.getMethod().getReturnType())) {
               return Flux.error(new RuntimeException("DEFAULT EMIT ERROR"));
            }
            return super.answer(invocation);
         }
      };
   }
}
