package payroll.rule;

import payroll.util.CalculationUtils;

import java.time.YearMonth;
import java.util.UUID;

/**
 * Standard pre-defined taxes that can be reused across different employees.
 */
public enum DefaultTax implements Tax {

    /**
     * Standard 13% personal income tax.
     */
    PERSONAL_INCOME_TAX {
        private final UUID id = UUID.randomUUID();
        private final String category = "Personal Income Tax";

        @Override
        public long calculate(long base, YearMonth interval) {
            return base - CalculationUtils.multiplyWithRate(base, "13");
        }

        @Override
        public UUID getId() {
            return id;
        }

        @Override
        public String report() {
            return "- Personal Income Tax (13.0%)";
        }

        @Override
        public String getCategory() {
            return category;
        }
    }

}
