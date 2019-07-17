package ro.victor.unittest.tdd.outsidein;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static java.util.Arrays.asList;
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
    public void store_a_deposit_transaction() {
        account.deposit(100);
        verify(transactionRepository).addDeposit(100);
    }
    @Test
    public void store_a_withdrawal_transaction() {
        account.withdraw(100);
        verify(transactionRepository).addWithdrawal(100);
    }
    @Test
    public void print_a_statement() {
        List<Transaction> transactions = asList(new Transaction());
        when(transactionRepository.allTransactions()).thenReturn(transactions);
        verify(statementPrinter).print(transactions);

    }
}
