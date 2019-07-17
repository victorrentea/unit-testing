package ro.victor.unittest.tdd.outsidein;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class TransactionRepositoryShould {


    private TransactionRepository transactionRepository;

    @Before
    public void initialize() {
        transactionRepository = new TransactionRepository();
    }

    @Test
    public void creates_and_stores_a_deposit_transaction() {
        transactionRepository.addDeposit(100);

        List<Transaction> transactions = transactionRepository.allTransactions();

        assertThat(transactions.size(), is(1));
        assertThat(transactions.get(0), is(transaction("01/01/2019", 100)));
    }

    private Transaction transaction(String date, int amount) {
        return null;
    }
}