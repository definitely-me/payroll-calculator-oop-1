package payroll.rule;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class BigDecimalCalculations {

    static long multiplyWithRate(long base, String rate) {
        var baseBD = new BigDecimal(base);
        var rateBD = new BigDecimal(rate);
        return baseBD.multiply(rateBD)
                .setScale(0, RoundingMode.HALF_UP)
                .longValue();
    }
    static long divideWithRate(long base, String rate) {
        var baseBD = new BigDecimal(base);
        var rateBD = new BigDecimal(rate);
        return baseBD.divide(rateBD, 0, RoundingMode.HALF_UP)
                .longValue();
    }
}
