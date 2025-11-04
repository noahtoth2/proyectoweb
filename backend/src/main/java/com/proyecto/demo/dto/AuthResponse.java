package com.proyecto.demo.dto;

public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private UserDTO user;
    private String message;
    
    // Constructores
    public AuthResponse() {}
    
    public AuthResponse(String token, UserDTO user) {
        this.token = token;
        this.user = user;
    }
    
    public AuthResponse(String message) {
        this.message = message;
    }
    
    // Getters y Setters
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public UserDTO getUser() {
        return user;
    }
    
    public void setUser(UserDTO user) {
        this.user = user;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}
