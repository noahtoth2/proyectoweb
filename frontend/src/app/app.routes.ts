import { Routes } from '@angular/router';
import { BarcoViewComponent } from './barco/barco-view/barco-view.component';
import { BarcoListComponent } from './barco/barco-list/barco-list.component';
import { BarcoEditComponent } from './barco/barco-edit/barco-edit.component';

export const routes: Routes = [
  { path: 'barco/list', component: BarcoListComponent },
  { path: 'barco/view/:id', component: BarcoViewComponent },
  { path: 'barco/edit/:id', component: BarcoEditComponent },
  { path: '', pathMatch: 'full', redirectTo: 'barco/list' },
  { path: 'jugadores', component: BarcoListComponent },
  { path: 'modelos', component: BarcoListComponent },
  { path: 'posiciones', component: BarcoListComponent },
  { path: '**', redirectTo: 'barco/list' }
];
