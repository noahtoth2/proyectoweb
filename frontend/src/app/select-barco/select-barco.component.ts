import { Component, OnInit, signal, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

interface Barco {
  id: number;
  modelo: {
    id: number;
    nombre: string;
  };
}

@Component({
  selector: 'app-select-barco',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="select-container">
      <div class="select-card">
        <h2>Selecciona tu Barco</h2>
        <p>Elige el barco con el que jugarás. No podrás cambiarlo después.</p>
        
        @if (errorMessage()) {
          <div class="error-message">{{ errorMessage() }}</div>
        }
        
        <div class="barcos-grid">
          @for (barco of availableBarcos(); track barco.id) {
            <div class="barco-card" (click)="selectBarco(barco.id)">
              <h3>{{ barco.modelo.nombre }}</h3>
              <p>ID: {{ barco.id }}</p>
            </div>
          }
          
          @if (availableBarcos().length === 0) {
            <p class="no-barcos">No hay barcos disponibles en este momento.</p>
          }
        </div>
        
        <button class="btn-cancel" (click)="logout()">Cancelar</button>
      </div>
    </div>
  `,
  styles: [`
    .select-container {
      display: flex;
      justify-content: center;
      align-items: center;
      min-height: 100vh;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      padding: 20px;
    }
    
    .select-card {
      background: white;
      border-radius: 10px;
      padding: 40px;
      box-shadow: 0 10px 40px rgba(0,0,0,0.2);
      width: 100%;
      max-width: 800px;
    }
    
    h2 {
      text-align: center;
      color: #333;
      margin-bottom: 10px;
    }
    
    p {
      text-align: center;
      color: #666;
      margin-bottom: 30px;
    }
    
    .barcos-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
      gap: 20px;
      margin-bottom: 30px;
    }
    
    .barco-card {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
      padding: 30px;
      border-radius: 10px;
      cursor: pointer;
      transition: transform 0.3s, box-shadow 0.3s;
      text-align: center;
    }
    
    .barco-card:hover {
      transform: translateY(-5px);
      box-shadow: 0 10px 20px rgba(0,0,0,0.2);
    }
    
    .barco-card h3 {
      margin: 0;
      font-size: 18px;
    }
    
    .barco-card p {
      margin: 10px 0 0;
      opacity: 0.8;
      color: white;
    }
    
    .error-message {
      background: #fee;
      color: #c33;
      padding: 12px;
      border-radius: 5px;
      margin-bottom: 20px;
      text-align: center;
    }
    
    .no-barcos {
      grid-column: 1 / -1;
      text-align: center;
      color: #999;
      font-style: italic;
    }
    
    .btn-cancel {
      width: 100%;
      padding: 12px;
      background: #999;
      color: white;
      border: none;
      border-radius: 5px;
      cursor: pointer;
      transition: background 0.3s;
    }
    
    .btn-cancel:hover {
      background: #777;
    }
  `]
})
export class SelectBarcoComponent implements OnInit, OnDestroy {
  availableBarcos = signal<Barco[]>([]);
  errorMessage = signal('');
  private abortController: AbortController | null = null;
  
  constructor(private router: Router) {}

  ngOnDestroy() {
    if (this.abortController) {
      this.abortController.abort();
    }
  }
  
  ngOnInit() {
    const userId = localStorage.getItem('userId');
    if (!userId) {
      this.router.navigate(['/login']);
      return;
    }
    
    this.loadAvailableBarcos(parseInt(userId));
  }
  
  loadAvailableBarcos(userId: number) {
    if (this.abortController) {
      this.abortController.abort();
    }
    this.abortController = new AbortController();

    fetch(`http://localhost:8080/api/auth/available-barcos?userId=${userId}`, {
      signal: this.abortController.signal
    })
      .then(response => response.json())
      .then(data => {
        this.availableBarcos.set(data);
      })
      .catch(error => {
        if (error.name === 'AbortError') {
          console.log('Petición cancelada (navegación)');
          return;
        }
        this.errorMessage.set('Error al cargar barcos disponibles');
        console.error('Error:', error);
      });
  }
  
  selectBarco(barcoId: number) {
    const userId = localStorage.getItem('userId');
    if (!userId) return;
    
    if (this.abortController) {
      this.abortController.abort();
    }
    this.abortController = new AbortController();
    
    fetch(`http://localhost:8080/api/auth/select-barco?userId=${userId}&barcoId=${barcoId}`, {
      method: 'POST',
      signal: this.abortController.signal
    })
      .then(response => response.json())
      .then(data => {
        if (typeof data === 'string') {
          this.errorMessage.set(data);
        } else {
          localStorage.setItem('barcoSeleccionadoId', barcoId.toString());
          alert('Barco seleccionado exitosamente');
          this.router.navigate(['/lobby']);
        }
      })
      .catch(error => {
        if (error.name === 'AbortError') {
          console.log('Petición cancelada (navegación)');
          return;
        }
        this.errorMessage.set('Error al seleccionar barco');
        console.error('Error:', error);
      });
  }
  
  logout() {
    localStorage.clear();
    this.router.navigate(['/login']);
  }
}
