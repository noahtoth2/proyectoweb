import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { switchMap } from 'rxjs/operators';
import { Barco } from '../../model/game-models'; // ✅ Usar modelo unificado
import { BarcoService } from '../../shared/barco.service';

@Component({
  selector: 'app-barco-edit',
  imports: [CommonModule, FormsModule],
  templateUrl: './barco-edit.component.html',
  styleUrl: './barco-edit.component.css'
})
export class BarcoEditComponent implements OnInit {
  barcoService = inject(BarcoService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  
  barco: Barco = {
    id: 0,
    velocidadX: 0, // ✅ Usar velocidad vectorial
    velocidadY: 0, // ✅ Usar velocidad vectorial
    posicionId: 0,
    modeloId: 0,
    jugadorId: 0,
    tableroId: 0
  };

  ngOnInit(): void {
    this.route.params.pipe(
      switchMap((params: any) => this.barcoService.findById(params['id']))
    ).subscribe((resp: Barco) => {
      this.barco = {
        id: resp.id || 0,
        velocidadX: resp.velocidadX || 0, // ✅ Usar velocidad vectorial
        velocidadY: resp.velocidadY || 0, // ✅ Usar velocidad vectorial
        posicionId: resp.posicionId || 0,
        modeloId: resp.modeloId || 0,
        jugadorId: resp.jugadorId || 0,
        tableroId: resp.tableroId || 0
      };
    });
  }

  guardar() {
    const barcoData: Barco = {
      id: this.barco.id,
      velocidadX: Number(this.barco.velocidadX) || 0, // ✅ Usar velocidad vectorial
      velocidadY: Number(this.barco.velocidadY) || 0, // ✅ Usar velocidad vectorial
      posicionId: Number(this.barco.posicionId) || undefined,
      modeloId: Number(this.barco.modeloId) || undefined,
      jugadorId: Number(this.barco.jugadorId) || undefined,
      tableroId: Number(this.barco.tableroId) || undefined
    };
    
    console.log("=== DEBUG FRONTEND GUARDAR ===");
    console.log("Barco original:", this.barco);
    console.log("Barco a enviar:", barcoData);
    console.log("==============================");
    
    this.barcoService.updateBarco(barcoData).subscribe({
      next: (resp: Barco) => {
        console.log("Respuesta del backend:", resp);
        alert("Barco actualizado correctamente!");
        this.router.navigate(['/barco/list']);
      },
      error: (error) => {
        console.error("Error al actualizar:", error);
        alert("Error al actualizar el barco");
      }
    });
  }
}