import { Jugador } from './game-models';

export interface Partida {
  id: number;
  codigo: string;
  nombre: string;
  creadorId: number;
  creadorNombre: string;
  jugadores: Jugador[];
  maxJugadores: number;
  iniciada: boolean;
  finalizada: boolean;
  turnoActualId?: number;
  turnoActualNombre?: string;
  fechaCreacion: Date;
  fechaInicio?: Date;
  fechaFin?: Date;
  ganadorId?: number;
  ganadorNombre?: string;
  cantidadJugadores: number;
  jugadorBarcoSelecciones: { [jugadorId: number]: number }; // jugadorId -> barcoId
}

export interface CrearPartidaRequest {
  nombre: string;
  nombreJugador: string;
  maxJugadores: number;
}

export interface UnirsePartidaRequest {
  codigo: string;
  nombreJugador: string;
}
