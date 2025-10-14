import { Component, OnInit, signal, inject, model } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { switchMap } from 'rxjs/operators';
import { Barco } from '../../model/barco';
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
  
  barco = model<Barco>({
    id: 0,
    velocidad: 0,
    posicionId: 0,
    modeloId: 0,
    jugadorId: 0,
    tableroId: 0
  });

  ngOnInit(): void {
    this.route.params.pipe(
      switchMap((params: any) => this.barcoService.findById(params['id']))
    ).subscribe((resp: Barco) => this.barco.set(resp));
  }

  guardar() {
    console.log("Guardar", this.barco());
    this.barcoService.updateBarco(this.barco()).subscribe((resp: Barco) => {
      console.log("Barco actualizado", resp);
    });
  }
}