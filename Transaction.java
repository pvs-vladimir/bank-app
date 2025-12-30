import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    public static enum Type {
        DEPOSIT("ПОПОЛНЕНИЕ"),
        WITHDRAW("СНЯТИЕ"),
        TRANSFER("ПЕРЕВОД");

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
    private final double amount;
    private final Integer fromAccountNumber;
    private final Integer toAccountNumber;
    private final LocalDateTime timestamp;
    private boolean success;
    private String message;

    public Transaction(Type type, double amount,
                       Integer fromAccountNumber, Integer toAccountNumber) {
        this.type = type;
        this.amount = amount;
        this.fromAccountNumber = fromAccountNumber;
        this.toAccountNumber = toAccountNumber;
        this.timestamp = LocalDateTime.now();
        this.success = true;
        this.message = "ОК";
    }

    public Type getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public Integer getFromAccountNumber() {
        return fromAccountNumber;
    }

    public Integer getToAccountNumber() {
        return toAccountNumber;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();   
        sb.append("| ВРЕМЯ: ").append(String.format("%-19s", timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
          .append(" | ТИП: ").append(String.format("%-10s", type.toString()))
          .append(" | СУММА: ").append(String.format("%-10.2f", amount))
          .append(" | ОТ: ").append(String.format("%-5s", (fromAccountNumber != null ? fromAccountNumber : "-")))
          .append(" | КОМУ: ").append(String.format("%-5s", (toAccountNumber != null ? toAccountNumber : "-")))
          .append(" | СТАТУС: ").append(String.format("%-8s", (success ? "УСПЕШНО" : "СБОЙ")))
          .append(" | СООБЩЕНИЕ: ").append(message).append(String.format("%-35s", " |"));

        return sb.toString();
    }
}