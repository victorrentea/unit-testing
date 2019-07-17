package ro.victor.unittest.tdd.outsidein;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PrintStatementFeature {
//    Given a client makes a deposit of 1000 on 10-01-2019
//    And a withdrawal of 100 on 13-01-2019
//    And a deposit of 500 on 14-01-2019
//    When she prints her bank statement
//    Then she would see

//    date       | amount | balance
//    14/01/2019 | 500.00 | 1400.00
//    13/01/2019 | -100.00 | 900.00
//    10/01/2019 | 1000.00 | 1000.00

    @Mock
    private Console console;
    private Account account;

    @Before
    public void initialize() {
        TransactionRepository transactionRepository = new TransactionRepository();
        StatementPrinter statementPrinter = new StatementPrinter();
        account = new Account(transactionRepository, statementPrinter);
    }

    @Test
    public void print_statement_containing_all_transactions() {

        account.deposit(1000);
        account.withdraw(100);
        account.deposit(500);
        account.printStatement();

        verify(console).printLine("date       | amount | balance");
        verify(console).printLine("14/01/2019 | 500.00 | 1400.00");
        verify(console).printLine("13/01/2019 | -100.00 | 900.00");
        verify(console).printLine("10/01/2019 | 1000.00 | 1000.00");
    }


}
