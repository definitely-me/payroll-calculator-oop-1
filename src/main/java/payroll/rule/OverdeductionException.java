package payroll.rule;

public class OverdeductionException extends Exception {
    public OverdeductionException(String message) {
        super(message);
    }
}
