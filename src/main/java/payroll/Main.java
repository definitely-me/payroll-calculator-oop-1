package payroll;

import payroll.domain.Employee;
import payroll.domain.DateInterval;
import payroll.rule.*;
import payroll.service.PayrollEngine;
import payroll.service.PayrollRegistry;

import java.time.YearMonth;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        var vasya = new Employee(1);
        var registry = new PayrollRegistry();
        var engine = new PayrollEngine(registry);

        var salary = new ProportionalAccrual(new DateInterval("2023-10-01", "2030-10-01"), 8000000, "Salary");
        var sickLeave = new ProportionalAccrual(new DateInterval("2026-01-10", "2026-01-15"), 1500000, "Sick Leave");
        var bonus = new FixedAccrual(new DateInterval("2026-01-01", "2026-01-31"), 2000000, "Quarterly Bonus");
        var nightShift = new PercentageAccrual(new DateInterval("2026-01-01", "2026-01-31"), "15", "Night Shift Bonus");

        var alimony = new PercentageDeduction(new DateInterval("2023-10-01", "2030-10-01"), "30", "Alimony");
        var advance = new FixedDeduction(new DateInterval("2026-01-01", "2026-01-31"), 1000000, "Advance Payment");

        registry.setComponentsFor(vasya, List.of(
                salary, sickLeave, bonus, nightShift,
                DefaultTax.PERSONAL_INCOME_TAX,
                alimony, advance
        ));

        try {
            System.out.println(engine.runPayroll(vasya, YearMonth.of(2026, 1), "50").report());
        } catch (Exception e) {
            System.err.println("The calculation was rejected for the employee #" + vasya.id() + ": " + e.getMessage());
        }
    }
}
