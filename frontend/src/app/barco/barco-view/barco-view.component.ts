import { Component, signal, WritableSignal, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { Barco } from '../../model/game-models'; // ✅ Usar modelo unificado
import { BarcoService } from '../../shared/barco.service';

@Component({
  selector: 'app-barco-view',
  imports: [CommonModule],
  templateUrl: './barco-view.component.html',
  styleUrl: './barco-view.component.css'
})
export class BarcoViewComponent implements OnInit {
  private barcoService = inject(BarcoService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  
  barco: WritableSignal<Barco> = signal<Barco>({
    id: 0,
    velocidadX: 0, // ✅ Usar velocidad vectorial
    velocidadY: 0, // ✅ Usar velocidad vectorial
    posicionId: 0,
    modeloId: 0,
    jugadorId: 0,
    tableroId: 0
  });

  isLoading = signal(true);
  error = signal<string | null>(null);

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadBarco(Number(id));
    }
  }

  loadBarco(id: number): void {
    this.isLoading.set(true);
    this.error.set(null);
    
    this.barcoService.findById(id).subscribe({
      next: (barco) => {
        this.barco.set(barco);
        this.isLoading.set(false);
      },
      error: (err) => {
        this.error.set('Error al cargar el barco');
        this.isLoading.set(false);
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/barco/list']);
  }

  editBarco(): void {
    this.router.navigate(['/barco/edit', this.barco().id]);
  }
}
