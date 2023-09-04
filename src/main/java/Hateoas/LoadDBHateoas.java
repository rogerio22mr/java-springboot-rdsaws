package Hateoas;

import Hateoas.Entities.EmployeeHateoas;
import Hateoas.Entities.OrderHateoas;
import Hateoas.Entities.Status;
import Hateoas.Repositories.EmployeeRepositoryHateoas;
import Hateoas.Repositories.OrderRepositoryHateoas;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDBHateoas {

    private Logger log = LoggerFactory.getLogger(LoadDBHateoas.class);

    @Bean
    CommandLineRunner commandLineRunner(EmployeeRepositoryHateoas employeeRepository, OrderRepositoryHateoas orderRepository) {
        return args -> {
            log.info("Log of event save user 1: " + employeeRepository.save(new EmployeeHateoas("Rogerio Ricardo",
                    "Rua dos trabalhadores, 293 Apartamento 11", "Desenvolvedor")));
            log.info("Log of event save user 2: " + employeeRepository.save(new EmployeeHateoas("Maria Silva",
                    "avenida silveira dutra 1002", "Chef")));
            log.info("Log of event save order 1: " + orderRepository.save(new OrderHateoas(Status.COMPLETED, "review")));
            log.info("Log of event save order 2: " + orderRepository.save(new OrderHateoas(Status.IN_PROGRES, "travel")));
            log.info("Log of event save order 3: " + orderRepository.save(new OrderHateoas(Status.IN_PROGRES, "sale")));
        };
    }


}
