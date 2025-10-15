import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Posicion } from '../model/game-models';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PosicionService {
  private baseUrl = environment.apiUrl; // ✅ Usar environment

  constructor(private http: HttpClient) {}

  getAllPosiciones(): Observable<Posicion[]> {
    return this.http.get<Posicion[]>(`${this.baseUrl}/posicion/list`);
  }

  getPosicionById(id: number): Observable<Posicion> {
    return this.http.get<Posicion>(`${this.baseUrl}/posicion/${id}`);
  }

  createPosicion(posicion: Posicion): Observable<Posicion> {
    return this.http.post<Posicion>(`${this.baseUrl}/posicion`, posicion);
  }

  updatePosicion(id: number, posicion: Posicion): Observable<Posicion> {
    return this.http.put<Posicion>(`${this.baseUrl}/posicion/${id}`, posicion);
  }

  deletePosicion(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/posicion/${id}`);
  }

  // Métodos específicos para el movimiento
  moveBarco(barcoId: number, newX: number, newY: number): Observable<Posicion> {
    return this.http.put<Posicion>(`${this.baseUrl}/posicion/move-barco/${barcoId}`, { x: newX, y: newY });
  }

  getValidMoves(barcoId: number): Observable<Posicion[]> {
    return this.http.get<Posicion[]>(`${this.baseUrl}/posicion/valid-moves/${barcoId}`);
  }
}