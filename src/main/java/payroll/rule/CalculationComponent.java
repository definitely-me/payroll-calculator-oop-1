package payroll.rule;

import java.time.YearMonth;
import java.util.UUID;

/**
 * Represents a single rule for payroll calculation.
 * It can be an accrual, a deduction, or a tax.
 */
public sealed interface CalculationComponent extends Reportable permits Accrual, Deduction, Tax {

    /**
     * Calculates the new money amount after applying this rule.
     *
     * @param base     the current money amount before calculation
     * @param interval the target month and year
     * @return the new money amount
     */
    long calculate(long base, YearMonth interval);

    UUID getId();
    String getCategory();
}
