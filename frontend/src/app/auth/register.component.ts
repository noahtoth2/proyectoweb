import { Component, signal, OnDestroy } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

interface RegisterRequest {
  username: string;
  email: string;
  password: string;
  confirmPassword?: string;
  roles: string[];
}

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule, CommonModule],
  template: `
    <div class="register-container">
      <div class="register-card">
        <h2>Crear Cuenta</h2>
        
        @if (errorMessage()) {
          <div class="error-message">{{ errorMessage() }}</div>
        }
        
        <form (ngSubmit)="onSubmit()">
          <div class="form-group">
            <label>Nombre de Usuario</label>
            <input 
              type="text" 
              [(ngModel)]="username" 
              name="username" 
              required 
              placeholder="Ingresa tu usuario">
          </div>
          
          <div class="form-group">
            <label>Email</label>
            <input 
              type="email" 
              [(ngModel)]="email" 
              name="email" 
              required 
              placeholder="correo@ejemplo.com">
          </div>
          
          <div class="form-group">
            <label>Contraseña</label>
            <input 
              type="password" 
              [(ngModel)]="password" 
              name="password" 
              required 
              placeholder="Mínimo 6 caracteres">
          </div>
          
          <div class="form-group">
            <label>Confirmar Contraseña</label>
            <input 
              type="password" 
              [(ngModel)]="confirmPassword" 
              name="confirmPassword" 
              required 
              placeholder="Repite tu contraseña">
          </div>
          
          <div class="form-group">
            <label>Tipo de Cuenta</label>
            <select [(ngModel)]="accountType" name="accountType">
              <option value="user">Usuario (Jugador)</option>
              <option value="admin">Administrador</option>
            </select>
          </div>
          
          <button type="submit" class="btn-primary">Registrarse</button>
        </form>
        
        <div class="login-link">
          ¿Ya tienes cuenta? 
          <a (click)="goToLogin()">Inicia Sesión</a>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .register-container {
      display: flex;
      justify-content: center;
      align-items: center;
      min-height: 100vh;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      padding: 20px;
    }
    
    .register-card {
      background: white;
      border-radius: 10px;
      padding: 40px;
      box-shadow: 0 10px 40px rgba(0,0,0,0.2);
      width: 100%;
      max-width: 400px;
    }
    
    h2 {
      text-align: center;
      color: #333;
      margin-bottom: 30px;
    }
    
    .form-group {
      margin-bottom: 20px;
    }
    
    label {
      display: block;
      margin-bottom: 5px;
      color: #555;
      font-weight: 500;
    }
    
    input, select {
      width: 100%;
      padding: 12px;
      border: 1px solid #ddd;
      border-radius: 5px;
      font-size: 14px;
      box-sizing: border-box;
    }
    
    input:focus, select:focus {
      outline: none;
      border-color: #667eea;
    }
    
    .btn-primary {
      width: 100%;
      padding: 12px;
      background: #667eea;
      color: white;
      border: none;
      border-radius: 5px;
      font-size: 16px;
      cursor: pointer;
      transition: background 0.3s;
    }
    
    .btn-primary:hover {
      background: #5568d3;
    }
    
    .error-message {
      background: #fee;
      color: #c33;
      padding: 12px;
      border-radius: 5px;
      margin-bottom: 20px;
      text-align: center;
    }
    
    .login-link {
      text-align: center;
      margin-top: 20px;
      color: #666;
    }
    
    .login-link a {
      color: #667eea;
      cursor: pointer;
      text-decoration: none;
    }
    
    .login-link a:hover {
      text-decoration: underline;
    }
  `]
})
export class RegisterComponent implements OnDestroy {
  username = '';
  email = '';
  password = '';
  confirmPassword = '';
  accountType = 'user';
  
  errorMessage = signal('');
  private abortController: AbortController | null = null;
  
  constructor(private router: Router) {}

  ngOnDestroy() {
    if (this.abortController) {
      this.abortController.abort();
    }
  }
  
  onSubmit() {
    this.errorMessage.set('');
    
    // Validaciones
    if (!this.username || !this.email || !this.password || !this.confirmPassword) {
      this.errorMessage.set('Todos los campos son obligatorios');
      return;
    }
    
    if (this.password !== this.confirmPassword) {
      this.errorMessage.set('Las contraseñas no coinciden');
      return;
    }
    
    if (this.password.length < 6) {
      this.errorMessage.set('La contraseña debe tener al menos 6 caracteres');
      return;
    }
    
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(this.email)) {
      this.errorMessage.set('Email inválido');
      return;
    }
    
    // Crear objeto de registro
    const registerRequest: RegisterRequest = {
      username: this.username,
      email: this.email,
      password: this.password,
      roles: [this.accountType]
    };
    
    // Cancelar petición anterior si existe
    if (this.abortController) {
      this.abortController.abort();
    }
    this.abortController = new AbortController();
    
    // Llamar al servicio de autenticación
    fetch('http://localhost:8080/api/auth/register', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(registerRequest),
      signal: this.abortController.signal
    })
    .then(response => response.json())
    .then(data => {
      if (data.message && data.message.startsWith('Error')) {
        this.errorMessage.set(data.message);
      } else {
        // Guardar JWT token automáticamente después del registro
        if (data.token) {
          localStorage.setItem('jwt_token', data.token);
          localStorage.setItem('currentUser', JSON.stringify(data.user));
          localStorage.setItem('userId', data.user.id.toString());
          localStorage.setItem('userRoles', JSON.stringify(data.user.roles));
          console.log('✅ Usuario registrado y JWT guardado');
          
          // Redirigir al lobby directamente
          this.router.navigate(['/lobby']);
        } else {
          alert('Cuenta creada exitosamente. Por favor inicia sesión.');
          this.router.navigate(['/login']);
        }
      }
    })
    .catch(error => {
      if (error.name === 'AbortError') {
        console.log('Petición cancelada (navegación)');
        return;
      }
      this.errorMessage.set('Error al registrar. Intenta nuevamente.');
      console.error('Error:', error);
    });
  }
  
  goToLogin() {
    this.router.navigate(['/login']);
  }
}
