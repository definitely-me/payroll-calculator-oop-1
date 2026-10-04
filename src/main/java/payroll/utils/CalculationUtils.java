package payroll.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class CalculationUtils {

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



    private CalculationUtils() {
    }
}
