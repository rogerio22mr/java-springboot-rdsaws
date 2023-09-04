package ApiRest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDB {

    private Logger log = LoggerFactory.getLogger(LoadDB.class);

    @Bean
    CommandLineRunner commandLineRunner(EmployeeRepository repository) {
        return args -> {
            log.info("Log of event save user 1: " + repository.save(new Employee("Rogerio Ricardo",
                    "Rua dos trabalhadores, 293 Apartamento 11", "Desenvolvedor")));
            log.info("Log of event save user 2: " + repository.save(new Employee("Maria Silva",
                    "avenida silveira dutra 1002", "Chef")));
        };
    }

}
