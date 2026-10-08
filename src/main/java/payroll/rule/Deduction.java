package payroll.rule;

/**
 * Represents a deduction component in the payroll calculation.
 * <p>
 * Execution order: Deductions are calculated <b>third (last)</b> in the PayrollEngine,
 * after all Accruals and Taxes have been applied to the balance.
 * </p>
 */
public non-sealed interface Deduction extends CalculationComponent {
}
