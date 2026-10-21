package payroll.domain;

import java.util.List;

/**
 * Creates a new payslip.
 */
public record Payslip(Employee employee, List<PayslipLine> payslipLines, long total) {

    public Payslip {
        payslipLines = List.copyOf(payslipLines);
    }

    @Override
    public String toString() {
        String cap = "==================================================" +
                "======================================================================================";
        StringBuilder report = new StringBuilder();

        report.append(cap).append("\n");
        report.append(String.format("| %55s %.8s;   Net Pay: %d rub. %02d kop. %-37s|", "Employee id: ",
                        employee.id().toString(),
                        total / 100,
                        Math.abs(total % 100), " "))
                .append("\n");
        report.append(cap).append("\n");
        report.append(String.format("|            %-34s |          %-17s |          %-17s |          %-17s |",
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


