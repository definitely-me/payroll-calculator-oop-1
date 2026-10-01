package payroll.rule;

public sealed interface CalculationComponent permits Accrual, Deduction{

    public long calculate(long base);
}
