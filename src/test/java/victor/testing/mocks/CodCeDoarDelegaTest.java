package victor.testing.mocks;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CodCeDoarDelegaTest {
    @Mock
    A mockA;
    @Mock
    B mockB;
    @InjectMocks
    CodCeDoarDelega target;

    @Test // scrie-l la inceput. Scrie-l ca sa inveti mockuri. Scrie 100 din astea.
    // apoi, vine "the awakening" phase: vei realiza ca de fapt testul asta acopera prea putin risc.
    // vei transforma aceste teste in teste mai 'ample'. Adica poate vei testa fct ta impreuna cu met1 si met2()
    void deTestat() {
        // given

        // when (apel de prod)
        target.deTestat();

        // then (assert+verificari)
        Mockito.verify(mockA).met1();
        Mockito.verify(mockB).met2();
    }
}