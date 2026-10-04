package payroll;

import payroll.domain.Employee;
import payroll.domain.DateInterval;
import payroll.rule.*;
import payroll.service.PayrollEngine;
import payroll.service.PayrollRegistry;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        var registry = new PayrollRegistry();
        var engine = new PayrollEngine(registry);
        var random = new Random();

        DateInterval standardInterval = new DateInterval("2023-01-01", "2030-12-31");
        DateInterval currentMonthInterval = new DateInterval("2026-01-01", "2026-01-31");
        DateInterval partialInterval = new DateInterval("2026-01-10", "2026-01-15");

        for (int i = 1; i <= 5; i++) {
            var employee = new Employee(i);
            List<CalculationComponent> components = new ArrayList<>();

            long salaryAmount = (60000L + random.nextInt(90001)) * 100L;
            components.add(new ProportionalAccrual(standardInterval, salaryAmount, "Salary"));

            if (random.nextDouble() < 0.3) {
                long sickAmount = (10000L + random.nextInt(10001)) * 100L;
                components.add(new ProportionalAccrual(partialInterval, sickAmount, "Sick Leave"));
            }

            if (random.nextBoolean()) {
                long bonusAmount = (10000L + random.nextInt(20001)) * 100L;
                components.add(new FixedAccrual(currentMonthInterval, bonusAmount, "Quarterly Bonus"));
            }

            if (random.nextDouble() < 0.4) {
                String rate = String.valueOf(10 + random.nextInt(11));  
                components.add(new PercentageAccrual(currentMonthInterval, rate, "Night Shift Bonus"));
            }

            components.add(DefaultTax.PERSONAL_INCOME_TAX);

            if (random.nextDouble() < 0.2) {
                String rate = random.nextBoolean() ? "25" : "33";  
                components.add(new PercentageDeduction(standardInterval, rate, "Alimony"));
            }

            if (random.nextDouble() < 0.7) {
                long advanceAmount = (15000L + random.nextInt(15001)) * 100L;
                components.add(new FixedDeduction(currentMonthInterval, advanceAmount, "Advance Payment"));
            }

            registry.setComponentsFor(employee, components);

            try {
                System.out.println(engine.runPayroll(employee, YearMonth.of(2026, 1), "50").report());
            } catch (Exception e) {
                System.err.println("The calculation was rejected for employee #" + employee.id() + ": " + e.getMessage());
            }
            System.out.println("\n");
        }
    }
}
