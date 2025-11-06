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

    console.log('Intentando login con:', this.username);

    fetch('http://localhost:8080/api/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username: this.username, password: this.password })
    })
    .then(response => {
      console.log('Response status:', response.status);
      return response.json();
    })
    .then((data: AuthResponse) => {
      console.log('Login response:', data);
      
      if (data.message && data.message.startsWith('Error')) {
        this.errorMessage.set(data.message);
        return;
      }
      
      // Guardar info de usuario en localStorage
      localStorage.setItem('currentUser', this.username);
      localStorage.setItem('userId', data.user.id.toString());
      localStorage.setItem('userRoles', JSON.stringify(data.user.roles));
      localStorage.setItem('authToken', data.token);
      
      console.log('Roles del usuario:', data.user.roles);
      console.log('Barco seleccionado:', data.user.barcoSeleccionado);
      
      // Redireccionar según el rol (los roles vienen sin el prefijo ROLE_)
      if (data.user.roles.includes('ADMIN')) {
        console.log('Redirigiendo a admin panel');
        // Admin va al panel de administración
        this.router.navigate(['/admin']);
      } else if (data.user.roles.includes('USER')) {
        // Usuario normal: verificar si ya seleccionó barco
        if (data.user.barcoSeleccionado) {
          console.log('Usuario tiene barco, redirigiendo a lobby');
          localStorage.setItem('barcoSeleccionadoId', data.user.barcoSeleccionado.id.toString());
          this.router.navigate(['/lobby']);
        } else {
          console.log('Usuario sin barco, redirigiendo a selección');
          // Ir a selección de barco
          this.router.navigate(['/select-barco']);
        }
      } else {
        console.log('Usuario sin roles reconocidos:', data.user.roles);
        this.errorMessage.set('El usuario no tiene roles asignados');
      }
    })
    .catch(error => {
      this.errorMessage.set('Error de conexión. Intenta nuevamente.');
      console.error('Error completo:', error);
    });
  }
  
  goToRegister() {
    this.router.navigate(['/register']);
  }
}