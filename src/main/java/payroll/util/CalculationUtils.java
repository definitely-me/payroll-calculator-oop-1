package payroll.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Util methods used to calculate the money safely with BigDecimal.
 */
public final class CalculationUtils {

    private CalculationUtils() {
    }

    public static long multiplyWithRate(long base, String rate) {
        rate = rate.trim().replace(" ", "");
        var baseBD = new BigDecimal(base);
        var rateBD = new BigDecimal(rate).movePointLeft(2);
        return baseBD.multiply(rateBD)
                .setScale(0, RoundingMode.HALF_UP)
                .longValue();

    }

    public static long divisionWithDecimal(long base, long decimalValue) {
        var baseBD = new BigDecimal(base);
        var divisorBD = new BigDecimal(decimalValue);
        return baseBD.divide(divisorBD, 0, RoundingMode.HALF_UP).longValue();
    }

    public static long requireNonNegative(long l) {
        if (l < 0) {
            throw new IllegalArgumentException("this val can't be negative");
        }
        return l;
    }

    public static String requireNonNegative(String s) {
        if (s.contains("-")) {
            throw new IllegalArgumentException("this val can't be negative");
        }
        return s;
    }
}

