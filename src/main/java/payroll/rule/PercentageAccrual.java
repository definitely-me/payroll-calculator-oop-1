package payroll.rule;

import payroll.domain.DateInterval;
import payroll.utils.CalculationUtils;
import payroll.utils.DateUtils;

import java.time.YearMonth;


public class PercentageAccrual extends AbstractCalculationComponent implements Accrual {

    private final String rate;

    public PercentageAccrual(DateInterval dateInterval, String rate, String name) {
        super(dateInterval, name);
        this.rate = rate.trim().replace(" ", "");
    }

    @Override
    public long calculate(long base, YearMonth interval) {
        if (DateUtils.getOverlapDays(validityInterval, interval) > 0) {
            return base + CalculationUtils.multiplyWithRate(base, rate);
        }
        return base;
    }

    @Override
    public String report() {
        return "+ " + name + " (" + rate + "%)";
    }
}
