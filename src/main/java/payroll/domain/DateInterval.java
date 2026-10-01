package payroll.domain;

import java.time.LocalDate;

/**
 * Represents a time interval with a start and end date.
 * <p>
 * The string dates provided to the constructor must be in the ISO-8601 format: {@code yyyy-MM-dd}.
 * For example: {@code "2023-10-01"}.
 * </p>
 */
public record DateInterval(LocalDate startDate, LocalDate endDate) {

    public DateInterval(String startDate, String endDate) {
        this(LocalDate.parse(startDate), LocalDate.parse(endDate));
    }

    public DateInterval {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("start date can't' be later than the end date.");
        }
    }

}
