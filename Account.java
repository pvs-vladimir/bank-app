public abstract class Account {
    public static enum Type {
        DEBIT("ДЕБЕТОВЫЙ"),
        CREDIT("КРЕДИТНЫЙ");

        private final String showType;

        Type(String showType) {
            this.showType = showType;
        }

        @Override
        public String toString() {
            return showType;
        }
    }

    private final Type type;
    private static int nextAccountNumber = 0;

    private final int accountNumber;
    protected double balance;
    private final Customer owner;

    public Account(Customer owner, Type type) {
        this.type = type;
        this.accountNumber = nextAccountNumber++;
        this.balance = 0.0;
        this.owner = owner;
    }

    public Type getType() {
        return type;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public Customer getOwner() {
        return owner;
    }

    public boolean deposit(double amount) {
        if (amount > 0){
            balance += amount;
            return true;
        }
        return false;
    }

    public abstract boolean withdraw(double amount);

    public boolean transfer(Account to, double amount) {
        if (this.withdraw(amount)) {
            return to.deposit(amount);
        }
        return false;
    }
}