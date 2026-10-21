# PayrollPipeline

A Java payroll calculation engine that processes salary components across date intervals. 

Execution order of operations:
1. **Accruals** (additions to salary, e.g., base salary, bonuses)
2. **Taxes** (mandatory withholdings, e.g., personal income tax)
3. **Deductions** (other subtractions, e.g., fines, alimony)

The system supports generating complete payslips, employee component management, versioning, and structural diff reporting between payslip versions.

---

## Architecture & Core Features

- **Sealed Interfaces:** `CalculationComponent` is a sealed interface permitted for `Accrual`, `Deduction`, and `Tax`.
- **Extensible Component Hierarchy:** A set of ready-to-use classes (`FixedAccrual`, `PercentageAccrual`, `ProportionalAccrual`, `FixedDeduction`, `PercentageDeduction`, etc.) allows defining calculation rules and behaviors instantly without custom boilerplate.
- **Date Intervals:** Each component operates within a defined `DateInterval`, allowing time-bounded rules and proportional calculations for partial months.
- **Overdeduction Guard:** Validates total deductions against a configured maximum rate (`OverdeductionException` is thrown if exceeded).
- **Money Representation:** Monetary values are stored as `long` to prevent floating-point precision errors. Rates and division use safe calculations with `BigDecimal`.
- **Diff & Versioning:** Tracks history via `PayslipVersion` and generates `PayslipDiffReport` comparing two versions or even comparing diffs (second-order diffs).

---

## Project Structure

- `payroll.domain` — Domain models (`Employee`, `Payslip`, `PayslipLine`, `PayslipVersion`, `PayslipDiffReport`, `DateInterval`).
- `payroll.rule` — Calculation rules and components (`CalculationComponent`, `Accrual`, `Tax`, `Deduction`, implementations like `FixedAccrual`, `DefaultTax`, etc.).
- `payroll.service` — Core system service and engine (`PayrollSystem`, `PayrollEngine`, `PayrollRegistry`, `PayslipRegistry`).
- `payroll.util` — Utility classes for money (`CalculationUtils`) and dates (`DateUtils`) calculations.
