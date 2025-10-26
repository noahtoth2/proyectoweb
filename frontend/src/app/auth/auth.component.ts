import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-auth',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.css']
})
export class AuthComponent {
  isLogin = signal(true);
  loginData = {
    email: '',
    password: ''
  };
  registerData = {
    nombre: '',
    email: '',
    password: '',
    confirmPassword: ''
  };

  constructor(private router: Router) {}

  toggleMode() {
    this.isLogin.set(!this.isLogin());
  }

  onLogin() {
    // Autenticación de juguete - solo verificar que los campos no estén vacíos
    if (this.loginData.email && this.loginData.password) {
      localStorage.setItem('currentUser', JSON.stringify({
        email: this.loginData.email,
        nombre: this.loginData.email.split('@')[0]
      }));
      this.router.navigate(['/tablero']);
    } else {
      alert('Por favor completa todos los campos');
    }
  }

  onRegister() {
    // Registro de juguete - verificar campos básicos
    if (this.registerData.nombre && this.registerData.email && 
        this.registerData.password && this.registerData.confirmPassword) {
      if (this.registerData.password !== this.registerData.confirmPassword) {
        alert('Las contraseñas no coinciden');
        return;
      }
      localStorage.setItem('currentUser', JSON.stringify({
        email: this.registerData.email,
        nombre: this.registerData.nombre
      }));
      this.router.navigate(['/tablero']);
    } else {
      alert('Por favor completa todos los campos');
    }
  }

  loginAsGuest() {
    localStorage.setItem('currentUser', JSON.stringify({
      email: 'invitado@regata.com',
      nombre: 'Invitado'
    }));
    this.router.navigate(['/tablero']);
  }
}