package payroll.rule;

import payroll.domain.DateInterval;
import payroll.utils.CalculationUtils;
import payroll.utils.DateUtils;

import java.time.YearMonth;

public class ProportionalAccrual extends AbstractCalculationComponent implements Accrual {
    long amount;
    public ProportionalAccrual(DateInterval dateInterval, long amount, String name) {
        super(dateInterval, name);
        this.amount = amount;
    }

    @Override
    public long calculate(long base, YearMonth interval) {
        long overlap = DateUtils.getOverlapDays(validityInterval, interval);
        long total = CalculationUtils.divisionWithDecimal(amount * overlap, interval.lengthOfMonth());
        return base + total;
    }

    @Override
    public String report() {
        long rubles = amount / 100;
        long kopecks = amount % 100;
        return String.format("+ %s (%d rub. %02d kop.)", name, rubles, kopecks);
    }
}
