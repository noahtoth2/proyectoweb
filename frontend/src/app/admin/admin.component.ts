import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

interface User {
  id: number;
  username: string;
  email: string;
  roles: string[];
  activo: boolean;
  fechaCreacion: string;
  ultimoAcceso?: string;
  barcoSeleccionadoId?: number;
  barcoSeleccionadoNombre?: string;
}

interface Barco {
  id: number;
  velocidadX: number;
  velocidadY: number;
  modelo?: {
    id: number;
    nombre: string;
    color: string;
  };
  jugador?: {
    id: number;
    nombre: string;
  };
  jugadorNombre?: string;
  posicion?: {
    id: number;
    x: number;
    y: number;
  };
  tableroId?: number;
}

interface Modelo {
  id: number;
  nombre: string;
  color: string;
}

interface Tablero {
  id: number;
  celdas?: any[];
}

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {
  private apiUrl = 'http://localhost:8080/api';
  
  // Estado actual
  vistaActual = signal<'usuarios' | 'barcos' | 'modelos' | 'tableros'>('usuarios');
  cargando = signal(false);
  mensaje = signal('');
  
  // Datos
  usuarios = signal<User[]>([]);
  barcos = signal<Barco[]>([]);
  modelos = signal<Modelo[]>([]);
  tableros = signal<Tablero[]>([]);
  
  // Edición
  usuarioEditando = signal<User | null>(null);
  barcoEditando = signal<Barco | null>(null);
  modeloEditando = signal<Modelo | null>(null);
  
  // Nuevo modelo
  nuevoModelo = {
    nombre: '',
    color: '#000000'
  };

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit() {
    // Verificar que es admin
    const roles = localStorage.getItem('userRoles');
    if (!roles || !JSON.parse(roles).includes('ADMIN')) {
      this.router.navigate(['/login']);
      return;
    }
    
    this.cargarUsuarios();
  }

  // ========== NAVEGACIÓN ==========
  cambiarVista(vista: 'usuarios' | 'barcos' | 'modelos' | 'tableros') {
    this.vistaActual.set(vista);
    this.mensaje.set('');
    
    switch(vista) {
      case 'usuarios':
        this.cargarUsuarios();
        break;
      case 'barcos':
        this.cargarBarcos();
        break;
      case 'modelos':
        this.cargarModelos();
        break;
      case 'tableros':
        this.cargarTableros();
        break;
    }
  }

  // ========== USUARIOS ==========
  cargarUsuarios() {
    this.cargando.set(true);
    this.http.get<User[]>(`${this.apiUrl}/auth/users`).subscribe({
      next: (data) => {
        this.usuarios.set(data);
        this.cargando.set(false);
      },
      error: (error) => {
        console.error('Error al cargar usuarios:', error);
        this.mensaje.set('Error al cargar usuarios');
        this.cargando.set(false);
      }
    });
  }

  toggleUsuarioStatus(userId: number) {
    this.http.put<User>(`${this.apiUrl}/auth/users/${userId}/toggle-status`, {}).subscribe({
      next: (user) => {
        // Actualizar en la lista
        const usuarios = this.usuarios();
        const index = usuarios.findIndex(u => u.id === userId);
        if (index !== -1) {
          usuarios[index] = user;
          this.usuarios.set([...usuarios]);
        }
        this.mensaje.set(`Usuario ${user.activo ? 'activado' : 'desactivado'} exitosamente`);
      },
      error: (error) => {
        console.error('Error al cambiar estado:', error);
        this.mensaje.set('Error al cambiar estado del usuario');
      }
    });
  }

  eliminarUsuario(userId: number, username: string) {
    if (!confirm(`¿Estás seguro de eliminar al usuario ${username}?`)) {
      return;
    }

    this.http.delete(`${this.apiUrl}/auth/users/${userId}`).subscribe({
      next: () => {
        this.usuarios.set(this.usuarios().filter(u => u.id !== userId));
        this.mensaje.set('Usuario eliminado exitosamente');
      },
      error: (error) => {
        console.error('Error al eliminar usuario:', error);
        this.mensaje.set('Error al eliminar usuario');
      }
    });
  }

  // ========== BARCOS ==========
  cargarBarcos() {
    this.cargando.set(true);
    this.http.get<Barco[]>(`${this.apiUrl}/barco/list`).subscribe({
      next: (data) => {
        this.barcos.set(data);
        this.cargando.set(false);
      },
      error: (error) => {
        console.error('Error al cargar barcos:', error);
        this.mensaje.set('Error al cargar barcos');
        this.cargando.set(false);
      }
    });
  }

  editarBarco(barco: Barco) {
    this.barcoEditando.set({...barco});
  }

  guardarBarco() {
    const barco = this.barcoEditando();
    if (!barco) return;

    this.http.put<Barco>(`${this.apiUrl}/barco`, barco).subscribe({
      next: (barcoActualizado) => {
        const barcos = this.barcos();
        const index = barcos.findIndex(b => b.id === barco.id);
        if (index !== -1) {
          barcos[index] = barcoActualizado;
          this.barcos.set([...barcos]);
        }
        this.barcoEditando.set(null);
        this.mensaje.set('Barco actualizado exitosamente');
      },
      error: (error) => {
        console.error('Error al actualizar barco:', error);
        this.mensaje.set('Error al actualizar barco');
      }
    });
  }

  cancelarEdicionBarco() {
    this.barcoEditando.set(null);
  }

  eliminarBarco(barcoId: number, nombreModelo?: string) {
    if (!confirm(`¿Estás seguro de eliminar el barco ${nombreModelo || barcoId}?`)) {
      return;
    }

    this.http.delete(`${this.apiUrl}/barco/${barcoId}`).subscribe({
      next: () => {
        this.barcos.set(this.barcos().filter(b => b.id !== barcoId));
        this.mensaje.set('Barco eliminado exitosamente');
      },
      error: (error) => {
        console.error('Error al eliminar barco:', error);
        this.mensaje.set('Error al eliminar barco');
      }
    });
  }

  // ========== MODELOS ==========
  cargarModelos() {
    this.cargando.set(true);
    this.http.get<Modelo[]>(`${this.apiUrl}/modelo/list`).subscribe({
      next: (data) => {
        this.modelos.set(data);
        this.cargando.set(false);
      },
      error: (error) => {
        console.error('Error al cargar modelos:', error);
        this.mensaje.set('Error al cargar modelos');
        this.cargando.set(false);
      }
    });
  }

  crearModelo() {
    if (!this.nuevoModelo.nombre) {
      this.mensaje.set('El nombre del modelo es obligatorio');
      return;
    }

    this.http.post<Modelo>(`${this.apiUrl}/modelo`, this.nuevoModelo).subscribe({
      next: (modelo) => {
        this.modelos.set([...this.modelos(), modelo]);
        this.nuevoModelo = { nombre: '', color: '#000000' };
        this.mensaje.set('Modelo creado exitosamente');
      },
      error: (error) => {
        console.error('Error al crear modelo:', error);
        this.mensaje.set('Error al crear modelo');
      }
    });
  }

  editarModelo(modelo: Modelo) {
    this.modeloEditando.set({...modelo});
  }

  guardarModelo() {
    const modelo = this.modeloEditando();
    if (!modelo) return;

    this.http.put<Modelo>(`${this.apiUrl}/modelo`, modelo).subscribe({
      next: (modeloActualizado) => {
        const modelos = this.modelos();
        const index = modelos.findIndex(m => m.id === modelo.id);
        if (index !== -1) {
          modelos[index] = modeloActualizado;
          this.modelos.set([...modelos]);
        }
        this.modeloEditando.set(null);
        this.mensaje.set('Modelo actualizado exitosamente');
      },
      error: (error) => {
        console.error('Error al actualizar modelo:', error);
        this.mensaje.set('Error al actualizar modelo');
      }
    });
  }

  cancelarEdicionModelo() {
    this.modeloEditando.set(null);
  }

  eliminarModelo(modeloId: number, nombre: string) {
    if (!confirm(`¿Estás seguro de eliminar el modelo ${nombre}?`)) {
      return;
    }

    this.http.delete(`${this.apiUrl}/modelo/${modeloId}`).subscribe({
      next: () => {
        this.modelos.set(this.modelos().filter(m => m.id !== modeloId));
        this.mensaje.set('Modelo eliminado exitosamente');
      },
      error: (error) => {
        console.error('Error al eliminar modelo:', error);
        this.mensaje.set('Error al eliminar modelo');
      }
    });
  }

  // ========== TABLEROS ==========
  cargarTableros() {
    this.cargando.set(true);
    this.http.get<Tablero[]>(`${this.apiUrl}/tablero/list`).subscribe({
      next: (data) => {
        this.tableros.set(data);
        this.cargando.set(false);
      },
      error: (error) => {
        console.error('Error al cargar tableros:', error);
        this.mensaje.set('Error al cargar tableros');
        this.cargando.set(false);
      }
    });
  }

  // ========== UTILIDADES ==========
  cerrarSesion() {
    localStorage.clear();
    this.router.navigate(['/login']);
  }

  getRolesDisplay(roles: string[]): string {
    return roles.join(', ');
  }

  formatDate(dateString?: string): string {
    if (!dateString) return 'Nunca';
    const date = new Date(dateString);
    return date.toLocaleDateString('es-ES', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }
}
