package org.mockito.configuration;

import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// this class has to be exactly in this package, as Mockito looks for this classname at startup automatically: org.mockito.configuration.MockitoConfiguration
public class MockitoConfiguration extends DefaultMockitoConfiguration{
   @Override
   public Answer<Object> getDefaultAnswer() {
      return new ReturnsEmptyValues() {
         @SuppressWarnings("ReactiveStreamsUnusedPublisher")
         @Override
         public Object answer(InvocationOnMock invocation) {
            // by default mocks returning Mono/Flux should emit an error instead of throwing it, for any method not stubbed.
            if (Mono.class.isAssignableFrom(invocation.getMethod().getReturnType())) {
               return Mono.error(new DefaultErrorSignalException());
            }
            if (Flux.class.isAssignableFrom(invocation.getMethod().getReturnType())) {
               return Flux.error(new DefaultErrorSignalException());
            }
            return super.answer(invocation);
         }
      };
   }
}
