package payroll.domain;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Creates a new payslip version to store with employee and month
 *
 * @param versionId generates automatically to ensure uniqueness
 * @param createdAt timestamp to associate with
 * @param payslip   payslip instance to store
 */
public record PayslipVersion(UUID versionId, LocalDateTime createdAt, Payslip payslip) {

    public PayslipVersion(Payslip payslip) {
        this(UUID.randomUUID(), LocalDateTime.now(), payslip);
    }

}
