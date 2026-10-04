package payroll.domain;

import java.time.LocalDate;
/**
 * Represents a time interval with a start and end date.
 * <p>
 * The string dates provided to the constructor must be in the ISO-8601 format: {@code yyyy-MM-dd}.
 * For example: {@code "2026-10-01"}.
 * </p>
 */
public record DateInterval(LocalDate startDate, LocalDate endDate) {

    /**
     * Creates a time interval from strings.
     * <p>Format must be yyyy-MM-dd (for example: "2026-10-01").</p>
     *
     * @param startDate the string start date
     * @param endDate   the string end date
     */
    public DateInterval(String startDate, String endDate) {
        this(LocalDate.parse(startDate), LocalDate.parse(endDate));
    }

    public DateInterval {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("start date can't' be later than the end date.");
        }
    }
}
