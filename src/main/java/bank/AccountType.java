package bank;

public enum AccountType {
    DEBIT("ДЕБЕТОВЫЙ"),
    CREDIT("КРЕДИТНЫЙ");

    private final String showType;

    AccountType(String showType) {
        this.showType = showType;
    }

    @Override
    public String toString() {
        return showType;
    }
}
