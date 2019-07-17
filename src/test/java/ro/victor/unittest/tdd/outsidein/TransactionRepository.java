package ro.victor.unittest.tdd.outsidein;

import java.util.List;

public class TransactionRepository {
    public void addDeposit(int amount) {
        throw new UnsupportedOperationException();
    }

    public void addWithdrawal(int amount) {
        throw new UnsupportedOperationException();
    }

    public List<Transaction> allTransactions() {
        throw new UnsupportedOperationException();
    }
}
