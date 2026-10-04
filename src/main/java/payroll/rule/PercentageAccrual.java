package payroll.rule;

import payroll.domain.DateInterval;
import payroll.utils.CalculationUtils;
import payroll.utils.DateUtils;

import java.time.YearMonth;


/**
 * An accrual that adds money based on a percentage of the current base.
 */
public class PercentageAccrual extends AbstractCalculationComponent implements Accrual {

    private final String rate;

    /**
     * Creates a percentage-based accrual.
     *
     * @param dateInterval the period when this rule is active
     * @param rate         the percentage rate as a string (for example: "15")
     * @param name         the name shown in the report
     */
    public PercentageAccrual(DateInterval dateInterval, String rate, String name) {
        super(dateInterval, name);
        this.rate = rate.trim().replace(" ", "");
    }

    @Override
    public long calculate(long base, YearMonth interval) {
        if (DateUtils.getOverlapDays(getValidityInterval(), interval) > 0) {
            return base + CalculationUtils.multiplyWithRate(base, rate);
        }
        return base;
    }

    @Override
    public String report() {
        return "+ " + getName() + " (" + rate + "%)";
    }
}
