package bank;

import java.math.BigDecimal;

public abstract class Account {
    private final AccountType type;
    private static int nextAccountNumber = 0;

    private final int accountNumber;
    protected BigDecimal balance;
    private final Customer owner;

    public Account(Customer owner, AccountType type) {
        this.type = type;
        this.accountNumber = nextAccountNumber++;
        this.balance = BigDecimal.ZERO.setScale(Common.MONEY_UNIT_PRECISION);
        this.owner = owner;
    }

    public AccountType getType() {
        return type;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Customer getOwner() {
        return owner;
    }

    public boolean deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > 0){
            balance = balance.add(amount);
            return true;
        }
        return false;
    }

    public abstract boolean withdraw(BigDecimal amount);

    public boolean transfer(Account to, BigDecimal amount) {
        if (this.withdraw(amount)) {
            return to.deposit(amount);
        }
        return false;
    }
}