package ro.victor.unittest.tdd.outsidein;

import java.util.Objects;

public class Transaction {
    private final String dateStr;
    private final int amount;

    public Transaction(String dateStr, int amount) {
        this.dateStr = dateStr;
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return amount == that.amount &&
                Objects.equals(dateStr, that.dateStr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateStr, amount);
    }
}
