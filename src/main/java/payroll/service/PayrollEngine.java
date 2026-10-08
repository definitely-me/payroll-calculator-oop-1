package payroll.service;

import payroll.domain.Employee;
import payroll.domain.Payslip;
import payroll.domain.PayslipLine;
import payroll.domain.PayslipVersion;
import payroll.rule.*;
import payroll.util.CalculationUtils;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

class PayrollEngine {

    private final PayrollRegistry prlRegistry;

    private final PayslipRegistry pspRegistry;

    PayrollEngine(PayrollRegistry prlRegistry, PayslipRegistry pspRegistry) {
        this.prlRegistry = prlRegistry;
        this.pspRegistry = pspRegistry;
    }

    PayslipVersion runPayroll(Employee employee, YearMonth period, String overdeductionRate) throws OverdeductionException {
        long total = 0;
        long gross = 0;
        long totalDeductions = 0;

        final List<CalculationComponent> calculationComponents = prlRegistry.getComponentsFor(employee);

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
            total = a.calculate(total, period);
            long diff = total - before;
            payslipLines.add(new PayslipLine(a.report(), a.getCategory(), before, diff));
        }

        gross = total;

        for (Tax t : taxes) {
            long before = total;
            total = t.calculate(total, period);
            long diff = total - before;
            payslipLines.add(new PayslipLine(t.report(), t.getCategory(), before, diff));
            totalDeductions += diff;
        }
        for (Deduction d : deductions) {
            long before = total;
            total = d.calculate(total, period);
            long diff = total - before;
            totalDeductions += diff;
            payslipLines.add(new PayslipLine(d.report(), d.getCategory(), before, diff));
        }
        if (Math.abs(totalDeductions) > CalculationUtils.multiplyWithRate(gross, overdeductionRate)) {
            System.out.println("shit");
            throw new OverdeductionException("exceeded overdeduction rate");
        }
        PayslipVersion payslipVersion = new PayslipVersion(new Payslip(employee, payslipLines, total));
        pspRegistry.registerPayslipVersion(employee, period.getMonth(), payslipVersion);
        return payslipVersion;
    }
}