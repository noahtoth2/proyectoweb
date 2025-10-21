package com.proyecto.demo.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.demo.dto.BarcoDTO;
import com.proyecto.demo.services.BarcoService;
import com.proyecto.demo.mappers.BarcoMapper;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.proyecto.demo.dto.BarcoDTO;
import com.proyecto.demo.services.BarcoService;

@WebMvcTest(controllers = BarcoController.class)
@AutoConfigureWebMvc
@Import({BarcoService.class, BarcoMapper.class})
public class BarcoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BarcoService barcoService;

    @Autowired
    private ObjectMapper objectMapper;

    private BarcoDTO barcoDTO;
    private List<BarcoDTO> barcoDTOs;

    @BeforeEach
    void setUp() {
        barcoDTO = new BarcoDTO();
        barcoDTO.setId(1L);
        barcoDTO.setVelocidadX(10.0);
        barcoDTO.setVelocidadY(20.0);

        barcoDTOs = Arrays.asList(barcoDTO);
    }

    @Test
    void whenGetBarcos_thenReturnJsonArray() throws Exception {
        when(barcoService.listarBarcos()).thenReturn(barcoDTOs);

        mockMvc.perform(get("/api/barco/list")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(barcoDTO.getId()))
                .andExpect(jsonPath("$[0].velocidadX").value(barcoDTO.getVelocidadX()))
                .andExpect(jsonPath("$[0].velocidadY").value(barcoDTO.getVelocidadY()));
    }

    @Test
    void whenGetBarcoById_thenReturnBarco() throws Exception {
        when(barcoService.recuperarBarco(1L)).thenReturn(barcoDTO);

        mockMvc.perform(get("/api/barco/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(barcoDTO.getId()))
                .andExpect(jsonPath("$.velocidadX").value(barcoDTO.getVelocidadX()))
                .andExpect(jsonPath("$.velocidadY").value(barcoDTO.getVelocidadY()));
    }

    @Test
    void whenCreateBarco_thenReturnCreatedBarco() throws Exception {
        when(barcoService.crear(barcoDTO)).thenReturn(barcoDTO);

        mockMvc.perform(post("/api/barco")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(barcoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(barcoDTO.getId()))
                .andExpect(jsonPath("$.velocidadX").value(barcoDTO.getVelocidadX()))
                .andExpect(jsonPath("$.velocidadY").value(barcoDTO.getVelocidadY()));
    }

    @Test
    void whenUpdateBarco_thenReturnUpdatedBarco() throws Exception {
        when(barcoService.actualizarBarco(barcoDTO)).thenReturn(barcoDTO);

        mockMvc.perform(put("/api/barco")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(barcoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(barcoDTO.getId()))
                .andExpect(jsonPath("$.velocidadX").value(barcoDTO.getVelocidadX()))
                .andExpect(jsonPath("$.velocidadY").value(barcoDTO.getVelocidadY()));
    }

    @Test
    void whenDeleteBarco_thenReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/barco/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}