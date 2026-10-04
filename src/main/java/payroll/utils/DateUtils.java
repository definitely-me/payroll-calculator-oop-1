package payroll.utils;

import payroll.domain.DateInterval;

import java.time.YearMonth;

public final class DateUtils {

    private DateUtils() {}
    public static long getOverlapDays(DateInterval i1, YearMonth i2) {
        long maxStart = Math.max(i1.startDate().toEpochDay(), i2.atDay(1).toEpochDay());
        long minEnd = Math.min(i1.endDate().toEpochDay(), i2.atEndOfMonth().toEpochDay());
        return (minEnd - maxStart) < 0 ? 0 : minEnd - maxStart + 1;
    }
}
