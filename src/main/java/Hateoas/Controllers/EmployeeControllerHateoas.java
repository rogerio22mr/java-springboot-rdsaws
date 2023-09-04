package Hateoas.Controllers;

import Hateoas.Entities.EmployeeHateoas;
import Hateoas.Entities.OrderHateoas;
import Hateoas.Exceptions.EmployeeNotFoundException;
import Hateoas.Repositories.EmployeeRepositoryHateoas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class EmployeeControllerHateoas {

    @Autowired
    private EmployeeRepositoryHateoas employeeRepository;

    @GetMapping("employees")
    ResponseEntity<List<EmployeeHateoas>> getAllEmployees() {
        Long idEmployee;
        Link linkUri;
        List<EmployeeHateoas> employeeList = employeeRepository.findAll();
        if (employeeList.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        for (EmployeeHateoas employee: employeeList) {
            idEmployee = employee.getId();
            linkUri = linkTo(methodOn(EmployeeControllerHateoas.class).getEmployeeById(idEmployee)).withSelfRel();
            employee.add(linkUri);
        }
        return new ResponseEntity<>(employeeList, HttpStatus.OK);
    }

    @GetMapping("employees/{id}")
    ResponseEntity<EmployeeHateoas> getEmployeeById(@PathVariable Long id) {
        Optional<EmployeeHateoas> employeePointer = employeeRepository.findById(id);
        if (employeePointer.isPresent()) {
            EmployeeHateoas employee = employeePointer.get();
            employee.add(linkTo(methodOn(EmployeeControllerHateoas.class).getAllEmployees()).withRel("All Employees"));
            return new ResponseEntity<>(employee, HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("employees")
    public EmployeeHateoas createEmployee(@RequestBody EmployeeHateoas employee) {
        return employeeRepository.save(employee);
    }

    @PutMapping("employees/{id}")
    public EmployeeHateoas updateEmployee(@RequestBody EmployeeHateoas updateEmployee , @PathVariable Long id) {
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
