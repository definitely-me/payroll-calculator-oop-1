package payroll.rule;

import payroll.domain.DateInterval;

public class Salary extends AbstractCalculationComponent implements Accrual {
    long amount;

    public Salary(DateInterval dateInterval, long amount) {
        super(dateInterval);
        this.amount = amount;
    }

    @Override
    public long calculate(long base) {return base +  amount;}
}
