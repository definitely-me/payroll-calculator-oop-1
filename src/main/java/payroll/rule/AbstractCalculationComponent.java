package payroll.rule;

import payroll.domain.DateInterval;

public abstract class AbstractCalculationComponent {

    DateInterval validityInterval;
    String name;

    public AbstractCalculationComponent(DateInterval dateInterval, String name) {
        this.validityInterval = dateInterval;
        this.name = name;
    }

}
