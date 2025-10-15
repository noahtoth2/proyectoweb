import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { BarcoService } from '../shared/barco.service';
import { TableroService, MovimientoResponse, PosicionResponse } from '../shared/tablero.service';
import { JugadorService } from '../shared/jugador.service';
import { PosicionService } from '../shared/posicion.service';
import { Barco, Celda, CeldaDTO, Jugador, Tablero, Posicion } from '../model/game-models';

@Component({
  selector: 'app-tablero',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './tablero.component.html',
  styleUrl: './tablero.component.css'
})
export class TableroComponent implements OnInit {
  currentUser: any = null;
  tablero = signal<Celda[][]>([]);
  barcos = signal<Barco[]>([]);
  jugadores = signal<Jugador[]>([]);
  selectedBarco = signal<Barco | null>(null);
  validMoves = signal<Posicion[]>([]);
  
  // Estado del juego
  gameMessage = signal<string>('');
  posicionFutura = signal<PosicionResponse | null>(null);
  juegoTerminado = signal<boolean>(false);
  ganador = signal<string | null>(null);
  
  // Sistema de turnos
  juegoIniciado = signal<boolean>(false);
  turnoActual = signal<number>(0); // Índice del jugador actual
  jugadorActual = signal<Jugador | null>(null);
  faseDelTurno = signal<'seleccionar-barco' | 'mover-barco'>('seleccionar-barco');
  
  // Formularios
  showCreatePlayer = signal(false);
  showCreateBarco = signal(false);
  newPlayerName = '';
  newBarco = {
    velocidadX: 0, // Componente X de velocidad
    velocidadY: 1, // Componente Y de velocidad
    jugadorId: 0
  };

  // Controles de UI
  showPlayersDropdown = false;
  showBoatsDropdown = false;

  // Configuración del tablero según la imagen
  readonly BOARD_WIDTH = 21;
  readonly BOARD_HEIGHT = 21;

  constructor(
    private router: Router,
    private barcoService: BarcoService,
    private tableroService: TableroService,
    private jugadorService: JugadorService,
    private posicionService: PosicionService
  ) {}

  // Método para contar barcos de un jugador
  getBarcoCountForJugador(jugador: Jugador): number {
    if (!jugador.id) return 0;
    return this.barcos().filter(b => b.jugadorId && b.jugadorId === jugador.id).length;
  }

  // Método para contar barcos colocados en el tablero
  getBarcosColocados(): number {
    return this.barcos().filter(b => b.posicion != null).length;
  }

  ngOnInit() {
    console.log('TableroComponent ngOnInit iniciado');
    const userData = localStorage.getItem('currentUser');
    if (!userData) {
      console.log('No hay usuario en localStorage, redirigiendo a home');
      this.router.navigate(['/']);
      return;
    }
    this.currentUser = JSON.parse(userData);
    console.log('Usuario encontrado:', this.currentUser);
    this.initializeBoard();
    this.loadJugadores();
    this.loadBarcos();
  }

  initializeBoard() {
    console.log('Inicializando tablero...');
    // Cargar tablero desde el backend
    this.tableroService.getAllTableros().subscribe({
      next: (tableros) => {
        console.log('Tableros obtenidos:', tableros);
        if (tableros.length > 0) {
          const tableroId = tableros[0].id;
          console.log('Cargando tablero con ID:', tableroId);
          this.loadBoardFromBackend(tableroId!);
        } else {
          // Si no hay tableros, crear uno nuevo
          console.log('No se encontraron tableros, creando uno nuevo');
          this.createInitialBoard();
        }
      },
      error: (error) => {
        console.error('Error loading tableros:', error);
        // Fallback: crear tablero local
        this.createLocalBoard();
      }
    });
  }

  loadBoardFromBackend(tableroId: number) {
    this.tableroService.getCeldas(tableroId).subscribe({
      next: (celdas: CeldaDTO[]) => {
        const board: Celda[][] = [];
        
        // Inicializar tablero vacío
        for (let y = 0; y < this.BOARD_HEIGHT; y++) {
          const row: Celda[] = [];
          for (let x = 0; x < this.BOARD_WIDTH; x++) {
            row.push({
              tipocelda: 'A', // Por defecto agua
              x,
              y
            });
          }
          board.push(row);
        }

        // Aplicar celdas del backend - convertir CeldaDTO a Celda
        celdas.forEach((celdaDTO: CeldaDTO) => {
          if (celdaDTO.fila < this.BOARD_HEIGHT && celdaDTO.columna < this.BOARD_WIDTH) {
            board[celdaDTO.fila][celdaDTO.columna] = {
              id: celdaDTO.id,
              tipocelda: celdaDTO.tipo,
              x: celdaDTO.columna,
              y: celdaDTO.fila
            };
          }
        });

        this.tablero.set(board);
        console.log('Tablero cargado desde el backend:', board);
      },
      error: (error) => {
        console.error('Error loading celdas:', error);
        this.createLocalBoard();
      }
    });
  }

  createInitialBoard() {
    // Crear un tablero nuevo en el backend
    this.tableroService.createTablero({}).subscribe({
      next: (tablero) => {
        if (tablero.id) {
          this.loadBoardFromBackend(tablero.id);
        }
      },
      error: (error) => {
        console.error('Error creating tablero:', error);
        this.createLocalBoard();
      }
    });
  }

  createLocalBoard() {
    // Crear tablero local en forma de H (fallback)
    const board: Celda[][] = [];
    
    for (let y = 0; y < this.BOARD_HEIGHT; y++) {
      const row: Celda[] = [];
      for (let x = 0; x < this.BOARD_WIDTH; x++) {
        let tipo: 'A' | 'P' | 'M' | 'X' = 'A'; // A=Agua por defecto
        
        // Forma de H:
        // Brazo izquierdo (columnas 0-7)
        if (x >= 0 && x <= 7) {
          // Posición de partida
          if (y === 1 && x === 3) {
            tipo = 'P';
          } else {
            tipo = 'A'; // Agua navegable
          }
        }
        // Brazo derecho (columnas 13-20)
        else if (x >= 13 && x <= 20) {
          // Posición de partida
          if (y === 1 && x === 17) {
            tipo = 'P';
          } else {
            tipo = 'A'; // Agua navegable
          }
        }
        // Puente horizontal (filas 9-11)
        else if (y >= 9 && y <= 11) {
          // Meta en el centro
          if (y === 10 && x === 10) {
            tipo = 'M';
          } else {
            tipo = 'A'; // Agua navegable
          }
        }
        // Todo lo demás son paredes
        else {
          tipo = 'X';
        }

        row.push({
          tipocelda: tipo,
          x,
          y
        });
      }
      board.push(row);
    }
    
    this.tablero.set(board);
  }

  loadJugadores() {
    this.jugadorService.getAllJugadores().subscribe({
      next: (jugadores) => {
        this.jugadores.set(jugadores);
        
        // Si no hay jugadores, crear uno para el usuario actual
        if (jugadores.length === 0) {
          this.createInitialPlayer();
        }
      },
      error: (error) => {
        console.error('Error loading jugadores:', error);
        // En caso de error, crear jugador local
        this.createLocalPlayer();
      }
    });
  }

  loadBarcos() {
    this.barcoService.getAllBarcos().subscribe({
      next: (barcos: any[]) => {
        // Convertir barcos del backend (con velocidad lineal) a barcos con velocidad vectorial
        const barcosVectoriales = barcos.map(barco => this.convertFromOldBarco(barco));
        this.barcos.set(barcosVectoriales);
        console.log('Barcos recargados desde backend:', barcosVectoriales);
      },
      error: (error: any) => {
        console.error('Error loading barcos:', error);
      }
    });
  }

  loadBarcosAndRestoreSelection(selectedBarcoId: number) {
    console.log('Recargando barcos y restaurando selección del barco:', selectedBarcoId);
    
    this.barcoService.getAllBarcos().subscribe({
      next: (barcos: any[]) => {
        // Convertir barcos del backend (con velocidad lineal) a barcos con velocidad vectorial
        const barcosVectoriales = barcos.map(barco => this.convertFromOldBarco(barco));
        this.barcos.set(barcosVectoriales);
        
        // Restaurar la selección del barco
        const barcoParaSeleccionar = barcosVectoriales.find(b => b.id === selectedBarcoId);
        if (barcoParaSeleccionar) {
          console.log('Restaurando selección del barco:', barcoParaSeleccionar);
          this.selectedBarco.set(barcoParaSeleccionar);
          // Recalcular movimientos válidos para el barco restaurado
          this.calculateValidMoves(barcoParaSeleccionar);
        } else {
          console.log('No se pudo encontrar el barco para restaurar selección');
          this.selectedBarco.set(null);
          this.validMoves.set([]);
        }
        
        console.log('Estado después de recargar:', {
          barcos: this.barcos(),
          selectedBarco: this.selectedBarco(),
          validMoves: this.validMoves()
        });
      },
      error: (error: any) => {
        console.error('Error loading barcos:', error);
        // En caso de error, al menos limpiar el estado
        this.selectedBarco.set(null);
        this.validMoves.set([]);
      }
    });
  }

  // Método para convertir de barco vectorial a barco backend
  convertToOldBarco(barco: Barco): any {
    return {
      id: barco.id,
      velocidad: Math.max(Math.abs(barco.velocidadX), Math.abs(barco.velocidadY)), // Velocidad máxima como lineal
      jugadorId: barco.jugadorId,
      posicionId: barco.posicionId,
      modeloId: barco.modeloId,
      tableroId: barco.tableroId
    };
  }

  // Método para convertir de barco backend a barco vectorial
  convertFromOldBarco(barco: any): Barco {
    // ✅ NORMALIZAR VELOCIDADES: usar 0 como default en lugar de 1
    const velocidadX = typeof barco.velocidadX === 'number' ? barco.velocidadX : 0;
    const velocidadY = typeof barco.velocidadY === 'number' ? barco.velocidadY : 0;
    
    console.log('🔄 Convirtiendo barco del backend:', {
      id: barco.id,
      backend: { vx: barco.velocidadX, vy: barco.velocidadY, v: barco.velocidad },
      frontend: { velocidadX, velocidadY }
    });
    
    return {
      id: barco.id,
      velocidadX: velocidadX,
      velocidadY: velocidadY,
      jugadorId: barco.jugadorId,
      posicionId: barco.posicionId,
      modeloId: barco.modeloId,
      tableroId: barco.tableroId,
      posicion: barco.posicion,
      modelo: barco.modelo,
      jugador: barco.jugador,
      tablero: barco.tablero
    };
  }

  createInitialPlayer() {
    const newPlayer: Jugador = {
      nombre: this.currentUser.nombre
    };
    
    this.jugadorService.createJugador(newPlayer).subscribe({
      next: (jugador) => {
        this.jugadores.set([jugador]);
      },
      error: (error) => {
        console.error('Error creating player:', error);
        this.createLocalPlayer();
      }
    });
  }

  createLocalPlayer() {
    // Fallback local si no hay conexión al backend
    const localPlayer: Jugador = {
      id: 1,
      nombre: this.currentUser.nombre
    };
    this.jugadores.set([localPlayer]);
  }

  createPlayer() {
    if (!this.newPlayerName.trim()) return;
    
    const newPlayer: Jugador = {
      nombre: this.newPlayerName
    };
    
    this.jugadorService.createJugador(newPlayer).subscribe({
      next: (jugador) => {
        this.jugadores.update(players => [...players, jugador]);
        this.newPlayerName = '';
        this.showCreatePlayer.set(false);
      },
      error: (error) => {
        console.error('Error creating player:', error);
        // Fallback local
        const localPlayer: Jugador = {
          id: this.jugadores().length + 1,
          nombre: this.newPlayerName
        };
        this.jugadores.update(players => [...players, localPlayer]);
        this.newPlayerName = '';
        this.showCreatePlayer.set(false);
      }
    });
  }

  createBarco() {
    // Convertir jugadorId a número para asegurar el tipo correcto
    const jugadorId = Number(this.newBarco.jugadorId);
    
    if (jugadorId === 0 || isNaN(jugadorId)) {
      this.gameMessage.set('⚠️ Debes seleccionar un jugador para crear el barco');
      return;
    }

    console.log('Creando barco para jugador ID:', jugadorId);
    console.log('Jugadores disponibles:', this.jugadores());

    // Crear barco sin posición inicial - el usuario lo colocará manualmente
    const newBarco: Barco = {
      velocidadX: this.newBarco.velocidadX,
      velocidadY: this.newBarco.velocidadY,
      jugadorId: jugadorId
      // Sin posición - se colocará al hacer clic
    };

    this.barcoService.createBarco(this.convertToOldBarco(newBarco)).subscribe({
      next: (barco) => {
        const createdBarco = this.convertFromOldBarco(barco);
        this.barcos.update(boats => [...boats, createdBarco]);
        this.selectedBarco.set(createdBarco); // Seleccionar automáticamente para colocación
        this.gameMessage.set('🚢 Barco creado. Haz clic en una casilla de Partida (P - naranja) para colocarlo en la línea de salida.');
        this.resetBarcoForm();
      },
      error: (error) => {
        console.error('Error creating barco:', error);
        // Fallback local
        const localBarco: Barco = {
          id: this.barcos().length + 1,
          velocidadX: this.newBarco.velocidadX,
          velocidadY: this.newBarco.velocidadY,
          jugadorId: this.newBarco.jugadorId
          // Sin posición - se colocará al hacer clic
        };
        this.barcos.update(boats => [...boats, localBarco]);
        this.selectedBarco.set(localBarco); // Seleccionar automáticamente para colocación
        this.gameMessage.set('🚢 Barco creado. Haz clic en una casilla de Partida (P - naranja) para colocarlo en la línea de salida.');
        this.resetBarcoForm();
      }
    });
  }

  getStartPositions(): Posicion[] {
    const positions: Posicion[] = [];
    const usedPositions = this.barcos().map(b => b.posicion).filter(p => p);
    
    // Área de inicio (lado izquierdo del área navegable)
    for (let y = 7; y <= 13; y++) {
      for (let x = 3; x <= 5; x++) {
        const isUsed = usedPositions.some(pos => pos!.x === x && pos!.y === y);
        if (!isUsed && this.tablero()[y][x].tipocelda === 'A') {
          positions.push({ x, y });
        }
      }
    }
    
    return positions;
  }

  resetBarcoForm() {
    this.newBarco = {
      velocidadX: 0,
      velocidadY: 1,
      jugadorId: 0
    };
    this.showCreateBarco.set(false);
  }

  selectBarco(barco: Barco) {
    // Verificar si puede seleccionar este barco (sistema de turnos)
    if (!this.puedeSeleccionarBarco(barco)) {
      this.gameMessage.set(`❌ No es tu turno. Es el turno de ${this.jugadorActual()?.nombre}`);
      return;
    }

    this.selectedBarco.set(barco);
    
    if (!barco.posicion) {
      // Barco sin posición - necesita ser colocado
      this.validMoves.set([]);
      this.gameMessage.set('🏁 Barco seleccionado. Haz clic en una casilla de Partida (P - naranja) para colocarlo en la línea de salida.');
    } else {
      // Barco ya colocado - calcular movimientos
      this.calculateValidMoves(barco);
      if (this.juegoIniciado()) {
        this.faseDelTurno.set('mover-barco');
        this.gameMessage.set(`⚡ Barco seleccionado. Elige cómo cambiar la velocidad y mover.`);
      }
    }
  }

  // Método para eliminar un barco
  deleteBarco(barcoId: number) {
    if (!barcoId) return;
    
    if (confirm('¿Estás seguro de que quieres eliminar este barco?')) {
      this.barcoService.deleteBarco(barcoId).subscribe({
        next: () => {
          // Actualizar lista de barcos
          this.barcos.update(boats => boats.filter(b => b.id !== barcoId));
          
          // Limpiar selección si el barco eliminado estaba seleccionado
          if (this.selectedBarco()?.id === barcoId) {
            this.selectedBarco.set(null);
            this.validMoves.set([]);
            this.posicionFutura.set(null);
          }
          
          this.gameMessage.set('Barco eliminado exitosamente');
        },
        error: (error) => {
          console.error('Error al eliminar barco:', error);
          this.gameMessage.set('Error al eliminar el barco');
        }
      });
    }
  }

  // NUEVOS MÉTODOS DE LÓGICA DEL JUEGO

  /**
   * Calcula los movimientos válidos para un barco usando la lógica del backend
   */
  calculateValidMoves(barco: Barco) {
    console.log('=== CALCULANDO MOVIMIENTOS VÁLIDOS ===');
    console.log('Barco:', barco);
    
    if (!barco.posicion || !barco.id) {
      console.log('Barco sin posición o ID, no hay movimientos válidos');
      this.validMoves.set([]);
      return;
    }

    const moves: Posicion[] = [];
    const currentX = barco.posicion.x;
    const currentY = barco.posicion.y;
    
    console.log('Posición actual:', currentX, currentY);
    console.log('Velocidad actual:', barco.velocidadX, barco.velocidadY);

    // ✅ VALIDAR Y NORMALIZAR VELOCIDADES DEL BARCO
    const velocidadX = barco.velocidadX || 0;
    const velocidadY = barco.velocidadY || 0;
    
    if (barco.velocidadX !== velocidadX || barco.velocidadY !== velocidadY) {
      console.warn('⚠️ Velocidades undefined/null normalizadas:', {
        original: { vx: barco.velocidadX, vy: barco.velocidadY },
        normalizada: { vx: velocidadX, vy: velocidadY }
      });
    }

    // ✅ REGLAS CORRECTAS: Solo se puede cambiar X O Y, no ambos
    // Movimientos válidos: (+1,0), (-1,0), (0,+1), (0,-1), (0,0)
    const validDeltas = [
      { deltaVx: -1, deltaVy: 0 },  // Disminuir velocidad X
      { deltaVx: 1, deltaVy: 0 },   // Aumentar velocidad X
      { deltaVx: 0, deltaVy: -1 },  // Disminuir velocidad Y
      { deltaVx: 0, deltaVy: 1 },   // Aumentar velocidad Y
      { deltaVx: 0, deltaVy: 0 }    // No cambiar velocidad
    ];

    for (const delta of validDeltas) {
      const { deltaVx, deltaVy } = delta;
      
      // Calcular posición futura si aplicamos este cambio de velocidad
      const newVx = velocidadX + deltaVx;
      const newVy = velocidadY + deltaVy;
      const futureX = currentX + newVx;
      const futureY = currentY + newVy;
      
      console.log(`Delta(${deltaVx},${deltaVy}) -> NewVel(${newVx},${newVy}) -> Pos(${futureX},${futureY})`);
      
      // Verificar que la posición esté dentro del tablero
      if (futureX >= 0 && futureX < this.BOARD_WIDTH && 
          futureY >= 0 && futureY < this.BOARD_HEIGHT) {
        
        // Verificar que la celda exista en el tablero
        if (this.tablero()[futureY] && this.tablero()[futureY][futureX]) {
          const celda = this.tablero()[futureY][futureX];
          console.log(`Celda en (${futureX},${futureY}): ${celda.tipocelda}`);
          
          // Agregar como movimiento válido (el backend validará después)
          moves.push({
            x: futureX,
            y: futureY,
            deltaVx: deltaVx,
            deltaVy: deltaVy
          } as any);
        } else {
          console.log(`Celda (${futureX},${futureY}) no existe en tablero`);
        }
      } else {
        console.log(`Posición (${futureX},${futureY}) fuera de límites del tablero`);
      }
    }

    console.log('Movimientos válidos calculados:', moves);
    this.validMoves.set(moves);
    
    // También mostrar la posición futura con la velocidad actual (sin cambios)
    this.mostrarPosicionFutura(barco);
  }

  /**
   * Muestra la posición futura de un barco
   */
  mostrarPosicionFutura(barco: Barco) {
    if (!barco.id) return;
    
    this.tableroService.obtenerPosicionFutura(1, barco.id).subscribe({
      next: (posicion) => {
        this.posicionFutura.set(posicion);
      },
      error: (error) => {
        console.error('Error al obtener posición futura:', error);
      }
    });
  }

  /**
   * Cambia la velocidad y mueve el barco usando la lógica del backend
   */
  cambiarVelocidadYMover(deltaVx: number, deltaVy: number) {
    const barco = this.selectedBarco();
    if (!barco || !barco.id) {
      console.log('No hay barco seleccionado para mover');
      return;
    }

    // ✅ VALIDACIÓN ESTRICTA DE REGLAS DE VELOCIDAD
    console.log('=== VALIDANDO CAMBIO DE VELOCIDAD ===');
    console.log('Delta solicitado:', { deltaVx, deltaVy });
    
    // Validar que los deltas sean enteros válidos
    if (!Number.isInteger(deltaVx) || !Number.isInteger(deltaVy)) {
      console.error('❌ Los deltas deben ser números enteros');
      this.gameMessage.set('❌ Error: valores de velocidad inválidos');
      return;
    }
    
    // Validar rango ±1
    if (Math.abs(deltaVx) > 1 || Math.abs(deltaVy) > 1) {
      console.error('❌ Los deltas deben estar en rango ±1');
      console.error('Recibido:', { deltaVx, deltaVy });
      this.gameMessage.set('❌ Solo se puede cambiar velocidad en ±1');
      return;
    }
    
    // Validar que solo se cambie X o Y, no ambos
    if (deltaVx !== 0 && deltaVy !== 0) {
      console.error('❌ No se puede cambiar X e Y simultáneamente');
      console.error('Recibido:', { deltaVx, deltaVy });
      this.gameMessage.set('❌ Solo puedes cambiar X o Y, no ambos');
      return;
    }
    
    // Validar que al menos uno cambie
    if (deltaVx === 0 && deltaVy === 0) {
      console.error('❌ Debe cambiar al menos un componente');
      this.gameMessage.set('❌ Debes cambiar al menos un componente de velocidad');
      return;
    }
    
    console.log('✅ Validación de deltas pasada');

    console.log('=== SINCRONIZANDO CON BACKEND ===');
    console.log('Barco ID:', barco.id);
    console.log('Delta velocidad:', deltaVx, deltaVy);

    // Solo sincronizar con el backend, la UI ya está actualizada
    this.tableroService.cambiarVelocidadBarco(1, barco.id, deltaVx, deltaVy).subscribe({
      next: (resultado) => {
        console.log('Velocidad cambiada en backend:', resultado);
        if (resultado.success) {
          // Mover en el backend
          this.tableroService.moverBarco(1, barco.id!).subscribe({
            next: (movResult) => {
              console.log('Movimiento confirmado por backend:', movResult);
              this.gameMessage.set(movResult.mensaje);
              
              // VERIFICAR SI EL MOVIMIENTO FUE EXITOSO
              if (!movResult.exitoso) {
                console.log('❌ MOVIMIENTO RECHAZADO POR BACKEND - REVIRTIENDO UI');
                console.log('Motivo del rechazo:', movResult.mensaje);
                
                // Guardar el ID del barco seleccionado antes de recargar
                const selectedBarcoId = barco.id;
                
                if (selectedBarcoId) {
                  // El backend rechazó el movimiento, revertir la UI
                  this.loadBarcosAndRestoreSelection(selectedBarcoId);
                } else {
                  // Si no hay ID, solo recargar normalmente
                  this.loadBarcos();
                  this.validMoves.set([]);
                }
                
                this.gameMessage.set(movResult.mensaje || 'Movimiento no válido');
                return;
              }
              
              console.log('✅ MOVIMIENTO ACEPTADO POR BACKEND');
              
              // Verificar si la posición del backend coincide con la UI
              if (movResult.nuevaPosicion.x !== barco.posicion?.x || 
                  movResult.nuevaPosicion.y !== barco.posicion?.y) {
                console.log('Corrigiendo posición - Backend vs UI:', movResult.nuevaPosicion, barco.posicion);
                // Corregir si hay diferencia
                this.barcos.update(boats => 
                  boats.map(b => 
                    b.id === barco.id 
                      ? { 
                          ...b, 
                          posicion: { 
                            ...b.posicion, 
                            x: movResult.nuevaPosicion.x, 
                            y: movResult.nuevaPosicion.y 
                          }
                        }
                      : b
                  )
                );
                
                this.selectedBarco.update(b => 
                  b && b.id === barco.id 
                    ? { 
                        ...b, 
                        posicion: { 
                          ...b.posicion, 
                          x: movResult.nuevaPosicion.x, 
                          y: movResult.nuevaPosicion.y 
                        }
                      }
                    : b
                );
              }

              // Manejar resultado del juego
              if (movResult.tipo === 'META_ALCANZADA') {
                this.juegoTerminado.set(true);
                const jugador = this.jugadores().find(j => j.id === barco?.jugadorId);
                this.ganador.set(jugador?.nombre || 'Jugador desconocido');
                alert(`¡${jugador?.nombre || 'Jugador'} ha ganado la carrera!`);
                this.selectedBarco.set(null);
              } else if (movResult.tipo === 'DESTRUIDO') {
                console.log('💥 Barco destruido detectado');
                this.barcos.update(boats => boats.filter(b => b.id !== barco.id));
                this.selectedBarco.set(null);
                
                // Avanzar turno después de destrucción
                if (this.juegoIniciado()) {
                  console.log('🔄 Avanzando turno después de destrucción...');
                  setTimeout(() => {
                    console.log('⏰ setTimeout destrucción ejecutándose - llamando siguienteTurno()');
                    this.siguienteTurno();
                  }, 1500);
                } else {
                  console.log('❌ Juego no iniciado, no se avanza turno tras destrucción');
                }
              } else {
                // Movimiento exitoso - completar turno
                console.log('🎯 Movimiento exitoso detectado');
                console.log('Estado del juego iniciado:', this.juegoIniciado());
                
                this.selectedBarco.set(null);
                this.validMoves.set([]);
                
                // ✅ AVANZAR TURNO DESPUÉS DE MOVIMIENTO EXITOSO
                if (this.juegoIniciado()) {
                  console.log('🔄 Turno completado, avanzando al siguiente jugador...');
                  console.log('⏰ Programando setTimeout para avanzar turno en 1.5 segundos');
                  setTimeout(() => {
                    console.log('⏰ setTimeout ejecutándose - llamando siguienteTurno()');
                    this.siguienteTurno();
                  }, 1500);
                } else {
                  console.log('❌ Juego no iniciado, no se avanza turno');
                }
              }
            },
            error: (error) => {
              console.error('❌ ERROR al mover en backend:', error);
              console.log('REVIRTIENDO UI debido a error de backend...');
              
              // Guardar ID del barco para restaurar selección
              const selectedBarcoId = barco.id;
              
              if (selectedBarcoId) {
                this.loadBarcosAndRestoreSelection(selectedBarcoId);
              } else {
                this.loadBarcos();
                this.validMoves.set([]);
              }
              
              this.gameMessage.set('Error al mover el barco. Posición revertida.');
            }
          });
        } else {
          console.error('❌ ERROR al cambiar velocidad:', resultado.message);
          console.log('REVIRTIENDO UI debido a error en cambio de velocidad...');
          
          // Guardar ID del barco para restaurar selección
          const selectedBarcoId = barco.id;
          
          if (selectedBarcoId) {
            this.loadBarcosAndRestoreSelection(selectedBarcoId);
          } else {
            this.loadBarcos();
            this.validMoves.set([]);
          }
          
          this.gameMessage.set(resultado.message || 'Error al cambiar velocidad. Posición revertida.');
        }
      },
      error: (error) => {
        console.error('❌ ERROR al cambiar velocidad:', error);
        console.log('REVIRTIENDO UI debido a error de conexión...');
        
        // Guardar ID del barco para restaurar selección
        const selectedBarcoId = barco.id;
        
        if (selectedBarcoId) {
          this.loadBarcosAndRestoreSelection(selectedBarcoId);
        } else {
          this.loadBarcos();
          this.validMoves.set([]);
        }
        
        this.gameMessage.set('Error de conexión al cambiar velocidad. Posición revertida.');
      }
    });
  }

  /**
   * Mueve un barco aplicando las reglas del juego
   */
  moverBarco(barcoId: number) {
    console.log('=== MOVIENDO BARCO ===');
    console.log('ID del barco a mover:', barcoId);
    
    // Obtener el estado actual del barco antes del movimiento
    const barcoAntes = this.barcos().find(b => b.id === barcoId);
    console.log('Estado del barco antes del movimiento:', barcoAntes);
    
    this.tableroService.moverBarco(1, barcoId).subscribe({
      next: (resultado: MovimientoResponse) => {
        console.log('=== RESPUESTA DEL BACKEND ===');
        console.log('Resultado completo:', resultado);
        console.log('Nueva posición del backend:', resultado.nuevaPosicion);
        
        this.gameMessage.set(resultado.mensaje);
        
        // Actualizar SOLO la posición del barco con la respuesta del backend
        this.barcos.update(boats => {
          const nuevosBarcos = boats.map(b => {
            if (b.id === barcoId) {
              const barcoActualizado = { 
                ...b, 
                posicion: {
                  id: b.posicion?.id, // Preservar ID si existe
                  x: resultado.nuevaPosicion.x, 
                  y: resultado.nuevaPosicion.y,
                  barcoId: b.id
                }
              };
              console.log('Barco actualizado localmente:', barcoActualizado);
              return barcoActualizado;
            }
            return b;
          });
          console.log('Array completo de barcos después de actualizar:', nuevosBarcos);
          return nuevosBarcos;
        });

        // Actualizar el barco seleccionado también
        this.selectedBarco.update(b => {
          if (b && b.id === barcoId) {
            const barcoSeleccionadoActualizado = { 
              ...b, 
              posicion: {
                id: b.posicion?.id,
                x: resultado.nuevaPosicion.x, 
                y: resultado.nuevaPosicion.y,
                barcoId: b.id
              }
            };
            console.log('Barco seleccionado actualizado:', barcoSeleccionadoActualizado);
            return barcoSeleccionadoActualizado;
          }
          return b;
        });

        // Log de verificación final
        setTimeout(() => {
          console.log('=== VERIFICACIÓN FINAL ===');
          console.log('Todos los barcos:', this.barcos());
          console.log('Barco seleccionado:', this.selectedBarco());
          
          // Verificar si el barco está en la posición correcta
          const barcoEnPosicion = this.getBarcoAtPosition(resultado.nuevaPosicion.x, resultado.nuevaPosicion.y);
          console.log(`Barco encontrado en posición (${resultado.nuevaPosicion.x}, ${resultado.nuevaPosicion.y}):`, barcoEnPosicion);
        }, 100);

        // Manejar diferentes tipos de resultado
        switch (resultado.tipo) {
          case 'META_ALCANZADA':
            this.juegoTerminado.set(true);
            const barco = this.barcos().find(b => b.id === barcoId);
            const jugador = this.jugadores().find(j => j.id === barco?.jugadorId);
            this.ganador.set(jugador?.nombre || 'Jugador desconocido');
            alert(`¡${jugador?.nombre || 'Jugador'} ha ganado la carrera!`);
            this.selectedBarco.set(null);
            this.validMoves.set([]);
            break;
            
          case 'DESTRUIDO':
            // Remover el barco destruido
            this.barcos.update(boats => boats.filter(b => b.id !== barcoId));
            this.selectedBarco.set(null);
            this.validMoves.set([]);
            break;
            
          case 'CONTINUA':
            // El juego continúa normalmente - recalcular movimientos válidos
            const updatedBarco = this.barcos().find(b => b.id === barcoId);
            if (updatedBarco) {
              this.calculateValidMoves(updatedBarco);
            }
            
            // Si es un juego por turnos, pasar al siguiente turno
            if (this.juegoIniciado()) {
              setTimeout(() => {
                this.siguienteTurno();
              }, 1500); // Dar tiempo para leer el mensaje
            }
            break;
        }
        
      },
      error: (error) => {
        console.error('Error al mover barco:', error);
        this.gameMessage.set('Error al mover el barco');
      }
    });
  }

  /**
   * Coloca un barco sin posición en el tablero por primera vez
   */
  colocarBarcoInicial(x: number, y: number) {
    const barco = this.selectedBarco();
    if (!barco || barco.posicion) {
      console.log('No hay barco seleccionado o ya tiene posición');
      return;
    }

    // Verificar que sea una casilla de Partida (P) - solo se puede colocar en líneas de salida
    if (this.tablero()[y][x].tipocelda !== 'P') {
      this.gameMessage.set('Solo puedes colocar barcos en las casillas de Partida (P) - casillas naranjas');
      return;
    }

    // Verificar que no haya otro barco en esa posición
    if (this.getBarcoAtPosition(x, y)) {
      this.gameMessage.set('Ya hay un barco en esa posición de partida');
      return;
    }

    console.log('Colocando barco en casilla de partida:', x, y);

    // Crear nueva posición
    const nuevaPosicion: Posicion = { x, y };

    // Actualizar el barco localmente primero
    this.barcos.update(boats => 
      boats.map(b => 
        b.id === barco.id 
          ? { ...b, posicion: nuevaPosicion }
          : b
      )
    );

    // Actualizar barco seleccionado
    this.selectedBarco.update(b => b ? { ...b, posicion: nuevaPosicion } : null);

    // Intentar actualizar en el backend si el barco tiene ID
    if (barco.id) {
      this.posicionService.createPosicion({
        x,
        y,
        barcoId: barco.id
      }).subscribe({
        next: (posicion) => {
          console.log('Posición creada en backend:', posicion);
          this.gameMessage.set(`🚢 Barco colocado en línea de partida (${x}, ${y}). ¡Listo para la regata!`);
        },
        error: (error) => {
          console.error('Error al crear posición en backend:', error);
          this.gameMessage.set(`Barco colocado localmente en partida (${x}, ${y})`);
        }
      });
    } else {
      this.gameMessage.set(`🚢 Barco colocado en línea de partida (${x}, ${y}). ¡Listo para la regata!`);
    }
  }

  /**
   * Método para mover a una posición específica (usado por el clic en celda)
   */
  moveBarcoToPosition(x: number, y: number) {
    const barco = this.selectedBarco();
    if (!barco || !barco.posicion) {
      console.log('No hay barco seleccionado o no tiene posición');
      return;
    }

    // Encontrar el cambio de velocidad necesario para llegar a esa posición
    const validMove = this.validMoves().find(move => move.x === x && move.y === y) as any;
    if (!validMove) {
      this.gameMessage.set('Movimiento no válido');
      console.log('Movimiento no válido para posición:', x, y);
      console.log('Movimientos válidos:', this.validMoves());
      return;
    }

    console.log('=== MOVIMIENTO DIRECTO ===');
    console.log('Moviendo barco desde:', barco.posicion, 'hacia:', x, y);
    console.log('Delta requerido:', validMove.deltaVx, validMove.deltaVy);
    console.log('Velocidad actual del barco:', barco.velocidadX, barco.velocidadY);
    
    // ✅ VALIDACIÓN EXTRA: Verificar que el validMove respete las reglas
    if (Math.abs(validMove.deltaVx) > 1 || Math.abs(validMove.deltaVy) > 1) {
      console.error('❌ ERROR: validMove tiene deltas fuera de rango:', validMove);
      this.gameMessage.set('❌ Error interno: movimiento inválido calculado');
      return;
    }
    
    if (validMove.deltaVx !== 0 && validMove.deltaVy !== 0) {
      console.error('❌ ERROR: validMove viola regla X o Y:', validMove);
      this.gameMessage.set('❌ Error interno: movimiento diagonal no permitido');
      return;
    }

    // Verificar turnos
    if (!this.puedeRealizarAccion()) {
      this.gameMessage.set(`❌ No es tu turno. Es el turno de ${this.jugadorActual()?.nombre}`);
      return;
    }

    // ACTUALIZAR INMEDIATAMENTE en la interfaz (optimistic update)
    const velocidadXActual = barco.velocidadX || 0;
    const velocidadYActual = barco.velocidadY || 0;
    const newVx = velocidadXActual + validMove.deltaVx;
    const newVy = velocidadYActual + validMove.deltaVy;
    
    console.log('Velocidades normalizadas:', { velocidadXActual, velocidadYActual });
    console.log('Nuevas velocidades calculadas:', { newVx, newVy });
    console.log('Actualizando UI inmediatamente...');
    
    // Actualizar inmediatamente para mejor UX
    this.barcos.update(boats => 
      boats.map(b => 
        b.id === barco.id 
          ? { 
              ...b, 
              posicion: { ...b.posicion, x, y },
              velocidadX: newVx,
              velocidadY: newVy
            }
          : b
      )
    );
    
    this.selectedBarco.update(b => 
      b && b.id === barco.id 
        ? { 
            ...b, 
            posicion: { ...b.posicion, x, y },
            velocidadX: newVx,
            velocidadY: newVy
          }
        : b
    );

    // Limpiar movimientos válidos
    this.validMoves.set([]);

    // Aplicar el cambio de velocidad y mover en el backend
    this.cambiarVelocidadYMover(validMove.deltaVx, validMove.deltaVy);
  }

  updateBarcoPositionAndVelocity(barcoId: number, newPosition: Posicion, newVx: number, newVy: number) {
    this.barcos.update(boats => 
      boats.map(b => 
        b.id === barcoId 
          ? { ...b, posicion: newPosition, velocidadX: newVx, velocidadY: newVy }
          : b
      )
    );
  }

  updateBarcoPositionAndVelocityLocal(barcoId: number, x: number, y: number, newVx: number, newVy: number) {
    this.barcos.update(boats => 
      boats.map(b => 
        b.id === barcoId 
          ? { ...b, posicion: { ...b.posicion, x, y }, velocidadX: newVx, velocidadY: newVy }
          : b
      )
    );
  }

  getBarcoAtPosition(x: number, y: number): Barco | null {
    const barco = this.barcos().find(barco => 
      barco.posicion && barco.posicion.x === x && barco.posicion.y === y
    ) || null;
    
    return barco;
  }

  isValidMovePosition(x: number, y: number): boolean {
    const isValid = this.validMoves().some(move => move.x === x && move.y === y);
    // Debug solo para algunas celdas para no sobrecargar
    if (isValid) {
      console.log(`Celda (${x}, ${y}) es movimiento válido`);
    }
    return isValid;
  }

  getCeldaClass(celda: Celda): string {
    let classes = ['celda'];
    
    switch (celda.tipocelda) {
      case 'A':
        classes.push('agua');
        break;
      case 'P':
        classes.push('partida');
        break;
      case 'M':
        classes.push('meta');
        break;
      case 'X':
        classes.push('pared');
        break;
    }

    const barco = this.getBarcoAtPosition(celda.x, celda.y);
    if (barco) {
      classes.push('ocupada');
      if (this.selectedBarco()?.id === barco.id) {
        classes.push('selected');
      }
    }

    // Resaltar movimientos válidos
    if (this.isValidMovePosition(celda.x, celda.y)) {
      classes.push('valid-move');
    }

    return classes.join(' ');
  }

  onCeldaClick(celda: Celda) {
    console.log('Clic en celda:', celda);
    console.log('Barco seleccionado:', this.selectedBarco());
    console.log('Tipo de celda:', celda.tipocelda);
    
    const barco = this.getBarcoAtPosition(celda.x, celda.y);
    console.log('Barco en posición:', barco);
    
    if (barco) {
      // Seleccionar barco
      console.log('Seleccionando barco existente');
      this.selectBarco(barco);
    } else if (this.selectedBarco() && !this.selectedBarco()?.posicion) {
      // Colocar barco sin posición en el tablero
      console.log('Intentando colocar barco sin posición');
      this.colocarBarcoInicial(celda.x, celda.y);
    } else if (this.isValidMovePosition(celda.x, celda.y)) {
      // Mover barco a esta posición
      console.log('Moviendo barco a posición válida');
      this.moveBarcoToPosition(celda.x, celda.y);
    } else {
      // Deseleccionar
      console.log('Deseleccionando barco');
      this.selectedBarco.set(null);
      this.validMoves.set([]);
    }
  }

  getJugadorName(jugadorId: number): string {
    const jugador = this.jugadores().find(j => j.id === jugadorId);
    return jugador?.nombre || 'Desconocido';
  }

  logout() {
    localStorage.removeItem('currentUser');
    this.router.navigate(['/']);
  }

  // SISTEMA DE TURNOS

  iniciarJuego() {
    if (this.jugadores().length < 2) {
      this.gameMessage.set('⚠️ Necesitas al menos 2 jugadores para iniciar el juego');
      return;
    }

    const jugadoresConBarcos = this.jugadores().filter(j => 
      this.barcos().some(b => b.jugadorId === j.id && b.posicion)
    );

    if (jugadoresConBarcos.length < 2) {
      this.gameMessage.set('⚠️ Cada jugador debe tener al menos un barco colocado en el tablero');
      return;
    }

    this.juegoIniciado.set(true);
    this.turnoActual.set(0);
    
    // Reiniciar estado de velocidad para el nuevo juego
    this.velocidadCambiadaEnTurno.set(false);
    this.cambioVelocidadTurno.set(null);
    
    this.actualizarJugadorActual();
    this.gameMessage.set(`🎮 ¡Juego iniciado! Turno de ${this.jugadorActual()?.nombre}`);
  }

  actualizarJugadorActual() {
    const jugadoresConBarcos = this.jugadores().filter(j => 
      this.barcos().some(b => b.jugadorId === j.id && b.posicion)
    );
    
    if (jugadoresConBarcos.length > 0) {
      this.jugadorActual.set(jugadoresConBarcos[this.turnoActual() % jugadoresConBarcos.length]);
      this.faseDelTurno.set('seleccionar-barco');
    }
  }

  siguienteTurno() {
    console.log('🔄 === AVANZANDO TURNO ===');
    console.log('Turno actual antes:', this.turnoActual());
    console.log('Jugador actual antes:', this.jugadorActual()?.nombre);
    
    this.turnoActual.update(t => t + 1);
    this.actualizarJugadorActual();
    
    console.log('Turno actual después:', this.turnoActual());
    console.log('Jugador actual después:', this.jugadorActual()?.nombre);
    
    this.selectedBarco.set(null);
    this.validMoves.set([]);
    
    // Reiniciar estado de velocidad para el nuevo turno
    this.velocidadCambiadaEnTurno.set(false);
    this.cambioVelocidadTurno.set(null);
    
    console.log('✅ Turno avanzado exitosamente');
    this.gameMessage.set(`🔄 Turno de ${this.jugadorActual()?.nombre}`);
  }

  puedeSeleccionarBarco(barco: Barco): boolean {
    if (!this.juegoIniciado()) return true; // Fuera del juego, puede seleccionar cualquiera
    return barco.jugadorId === this.jugadorActual()?.id;
  }

  puedeRealizarAccion(): boolean {
    if (!this.juegoIniciado()) return true;
    const barcoSeleccionado = this.selectedBarco();
    return barcoSeleccionado?.jugadorId === this.jugadorActual()?.id;
  }

  // Métodos de administración de barcos
  editarBarcoSeleccionado(): void {
    const barco = this.selectedBarco();
    if (barco?.id) {
      this.router.navigate(['/barco/edit', barco.id]);
    }
  }

  eliminarBarcoSeleccionado(): void {
    const barco = this.selectedBarco();
    if (barco?.id && confirm('¿Estás seguro de que quieres eliminar este barco?')) {
      this.barcoService.deleteBarco(barco.id).subscribe({
        next: () => {
          this.selectedBarco.set(null);
          this.loadBarcos(); // Recargar la lista
          alert('Barco eliminado correctamente');
        },
        error: (error) => {
          console.error('Error al eliminar barco:', error);
          alert('Error al eliminar el barco');
        }
      });
    }
  }

  irAListaBarcos(): void {
    this.router.navigate(['/barco/list']);
  }

  // Estado para control de cambios de velocidad por turno
  velocidadCambiadaEnTurno = signal<boolean>(false);
  cambioVelocidadTurno = signal<{deltaVx: number, deltaVy: number} | null>(null);

  // Control de velocidad durante el juego
  modificarVelocidad(deltaVx: number, deltaVy: number): void {
    const barco = this.selectedBarco();
    if (!barco || !barco.id) {
      console.log('No hay barco seleccionado para modificar velocidad');
      return;
    }

    if (!this.puedeRealizarAccion()) {
      this.gameMessage.set('❌ No puedes modificar la velocidad. No es tu turno o el juego no ha iniciado.');
      return;
    }

    // Verificar si ya cambió velocidad en este turno
    if (this.velocidadCambiadaEnTurno()) {
      this.gameMessage.set('❌ Ya cambiaste la velocidad en este turno. Solo un cambio por turno.');
      return;
    }

    // Validaciones frontend - solo un componente puede cambiar
    if (deltaVx !== 0 && deltaVy !== 0) {
      this.gameMessage.set('❌ Solo puedes cambiar velocidad en X o Y, no ambos en el mismo turno');
      return;
    }

    if (Math.abs(deltaVx) > 1 || Math.abs(deltaVy) > 1) {
      this.gameMessage.set('❌ Solo puedes cambiar la velocidad en ±1 por turno');
      return;
    }

    // Validar límites razonables de velocidad total
    const nuevaVx = (barco.velocidadX || 0) + deltaVx;
    const nuevaVy = (barco.velocidadY || 0) + deltaVy;
    
    if (Math.abs(nuevaVx) > 5 || Math.abs(nuevaVy) > 5) {
      this.gameMessage.set('❌ La velocidad no puede exceder ±5 en ningún componente');
      return;
    }

    console.log('=== MODIFICANDO VELOCIDAD ===');
    console.log('Barco ID:', barco.id);
    console.log('Delta velocidad:', deltaVx, deltaVy);
    console.log('Velocidad antes:', barco.velocidadX, barco.velocidadY);
    console.log('Velocidad después:', nuevaVx, nuevaVy);

    // Llamar al servicio para cambiar velocidad
    this.tableroService.cambiarVelocidadBarco(1, barco.id, deltaVx, deltaVy).subscribe({
      next: (resultado) => {
        console.log('Velocidad modificada exitosamente:', resultado);
        
        if (resultado.success) {
          // Marcar que ya se cambió velocidad en este turno
          this.velocidadCambiadaEnTurno.set(true);
          this.cambioVelocidadTurno.set({deltaVx, deltaVy});

          // Actualizar la velocidad del barco localmente
          this.barcos.update(boats => 
            boats.map(b => 
              b.id === barco.id 
                ? { 
                    ...b, 
                    velocidadX: nuevaVx,
                    velocidadY: nuevaVy
                  }
                : b
            )
          );

          // Actualizar el barco seleccionado
          this.selectedBarco.update(b => 
            b && b.id === barco.id 
              ? { 
                  ...b, 
                  velocidadX: nuevaVx,
                  velocidadY: nuevaVy
                }
              : b
          );

          // Recalcular movimientos válidos con la nueva velocidad
          const updatedBarco = this.selectedBarco();
          if (updatedBarco) {
            this.calculateValidMoves(updatedBarco);
          }

          const direccion = deltaVx !== 0 ? `X${deltaVx > 0 ? '+' : ''}${deltaVx}` : `Y${deltaVy > 0 ? '+' : ''}${deltaVy}`;
          this.gameMessage.set(`✅ Velocidad modificada: ${direccion} → v(${nuevaVx}, ${nuevaVy}). Ahora mueve el barco.`);
        } else {
          this.gameMessage.set('❌ No se pudo modificar la velocidad - ' + (resultado.message || 'Error desconocido'));
        }
      },
      error: (error) => {
        console.error('Error al modificar velocidad:', error);
        this.gameMessage.set('❌ Error al modificar velocidad');
      }
    });
  }

  // Validaciones para habilitar/deshabilitar botones de velocidad
  puedeAumentarVelocidadX(): boolean {
    const barco = this.selectedBarco();
    if (!barco || !this.puedeRealizarAccion() || this.velocidadCambiadaEnTurno()) return false;
    return (barco.velocidadX || 0) < 5;
  }

  puedeDisminuirVelocidadX(): boolean {
    const barco = this.selectedBarco();
    if (!barco || !this.puedeRealizarAccion() || this.velocidadCambiadaEnTurno()) return false;
    return (barco.velocidadX || 0) > -5;
  }

  puedeAumentarVelocidadY(): boolean {
    const barco = this.selectedBarco();
    if (!barco || !this.puedeRealizarAccion() || this.velocidadCambiadaEnTurno()) return false;
    return (barco.velocidadY || 0) < 5;
  }

  puedeDisminuirVelocidadY(): boolean {
    const barco = this.selectedBarco();
    if (!barco || !this.puedeRealizarAccion() || this.velocidadCambiadaEnTurno()) return false;
    return (barco.velocidadY || 0) > -5;
  }
}