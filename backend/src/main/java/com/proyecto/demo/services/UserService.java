package com.proyecto.demo.services;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.demo.dto.AuthResponse;
import com.proyecto.demo.dto.LoginRequest;
import com.proyecto.demo.dto.RegisterRequest;
import com.proyecto.demo.dto.UserDTO;
import com.proyecto.demo.mappers.UserMapper;
import com.proyecto.demo.models.Barco;
import com.proyecto.demo.models.Role;
import com.proyecto.demo.models.Role.RoleName;
import com.proyecto.demo.models.User;
import com.proyecto.demo.repository.BarcoRepository;
import com.proyecto.demo.repository.RoleRepository;
import com.proyecto.demo.repository.UserRepository;
import com.proyecto.demo.security.JwtService;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private BarcoRepository barcoRepository;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Validar usuario único
        if (userRepository.existsByUsername(request.getUsername())) {
            return new AuthResponse("Error: El nombre de usuario ya existe");
        }
        
        if (userRepository.existsByEmail(request.getEmail())) {
            return new AuthResponse("Error: El email ya está registrado");
        }
        
        // Crear nuevo usuario con contraseña encriptada
        User user = new User(
            request.getUsername(),
            request.getEmail(),
            passwordEncoder.encode(request.getPassword())
        );
        
        // Asignar roles
        Set<Role> roles = new HashSet<>();
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            for (String roleName : request.getRoles()) {
                if ("admin".equalsIgnoreCase(roleName)) {
                    Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                        .orElseThrow(() -> new RuntimeException("Error: Role ADMIN not found"));
                    roles.add(adminRole);
                } else {
                    Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("Error: Role USER not found"));
                    roles.add(userRole);
                }
            }
        } else {
            // Por defecto: usuario normal
            Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role USER not found"));
            roles.add(userRole);
        }
        
        user.setRoles(roles);
        user = userRepository.save(user);
        
        // Generar JWT token
        String jwtToken = jwtService.generateToken(user);
        
        UserDTO userDTO = userMapper.toDTO(user);
        return new AuthResponse(jwtToken, userDTO);
    }
    
    public AuthResponse login(LoginRequest request) {
        // Autenticar usando Spring Security
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
            )
        );
        
        // Si llega aquí, la autenticación fue exitosa
        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        if (!user.getActivo()) {
            throw new RuntimeException("Error: Usuario desactivado");
        }
        
        // Actualizar último acceso
        user.setUltimoAcceso(LocalDateTime.now());
        userRepository.save(user);
        
        // Generar JWT token
        String jwtToken = jwtService.generateToken(user);
        
        UserDTO userDTO = userMapper.toDTO(user);
        return new AuthResponse(jwtToken, userDTO);
    }
    
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
            .map(userMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
            .map(userMapper::toDTO)
            .orElse(null);
    }
    
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    
    @Transactional
    public UserDTO toggleUserStatus(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        user.setActivo(!user.getActivo());
        user = userRepository.save(user);
        
        return userMapper.toDTO(user);
    }
    
    @Transactional
    public UserDTO selectBarco(Long userId, Long barcoId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        // Verificar que el usuario es ROLE_USER
        if (!user.isUser()) {
            throw new RuntimeException("Solo los usuarios pueden seleccionar barcos");
        }
        
        // Verificar que el barco no esté seleccionado por otro usuario
        List<User> usuariosConBarco = userRepository.findAll().stream()
            .filter(u -> u.getBarcoSeleccionado() != null && 
                        u.getBarcoSeleccionado().getId().equals(barcoId) &&
                        !u.getId().equals(userId))
            .collect(Collectors.toList());
        
        if (!usuariosConBarco.isEmpty()) {
            throw new RuntimeException("Este barco ya está seleccionado por otro usuario");
        }
        
        Barco barco = barcoRepository.findById(barcoId)
            .orElseThrow(() -> new RuntimeException("Barco no encontrado"));
        
        user.setBarcoSeleccionado(barco);
        user = userRepository.save(user);
        
        return userMapper.toDTO(user);
    }
    
    public List<Barco> getAvailableBarcos(Long userId) {
        List<Barco> allBarcos = barcoRepository.findAll();
        List<User> allUsers = userRepository.findAll();
        
        // Obtener IDs de barcos ya seleccionados por otros usuarios
        Set<Long> selectedBarcoIds = allUsers.stream()
            .filter(u -> u.getBarcoSeleccionado() != null && !u.getId().equals(userId))
            .map(u -> u.getBarcoSeleccionado().getId())
            .collect(Collectors.toSet());
        
        // Devolver solo barcos no seleccionados
        return allBarcos.stream()
            .filter(b -> !selectedBarcoIds.contains(b.getId()))
            .collect(Collectors.toList());
    }
}
