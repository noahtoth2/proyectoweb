import { Routes } from '@angular/router';
import { BarcoListComponent } from './barco/barco-list/barco-list.component';

export const routes: Routes = [
  { path: '', redirectTo: '/barcos', pathMatch: 'full' },
  { path: 'barcos', component: BarcoListComponent },
  { path: 'jugadores', component: BarcoListComponent }, // Temporalmente usar BarcoList
  { path: 'modelos', component: BarcoListComponent }, // Temporalmente usar BarcoList
  { path: 'posiciones', component: BarcoListComponent }, // Temporalmente usar BarcoList
];
