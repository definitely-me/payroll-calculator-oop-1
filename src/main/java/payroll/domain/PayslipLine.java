package payroll.domain;

public record PayslipLine(String report, String category, long before, long diff) {

    @Override
    public String toString() {
        return String.format("| %-45s | %13d rub. %02d kop. | %13d rub. %02d kop. | %13d rub. %02d kop. |",
                report,
                before / 100, Math.abs(before) % 100,
                diff / 100, Math.abs(diff) % 100,
                (before + diff) / 100, Math.abs(before + diff) % 100);
    }
    //Math.abs() to every kop. val cuz rub. val shows the sign
}
