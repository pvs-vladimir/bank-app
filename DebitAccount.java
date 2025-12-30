public class DebitAccount extends Account {
    public DebitAccount(Customer owner) {
        super(owner, Type.DEBIT);
    }

    @Override
    public boolean withdraw(double amount) {
        if ((amount > 0) && (balance >= amount)) {
            balance -= amount;
            return true;
        }
        return false;
    }
}