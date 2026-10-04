package payroll.rule;

import payroll.domain.DateInterval;

/**
 * An abstract class with basic fields for all calculation components.
 */
public abstract class AbstractCalculationComponent {

    private final DateInterval validityInterval;
    private  final String name;

    /**
     * Creates a basic calculation component.
     *
     * @param dateInterval the period when this rule is active
     * @param name         the name shown in the report
     */
    public AbstractCalculationComponent(DateInterval dateInterval, String name) {
        this.validityInterval = dateInterval;
        this.name = name;
    }

    public DateInterval getValidityInterval() {
        return validityInterval;
    }

    public String getName() {
        return name;
    }
}
