package victor.testing.tdd.outsidein;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountShould {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private StatementPrinter statementPrinter;
    private Account account;

    @Before
    public void initialize() {
        account = new Account(transactionRepository, statementPrinter);
    }

    @Test
    public void persist_a_deposit() {
        account.deposit(100);
        verify(transactionRepository).addDeposit(100);
    }
    @Test
    public void persist_a_withdrawal() {
        account.withdraw(99);
        verify(transactionRepository).addWithdrawal(99);
    }

    @Test
    public void print_statement() {
        List<Transaction> transactionList = Collections.emptyList();
        when(transactionRepository.getAllTransactions()).thenReturn(transactionList);

        account.printStatement();
        verify(statementPrinter).print(transactionList);
    }

}
