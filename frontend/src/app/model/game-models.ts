export interface Celda {
  id?: number;
  tipocelda: 'A' | 'P' | 'M' | 'X'; // A=Agua, P=Partida, M=Meta, X=Pared/Obstáculo
  x: number;
  y: number;
  tableroId?: number;
}

// Interfaz para la respuesta del backend
export interface CeldaDTO {
  id: number;
  tipo: 'A' | 'P' | 'M' | 'X'; // A=Agua, P=Partida, M=Meta, X=Pared/Obstáculo
  fila: number;
  columna: number;
}

export interface Posicion {
  id?: number;
  x: number;
  y: number;
  barcoId?: number;
  tableroId?: number; // ⭐ Agregar tableroId
}

export interface Jugador {
  id?: number;
  nombre: string;
  barcoSeleccionadoId?: number; // Barco seleccionado en el lobby
}

export interface Modelo {
  id?: number;
  nombre: string;
  color?: string;
}

export interface Tablero {
  id?: number;
  celdas?: Celda[];
  barcos?: Barco[];
}

export interface Barco {
  id?: number;
  // Velocidad vectorial con componentes vx y vy
  velocidadX: number; // Componente X de la velocidad (vx)
  velocidadY: number; // Componente Y de la velocidad (vy)
  posicion?: Posicion;
  modelo?: Modelo;
  jugador?: Jugador;
  tablero?: Tablero;
  // Propiedades derivadas para el frontend
  posicionId?: number;
  modeloId?: number;
  jugadorId?: number;
  tableroId?: number;
}