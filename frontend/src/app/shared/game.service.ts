import { Injectable, signal } from '@angular/core';

export interface GameState {
  currentUser: any;
  jugadores: any[];
  barcos: any[];
  tablero: any[][];
}

@Injectable({
  providedIn: 'root'
})
export class GameService {
  private gameState = signal<GameState>({
    currentUser: null,
    jugadores: [],
    barcos: [],
    tablero: []
  });

  getGameState() {
    return this.gameState();
  }

  updateGameState(newState: Partial<GameState>) {
    this.gameState.update(state => ({ ...state, ...newState }));
  }

  resetGame() {
    this.gameState.set({
      currentUser: null,
      jugadores: [],
      barcos: [],
      tablero: []
    });
  }
}