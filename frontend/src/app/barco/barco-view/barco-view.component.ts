import { Component, signal, WritableSignal } from '@angular/core';
import { Barco } from '../../model/barco';

@Component({
  selector: 'app-barco-view',
  imports: [],
  templateUrl: './barco-view.component.html',
  styleUrl: './barco-view.component.css'
})
export class BarcoViewComponent {
  barco: WritableSignal<Barco>  = signal<Barco>({id: 1, velocidad: 10, posicionId: 2, modeloId: 3, jugadorId: 4, tableroId: 5});

}
