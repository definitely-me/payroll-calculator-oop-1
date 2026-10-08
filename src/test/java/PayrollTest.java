import org.junit.jupiter.api.Test;
import payroll.domain.DateInterval;
import payroll.domain.Employee;
import payroll.rule.*;
import payroll.service.PayrollSystem;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PayrollTest {

    @Test
    void testEqualsAndHashCodeDateInterval() {
        DateInterval d1 = new DateInterval("2026-01-01", "2026-01-31");
        DateInterval d2 = new DateInterval(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31));

        assertEquals(d1, d2, "Intervals with the same dates should be equal");
        assertEquals(d1.hashCode(), d2.hashCode(), "Hash codes should match for equal intervals");
    }

    @Test
    void testProportionalAccrualFullMonth() {
        DateInterval interval = new DateInterval("2026-01-01", "2026-01-31");
        ProportionalAccrual salary = new ProportionalAccrual(interval, 100000_00, "Salary");

        long result = salary.calculate(0, YearMonth.of(2026, 1));
        assertEquals(100000_00, result, "Full amount should be accrued for a full month");
    }

    @Test
    void testProportionalAccrualPartialMonth() {
        DateInterval interval = new DateInterval("2026-04-01", "2026-04-15");
        ProportionalAccrual salary = new ProportionalAccrual(interval, 60000_00, "Salary");

        long result = salary.calculate(0, YearMonth.of(2026, 4));
        assertEquals(30000_00, result, "Exactly half of the amount should be accrued for 15 out of 30 days");
    }

    @Test
    void testFixedAccrual() {
        DateInterval interval = new DateInterval("2026-01-15", "2026-01-20");
        FixedAccrual bonus = new FixedAccrual(interval, 5000_00, "Bonus");

        long result = bonus.calculate(10000_00, YearMonth.of(2026, 1));
        assertEquals(15000_00, result, "Fixed amount should be fully added to the base");
    }

    @Test
    void testPercentageAccrual() {
        DateInterval interval = new DateInterval("2026-01-01", "2026-01-31");
        PercentageAccrual bonus = new PercentageAccrual(interval, "15", "Night Shift");

        long result = bonus.calculate(10000_00, YearMonth.of(2026, 1));
        assertEquals(11500_00, result, "15% of 10000 is 1500. Total should be 11500");
    }

    @Test
    void testFixedDeduction() {
        DateInterval interval = new DateInterval("2026-01-01", "2026-01-31");
        FixedDeduction fine = new FixedDeduction(interval, 3000_00, "Fine");

        long result = fine.calculate(10000_00, YearMonth.of(2026, 1));
        assertEquals(7000_00, result, "Fixed deduction should be subtracted from the base");
    }

    @Test
    void testPercentageDeduction() {
        DateInterval interval = new DateInterval("2026-01-01", "2026-01-31");
        PercentageDeduction alimony = new PercentageDeduction(interval, "25", "Alimony");

        long result = alimony.calculate(10000_00, YearMonth.of(2026, 1));
        assertEquals(7500_00, result, "25% of 10000 is 2500. Total should be 7500");
    }

    @Test
    void testDefaultTax() {
        long result = DefaultTax.PERSONAL_INCOME_TAX.calculate(10000_00, YearMonth.of(2026, 1));
        assertEquals(8700_00, result, "13% tax should deduct 1300 from the base");
    }

    @Test
    void testOverdeductionExceptionThrown() {
        Employee emp = new Employee();
        PayrollSystem system = new PayrollSystem();

        system.registerEmployee(emp);
        system.addComponents(emp, List.of(
                new FixedAccrual(new DateInterval("2026-01-01", "2026-01-31"), 10000_00, "Salary"),
                new FixedDeduction(new DateInterval("2026-01-01", "2026-01-31"), 6000_00, "Huge Fine")
        ));

        assertThrows(OverdeductionException.class, () -> {
            system.runPayroll(emp, YearMonth.of(2026, 1), "50");
        }, "Should throw an exception when deductions exceed the 50% limit");
    }

    @Test
    void testOverdeductionBoundaryNotThrown() {
        Employee emp = new Employee();
        PayrollSystem system = new PayrollSystem();

        system.registerEmployee(emp);
        system.addComponents(emp, List.of(
                new FixedAccrual(new DateInterval("2026-01-01", "2026-01-31"), 10000_00, "Salary"),
                new FixedDeduction(new DateInterval("2026-01-01", "2026-01-31"), 5000_00, "Huge Fine")
        ));

        assertDoesNotThrow(() -> {
            system.runPayroll(emp, YearMonth.of(2026, 1), "50");
        }, "Should not throw an exception when deductions are exactly equal to the limit (50%)");
    }
}