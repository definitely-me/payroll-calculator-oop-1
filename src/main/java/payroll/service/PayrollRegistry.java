package payroll.service;

import payroll.domain.Employee;
import payroll.rule.CalculationComponent;

import java.util.*;

class PayrollRegistry {
    private final Map<Employee, List<CalculationComponent>> registry = new HashMap<>();

    PayrollRegistry() {
    }

    void replaceComponent(Employee employee, CalculationComponent old, CalculationComponent updated) {
        List<CalculationComponent> components = getPrivateComponentsFor(employee);
        if (components.stream().noneMatch((c) -> c.getId().equals(old.getId()))) {
            throw new IllegalArgumentException("There is no such a component to update");
        }
        deleteComponent(employee, old);
        components.add(updated);

    }

    void deleteComponent(Employee employee, CalculationComponent deleted) {
        List<CalculationComponent> components = getPrivateComponentsFor(employee);
        components.removeIf((c) -> c.getId().equals(deleted.getId()));
    }

    private List<CalculationComponent> getPrivateComponentsFor(Employee employee) {
        return registry.get(employee);
    }

    Set<Employee> getAllEmployees() {
        return Collections.unmodifiableSet(registry.keySet());
    }

    void registerEmployee(Employee employee) {
        if (registry.containsKey(employee)) {
            throw new IllegalArgumentException("There is already such an employee registered");
        }
        registry.put(employee, new ArrayList<>());
    }

    List<CalculationComponent> getComponentsFor(Employee employee) {
        if (!registry.containsKey(employee)) {
            throw new IllegalArgumentException("There is no such an employee registered");
        }
        return Collections.unmodifiableList(registry.get(employee));
    }

    void setComponentFor(Employee employee, CalculationComponent component) {
        if (!registry.containsKey(employee)) {
            throw new IllegalArgumentException("There is no such an employee registered");
        }
        registry.get(employee).add(component);
    }

    void setComponentsFor(Employee employee, List<CalculationComponent> components) {
        if (!registry.containsKey(employee)) {
            throw new IllegalArgumentException("There is no such an employee registered");
        }
        registry.put(employee, new ArrayList<>(components));
    }

}
