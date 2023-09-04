package ApiRest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("employees")
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @GetMapping("employee/{id}")
    public Employee getById(@PathVariable Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    @PostMapping("employees")
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeRepository.save(employee);
    }

    @PutMapping("employees/{id}")
    public Employee createEmployee(@RequestBody Employee updateEmployee , @PathVariable Long id) {
        return employeeRepository.findById(id).map(employee -> {
            employee.setName(updateEmployee.getName());
            employee.setAddress(updateEmployee.getAddress());
            employee.setRole(updateEmployee.getRole());
            return employeeRepository.save(employee);
        }).orElseGet(() -> {
            updateEmployee.setId(id);
            return employeeRepository.save(updateEmployee);
        });
    }

    @DeleteMapping("employees/{id}")
    void deleteEmployee(@PathVariable Long id) {
        employeeRepository.deleteById(id);
    }

}
