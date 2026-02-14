package bank;

public class CreditAccount extends Account {
    private final double creditLimit;

    public CreditAccount(Customer owner, double creditLimit) {
        super(owner, AccountType.CREDIT);
        this.creditLimit = creditLimit;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    @Override
    public boolean withdraw(double amount) {
        if ((amount > 0) && ((balance - amount) >= -creditLimit)) {
            balance -= amount;
            return true;
        }
        return false;
    }
}