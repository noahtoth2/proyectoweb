import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Jugador } from '../model/game-models';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class JugadorService {
  private baseUrl = environment.apiUrl; // ✅ Usar environment

  constructor(private http: HttpClient) {}

  getAllJugadores(): Observable<Jugador[]> {
    return this.http.get<Jugador[]>(`${this.baseUrl}/jugador/list`);
  }

  getJugadorById(id: number): Observable<Jugador> {
    return this.http.get<Jugador>(`${this.baseUrl}/jugador/${id}`);
  }

  createJugador(jugador: Jugador): Observable<Jugador> {
    return this.http.post<Jugador>(`${this.baseUrl}/jugador`, jugador);
  }

  updateJugador(jugador: Jugador): Observable<Jugador> {
    return this.http.put<Jugador>(`${this.baseUrl}/jugador`, jugador);
  }

  deleteJugador(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/jugador/${id}`);
  }
}