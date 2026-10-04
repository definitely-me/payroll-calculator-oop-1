package payroll.rule;

import payroll.utils.CalculationUtils;

import java.time.YearMonth;

/**
 * Standard pre-defined taxes that can be reused across different employees.
 */
public enum DefaultTax implements Tax {

    /**
     * Standard 13% personal income tax.
     */
    PERSONAL_INCOME_TAX {
        @Override
        public long calculate(long base, YearMonth interval) {
            return base - CalculationUtils.multiplyWithRate(base, "13");
        }
        @Override
        public String report() {
            return "- Personal Income Tax (13.0%)";
        }
    }

}
