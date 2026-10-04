package payroll.rule;

import payroll.utils.CalculationUtils;

import java.time.YearMonth;

public enum DefaultTax implements Tax {

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
