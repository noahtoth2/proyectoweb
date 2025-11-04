package com.proyecto.demo.mappers;

import com.proyecto.demo.dto.UserDTO;
import com.proyecto.demo.models.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {
    
    public UserDTO toDTO(User user) {
        if (user == null) return null;
        
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setActivo(user.getActivo());
        dto.setFechaCreacion(user.getFechaCreacion());
        dto.setUltimoAcceso(user.getUltimoAcceso());
        
        dto.setRoles(
            user.getRoles().stream()
                .map(role -> role.getName().name().replace("ROLE_", ""))
                .collect(Collectors.toSet())
        );
        
        if (user.getBarcoSeleccionado() != null) {
            dto.setBarcoSeleccionadoId(user.getBarcoSeleccionado().getId());
            // Usar el nombre del modelo en lugar de un campo inexistente
            if (user.getBarcoSeleccionado().getModelo() != null) {
                dto.setBarcoSeleccionadoNombre(user.getBarcoSeleccionado().getModelo().getNombre());
            }
        }
        
        return dto;
    }
}
