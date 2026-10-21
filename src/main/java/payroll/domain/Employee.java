package payroll.domain;

import java.util.UUID;

/**
 * Creates an Employee instance.
 *
 * @param id generates automatically to ensure uniqueness
 */
public record Employee(UUID id) {
    public Employee() {
        this(UUID.randomUUID());
    }
}
