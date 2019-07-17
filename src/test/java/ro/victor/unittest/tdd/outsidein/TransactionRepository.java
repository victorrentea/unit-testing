package ro.victor.unittest.tdd.outsidein;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

public class TransactionRepository {
    private final Clock clock;
    private List<Transaction> transactions = new ArrayList<>();

    public TransactionRepository(Clock clock) {
        this.clock = clock;
    }

    public void addDeposit(int amount) {
        transactions.add(new Transaction(clock.getDateAsString(), amount));
    }

    public void addWithdrawal(int amount) {
        int negativeAmount = -amount;
        transactions.add(new Transaction(clock.getDateAsString(), negativeAmount));
    }

    public List<Transaction> getAllTransactions() {
        return unmodifiableList(transactions);
    }
}
