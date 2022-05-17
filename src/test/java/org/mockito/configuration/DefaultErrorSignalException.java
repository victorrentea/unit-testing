package org.mockito.configuration;

/**
 * Emitted as error signal by any method returning a {@link org.reactivestreams.Publisher} of a Mockito mock,
 * unless the method is explicitly stubbed, ie. when(...).thenReturn(...)
 */
public class DefaultErrorSignalException extends RuntimeException {
    public DefaultErrorSignalException(String methodName) {
        super("Method '" + methodName + "' was not stubbed and it emitted an error signal with this exception when subscribed");
    }
}
