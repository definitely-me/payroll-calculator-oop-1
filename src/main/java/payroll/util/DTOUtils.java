package payroll.util;

import payroll.domain.Payslip;
import payroll.domain.PayslipLine;
import payroll.domain.PayslipDiffReport;

import java.util.*;

public final class DTOUtils {

    public static PayslipLine mergeLines(List<PayslipLine> lines) {
        if (lines == null || lines.isEmpty()) {
            throw new IllegalArgumentException("lines.size() should be not empty or null");
        }
        if (lines.size() == 1) {
            return lines.getFirst();
        }

        String category = lines.getFirst().category();
        String mergedReport = "Σ " + category + " (Merged)";

        long totalBefore = lines.stream().mapToLong(PayslipLine::before).sum();
        long totalDiff = lines.stream().mapToLong(PayslipLine::diff).sum();

        return new PayslipLine(mergedReport, category, totalBefore, totalDiff);
    }

    public static PayslipLine comparePayslipLines(PayslipLine one, PayslipLine other) {
        if (one.before() == other.before() && one.diff() == other.diff()) {
            return null;
        }

        if (!one.category().equals(other.category())) {
            return null;
        }

        long beforeDelta = one.diff();
        long actionDelta = other.diff() - one.diff();

        String diffName = "Δ " + other.report();

        return new PayslipLine(diffName, other.category(), beforeDelta, actionDelta);
    }

    public static PayslipDiffReport reportDiff(Payslip one, Payslip other) {
        return calculateDiff(one.employee(), one.total(), one.payslipLines(), other.total(), other.payslipLines());
    }

    public static PayslipDiffReport reportDiff(PayslipDiffReport one, PayslipDiffReport other) {
        return calculateDiff(one.employee(), one.total(), one.payslipLines(), other.total(), other.payslipLines());
    }

    private static PayslipDiffReport calculateDiff(payroll.domain.Employee employee, long oneTotal, List<PayslipLine> oneLines, long otherTotal, List<PayslipLine> otherLines) {
        Map<String, List<PayslipLine>> oneSortedLines = new LinkedHashMap<>();
        Map<String, List<PayslipLine>> otherSortedLines = new LinkedHashMap<>();

        for (var line : oneLines) {
            oneSortedLines.computeIfAbsent(line.category(), (c) -> new ArrayList<>()).add(line);
        }
        for (var line : otherLines) {
            otherSortedLines.computeIfAbsent(line.category(), (c) -> new ArrayList<>()).add(line);
        }

        Set<String> keyset = new LinkedHashSet<>(otherSortedLines.keySet());
        keyset.addAll(otherSortedLines.keySet());

        List<PayslipLine> diffLines = new ArrayList<>();

        for (var key : keyset) {
            List<PayslipLine> oneList = oneSortedLines.get(key);
            List<PayslipLine> otherList = otherSortedLines.get(key);
            PayslipLine diffLine = null;

            if (oneSortedLines.containsKey(key) && otherSortedLines.containsKey(key)) {
                if (new HashSet<>(oneList).equals(new HashSet<>(otherList))) {
                    continue;
                }
                diffLine = comparePayslipLines(mergeLines(oneList), mergeLines(otherList));
            }
            else if (!oneSortedLines.containsKey(key) && otherSortedLines.containsKey(key)) {
                diffLine = comparePayslipLines(
                        new PayslipLine(otherList.getFirst().report(), otherList.getFirst().category(), 0, 0),
                        mergeLines(otherList));
            }
            else if (oneSortedLines.containsKey(key) && !otherSortedLines.containsKey(key)){
                diffLine = comparePayslipLines(
                        new PayslipLine(oneList.getFirst().report(), oneList.getFirst().category(), 0, 0),
                        mergeLines(oneList));
            }

            if (diffLine != null) {
                diffLines.add(diffLine);
            }
        }

        long totalDiff = otherTotal - oneTotal;
        return new PayslipDiffReport(employee, totalDiff, diffLines);
    }
}
