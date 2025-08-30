package com.proyecto.demo.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//import co.edu.javeriana.ejemplojpa.model.Company;
/*import co.edu.javeriana.ejemplojpa.model.Person;
import co.edu.javeriana.ejemplojpa.repository.CompanyRepository;
import co.edu.javeriana.ejemplojpa.repository.PersonRepository;
/* */ 
import com.proyecto.demo.models.Jugador;
import com.proyecto.demo.models.Barco;
import com.proyecto.demo.models.Modelo;
import com.proyecto.demo.repository.BarcoRepository;
import com.proyecto.demo.repository.JugadorRepository;
import com.proyecto.demo.repository.ModeloRepository;

@Component
public class DbInitializer implements CommandLineRunner {

    @Autowired
    private JugadorRepository jugadorRepository;

    @Autowired
    private BarcoRepository barcoRepository;

    @Override
    public void run(String... args) throws Exception {
        Person person = personRepository.save(
                new Person("123",
                        "Alice",
                        "Alisson"));
        Person person2 = personRepository.save(
                new Person("456",
                        "Bob",
                        "Bobson"));
        Person person3 = personRepository.save(
                new Person("789",
                        "Carla",
                        "Carlson"));
        Company company = companyRepository.save(
            new Company("company1", "123445")
        );

        company.getEmployees().add(person);
        // person.setEmployer(company);
        person.getEmployers().add(company);
        personRepository.save(person);

        
    
    }

}