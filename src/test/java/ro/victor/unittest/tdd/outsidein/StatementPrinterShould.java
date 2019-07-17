package ro.victor.unittest.tdd.outsidein;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class StatementPrinterShould {

    @Mock
    private Console console;
    private StatementPrinter statementPrinter;

    @Before
    public void initialize() {
        statementPrinter = new StatementPrinter(console);
    }
    @Test
    public void print_transactions_in_reverse_order() {
        List<Transaction> transactions = Arrays.asList(
            new Transaction("01/04/2014", 1000),
            new Transaction("02/04/2014", -100),
            new Transaction("10/04/2014", 500)
        );
        statementPrinter.print(transactions);

        verify(console).printLine("DATE       | AMOUNT  | BALANCE");
        verify(console).printLine("10/04/2014 | 500.00 | 1400.00");
        verify(console).printLine("02/04/2014 | -100.00 | 900.00");
        verify(console).printLine("01/04/2014 | 1000.00 | 1000.00");
    }
}
