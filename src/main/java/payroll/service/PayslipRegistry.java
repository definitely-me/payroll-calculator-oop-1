package payroll.service;

import payroll.domain.Employee;
import payroll.domain.PayslipVersion;

import java.time.Month;
import java.util.*;

class PayslipRegistry {
    private final Map<Employee, Map<Month, List<PayslipVersion>>> registry = new HashMap<>();

    List<PayslipVersion> getAllPayslipsFor(Employee employee) {
        Map<Month, List<PayslipVersion>> empMap = registry.get(employee);
        if (empMap == null) {
            return Collections.emptyList();
        }
        return empMap.values().stream()
                .flatMap(List::stream)
                .toList();
    }

    List<PayslipVersion> getPayslipsFor(Employee employee, Month month) {
        Map<Month, List<PayslipVersion>> empMap = registry.get(employee);
        if (empMap == null || !empMap.containsKey(month)) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(empMap.get(month));
    }

    void registerPayslipVersion(Employee employee, Month month, PayslipVersion version) {
        registry.computeIfAbsent(employee, (k) -> new HashMap<>())
                .computeIfAbsent(month, (k) -> new ArrayList<>()).add(version);
    }
}
