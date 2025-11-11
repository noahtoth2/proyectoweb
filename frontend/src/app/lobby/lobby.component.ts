import { Component, OnInit, signal, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { PartidaService } from '../shared/partida.service';
import { Partida, CrearPartidaRequest, UnirsePartidaRequest } from '../model/partida';

interface Barco {
  id: number;
  modelo: {
    nombre: string;
  };
}

@Component({
  selector: 'app-lobby',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './lobby.component.html',
  styleUrls: ['./lobby.component.css']
})
export class LobbyComponent implements OnInit, OnDestroy {
  vistaActual = signal<'menu' | 'crear' | 'unirse' | 'sala'>('menu');
  partidasActivas = signal<Partida[]>([]);
  partidaActual = signal<Partida | null>(null);
  mensajeError = signal<string>('');
  cargando = signal<boolean>(false);
  miJugadorId = signal<number | null>(null);
  
  // Para selección de barcos
  barcosDisponibles = signal<Barco[]>([]);
  barcoSeleccionado = signal<number | null>(null);
  barcosOcupados = signal<Set<number>>(new Set());

  // Para crear partida
  nuevaPartida: CrearPartidaRequest = {
    nombre: '',
    nombreJugador: '',
    maxJugadores: 4
  };

  // Para unirse a partida
  unirseData: UnirsePartidaRequest = {
    codigo: '',
    nombreJugador: ''
  };

  // Polling interval
  private pollingInterval: any;

  constructor(
    private partidaService: PartidaService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarPartidasActivas();
  }

  ngOnDestroy(): void {
    this.detenerPolling();
  }

  cargarPartidasActivas(): void {
    this.partidaService.listarPartidasActivas().subscribe({
      next: (partidas) => {
        this.partidasActivas.set(partidas);
      },
      error: (error) => {
        console.error('Error al cargar partidas:', error);
      }
    });
  }

  mostrarCrearPartida(): void {
    this.vistaActual.set('crear');
    this.mensajeError.set('');
  }

  mostrarUnirsePartida(): void {
    this.vistaActual.set('unirse');
    this.mensajeError.set('');
  }

  volverAlMenu(): void {
    this.vistaActual.set('menu');
    this.mensajeError.set('');
    this.detenerPolling();
    this.cargarPartidasActivas();
  }

  crearPartida(): void {
    if (!this.nuevaPartida.nombre || !this.nuevaPartida.nombreJugador) {
      this.mensajeError.set('Por favor completa todos los campos');
      return;
    }

    this.cargando.set(true);
    this.partidaService.crearPartida(this.nuevaPartida).subscribe({
      next: (partida) => {
        this.partidaActual.set(partida);
        // ⭐ Guardar ID del jugador creador (el primero en la lista)
        if (partida.jugadores.length > 0 && partida.jugadores[0].id) {
          this.miJugadorId.set(partida.jugadores[0].id);
          localStorage.setItem('miJugadorId', partida.jugadores[0].id.toString());
        }
        this.vistaActual.set('sala');
        this.cargarBarcosDisponibles(); // ⭐ Cargar barcos disponibles
        this.iniciarPolling();
        this.cargando.set(false);
        this.mensajeError.set('');
      },
      error: (error) => {
        console.error('Error al crear partida:', error);
        this.mensajeError.set('Error al crear la partida');
        this.cargando.set(false);
      }
    });
  }

  unirsePartida(): void {
    if (!this.unirseData.codigo || !this.unirseData.nombreJugador) {
      this.mensajeError.set('Por favor completa todos los campos');
      return;
    }

    this.cargando.set(true);
    this.partidaService.unirsePartida(this.unirseData).subscribe({
      next: (partida) => {
        this.partidaActual.set(partida);
        // ⭐ Guardar ID del jugador que se unió (el último en la lista)
        if (partida.jugadores.length > 0) {
          const miJugador = partida.jugadores[partida.jugadores.length - 1];
          if (miJugador.id) {
            this.miJugadorId.set(miJugador.id);
            localStorage.setItem('miJugadorId', miJugador.id.toString());
          }
        }
        this.vistaActual.set('sala');
        this.cargarBarcosDisponibles(); // ⭐ Cargar barcos disponibles
        this.iniciarPolling();
        this.cargando.set(false);
        this.mensajeError.set('');
      },
      error: (error) => {
        console.error('Error al unirse a partida:', error);
        this.mensajeError.set('No se pudo unir a la partida. Verifica el código.');
        this.cargando.set(false);
      }
    });
  }

  unirseAPartidaActiva(partida: Partida): void {
    const nombreJugador = prompt('Ingresa tu nombre:');
    if (!nombreJugador) return;

    this.cargando.set(true);
    this.partidaService.unirsePartida({
      codigo: partida.codigo,
      nombreJugador: nombreJugador
    }).subscribe({
      next: (partidaActualizada) => {
        this.partidaActual.set(partidaActualizada);
        // ⭐ Guardar ID del jugador que se unió
        if (partidaActualizada.jugadores.length > 0) {
          const miJugador = partidaActualizada.jugadores[partidaActualizada.jugadores.length - 1];
          if (miJugador.id) {
            this.miJugadorId.set(miJugador.id);
            localStorage.setItem('miJugadorId', miJugador.id.toString());
          }
        }
        this.vistaActual.set('sala');
        this.cargarBarcosDisponibles(); // ⭐ Cargar barcos disponibles
        this.iniciarPolling();
        this.cargando.set(false);
      },
      error: (error) => {
        console.error('Error al unirse:', error);
        this.mensajeError.set('No se pudo unir a la partida');
        this.cargando.set(false);
      }
    });
  }

  iniciarJuego(): void {
    const partida = this.partidaActual();
    if (!partida) return;

    if (partida.jugadores.length < 2) {
      this.mensajeError.set('Se necesitan al menos 2 jugadores para iniciar');
      return;
    }

    // ⭐ Validar que todos los jugadores hayan seleccionado un barco
    if (!this.barcoSeleccionado()) {
      this.mensajeError.set('Debes seleccionar un barco antes de iniciar');
      return;
    }

    this.cargando.set(true);
    this.partidaService.iniciarPartida(partida.id).subscribe({
      next: (partidaActualizada) => {
        this.partidaActual.set(partidaActualizada);
        this.detenerPolling();
        
        // ⭐ Guardar información completa antes de navegar
        localStorage.setItem('partidaActual', JSON.stringify(partidaActualizada));
        
        // Guardar información del jugador actual
        const miId = this.miJugadorId();
        if (miId) {
          const miJugador = partidaActualizada.jugadores.find(j => j.id === miId);
          if (miJugador) {
            localStorage.setItem('jugadorActual', JSON.stringify(miJugador));
          }
        }
        
        this.router.navigate(['/tablero']);
        this.cargando.set(false);
      },
      error: (error) => {
        console.error('Error al iniciar juego:', error);
        this.mensajeError.set('Error al iniciar el juego');
        this.cargando.set(false);
      }
    });
  }

  salirDeSala(): void {
    this.detenerPolling();
    this.partidaActual.set(null);
    this.miJugadorId.set(null);
    this.barcoSeleccionado.set(null);
    this.barcosOcupados.set(new Set());
    localStorage.removeItem('miJugadorId');
    this.volverAlMenu();
  }

  copiarCodigo(): void {
    const partida = this.partidaActual();
    if (partida) {
      navigator.clipboard.writeText(partida.codigo);
      alert('Código copiado al portapapeles');
    }
  }

  // ========== SELECCIÓN DE BARCOS ==========
  
  cargarBarcosDisponibles(): void {
    const userId = localStorage.getItem('userId');
    if (!userId) return;

    fetch(`http://localhost:8080/api/auth/available-barcos?userId=${userId}`)
      .then(response => response.json())
      .then((barcos: Barco[]) => {
        this.barcosDisponibles.set(barcos);
      })
      .catch(error => {
        console.error('Error al cargar barcos:', error);
      });
  }

  seleccionarBarco(barcoId: number): void {
    // Verificar si el barco ya está ocupado por otro jugador en esta partida
    if (this.barcosOcupados().has(barcoId)) {
      this.mensajeError.set('Este barco ya fue seleccionado por otro jugador');
      return;
    }

    this.barcoSeleccionado.set(barcoId);
    this.barcosOcupados().add(barcoId);
    localStorage.setItem('barcoSeleccionadoId', barcoId.toString());
    this.mensajeError.set('');
  }

  barcoEstaOcupado(barcoId: number): boolean {
    return this.barcosOcupados().has(barcoId);
  }

  barcoEsMiSeleccion(barcoId: number): boolean {
    return this.barcoSeleccionado() === barcoId;
  }

  private iniciarPolling(): void {
    this.detenerPolling();
    this.pollingInterval = setInterval(() => {
      const partida = this.partidaActual();
      if (partida && !partida.iniciada) {
        this.partidaService.obtenerPartida(partida.id).subscribe({
          next: (partidaActualizada) => {
            this.partidaActual.set(partidaActualizada);
            
            // Si la partida ya se inició, navegar al tablero
            if (partidaActualizada.iniciada) {
              this.detenerPolling();
              
              // ⭐ Guardar información completa antes de navegar
              localStorage.setItem('partidaActual', JSON.stringify(partidaActualizada));
              
              // ⭐ Guardar información del jugador actual
              const miId = this.miJugadorId();
              if (miId) {
                const miJugador = partidaActualizada.jugadores.find(j => j.id === miId);
                if (miJugador) {
                  localStorage.setItem('jugadorActual', JSON.stringify(miJugador));
                }
              }
              
              this.router.navigate(['/tablero']);
            }
          },
          error: (error) => {
            console.error('Error en polling:', error);
          }
        });
      }
    }, 2000); // Actualizar cada 2 segundos
  }

  private detenerPolling(): void {
    if (this.pollingInterval) {
      clearInterval(this.pollingInterval);
      this.pollingInterval = null;
    }
  }

  esCreador(): boolean {
    const partida = this.partidaActual();
    const miId = this.miJugadorId();
    
    if (!partida || !miId) return false;
    
    // ⭐ Verificar si MI ID es igual al ID del creador
    return miId === partida.creadorId;
  }
}
