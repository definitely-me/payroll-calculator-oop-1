import org.junit.jupiter.api.Test;
import payroll.domain.*;
import payroll.rule.*;
import payroll.service.PayrollSystem;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;

public class PayrollTest {

    @Test
    void testEqualsAndHashCodeDateInterval() {
        DateInterval d1 = new DateInterval("2026-01-01", "2026-01-31");
        DateInterval d2 = new DateInterval(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31));
        assertEquals(d1, d2);
        assertEquals(d1.hashCode(), d2.hashCode());
    }

    @Test
    void testEmployeeEqualsAndHashCode() {
        Employee emp1 = new Employee();
        Employee emp2 = new Employee();
        assertNotEquals(emp1, emp2);
        assertEquals(emp1, emp1);
        assertEquals(emp1.hashCode(), emp1.hashCode());
    }

    @Test
    void testPayslipDefensiveCopying() {
        Employee emp = new Employee();
        List<PayslipLine> lines = new ArrayList<>();
        lines.add(new PayslipLine("Bonus", "Accrual", 0, 100));
        Payslip payslip = new Payslip(emp, lines, 100);
        lines.add(new PayslipLine("Salary", "Accrual", 100, 200));
        assertEquals(1, payslip.payslipLines().size());
    }

    @Test
    void testPayslipImmutability() {
        Employee emp = new Employee();
        Payslip payslip = new Payslip(emp, List.of(), 0);
        assertThrows(UnsupportedOperationException.class, () -> payslip.payslipLines().add(new PayslipLine("Test", "Test", 0, 0)));
    }

    @Test
    void testProportionalAccrualFullMonth() {
        DateInterval interval = new DateInterval("2026-01-01", "2026-01-31");
        ProportionalAccrual salary = new ProportionalAccrual(interval, 100000_00, "Salary");
        long result = salary.calculate(0, YearMonth.of(2026, 1));
        assertEquals(100000_00, result);
    }

    @Test
    void testProportionalAccrualPartialMonth() {
        DateInterval interval = new DateInterval("2026-04-01", "2026-04-15");
        ProportionalAccrual salary = new ProportionalAccrual(interval, 60000_00, "Salary");
        long result = salary.calculate(0, YearMonth.of(2026, 4));
        assertEquals(30000_00, result);
    }

    @Test
    void testFixedAccrual() {
        DateInterval interval = new DateInterval("2026-01-15", "2026-01-20");
        FixedAccrual bonus = new FixedAccrual(interval, 5000_00, "Bonus");
        long result = bonus.calculate(10000_00, YearMonth.of(2026, 1));
        assertEquals(15000_00, result);
    }

    @Test
    void testPercentageAccrual() {
        DateInterval interval = new DateInterval("2026-01-01", "2026-01-31");
        PercentageAccrual bonus = new PercentageAccrual(interval, "15", "Night Shift");
        long result = bonus.calculate(10000_00, YearMonth.of(2026, 1));
        assertEquals(11500_00, result);
    }

    @Test
    void testFixedDeduction() {
        DateInterval interval = new DateInterval("2026-01-01", "2026-01-31");
        FixedDeduction fine = new FixedDeduction(interval, 3000_00, "Fine");
        long result = fine.calculate(10000_00, YearMonth.of(2026, 1));
        assertEquals(7000_00, result);
    }

    @Test
    void testPercentageDeduction() {
        DateInterval interval = new DateInterval("2026-01-01", "2026-01-31");
        PercentageDeduction alimony = new PercentageDeduction(interval, "25", "Alimony");
        long result = alimony.calculate(10000_00, YearMonth.of(2026, 1));
        assertEquals(7500_00, result);
    }

    @Test
    void testDefaultTax() {
        long result = DefaultTax.PERSONAL_INCOME_TAX.calculate(10000_00, YearMonth.of(2026, 1));
        assertEquals(8700_00, result);
    }

    @Test
    void testFractionalKopecksRounding() {
        DateInterval interval = new DateInterval("2026-01-01", "2026-01-31");
        PercentageDeduction testDeduction = new PercentageDeduction(interval, "13", "Test Rounding");
        long result = testDeduction.calculate(1000_33, YearMonth.of(2026, 1));
        assertEquals(870_29, result);
    }

    @Test
    void testIntervalsOutOfPeriod() {
        DateInterval interval = new DateInterval("2026-02-01", "2026-02-28");
        FixedAccrual futureBonus = new FixedAccrual(interval, 5000_00, "Future Bonus");
        long result = futureBonus.calculate(10000_00, YearMonth.of(2026, 1));
        assertEquals(10000_00, result);
    }

    @Test
    void testRegisterDuplicateEmployeeThrowsException() {
        PayrollSystem system = new PayrollSystem();
        Employee emp = new Employee();
        system.registerEmployee(emp);
        assertThrows(IllegalArgumentException.class, () -> system.registerEmployee(emp));
    }

    @Test
    void testAddComponentToUnknownEmployeeThrowsException() {
        PayrollSystem system = new PayrollSystem();
        Employee emp = new Employee();
        DateInterval interval = new DateInterval("2026-01-01", "2026-01-31");
        assertThrows(IllegalArgumentException.class, () -> system.addComponent(emp, new FixedAccrual(interval, 100, "Test")));
    }

    @Test
    void testReplaceComponentSuccess() {
        PayrollSystem system = new PayrollSystem();
        Employee emp = new Employee();
        system.registerEmployee(emp);
        DateInterval interval = new DateInterval("2026-01-01", "2026-01-31");
        FixedAccrual oldComponent = new FixedAccrual(interval, 100, "Test");
        FixedAccrual newComponent = new FixedAccrual(interval, 500, "Test");
        system.addComponent(emp, oldComponent);
        system.replaceComponent(emp, oldComponent, newComponent);
        List<CalculationComponent> components = system.getComponents(emp);
        assertEquals(1, components.size());
        assertEquals(newComponent, components.getFirst());
    }

    @Test
    void testDeleteComponent() {
        PayrollSystem system = new PayrollSystem();
        Employee emp = new Employee();
        system.registerEmployee(emp);
        DateInterval interval = new DateInterval("2026-01-01", "2026-01-31");
        FixedAccrual component = new FixedAccrual(interval, 100, "Test");
        system.addComponent(emp, component);
        system.deleteComponent(emp, component);
        assertTrue(system.getComponents(emp).isEmpty());
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
        assertThrows(OverdeductionException.class, () -> system.runPayroll(emp, YearMonth.of(2026, 1), "50"));
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
        assertDoesNotThrow(() -> system.runPayroll(emp, YearMonth.of(2026, 1), "50"));
    }

    @Test
    void testRunPayrollExecutionOrder() throws OverdeductionException {
        Employee emp = new Employee();
        PayrollSystem system = new PayrollSystem();
        system.registerEmployee(emp);
        DateInterval interval = new DateInterval("2026-01-01", "2026-01-31");
        system.addComponents(emp, List.of(
                new FixedDeduction(interval, 1000_00, "Fine"),
                DefaultTax.PERSONAL_INCOME_TAX,
                new FixedAccrual(interval, 10000_00, "Salary")
        ));
        PayslipVersion version = system.runPayroll(emp, YearMonth.of(2026, 1), "50");
        List<PayslipLine> lines = version.payslip().payslipLines();
        assertEquals(3, lines.size());
        assertEquals("Salary", lines.get(0).category());
        assertEquals("Personal Income Tax", lines.get(1).category());
        assertEquals("Fine", lines.get(2).category());
    }

    @Test
    void testEmptyComponentsPayroll() throws OverdeductionException {
        Employee emp = new Employee();
        PayrollSystem system = new PayrollSystem();
        system.registerEmployee(emp);
        PayslipVersion version = system.runPayroll(emp, YearMonth.of(2026, 1), "50");
        assertEquals(0, version.payslip().total());
        assertTrue(version.payslip().payslipLines().isEmpty());
    }

    @Test
    void testIdenticalPayslipsDiff() throws OverdeductionException {
        PayrollSystem system = new PayrollSystem();
        Employee emp = new Employee();
        system.registerEmployee(emp);
        DateInterval interval = new DateInterval("2026-01-01", "2026-01-31");
        system.addComponent(emp, new FixedAccrual(interval, 10000_00, "Salary"));
        PayslipVersion v1 = system.runPayroll(emp, YearMonth.of(2026, 1), "50");
        PayslipVersion v2 = system.runPayroll(emp, YearMonth.of(2026, 1), "50");
        PayslipDiffReport diff = system.getDiffReport(v1, v2);
        assertEquals(0, diff.total());
        assertTrue(diff.payslipLines().isEmpty());
    }

    @Test
    void testDiffWithNewComponent() throws OverdeductionException {
        PayrollSystem system = new PayrollSystem();
        Employee emp = new Employee();
        system.registerEmployee(emp);
        DateInterval interval = new DateInterval("2026-01-01", "2026-01-31");
        FixedAccrual salary = new FixedAccrual(interval, 10000_00, "Salary");
        system.addComponent(emp, salary);
        PayslipVersion v1 = system.runPayroll(emp, YearMonth.of(2026, 1), "50");
        FixedAccrual bonus = new FixedAccrual(interval, 5000_00, "Bonus");
        system.addComponent(emp, bonus);
        PayslipVersion v2 = system.runPayroll(emp, YearMonth.of(2026, 1), "50");
        PayslipDiffReport diff = system.getDiffReport(v1, v2);
        assertEquals(5000_00, diff.total());
        assertEquals(1, diff.payslipLines().size());
        assertEquals(5000_00, diff.payslipLines().getFirst().diff());
    }

    @Test
    void testDiffWithChangedAmount() throws OverdeductionException {
        PayrollSystem system = new PayrollSystem();
        Employee emp = new Employee();
        system.registerEmployee(emp);
        DateInterval interval = new DateInterval("2026-01-01", "2026-01-31");
        FixedAccrual salary1 = new FixedAccrual(interval, 10000_00, "Salary");
        system.addComponent(emp, salary1);
        PayslipVersion v1 = system.runPayroll(emp, YearMonth.of(2026, 1), "50");
        FixedAccrual salary2 = new FixedAccrual(interval, 12000_00, "Salary");
        system.replaceComponent(emp, salary1, salary2);
        PayslipVersion v2 = system.runPayroll(emp, YearMonth.of(2026, 1), "50");
        PayslipDiffReport diff = system.getDiffReport(v1, v2);
        assertEquals(2000_00, diff.total());
        assertEquals(1, diff.payslipLines().size());
        assertEquals(2000_00, diff.payslipLines().getFirst().diff());
    }

    @Test
    void testSecondOrderDiff() throws OverdeductionException {
        PayrollSystem system = new PayrollSystem();
        Employee emp = new Employee();
        system.registerEmployee(emp);
        DateInterval interval = new DateInterval("2026-01-01", "2026-01-31");
        FixedAccrual salary1 = new FixedAccrual(interval, 10000_00, "Salary");
        system.addComponent(emp, salary1);
        PayslipVersion v1 = system.runPayroll(emp, YearMonth.of(2026, 1), "50");
        FixedAccrual salary2 = new FixedAccrual(interval, 12000_00, "Salary");
        system.replaceComponent(emp, salary1, salary2);
        PayslipVersion v2 = system.runPayroll(emp, YearMonth.of(2026, 1), "50");
        FixedAccrual salary3 = new FixedAccrual(interval, 15000_00, "Salary");
        system.replaceComponent(emp, salary2, salary3);
        PayslipVersion v3 = system.runPayroll(emp, YearMonth.of(2026, 1), "50");
        PayslipDiffReport diff1 = system.getDiffReport(v1, v2);
        PayslipDiffReport diff2 = system.getDiffReport(v2, v3);
        PayslipDiffReport diffOfDiffs = system.getDiffReport(diff1, diff2);
        assertEquals(1000_00, diffOfDiffs.total());
        assertEquals(1, diffOfDiffs.payslipLines().size());
        assertEquals(1000_00, diffOfDiffs.payslipLines().getFirst().diff());
    }

    @Test
    void testDiffLineMergeLogic() throws OverdeductionException {
        PayrollSystem system = new PayrollSystem();
        Employee emp = new Employee();
        system.registerEmployee(emp);
        DateInterval interval = new DateInterval("2026-01-01", "2026-01-31");
        system.addComponents(emp, List.of(
                new FixedAccrual(interval, 100000_00, "Salary"),
                new FixedAccrual(interval, 3000_00, "Bonus")
        ));
        PayslipVersion v1 = system.runPayroll(emp, YearMonth.of(2026, 1), "50");
        system.addComponents(emp, List.of(
                new FixedAccrual(interval, 100000_00, "Salary"),
                new FixedAccrual(interval, 3000_00, "Bonus"),
                new FixedAccrual(interval, 6000_00, "Bonus")
        ));
        PayslipVersion v2 = system.runPayroll(emp, YearMonth.of(2026, 1), "50");
        PayslipDiffReport diff = system.getDiffReport(v1, v2);
        assertEquals(1, diff.payslipLines().size());
        assertTrue(diff.payslipLines().getFirst().report().contains("Merged"));
        assertEquals(6000_00, diff.payslipLines().getFirst().diff());
    }

    @Test
    void testNegativeValues() throws OverdeductionException {
        PayrollSystem system = new PayrollSystem();
        Employee emp = new Employee();
        system.registerEmployee(emp);
        DateInterval interval = new DateInterval("2026-01-01", "2026-01-31");
        assertThrows(IllegalArgumentException.class, () -> new FixedAccrual(interval, -100000_00, "Salary"));
        assertThrows(IllegalArgumentException.class, () -> new PercentageAccrual(interval, "-10", "Deduction"));
        assertThrows(IllegalArgumentException.class, () -> new PercentageDeduction(interval, "-10", "Deduction"));
        assertThrows(IllegalArgumentException.class, () -> new FixedDeduction(interval, -100, "Deduction"));
        assertThrows(IllegalArgumentException.class, () -> system.runPayroll(emp, YearMonth.of(2026, 1), "-50"));
    }

    @Test
    void testInvalidDateInterval() {
        assertThrows(IllegalArgumentException.class, () -> new DateInterval("2026-01-31", "2026-01-01"));
    }

    @Test
    void testProportionalAccrualCrossMonthInterval() throws OverdeductionException {
        PayrollSystem system = new PayrollSystem();
        Employee emp = new Employee();
        system.registerEmployee(emp);
        DateInterval interval = new DateInterval("2026-01-15", "2026-02-15");
        ProportionalAccrual salary = new ProportionalAccrual(interval, 100000_00, "Salary");
        system.addComponent(emp, salary);
        PayslipVersion version = system.runPayroll(emp, YearMonth.of(2026, 1), "50");
        long expected = (100000_00L * 17 + 31) / 31;
        assertEquals(expected, version.payslip().total());
        assertEquals(1, version.payslip().payslipLines().size());
    }

    @Test
    void testDiffAllScenarios() throws OverdeductionException {
        PayrollSystem system = new PayrollSystem();
        Employee emp = new Employee();
        system.registerEmployee(emp);
        DateInterval interval = new DateInterval("2026-01-01", "2026-01-31");

        system.addComponents(emp, List.of(
                new FixedAccrual(interval, 100000_00, "Salary"),
                new FixedAccrual(interval, 5000_00, "Bonus"),
                new FixedDeduction(interval, 2000_00, "Fine")
        ));
        PayslipVersion v1 = system.runPayroll(emp, YearMonth.of(2026, 1), "50");

        system.addComponents(emp, List.of(
                new FixedAccrual(interval, 100000_00, "Salary"),
                new FixedAccrual(interval, 5000_00, "Bonus"),
                new FixedAccrual(interval, 3000_00, "Bonus"),
                new FixedAccrual(interval, 10000_00, "Allowance"),
                new PercentageAccrual(interval, "10", "NightShift")
        ));
        PayslipVersion v2 = system.runPayroll(emp, YearMonth.of(2026, 1), "50");

        PayslipDiffReport diff = system.getDiffReport(v1, v2);

        assertEquals(4, diff.payslipLines().size());

        Map<String, PayslipLine> diffMap = new LinkedHashMap<>();
        for (var line : diff.payslipLines()) {
            diffMap.put(line.category(), line);
        }

        PayslipLine bonusDiff = diffMap.get("Bonus");
        assertTrue(bonusDiff.report().contains("Merged"));
        assertEquals(3000_00, bonusDiff.diff());
        assertEquals(5000_00, bonusDiff.before());

        PayslipLine fineDiff = diffMap.get("Fine");
        assertEquals(-2000_00, fineDiff.diff());
        assertEquals(0, fineDiff.before());

        PayslipLine allowanceDiff = diffMap.get("Allowance");
        assertEquals(10000_00, allowanceDiff.diff());
        assertEquals(0, allowanceDiff.before());

        PayslipLine nightShiftDiff = diffMap.get("NightShift");
        assertEquals(11800_00, nightShiftDiff.diff());
        assertEquals(0, nightShiftDiff.before());

        assertEquals(26800_00, diff.total());
    }
}
