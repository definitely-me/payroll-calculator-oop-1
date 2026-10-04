package payroll.rule;

/**
 * Thrown when the total deductions exceed the maximum allowed limit of the gross.
 */
public class OverdeductionException extends Exception {
    public OverdeductionException(String message) {
        super(message);
    }
}
