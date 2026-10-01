package payroll.service;

import payroll.domain.DateInterval;
import payroll.domain.Employee;
import payroll.domain.Payslip;
import payroll.domain.PayslipLine;
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

    public Payslip runPayroll(Employee employee, DateInterval interval) {
        long total = 0;

        List<CalculationComponent> calculationComponents = registry.getComponentsFor(employee);

        List<Accrual> accruals = new ArrayList<>();
        List<DefaultTax> taxes = new ArrayList<>();
        List<Deduction> deductions = new ArrayList<>();
        List<PayslipLine> payslipLines = new ArrayList<>();

        for (var component : calculationComponents) {
            switch (component) {
                case Accrual a -> accruals.add(a);
                case DefaultTax t -> taxes.add(t);
                case Deduction d -> deductions.add(d);
            }
        }

        for (Accrual a : accruals) {
            long before = total;
            total = a.calculate(total);
            long diff = before - total;
            payslipLines.add(new PayslipLine(a.report(), before, diff));
        }
        for (DefaultTax t : taxes) {
            long before = total;
            total = t.calculate(total);
            long diff = before - total;
            payslipLines.add(new PayslipLine(t.report(), before, diff));
        }
        for (Deduction d : deductions) {
            long before = total;
            total = d.calculate(total);
            long diff = before - total;
            payslipLines.add(new PayslipLine(d.report(), before, diff));
        }
        return new Payslip(employee, payslipLines, total);
    }
}