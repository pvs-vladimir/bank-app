package bank;

public enum TransactionType {
    DEPOSIT("ПОПОЛНЕНИЕ"),
    WITHDRAW("СНЯТИЕ"),
    TRANSFER("ПЕРЕВОД");

    private final String showType;

    TransactionType(String showType) {
        this.showType = showType;
    }

    @Override
    public String toString() {
        return showType;
    }
}
