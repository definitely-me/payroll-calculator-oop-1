package payroll.rule;

import payroll.domain.DateInterval;
import payroll.util.CalculationUtils;
import payroll.util.DateUtils;

import java.time.YearMonth;

/**
 * An accrual that pays proportionally based on active days in the month.
 */
public class ProportionalAccrual extends AbstractCalculationComponent implements Accrual {
    private final long amount;
    
    /**
     * Creates a proportional accrual.
     *
     * @param dateInterval the period when this rule is active
     * @param amount       the full amount for a complete month (in kopecks)
     * @param name         the name shown in the report
     */
    public ProportionalAccrual(DateInterval dateInterval, long amount, String name) {
        super(dateInterval, name);
        this.amount = amount;
    }

    @Override
    public long calculate(long base, YearMonth interval) {
        long overlap = DateUtils.getOverlapDays(getValidityInterval(), interval);
        long total = CalculationUtils.divisionWithDecimal(amount * overlap, interval.lengthOfMonth());
        return base + total;
    }

    @Override
    public String report() {
        long rubles = amount / 100;
        long kopecks = amount % 100;
        return String.format("+ %s (%d rub. %02d kop.)", getCategory(), rubles, kopecks);
    }
}
