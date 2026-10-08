package payroll.rule;

import payroll.domain.DateInterval;

import java.util.UUID;

/**
 * An abstract class with basic fields for all calculation components.
 */
public abstract class AbstractCalculationComponent {

    private final DateInterval validityInterval;
    private final String category;
    private final UUID id;

    /**
     * Creates a basic calculation component.
     *
     * @param dateInterval the period when this rule is active
     * @param category     the category shown in the report and used to calculate diff between two payslip versions
     */
    public AbstractCalculationComponent(DateInterval dateInterval, String category) {
        this.validityInterval = dateInterval;
        this.category = category;
        this.id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public DateInterval getValidityInterval() {
        return validityInterval;
    }

    public String getCategory() {
        return category;
    }
}
