package payroll.rule;

import payroll.domain.DateInterval;
import payroll.utils.DateUtils;

import java.time.YearMonth;


/**
 * An accrual that adds a fixed amount of money.
 */
public class FixedAccrual extends AbstractCalculationComponent implements Accrual {

    private final long amount;

    /**
     * Creates a fixed accrual.
     *
     * @param dateInterval the period when this rule is active
     * @param amount       the exact amount to add (in kopecks)
     * @param name         the name shown in the report
     */
    public FixedAccrual(DateInterval dateInterval, long amount, String name) {
        super(dateInterval, name);
        this.amount = amount;

    }

    @Override
    public long calculate(long base, YearMonth interval) {
        if (DateUtils.getOverlapDays(getValidityInterval(), interval) > 0) {
            return base + amount;
        }
        return base;
    }

    @Override
    public String report() {
        long rubles = amount / 100;
        long kopecks = amount % 100;
        return String.format("+ %s (%d rub. %02d kop.)", getName(), rubles, kopecks);
    }
}
