package payroll.service;

import payroll.domain.DateInterval;
import payroll.domain.Employee;
import payroll.domain.Payslip;
import payroll.rule.Accrual;
import payroll.rule.CalculationComponent;
import payroll.rule.Deduction;
import payroll.rule.DefaultTax;

import java.util.ArrayList;
import java.util.List;

public class PayrollEngine {

    private final PayrollRegistry registry;

    public PayrollEngine(PayrollRegistry registry) {
        this.registry = registry;
    }

    public void runPayroll(Employee employee, DateInterval interval) {
        long total = 0;

        Payslip payslip = new Payslip();
        List<CalculationComponent> calculationComponents = registry.getComponentsFor(employee);

        List<Accrual> accruals = new ArrayList<>();
        List<DefaultTax> taxes = new ArrayList<>();
        List<Deduction> deductions = new ArrayList<>();


        for (var component : calculationComponents) {
            switch (component) {
                case Accrual a -> accruals.add(a);
                case DefaultTax t -> taxes.add(t);
                case Deduction d -> deductions.add(d);
            }
        }

        for (Accrual a : accruals) {total = a.calculate(total);}
        for (DefaultTax t : taxes) {total = t.calculate(total);}
        for (Deduction d : deductions) {total = d.calculate(total);}

        System.out.println(total);

    }
}