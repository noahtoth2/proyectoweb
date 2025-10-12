import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { BarcoViewComponent } from './barco/barco-view/barco-view.component';
import { BarcoListComponent } from './barco/barco-list/barco-list.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet,BarcoViewComponent,BarcoListComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly dato2= signal("Hola!!!!");
  dato="Hola!!!!";
  cambiarDato(){
    this.dato2.set("Chau!!!!");
  }
}
