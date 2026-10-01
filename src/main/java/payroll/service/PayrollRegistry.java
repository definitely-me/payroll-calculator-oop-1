package payroll.service;

import payroll.domain.Employee;
import payroll.rule.CalculationComponent;

import java.util.*;

public class PayrollRegistry {
    Map<Employee, List<CalculationComponent>> registry = new HashMap<>();

    public PayrollRegistry() {

    }

    public Map<Employee, List<CalculationComponent>> getRegistry() {
        Map<Employee, List<CalculationComponent>> safeMap = new HashMap<>();
        for (var entry : registry.entrySet()) {
            safeMap.put(entry.getKey(), Collections.unmodifiableList(entry.getValue()));
        }
        return Collections.unmodifiableMap(safeMap);
    }

    public List<CalculationComponent> getComponentsFor(Employee employee) {
        if (!registry.containsKey(employee)) {
            throw new IllegalArgumentException("There is no such an employee registered");
        }
        return Collections.unmodifiableList(registry.get(employee));
    }

    public void setComponentsFor(Employee employee, CalculationComponent component) {
        registry.computeIfAbsent(employee, (k) -> new ArrayList<>()).add(component);
    }

    public void setComponentsFor(Employee employee, List<CalculationComponent> components) {
        registry.computeIfAbsent(employee, (k) -> new ArrayList<>()).addAll(components);
    }

}
