package com.proyecto.demo.dto;

import java.time.LocalDateTime;
import java.util.Set;

public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private Set<String> roles;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime ultimoAcceso;
    private Long barcoSeleccionadoId;
    private String barcoSeleccionadoNombre;
    
    // Constructores
    public UserDTO() {}
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public Set<String> getRoles() {
        return roles;
    }
    
    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
    
    public Boolean getActivo() {
        return activo;
    }
    
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public LocalDateTime getUltimoAcceso() {
        return ultimoAcceso;
    }
    
    public void setUltimoAcceso(LocalDateTime ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }
    
    public Long getBarcoSeleccionadoId() {
        return barcoSeleccionadoId;
    }
    
    public void setBarcoSeleccionadoId(Long barcoSeleccionadoId) {
        this.barcoSeleccionadoId = barcoSeleccionadoId;
    }
    
    public String getBarcoSeleccionadoNombre() {
        return barcoSeleccionadoNombre;
    }
    
    public void setBarcoSeleccionadoNombre(String barcoSeleccionadoNombre) {
        this.barcoSeleccionadoNombre = barcoSeleccionadoNombre;
    }
}
