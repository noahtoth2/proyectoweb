import { Routes } from '@angular/router';
import { BarcoViewComponent } from './barco/barco-view/barco-view.component';
import { BarcoListComponent } from './barco/barco-list/barco-list.component';
import { BarcoEditComponent } from './barco/barco-edit/barco-edit.component';
import { AuthComponent } from './auth/auth.component';
import { RegisterComponent } from './auth/register.component';
import { SelectBarcoComponent } from './select-barco/select-barco.component';
import { TableroComponent } from './tablero/tablero.component';
import { LobbyComponent } from './lobby/lobby.component';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: AuthComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'select-barco', component: SelectBarcoComponent },
  { path: 'lobby', component: LobbyComponent },
  { path: 'tablero', component: TableroComponent },
  { path: 'game', redirectTo: 'tablero' },
  { path: 'barco/list', component: BarcoListComponent },
  { path: 'barco/view/:id', component: BarcoViewComponent },
  { path: 'barco/edit/:id', component: BarcoEditComponent },
  { path: 'jugadores', component: BarcoListComponent },
  { path: 'modelos', component: BarcoListComponent },
  { path: 'posiciones', component: BarcoListComponent },
  { path: '**', redirectTo: 'login' }
];
