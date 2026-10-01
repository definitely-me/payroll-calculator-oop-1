package payroll.rule;

import java.math.BigDecimal;
import java.math.RoundingMode;

public enum DefaultTax implements Deduction {

    PERSONAL_INCOME_TAX {
        @Override
        public long calculate(long base) {
            return base - BigDecimalCalculations.multiplyWithRate(base, "13");
        }
        @Override
        public String report() {
            return "- Personal Income Tax (13.0%)";
        }
    },
    
    SOCIAL_CONTRIBUTION {
        @Override
        public long calculate(long base) {
            return base - BigDecimalCalculations.multiplyWithRate(base, "30");
        }
        @Override
        public String report() {
            return "- Social Contribution (30.0%)";
        }
    },
    
    ACCIDENT_INSURANCE {
        @Override
        public long calculate(long base) {
            return base - BigDecimalCalculations.multiplyWithRate(base, "0.2");
        }
        @Override
        public String report() {
            return "- Accident Insurance (0.2%)";
        }
    };

}
