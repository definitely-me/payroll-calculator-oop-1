package payroll.rule;

import payroll.domain.DateInterval;
import payroll.util.CalculationUtils;
import payroll.util.DateUtils;

import java.time.YearMonth;

/**
 * A deduction that removes money based on a percentage of the current base.
 */
public class PercentageDeduction extends AbstractCalculationComponent implements Deduction {

    private final String rate;

    /**
     * Creates a percentage-based deduction.
     *
     * @param dateInterval the period when this rule is active
     * @param rate         the percentage rate as a string (for example: "30")
     * @param name         the name shown in the report
     */
    public PercentageDeduction(DateInterval dateInterval, String rate, String name) {
        super(dateInterval, name);
        this.rate = CalculationUtils.requireNonNegative(rate);
    }

    @Override
    public long calculate(long base, YearMonth interval) {
        if (DateUtils.getOverlapDays(getValidityInterval(), interval) > 0) {
            return base - CalculationUtils.multiplyWithRate(base, rate);
        }
        return base;
    }

    @Override
    public String report() {
        return "- " + getCategory() + " (" + rate + "%)";
    }
}
