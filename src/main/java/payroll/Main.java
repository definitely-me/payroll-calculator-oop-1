package payroll;

import payroll.domain.Employee;
import payroll.domain.DateInterval;
import payroll.domain.PayslipVersion;
import payroll.rule.*;
import payroll.service.PayrollSystem;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        var system = new PayrollSystem();

        DateInterval standardInterval = new DateInterval("2023-01-01", "2030-12-31");
        DateInterval currentMonthInterval = new DateInterval("2026-01-01", "2026-01-31");
        YearMonth targetMonth = YearMonth.of(2026, 1);


        var employee = new Employee();

        system.registerEmployee(employee);

        List<CalculationComponent> initialComponents = new ArrayList<>();
        CalculationComponent baseSalary = new FixedAccrual(standardInterval, 100000_00L, "Salary");
        initialComponents.add(baseSalary);

        initialComponents.add(DefaultTax.PERSONAL_INCOME_TAX);

        system.addComponents(employee, initialComponents);

        try {
            PayslipVersion version1 = system.runPayroll(employee, targetMonth, "50");
            System.out.println("--- VERSION 1 (Base Salary + Tax) ---");
            System.out.println(version1.payslip().toString());

            CalculationComponent bonus = new FixedAccrual(currentMonthInterval, 20000_00L, "Quarterly Bonus");
            system.addComponent(employee, bonus);


            CalculationComponent updatedSalary = new FixedAccrual(standardInterval, 150000_00L, "Salary");
            system.replaceComponent(employee, baseSalary, updatedSalary);
            system.addComponent(employee, updatedSalary);

            PayslipVersion version2 = system.runPayroll(employee, targetMonth, "50");
            System.out.println("\n--- VERSION 2 (Increased Salary + Tax) ---");
            System.out.println(version2.payslip().toString());

            System.out.println("\n--- COMPARE VERSIONS API ---");
            String diff = system.reportDiff(version2, version1);
            System.out.println(diff);


        } catch (Exception e) {
            System.err.println("Error for employee: " + e.fillInStackTrace());
        }
    }
}

