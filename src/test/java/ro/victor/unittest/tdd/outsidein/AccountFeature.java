package ro.victor.unittest.tdd.outsidein;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AccountFeature {
    @Mock
    private Console console;
    @Mock
    private Clock clock;


    private Account account;

    @Before
    public void initialize() {
        TransactionRepository transactionRepository = new TransactionRepository(clock);
        StatementPrinter statementPrinter = new StatementPrinter(console);
        account = new Account(transactionRepository, statementPrinter);

    }
    @Test
    public void acceptance() {
        when(clock.getDateAsString()).thenReturn(
                "01/04/2014",
                "02/04/2014",
                "10/04/2014"
        );
        account.deposit(1000);
        account.withdraw(100);
        account.deposit(500);

        account.printStatement();

        verify(console).printLine("DATE       | AMOUNT  | BALANCE");
        verify(console).printLine("10/04/2014 | 500.00 | 1400.00");
        verify(console).printLine("02/04/2014 | -100.00 | 900.00");
        verify(console).printLine("01/04/2014 | 1000.00 | 1000.00");

    }
}
