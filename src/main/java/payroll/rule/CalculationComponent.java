package payroll.rule;

import java.time.YearMonth;

public sealed interface CalculationComponent extends Reportable permits Accrual, Deduction, Tax {

    public long calculate(long base, YearMonth interval);
}
