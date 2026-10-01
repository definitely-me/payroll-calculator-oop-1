package payroll.rule;

import java.math.BigDecimal;
import java.math.RoundingMode;

public enum DefaultTax implements Deduction {

    NDFL {
        @Override
        public long calculate(long base) {
            return base - BigDecimalCalculations.multiplyWithRate(base, "0.13");
        }
    },
    
    SOCIAL_CONTRIBUTION {
        @Override
        public long calculate(long base) {
            return base - BigDecimalCalculations.multiplyWithRate(base, "0.30");
        }
    },
    
    ACCIDENT_INSURANCE {
        @Override
        public long calculate(long base) {
            return base - BigDecimalCalculations.multiplyWithRate(base, "0.002");
        }
    };

}
