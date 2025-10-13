import { Routes } from '@angular/router';
import { BarcoViewComponent } from './barco/barco-view/barco-view.component';
import { BarcoListComponent } from './barco/barco-list/barco-list.component';

export const routes: Routes = [
  { path: '', redirectTo: '/barcos', pathMatch: 'full' },
  { path: 'barcos', component: BarcoListComponent },
  { path: 'barco/:id', component: BarcoViewComponent },
  { path: 'jugadores', component: BarcoListComponent }, // Temporal hasta crear componente
  { path: 'modelos', component: BarcoListComponent },   // Temporal hasta crear componente
  { path: 'posiciones', component: BarcoListComponent }, // Temporal hasta crear componente
  { path: '**', redirectTo: '/barcos' } // Ruta wildcard para páginas no encontradas
];
