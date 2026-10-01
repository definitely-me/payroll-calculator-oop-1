package payroll.rule;

import payroll.domain.DateInterval;

public class Alimony extends AbstractCalculationComponent implements Deduction{

    String rate;

    public Alimony(DateInterval dateInterval, String rate) {
        super(dateInterval);
        this.rate = rate;
    }

    @Override
    public long calculate(long base) {
        return base - BigDecimalCalculations.multiplyWithRate(base, rate);
    }
}
