import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Partida, CrearPartidaRequest, UnirsePartidaRequest } from '../model/partida';

@Injectable({
  providedIn: 'root'
})
export class PartidaService {
  private apiUrl = 'http://localhost:8080/api/partidas';

  constructor(private http: HttpClient) { }

  crearPartida(request: CrearPartidaRequest): Observable<Partida> {
    return this.http.post<Partida>(this.apiUrl, request);
  }

  unirsePartida(request: UnirsePartidaRequest): Observable<Partida> {
    return this.http.post<Partida>(`${this.apiUrl}/unirse`, request);
  }

  iniciarPartida(partidaId: number): Observable<Partida> {
    return this.http.post<Partida>(`${this.apiUrl}/${partidaId}/iniciar`, {});
  }

  seleccionarBarco(partidaId: number, jugadorId: number, barcoId: number): Observable<Partida> {
    return this.http.post<Partida>(`${this.apiUrl}/${partidaId}/seleccionar-barco`, {
      jugadorId,
      barcoId
    });
  }

  siguienteTurno(partidaId: number): Observable<Partida> {
    return this.http.post<Partida>(`${this.apiUrl}/${partidaId}/siguiente-turno`, {});
  }

  finalizarPartida(partidaId: number, ganadorId: number): Observable<Partida> {
    return this.http.post<Partida>(`${this.apiUrl}/${partidaId}/finalizar?ganadorId=${ganadorId}`, {});
  }

  obtenerPartida(partidaId: number): Observable<Partida> {
    return this.http.get<Partida>(`${this.apiUrl}/${partidaId}`);
  }

  obtenerPartidaPorCodigo(codigo: string): Observable<Partida> {
    return this.http.get<Partida>(`${this.apiUrl}/codigo/${codigo}`);
  }

  listarPartidasActivas(): Observable<Partida[]> {
    return this.http.get<Partida[]>(`${this.apiUrl}/activas`);
  }
}
