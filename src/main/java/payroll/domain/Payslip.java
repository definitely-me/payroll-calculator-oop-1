package payroll.domain;

import payroll.rule.Reportable;

import java.util.List;

/**
 * Represents a generated payslip document for an employee.
 * Contains the detailed list of calculation steps and the final net pay.
 */
public class Payslip implements Reportable {

    private final Employee employee;
    private final List<PayslipLine> payslipLines;
    private final long total;

    /**
     * Creates a new payslip.
     *
     * @param employee     the employee this payslip belongs to
     * @param payslipLines the detailed lines of calculation
     * @param total        the final calculated net pay (in kopecks)
     */
    public Payslip(Employee employee, List<PayslipLine> payslipLines, long total) {
        this.employee = employee;
        this.payslipLines = List.copyOf(payslipLines);
        this.total = total;
    }

    @Override
    public String report() {
        String cap = "==================================================" +
                "======================================================================================";
        StringBuilder report = new StringBuilder();

        report.append(cap).append("\n");
        report.append(String.format("| %45s %d;   Net Pay: %d rub. %02d kop. %-54s|", "Employee id: ",
                employee.id(),
                total / 100,
                Math.abs(total % 100), " "))
                .append("\n");
        report.append(cap).append("\n");
        report.append(String.format("|     %-41s |          %-17s |          %-17s |          %-17s |",
                "Calculation Component",
                "Before",
                "Diff",
                "After")).append("\n");
        report.append(cap).append("\n");
        for (var line : payslipLines) {
            report.append(line).append("\n");
        }
        report.append(cap).append("\n");
        return report.toString();
    }
}
