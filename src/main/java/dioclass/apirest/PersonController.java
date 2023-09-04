package dioclass.apirest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class PersonController {

    @Autowired
    private final PersonRepository personRepository;


    public PersonController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @GetMapping()
    public String helloWorld() {
        return ("This is my first API in Spring Boot!");
    }

    @GetMapping("/persons")
    public List<Person> getAllPerson() {
        return personRepository.findAll();
    }

    @GetMapping("/persons/{id}")
    public Optional<Person> getById(@PathVariable Long id) {
        return personRepository.findById(id);
    }
}
