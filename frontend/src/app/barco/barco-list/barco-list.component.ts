import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

interface Barco {
  id: number;
  velocidad: number;
  jugador?: {
    id: number;
    nombre: string;
  };
  modelo?: {
    id: number;
    nombre: string;
    color: string;
  };
  posicion?: {
    id: number;
    x: number;
    y: number;
  };
}

@Component({
  selector: 'app-barco-list',
  imports: [CommonModule],
  templateUrl: './barco-list.component.html',
  styleUrl: './barco-list.component.css'
})
export class BarcoListComponent implements OnInit {
  private http = inject(HttpClient);
  barcos: Barco[] = [];
  loading = true;
  error = '';

  ngOnInit() {
    this.loadBarcos();
  }

  loadBarcos() {
    this.loading = true;
    this.http.get<Barco[]>('http://localhost:8080/barco/list')
      .subscribe({
        next: (data) => {
          this.barcos = data;
          this.loading = false;
        },
        error: (error) => {
          console.error('Error loading barcos:', error);
          this.error = 'Error cargando los barcos. Asegúrate de que el backend esté ejecutándose.';
          this.loading = false;
        }
      });
  }
}
