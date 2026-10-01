package payroll.rule;

public sealed interface CalculationComponent extends Reportable permits Accrual, Deduction{

    public long calculate(long base);
}
