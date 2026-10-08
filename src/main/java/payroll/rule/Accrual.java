package payroll.rule;

/**
 * Represents an accrual component in the payroll calculation.
 * <p>
 * Execution order: Accruals are calculated <b>first</b> in the PayrollEngine,
 * forming the gross salary base before any taxes or deductions are applied.
 * </p>
 */
public non-sealed interface Accrual extends CalculationComponent {
}
