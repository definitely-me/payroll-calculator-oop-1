package payroll.domain;

import payroll.rule.Reportable;

import java.util.ArrayList;
import java.util.List;

public class Payslip implements Reportable {

    Employee employee;
    List<PayslipLine> payslipLines = new ArrayList<>();
    long total;

    public Payslip(Employee employee, List<PayslipLine> payslipLines, long total) {
        this.employee = employee;
        this.payslipLines = payslipLines;
        this.total = total;
    }

    @Override
    public String report() {
        String report = "";
        for (var line : payslipLines) {
            report += line +"\n";
        }
        return report;
    }
}
