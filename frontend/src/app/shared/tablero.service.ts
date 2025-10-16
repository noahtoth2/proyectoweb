import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Tablero, Celda, CeldaDTO, Barco, Jugador, Modelo, Posicion } from '../model/game-models';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TableroService {
  private baseUrl = environment.apiUrl; // ✅ Usar environment en lugar de hardcodear

  constructor(private http: HttpClient) {}

  // Tablero endpoints
  getAllTableros(): Observable<Tablero[]> {
    return this.http.get<Tablero[]>(`${this.baseUrl}/tablero/list`);
  }

  getTableroById(id: number): Observable<Tablero> {
    return this.http.get<Tablero>(`${this.baseUrl}/tablero/${id}`);
  }

  createTablero(tablero: Tablero): Observable<Tablero> {
    return this.http.post<Tablero>(`${this.baseUrl}/tablero`, tablero);
  }

  updateTablero(id: number, tablero: Tablero): Observable<Tablero> {
    return this.http.put<Tablero>(`${this.baseUrl}/tablero/${id}`, tablero);
  }

  deleteTablero(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/tablero/${id}`);
  }

  // Métodos específicos para el juego
  initializeTablero(): Observable<Tablero> {
    return this.http.post<Tablero>(`${this.baseUrl}/tablero/initialize`, {});
  }

  getTableroWithBarcos(tableroId: number): Observable<Tablero> {
    return this.http.get<Tablero>(`${this.baseUrl}/tablero/${tableroId}/with-barcos`);
  }

  // MÉTODOS DE LÓGICA DEL JUEGO

  // Cambiar velocidad de un barco
  cambiarVelocidadBarco(tableroId: number, barcoId: number, deltaVx: number, deltaVy: number): Observable<any> {
    const request = { deltaVx, deltaVy };
    return this.http.post<any>(`${this.baseUrl}/tablero/${tableroId}/barco/${barcoId}/cambiar-velocidad`, request);
  }

  // Mover un barco aplicando las reglas del juego
  moverBarco(tableroId: number, barcoId: number): Observable<MovimientoResponse> {
    return this.http.post<MovimientoResponse>(`${this.baseUrl}/tablero/${tableroId}/barco/${barcoId}/mover`, {});
  }

  // Obtener la posición futura de un barco
  obtenerPosicionFutura(tableroId: number, barcoId: number): Observable<PosicionResponse> {
    return this.http.get<PosicionResponse>(`${this.baseUrl}/tablero/${tableroId}/barco/${barcoId}/posicion-futura`);
  }

  // Obtener las celdas del tablero
  getCeldas(tableroId: number): Observable<CeldaDTO[]> {
    return this.http.get<CeldaDTO[]>(`${this.baseUrl}/tablero/${tableroId}/celdas`);
  }
}

// Interfaces para las respuestas del juego
export interface MovimientoResponse {
  exitoso: boolean;
  mensaje: string;
  tipo: 'CONTINUA' | 'META_ALCANZADA' | 'DESTRUIDO';
  nuevaPosicion: PosicionResponse;
}

export interface PosicionResponse {
  x: number;
  y: number;
}