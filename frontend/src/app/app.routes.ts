import { Routes } from '@angular/router';
import { BarcoViewComponent } from './barco/barco-view/barco-view.component';
import { BarcoListComponent } from './barco/barco-list/barco-list.component';
import { BarcoEditComponent } from './barco/barco-edit/barco-edit.component';
import { AuthComponent } from './auth/auth.component';
import { TableroComponent } from './tablero/tablero.component';

export const routes: Routes = [
  { path: '', component: AuthComponent },
  { path: 'tablero', component: TableroComponent },
  { path: 'game', redirectTo: 'tablero' }, // Redirección por compatibilidad
  { path: 'barco/list', component: BarcoListComponent },
  { path: 'barco/view/:id', component: BarcoViewComponent },
  { path: 'barco/edit/:id', component: BarcoEditComponent },
  { path: 'jugadores', component: BarcoListComponent },
  { path: 'modelos', component: BarcoListComponent },
  { path: 'posiciones', component: BarcoListComponent },
  { path: '**', redirectTo: '' }
];
