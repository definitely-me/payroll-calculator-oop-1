package payroll.service;

import payroll.domain.Employee;
import payroll.domain.PayslipVersion;
import payroll.rule.CalculationComponent;
import payroll.rule.OverdeductionException;

import java.time.Month;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Set;
import payroll.domain.PayslipDiffReport;
import payroll.domain.PayslipLine;

/**
 * Main API for the Payroll System.
 */
interface PayrollApi {

    /**
     * Registers an employee.
     */
    void registerEmployee(Employee employee);

    /**
     * Adds a calculation component to the employee.
     */
    void addComponent(Employee employee, CalculationComponent component);

    /**
     * Adds multiple calculation components.
     */
    void addComponents(Employee employee, List<CalculationComponent> components);

    /**
     * Replaces an existing component with a new one for the specified employee.
     */
    void replaceComponent(Employee employee, CalculationComponent old, CalculationComponent updated);

    /**
     * Removes a component from the employee.
     */
    void deleteComponent(Employee employee, CalculationComponent deleted);

    /**
     * Executes the payroll calculation for a given period.
     * <p>
     * Components are executed in order: Accruals -> Taxes -> Deductions.
     * The resulting payslip has a tabular output syntax:
     * | Calculation Component | Before | Diff | After |
     * </p>
     *
     * @param employee          the target employee
     * @param period            the target month and year
     * @param overdeductionRate maximum allowed deduction rate (e.g. "50")
     * @return the computed payslip version
     * @throws OverdeductionException if deductions exceed the allowed rate
     */
    PayslipVersion runPayroll(Employee employee, YearMonth period, String overdeductionRate) throws OverdeductionException;

    /**
     * Calculates the detailed object-based difference between two payslip versions.
     */
    PayslipDiffReport getDiffReport(PayslipVersion one, PayslipVersion other);

    /**
     * Calculates a second-order diff (diff of diffs) by comparing two diff reports.
     */
    PayslipDiffReport getDiffReport(PayslipDiffReport one, PayslipDiffReport other);

    /**
     * Generates a tabular diff report between two payslip versions.
     * <p>
     * This is a convenience method that calls {@link #getDiffReport} and returns its {@code toString()} representation.
     * Components are listed in the same execution order as in {@link #runPayroll}.
     * Output syntax: | Calculation Component | Before Delta | Action Delta | After Delta |
     * <br>
     * If a version contains multiple components of the same category, they are grouped
     * and displayed with a "Σ" prefix and a "(Merged)" suffix. If there is only one
     * component of a type, the report simply shows that single component as is.
     * </p>
     *
     * @param one   the first payslip version
     * @param other the second payslip version
     * @return formatted console diff report
     */
    String reportDiff(PayslipVersion one, PayslipVersion other);

    /**
     * Retrieves payslips for an employee in a specific month.
     */
    List<PayslipVersion> getPayslips(Employee employee, java.time.Month month);

    /**
     * Retrieves all historical payslips for an employee.
     */
    List<PayslipVersion> getAllPayslips(Employee employee);

    /**
     * Retrieves all active calculation components for an employee.
     */
    List<CalculationComponent> getComponents(Employee employee);

    /**
     * Retrieves all registered employees.
     */
    Set<Employee> getAllEmployees();
}