import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { Barco } from '../../model/game-models'; // ✅ Usar modelo unificado
import { BarcoService } from '../../shared/barco.service';

@Component({ 
  selector: 'app-barco-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './barco-list.component.html',
  styleUrls: ['./barco-list.component.css']
})
export class BarcoListComponent implements OnInit {
  barcos = signal<Barco[]>([]);
  barcoService = inject(BarcoService);
  private router = inject(Router);

  ngOnInit(): void {
    this.barcoService.findAll().subscribe(
      data => this.barcos.set(data)
    );
  }

  editarBarco(id: number): void {
    this.router.navigate(['/barco/edit', id]);
  }

  verDetalle(id: number): void {
    this.router.navigate(['/barco/view', id]);
  }

  eliminarBarco(id: number): void {
    if (confirm('¿Estás seguro de que quieres eliminar este barco?')) {
      this.barcoService.deleteBarco(id).subscribe({
        next: () => {
          // Recargar la lista después de eliminar
          this.barcoService.findAll().subscribe(
            data => this.barcos.set(data)
          );
          alert('Barco eliminado correctamente');
        },
        error: (error) => {
          console.error('Error al eliminar barco:', error);
          alert('Error al eliminar el barco');
        }
      });
    }
  }
}
