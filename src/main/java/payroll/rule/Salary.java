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

    @Override
    public String report() {
        long rubles = amount / 100;
        long kopecks = amount % 100;
        return String.format("+ Salary (%d rub. %02d kop.)", rubles, kopecks);
    }
}
