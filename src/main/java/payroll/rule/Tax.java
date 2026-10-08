package payroll.rule;

/**
 * Represents a tax component in the payroll calculation.
 * <p>
 * Execution order: Taxes are calculated <b>second</b> in the PayrollEngine,
 * immediately after all Accruals have been calculated, and before other Deductions.
 * </p>
 */
public non-sealed interface Tax extends CalculationComponent {
}
