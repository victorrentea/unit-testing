package victor.testing.tdd.outsidein;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TransactionRepositoryShould {

    @InjectMocks
    private TransactionRepository repo;
    @Mock
    private Clock clock;

    @Before
    public void initialize() {
        when(clock.getDateAsString()).thenReturn("17/07/2019");
    }

    @Test
    public void returns_created_deposit() {
        repo.addDeposit(100);
        List<Transaction> transactions = repo.getAllTransactions();
        assertEquals(Collections.singletonList(new Transaction("17/07/2019", 100)), transactions);
    }
    @Test
    public void returns_created_withdrawal() {
        repo.addWithdrawal(100);
        List<Transaction> transactions = repo.getAllTransactions();
        assertEquals(Collections.singletonList(new Transaction("17/07/2019", -100)), transactions);
    }
}
