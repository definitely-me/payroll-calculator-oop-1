package payroll;

import payroll.domain.Employee;
import payroll.domain.DateInterval;
import payroll.rule.Alimony;
import payroll.rule.DefaultTax;
import payroll.rule.Salary;
import payroll.service.PayrollEngine;
import payroll.service.PayrollRegistry;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        var vasya = new Employee(1);
        var registry = new PayrollRegistry();
        var engine = new PayrollEngine(registry);
        var alimony = new Alimony(new DateInterval("2023-10-01", "2026-10-01"), "30");
        var salary = new Salary(new DateInterval("2023-10-01", "2026-10-01"), 8000000);
        registry.setComponentsFor(vasya, List.of(alimony, salary, DefaultTax.PERSONAL_INCOME_TAX));
        System.out.println(engine.runPayroll(vasya, new DateInterval("2023-10-01", "2023-11-01")).report());

    }
}
