package payroll.domain;

import java.util.List;

/**
 * Represents a complete diff report between two payslip versions.
 * <p>
 * The {@link #toString()} method formats the entire report into a console-friendly
 * tabular view, including headers and all calculated difference lines.
 * </p>
 */
public record PayslipDiffReport(Employee employee, List<PayslipLine> payslipLines, long total) {
    public PayslipDiffReport {
        payslipLines = List.copyOf(payslipLines);
    }

    public PayslipDiffReport(Employee employee, long totalDiff, List<PayslipLine> diffLines) {
        this(employee, diffLines, totalDiff);
    }

    @Override
    public String toString() {
        String cap = "==================================================" +
                "======================================================================================";
        StringBuilder report = new StringBuilder();

        report.append(cap).append("\n");
        report.append(String.format("| %60s %.8s...;   Δ Net Pay: %d rub. %2d kop.   %-25s|",
                "DIFF REPORT for Employee id: ", employee.id().toString(), total / 100,
                Math.abs(total % 100), " ")).append("\n");
        report.append(cap).append("\n");
        report.append(String.format("|            %-34s |        %-19s |        %-19s |        %-19s |",
                "Calculation Component", "Before Delta", "Action Delta", "After Delta")).append("\n");
        report.append(cap).append("\n");

        if (payslipLines.isEmpty()) {
            report.append(String.format("| %-142s |", "No calculation differences found between these versions.")).append("\n");
        } else {
            for (PayslipLine line : payslipLines) {
                report.append(line).append("\n");
            }
        }
        report.append(cap).append("\n");
        return report.toString();
    }
}
