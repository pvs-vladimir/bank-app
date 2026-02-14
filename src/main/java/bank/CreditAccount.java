package bank;

import java.math.BigDecimal;

public class CreditAccount extends Account {
    private final BigDecimal creditLimit;

    public CreditAccount(Customer owner, BigDecimal creditLimit) {
        super(owner, AccountType.CREDIT);
        this.creditLimit = creditLimit;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    @Override
    public boolean withdraw(BigDecimal amount) {
        if ((amount.compareTo(BigDecimal.ZERO) > 0) &&
            (balance.subtract(amount).compareTo(creditLimit.negate()) >= 0)) {
            balance = balance.subtract(amount);
            return true;
        }
        return false;
    }
}