package payroll.service;

import payroll.domain.Employee;
import payroll.domain.Payslip;
import payroll.domain.PayslipLine;
import payroll.rule.*;
import payroll.utils.CalculationUtils;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class PayrollEngine {

    private final PayrollRegistry registry;

    public PayrollEngine(PayrollRegistry registry) {
        this.registry = registry;
    }

    public Payslip runPayroll(Employee employee, YearMonth interval, String overdeductionRate) throws OverdeductionException {
        long total = 0;
        long gross = 0;
        long totalDeductions = 0;

        List<CalculationComponent> calculationComponents = registry.getComponentsFor(employee);

        List<Accrual> accruals = new ArrayList<>();
        List<Tax> taxes = new ArrayList<>();
        List<Deduction> deductions = new ArrayList<>();
        List<PayslipLine> payslipLines = new ArrayList<>();

        for (var component : calculationComponents) {
            switch (component) {
                case Accrual a -> accruals.add(a);
                case Tax t -> taxes.add(t);
                case Deduction d -> deductions.add(d);
            }
        }

        for (Accrual a : accruals) {
            long before = total;
            total = a.calculate(total, interval);
            long diff = before - total;
            payslipLines.add(new PayslipLine(a.report(), before, diff));
        }

        gross = total;

        for (Tax t : taxes) {
            long before = total;
            total = t.calculate(total, interval);
            long diff = before - total;
            payslipLines.add(new PayslipLine(t.report(), before, diff));
            totalDeductions += diff;
        }
        for (Deduction d : deductions) {
            long before = total;
            total = d.calculate(total, interval);
            long diff = before - total;
            totalDeductions += diff;
            payslipLines.add(new PayslipLine(d.report(), before, diff));
        }
        if (totalDeductions > CalculationUtils.multiplyWithRate(gross, overdeductionRate)) {
            System.out.println(CalculationUtils.multiplyWithRate(gross, overdeductionRate));
            System.out.println(totalDeductions);
            throw new OverdeductionException("exceeded overdeduction rate");
        }
        return new Payslip(employee, payslipLines, total);
    }
}