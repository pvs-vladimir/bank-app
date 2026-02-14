package bank;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bank {
    private final Map<Integer, Customer> customers;
    private final Map<Integer, Account> accounts;
    private final List<Transaction> transactions;
    private final Map<Integer, List<Integer>> customerAccounts;

    public Bank() {
        this.customers = new HashMap<>();
        this.accounts = new HashMap<>();
        this.customerAccounts = new HashMap<>();
        this.transactions = new ArrayList<>();
    }

    public Collection<Customer> getCustomers() {
        return customers.values();
    }

    public Collection<Account> getAccounts() {
        return accounts.values();
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public Transaction getLastTransaction() {
        return !transactions.isEmpty() ? transactions.get(transactions.size() - 1) : null;
    }

    public Customer createCustomer(String fullName) {
        Customer customer = new Customer(fullName);
        customers.put(customer.getId(), customer);
        customerAccounts.put(customer.getId(), new ArrayList<>());
        return customer;
    }

    public Account openDebitAccount(Customer owner) {
        Account account = new DebitAccount(owner);
        accounts.put(account.getAccountNumber(), account);
        customerAccounts.get(owner.getId()).add(account.getAccountNumber());
        return account;
    }

    public Account openCreditAccount(Customer owner, BigDecimal creditLimit) {
        Account account = new CreditAccount(owner, creditLimit);
        accounts.put(account.getAccountNumber(), account);
        customerAccounts.get(owner.getId()).add(account.getAccountNumber());
        return account;
    }

    public Customer findCustomer(int customerId) {
        return customers.get(customerId);
    }

    public Account findAccount(int accountNumber) {
        return accounts.get(accountNumber);
    }

    public boolean deposit(int accountNumber, BigDecimal amount) {
        Transaction transaction = new Transaction(TransactionType.DEPOSIT, amount, null, accountNumber);

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            logTransaction(transaction, false, "Неверная сумма");
            return false;
        }
        
        Account account = findAccount(accountNumber); 
        if (account == null) {
            logTransaction(transaction, false, "Счет не найден");
            return false;
        }

        boolean success = account.deposit(amount);
        String message = success ? "ОК" : "Сбой пополнения";
        logTransaction(transaction, success, message);
        return success;
    }

    public boolean withdraw(int accountNumber, BigDecimal amount) {
        Transaction transaction = new Transaction(TransactionType.WITHDRAW, amount, accountNumber, null);

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            logTransaction(transaction, false, "Неверная сумма");
            return false;
        }
        
        Account account = findAccount(accountNumber); 
        if (account == null) {
            logTransaction(transaction, false, "Счет не найден");
            return false;
        }

        boolean success = account.withdraw(amount);
        String message = success ? "ОК" : "Недостаточно средств";
        logTransaction(transaction, success, message);
        return success;
    }

    public boolean transfer(int from, int to, BigDecimal amount) {
        Transaction transaction = new Transaction(TransactionType.TRANSFER, amount, from, to);
        
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            logTransaction(transaction, false, "Неверная сумма");
            return false;
        }

        Account fromAccount = findAccount(from);
        Account toAccount = findAccount(to); 
        if (fromAccount == null || toAccount == null) {
            String message  = (fromAccount == null) ? "Счет отправителя не найден"
                                                    : "Счет получателя не найден";
            logTransaction(transaction, false, message);
            return false;
        }

        boolean success = fromAccount.transfer(toAccount, amount);
        String message = success ? "ОК" : "Недостаточно средств у отправителя";
        logTransaction(transaction, success, message);
        return success;
    }

    public void printCustomerAccounts(int customerId) {
        Customer customer = customers.get(customerId);
        if (customer == null) {
            System.out.println("Клиент с ID " + customerId + " не найден");
            return;
        }

        if (customerAccounts.get(customerId).isEmpty()) {
            System.out.println("У клиента - " + customer.getFullName() + " нет открытых счетов");
            return;
        }

        System.out.println("Счета клиента - " + customer.getFullName() + " :");
        for (int accountId : customerAccounts.get(customerId)) {
            Account account = accounts.get(accountId);

            StringBuilder sb = new StringBuilder();
            sb.append("| НОМЕР СЧЕТА: ").append(String.format("%-5s", account.getAccountNumber()))
              .append(" | БАЛАНС: ").append(String.format("%-10.2f", account.getBalance()))
              .append(" | ТИП: ").append(String.format("%-9s", account.getType().toString())).append(" |");

            System.out.println(sb.toString());
        }       
    }

    public void printTransactions() {
        System.out.println("Совершено " + transactions.size() + " транзакций:");
        for (Transaction transaction : transactions) {
            System.out.println(transaction.toString());
        }
    }

    public void printReport() {
        AccountsStats accountsStats = getAccountsStat();
        TransactionsStats transactionsStats = getTransactionsStats();

        System.out.println("----------------ОТЧЕТ----------------");
        System.out.println("Дебетовые счета: " + accountsStats.debitCount);
        System.out.println("Кредитные счета: " + accountsStats.creditCount);
        System.out.println("Баланс дебетовых счетов: " + accountsStats.debitTotal);
        System.out.println("Баланс кредитных счетов: " + accountsStats.creditTotal);
        System.out.println();
        System.out.println("Количество успешных транзакций: " + transactionsStats.successCount);
        System.out.println("Количество сбоев транзакций: " + transactionsStats.failCount);
        System.out.println("-------------------------------------");
    }

    private void logTransaction(Transaction transaction, boolean success, String message) {
        transaction.setSuccess(success);
        transaction.setMessage(message);
        transactions.add(transaction);
    }

    private class AccountsStats {
        public int debitCount;
        public int creditCount;
        public BigDecimal debitTotal;
        public BigDecimal creditTotal;

        public AccountsStats() {
            this.debitCount = 0;
            this.creditCount = 0;
            this.debitTotal = BigDecimal.ZERO.setScale(Common.MONEY_UNIT_PRECISION);
            this.creditTotal = BigDecimal.ZERO.setScale(Common.MONEY_UNIT_PRECISION);
        }
    }

    private AccountsStats getAccountsStat() {
        AccountsStats stats = new AccountsStats();
        for (Account account : accounts.values()) {
            if (account instanceof DebitAccount) {
                stats.debitCount++;
                stats.debitTotal = stats.debitTotal.add(account.getBalance());
            } else if (account instanceof CreditAccount) {
                stats.creditCount++;
                stats.creditTotal = stats.creditTotal.add(account.getBalance());
            }
        }
        return stats;
    }

    private class TransactionsStats {
        public int successCount = 0;
        public int failCount = 0;
    }

    private TransactionsStats getTransactionsStats() {
        TransactionsStats stats = new TransactionsStats();
        for (Transaction transaction : transactions) {
            if (transaction.isSuccess()) {
                stats.successCount++;
            } else {
                stats.failCount++;
            }
        }
        return stats;
    }
}