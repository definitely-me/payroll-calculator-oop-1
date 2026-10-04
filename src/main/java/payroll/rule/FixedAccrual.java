package payroll.rule;

import payroll.domain.DateInterval;
import payroll.utils.DateUtils;

import java.time.YearMonth;


public class FixedAccrual extends ProportionalAccrual implements Accrual {

    public FixedAccrual(DateInterval dateInterval, long amount, String name) {
        super(dateInterval, amount, name);

    }

    @Override
    public long calculate(long base, YearMonth interval) {
        if (DateUtils.getOverlapDays(validityInterval, interval) > 0) {
            return base + amount;
        }
        return base;
    }
}
