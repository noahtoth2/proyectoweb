package com.proyecto.demo.controllers;

import com.proyecto.demo.dto.*;
import com.proyecto.demo.models.Barco;
import com.proyecto.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        AuthResponse response = userService.register(request);
        
        if (response.getMessage() != null && response.getMessage().startsWith("Error")) {
            return ResponseEntity.badRequest().body(response);
        }
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = userService.login(request);
        
        if (response.getMessage() != null && response.getMessage().startsWith("Error")) {
            return ResponseEntity.badRequest().body(response);
        }
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/select-barco")
    public ResponseEntity<?> selectBarco(@RequestParam Long userId, @RequestParam Long barcoId) {
        try {
            UserDTO user = userService.selectBarco(userId, barcoId);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/available-barcos")
    public ResponseEntity<List<Barco>> getAvailableBarcos(@RequestParam Long userId) {
        List<Barco> availableBarcos = userService.getAvailableBarcos(userId);
        return ResponseEntity.ok(availableBarcos);
    }
    
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
    
    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }
    
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/users/{id}/toggle-status")
    public ResponseEntity<UserDTO> toggleUserStatus(@PathVariable Long id) {
        UserDTO user = userService.toggleUserStatus(id);
        return ResponseEntity.ok(user);
    }
}
