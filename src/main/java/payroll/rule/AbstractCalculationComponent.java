package payroll.rule;

import payroll.domain.DateInterval;

public abstract class AbstractCalculationComponent {

    DateInterval dateInterval;

    public AbstractCalculationComponent(DateInterval dateInterval) {
        this.dateInterval = dateInterval;
    }
}
