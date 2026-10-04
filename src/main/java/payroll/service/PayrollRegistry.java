package payroll.service;

import payroll.domain.Employee;
import payroll.rule.CalculationComponent;

import java.util.*;

/**
 * A registry that holds the active calculation rules for each employee.
 */
public class PayrollRegistry {
    private final Map<Employee, List<CalculationComponent>> registry = new HashMap<>();

    public PayrollRegistry() {

    }

    public Map<Employee, List<CalculationComponent>> getRegistry() {
        Map<Employee, List<CalculationComponent>> safeMap = new HashMap<>();
        for (var entry : registry.entrySet()) {
            safeMap.put(entry.getKey(), Collections.unmodifiableList(entry.getValue()));
        }
        return Collections.unmodifiableMap(safeMap);
    }

    /**
     * Retrieves an unmodifiable list of components for a specific employee.
     *
     * @param employee the target employee
     * @return the list of active calculation components
     * @throws IllegalArgumentException if the employee is not registered
     */
    public List<CalculationComponent> getComponentsFor(Employee employee) {
        if (!registry.containsKey(employee)) {
            throw new IllegalArgumentException("There is no such an employee registered");
        }
        return Collections.unmodifiableList(registry.get(employee));
    }

    public void setComponentsFor(Employee employee, CalculationComponent component) {
        registry.computeIfAbsent(employee, (k) -> new ArrayList<>()).add(component);
    }

    /**
     * Assigns a list of calculation components to an employee.
     *
     * @param employee   the target employee
     * @param components the components to assign
     */
    public void setComponentsFor(Employee employee, List<CalculationComponent> components) {
        registry.computeIfAbsent(employee, (k) -> new ArrayList<>()).addAll(components);
    }

}
