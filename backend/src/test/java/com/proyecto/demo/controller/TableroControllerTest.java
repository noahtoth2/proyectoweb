package com.proyecto.demo.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;


import com.proyecto.demo.models.Barco;
import com.proyecto.demo.repository.BarcoRepository;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("integration-testing")
public class TableroControllerTest {

    @Autowired
    private BarcoRepository barcorepository;

    
    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void init(){
        barcorepository.save(new Barco(0.0, 0.0));
    }
    


    
}