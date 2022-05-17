package org.mockito.configuration.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.configuration.DefaultErrorSignalException;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import reactor.core.publisher.Mono;
import reactor.test.publisher.PublisherProbe;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ReactiveMockito {
    private static final Answer<Object> EMITS_ERROR = i -> Mono.error(new RuntimeException("ERROR emitted by default"));
    @Mock
    Dependency dependency;
    @InjectMocks
    TestedCode testedCode;

    @Test
    void noLaunchInPeace() {
        testedCode.peace().block();

        Mockito.verifyNoMoreInteractions(dependency);
    }

    @Test
    void launchInWar_not_stubbed() {
        assertThatThrownBy(() -> testedCode.war().block())
                .isInstanceOf(DefaultErrorSignalException.class);
    }

    @Test
    void launchInWar_stubbed() {
        PublisherProbe<Void> probe = PublisherProbe.empty();
        when(dependency.launchMissile()).thenReturn(probe.mono());

        testedCode.war().block();

        probe.assertWasSubscribed();
    }
}

interface Dependency {
    Mono<Void> launchMissile();
}

class TestedCode {
    private final Dependency dependency;

    TestedCode(Dependency dependency) {
        this.dependency = dependency;
    }

    public Mono<Void> peace() {
        return Mono.empty();
    }

    public Mono<Void> war() {
        return dependency.launchMissile();
    }
}