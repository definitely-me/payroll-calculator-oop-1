package payroll.rule;

import payroll.domain.DateInterval;
import payroll.utils.DateUtils;

import java.time.YearMonth;

public class FixedDeduction extends AbstractCalculationComponent implements Deduction {

    long amount;

    public FixedDeduction(DateInterval dateInterval, long amount, String name) {
        super(dateInterval, name);
        this.amount = amount;
    }

    @Override
    public long calculate(long base, YearMonth interval) {
        if (DateUtils.getOverlapDays(validityInterval, interval) > 0) {
            return base - amount;
        }
        return base;
    }

    @Override
    public String report() {
        long rubles = amount / 100;
        long kopecks = amount % 100;
        return String.format("- %s (%d rub. %02d kop.)", name, rubles, kopecks);
    }
}
