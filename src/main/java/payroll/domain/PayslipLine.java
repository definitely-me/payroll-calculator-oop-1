package payroll.domain;

public record PayslipLine(String report, long before, long diff) {


    @Override
    public String toString() {
        return String.format("%-30s | %10d | %10d | %10d", report, before, Math.abs(diff), before - diff);
    }
}
