import { Component, signal, WritableSignal } from '@angular/core';
import { Barco } from '../../model/barco';
import { inject } from '@angular/core';
import { BarcoService } from '../../shared/barco.service';

@Component({
  selector: 'app-barco-view',
  imports: [],
  templateUrl: './barco-view.component.html',
  styleUrl: './barco-view.component.css'
})
export class BarcoViewComponent {
  barcoService = inject(BarcoService);
  
  barco: WritableSignal<Barco> =signal<Barco>({
    id:0.0,
    velocidad:0.0,
    posicionId:0.0,
    modeloId:0.0,
    jugadorId:0.0,
    tableroId:0.0});

 



}
