package com.proyecto.demo.init;

import com.proyecto.demo.models.Role;
import com.proyecto.demo.models.Role.RoleName;
import com.proyecto.demo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Profile({"default"})
@Component
public class RoleDataInitializer {
    
    @Autowired
    private RoleRepository roleRepository;
    
    @PostConstruct
    public void init() {
        // Crear roles si no existen
        if (roleRepository.findByName(RoleName.ROLE_ADMIN).isEmpty()) {
            Role adminRole = new Role();
            adminRole.setName(RoleName.ROLE_ADMIN);
            roleRepository.save(adminRole);
            System.out.println("✓ Rol ADMIN creado");
        }
        
        if (roleRepository.findByName(RoleName.ROLE_USER).isEmpty()) {
            Role userRole = new Role();
            userRole.setName(RoleName.ROLE_USER);
            roleRepository.save(userRole);
            System.out.println("✓ Rol USER creado");
        }
    }
}
