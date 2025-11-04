import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

interface AuthResponse {
  token: string;
  type: string;
  user: {
    id: number;
    username: string;
    email: string;
    roles: string[];
    barcoSeleccionado: any;
    activo: boolean;
  };
  message?: string;
}

@Component({
  selector: 'app-auth',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.css']
})
export class AuthComponent {
  username = '';
  password = '';
  errorMessage = signal('');

  constructor(private router: Router) {}

  onLogin() {
    this.errorMessage.set('');
    
    if (!this.username || !this.password) {
      this.errorMessage.set('Usuario y contraseña son requeridos');
      return;
    }

    fetch('http://localhost:8080/api/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username: this.username, password: this.password })
    })
    .then(response => response.json())
    .then((data: AuthResponse) => {
      if (data.message && data.message.startsWith('Error')) {
        this.errorMessage.set(data.message);
        return;
      }
      
      // Guardar info de usuario en localStorage
      localStorage.setItem('currentUser', this.username);
      localStorage.setItem('userId', data.user.id.toString());
      localStorage.setItem('userRoles', JSON.stringify(data.user.roles));
      localStorage.setItem('authToken', data.token);
      
      // Redireccionar según el rol
      if (data.user.roles.includes('ROLE_ADMIN')) {
        // Admin va al panel de administración
        this.router.navigate(['/admin']);
      } else if (data.user.roles.includes('ROLE_USER')) {
        // Usuario normal: verificar si ya seleccionó barco
        if (data.user.barcoSeleccionado) {
          localStorage.setItem('barcoSeleccionadoId', data.user.barcoSeleccionado.id.toString());
          this.router.navigate(['/lobby']);
        } else {
          // Ir a selección de barco
          this.router.navigate(['/select-barco']);
        }
      }
    })
    .catch(error => {
      this.errorMessage.set('Error de conexión. Intenta nuevamente.');
      console.error('Error:', error);
    });
  }
  
  goToRegister() {
    this.router.navigate(['/register']);
  }
}