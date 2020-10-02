package victor.testing.tdd.outsidein;

import java.text.DecimalFormat;
import java.util.*;

public class StatementPrinter {
    public static final DecimalFormat FORMAT = new DecimalFormat("#.00");
    private final Console console;

    public StatementPrinter(Console console) {
        this.console = console;
    }
    private static class TransactionWithTotal {
        public final Transaction transaction;
        public final int total;

        private TransactionWithTotal(Transaction transaction, int total) {
            this.transaction = transaction;
            this.total = total;
        }
    }

    public void print(List<Transaction> transactionList) {
        createStatementHeader();
        createStatementBody(transactionList);
    }

    private void createStatementBody(List<Transaction> transactionList) {
        List<TransactionWithTotal> list = new ArrayList<>();
        int sold = 0;
        for (Transaction transaction : transactionList) {
            sold += transaction.getAmount();
            list.add(new TransactionWithTotal(transaction, sold));
        }
        Collections.reverse(list);

        for (TransactionWithTotal transaction : list) {
            console.printLine(formatLine(transaction));
        }
    }

    private void createStatementHeader() {
        console.printLine("DATE       | AMOUNT  | BALANCE");
    }

    private String formatLine(TransactionWithTotal transaction) {
        return transaction.transaction.getDateStr()  +
                " | " +
                FORMAT.format(transaction.transaction.getAmount()) +
                " | " +
                FORMAT.format(transaction.total);
    }
}
