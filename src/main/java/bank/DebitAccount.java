package bank;

import java.math.BigDecimal;

public class DebitAccount extends Account {
    public DebitAccount(Customer owner) {
        super(owner, AccountType.DEBIT);
    }

    @Override
    public boolean withdraw(BigDecimal amount) {
        if ((amount.compareTo(BigDecimal.ZERO) > 0) &&
            (balance.compareTo(amount) >= 0)) {
            balance = balance.subtract(amount);
            return true;
        }
        return false;
    }
}