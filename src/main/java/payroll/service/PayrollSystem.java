package payroll.service;

import payroll.domain.Employee;
import payroll.domain.PayslipDiffReport;
import payroll.domain.PayslipVersion;
import payroll.rule.CalculationComponent;
import payroll.rule.OverdeductionException;
import payroll.util.DTOUtils;

import java.time.YearMonth;
import java.util.List;
import java.util.Set;

public class PayrollSystem implements PayrollApi {

    private final PayrollRegistry prlRegistry = new PayrollRegistry();
    private final PayslipRegistry pspRegistry = new PayslipRegistry();

    private final PayrollEngine engine = new PayrollEngine(prlRegistry, pspRegistry);

    @Override
    public void registerEmployee(Employee employee) {
        prlRegistry.registerEmployee(employee);
    }

    @Override
    public void addComponent(Employee employee, CalculationComponent component) {
        prlRegistry.setComponentFor(employee, component);
    }

    @Override
    public void addComponents(Employee employee, List<CalculationComponent> components) {
        prlRegistry.setComponentsFor(employee, components);
    }

    @Override
    public void replaceComponent(Employee employee, CalculationComponent old, CalculationComponent updated) {
        prlRegistry.replaceComponent(employee, old, updated);
    }

    @Override
    public void deleteComponent(Employee employee, CalculationComponent deleted) {
        prlRegistry.deleteComponent(employee, deleted);
    }

    @Override
    public PayslipVersion runPayroll(Employee employee, YearMonth period, String overdeductionRate) throws OverdeductionException {
        return engine.runPayroll(employee, period, overdeductionRate);
    }

    @Override
    public PayslipDiffReport getDiffReport(PayslipVersion one, PayslipVersion other) {
        if (one.createdAt().isAfter(other.createdAt())) {
            return DTOUtils.reportDiff(other.payslip(), one.payslip());
        }
        return DTOUtils.reportDiff(one.payslip(), other.payslip());
    }

    @Override
    public PayslipDiffReport getDiffReport(PayslipDiffReport one, PayslipDiffReport other) {
        return DTOUtils.reportDiff(one, other);
    }

    @Override
    public String reportDiff(PayslipVersion one, PayslipVersion other) {
        return getDiffReport(one, other).toString();
    }

    @Override
    public List<PayslipVersion> getPayslips(Employee employee, java.time.Month month) {
        return pspRegistry.getPayslipsFor(employee, month);
    }

    @Override
    public List<CalculationComponent> getComponents(Employee employee) {
        return prlRegistry.getComponentsFor(employee);
    }

    @Override
    public Set<Employee> getAllEmployees() {
        return prlRegistry.getAllEmployees();
    }

    @Override
    public List<PayslipVersion> getAllPayslips(Employee employee) {
        return pspRegistry.getAllPayslipsFor(employee);
    }
}
