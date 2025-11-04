# 🎮 Sistema Multijugador - Regata Online

## ✅ Implementación Completada

Se ha integrado completamente el sistema multijugador en tu juego de Regata Online usando REST API.

## 🏗️ Arquitectura

### Backend (Spring Boot)
- **Entidad**: `Partida.java` - Gestiona las partidas multijugador
- **DTOs**: `PartidaDTO`, `CrearPartidaRequest`, `UnirsePartidaRequest`
- **Servicio**: `PartidaService.java` - Lógica de negocio (crear, unirse, iniciar, turnos)
- **Controlador**: `PartidaController.java` - 8 endpoints REST
- **Mapper**: `PartidaMapper.java` - Conversión entity ↔ DTO

### Frontend (Angular 17)
- **Servicio**: `partida.service.ts` - Cliente HTTP para API
- **Modelos**: `partida.ts` - Interfaces TypeScript
- **Componente Lobby**: `lobby.component.ts/html/css` - UI de gestión de partidas
- **Componente Tablero**: Integrado con soporte multijugador

## 🔄 Flujo de Juego Multijugador

### 1. Crear Partida
```typescript
Usuario → Lobby → "Crear Partida"
→ Backend genera código de 6 caracteres (ej: ABC123)
→ Usuario ve sala de espera con código para compartir
→ Polling cada 2 segundos esperando segundo jugador
```

### 2. Unirse a Partida
```typescript
Segundo usuario → Lobby → "Unirse" → Ingresa código
→ Backend valida código y agrega jugador
→ Ambos usuarios ven lista actualizada en sala
→ Cuando hay 2 jugadores: botón "Iniciar Juego" se habilita
```

### 3. Iniciar Juego
```typescript
Creador → "Iniciar Juego"
→ Backend marca partida como iniciada y asigna primer turno
→ Ambos usuarios redirigidos automáticamente a /tablero
→ Polling cada 3 segundos sincroniza estado del juego
```

### 4. Jugar (Turnos)
```typescript
Jugador en turno:
  - Ve indicador verde "🟢 TU TURNO"
  - Puede seleccionar barco y mover
  - Al completar movimiento → Backend avanza turno

Jugador esperando:
  - Ve indicador rojo "🔴 ESPERANDO"
  - Controles deshabilitados
  - Polling muestra movimientos del oponente en tiempo real
```

### 5. Finalizar
```typescript
Barco alcanza META → Backend registra ganador
→ Polling detecta finalización
→ Ambos jugadores ven mensaje de victoria
→ Opción de volver al lobby
```

## 📡 Endpoints API

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/partidas/` | Crear nueva partida |
| POST | `/api/partidas/unirse` | Unirse a partida por código |
| GET | `/api/partidas/activas` | Listar partidas disponibles |
| GET | `/api/partidas/{id}` | Obtener estado de partida |
| POST | `/api/partidas/{id}/iniciar` | Iniciar juego (requiere 2 jugadores) |
| POST | `/api/partidas/{id}/siguiente-turno` | Avanzar al siguiente turno |
| POST | `/api/partidas/{id}/finalizar` | Finalizar partida con ganador |
| DELETE | `/api/partidas/{id}` | Eliminar partida |

## 🎯 Características Implementadas

### ✅ Backend
- [x] Generación automática de códigos únicos (6 caracteres)
- [x] Validación de códigos existentes (evita colisiones)
- [x] Sistema de turnos por rondas (turno 0, 1, 2...)
- [x] Relación bidireccional Partida ↔ Jugador
- [x] Control de estados: en espera → iniciada → finalizada
- [x] Registro de ganador con nombre y ID
- [x] Validaciones: mínimo 2 jugadores, código válido, etc.

### ✅ Frontend - Lobby
- [x] 4 vistas: Menú → Crear/Unirse → Sala de espera
- [x] Formulario crear partida (nombre del jugador)
- [x] Formulario unirse (código + nombre)
- [x] Sala de espera con:
  - Código de partida con botón copiar 📋
  - Lista de jugadores conectados
  - Polling cada 2 segundos
  - Auto-navegación cuando inicia el juego
- [x] Manejo de errores (código inválido, partida llena, etc.)

### ✅ Frontend - Tablero
- [x] Detección automática de modo multijugador (LocalStorage)
- [x] Polling cada 3 segundos para sincronización
- [x] Indicadores visuales de turno (verde/rojo)
- [x] Restricción de acciones por turno (esMiTurno())
- [x] Mensaje dinámico de estado (getMensajeTurno())
- [x] Integración con sistema de turnos del servidor
- [x] Finalización automática cuando barco alcanza meta
- [x] Botón "Volver al Lobby"
- [x] Display del código de partida en header
- [x] Limpieza de intervalos en ngOnDestroy

## 🔧 Métodos Clave - TableroComponent

### Multijugador
```typescript
iniciarPollingMultijugador()      // Inicia sincronización cada 3s
detenerPollingMultijugador()      // Limpia intervalos
actualizarEstadoPartida()         // Sincroniza con servidor
esMiTurno()                       // Valida si puede jugar
siguienteTurnoMultijugador()      // Avanza turno en servidor
finalizarPartidaMultijugador()    // Declara ganador
volverAlLobby()                   // Limpia y navega a lobby
getMensajeTurno()                 // Mensaje dinámico de estado
```

### Modificados
```typescript
ngOnInit()        // Detecta modo multi, inicia polling
ngOnDestroy()     // Limpia intervalos
selectBarco()     // Valida esMiTurno() antes de seleccionar
onCeldaClick()    // Valida esMiTurno() antes de mover
siguienteTurno()  // Delega a multi si es necesario
moveBarcoToPosition() // Llama finalizarPartidaMultijugador() en META
```

## 💾 Almacenamiento LocalStorage

```typescript
// Al crear/unirse
localStorage.setItem('jugadorActual', JSON.stringify(jugador));
localStorage.setItem('partidaActual', JSON.stringify(partida));

// En tablero
const jugador = JSON.parse(localStorage.getItem('jugadorActual'));
const partida = JSON.parse(localStorage.getItem('partidaActual'));

// Al salir
localStorage.removeItem('partidaActual');
```

## 🎨 UI/UX Multijugador

### Header
```html
🏁 Regata Online | Bienvenido, Juan | 🎮 Partida: ABC123
[⬅️ Volver al Lobby] [Cerrar Sesión]
```

### Indicador de Turno
```
Modo Local:
🔄 Turno: Juan | Seleccionar Barco

Modo Multi (Mi turno):
🎮 ES TU TURNO - Realiza tu movimiento [🟢 TU TURNO]

Modo Multi (Esperando):
⏳ Turno de: María - Esperando... [🔴 ESPERANDO]
```

### Sala de Espera (Lobby)
```
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
🎮 Sala de Espera
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

📋 Código de Partida: ABC123 [Copiar]

👥 Jugadores Conectados (2/2):
  • Juan (Creador)
  • María

[🚀 Iniciar Juego]  [🚪 Salir]
```

## 🧪 Pruebas

### Escenario 1: Crear y Unirse
1. Usuario A → Lobby → Crear partida → "Juan"
2. Usuario A ve código "ABC123" en sala de espera
3. Usuario A copia código y comparte
4. Usuario B → Lobby → Unirse → Código "ABC123" + Nombre "María"
5. Ambos ven lista actualizada con 2 jugadores
6. Usuario A hace clic en "Iniciar Juego"
7. Ambos redirigidos a /tablero

### Escenario 2: Jugar por Turnos
1. Juego inicia con turno de Juan
2. Juan ve "🟢 TU TURNO", María ve "🔴 ESPERANDO"
3. Juan mueve su barco
4. Servidor avanza turno automáticamente
5. María ve "🟢 TU TURNO", Juan ve "🔴 ESPERANDO"
6. María mueve su barco
7. Ciclo continúa hasta que alguien gana

### Escenario 3: Victoria
1. Barco de Juan alcanza META
2. Backend registra a Juan como ganador
3. Polling detecta finalización en ambos clientes
4. Juan ve: "🏆 ¡Felicidades! ¡Has ganado la partida!"
5. María ve: "🏁 Partida finalizada. Ganador: Juan"
6. Ambos pueden volver al lobby

## 🐛 Solución de Problemas

### "No es tu turno"
- **Causa**: Estás en modo multijugador pero no es tu turno
- **Solución**: Espera a que el polling actualice y sea tu turno

### Partida no sincroniza
- **Causa**: Polling detenido o error de red
- **Solución**: Recarga la página, el polling se reinicia

### No puedo unirme a partida
- **Causa**: Código incorrecto o partida ya llena/iniciada
- **Solución**: Verifica el código o crea nueva partida

## 📊 Diagrama de Estados

```
┌─────────────┐
│   LOBBY     │
│  (Menú)     │
└──────┬──────┘
       │
       ├──────────┐
       ▼          ▼
  ┌────────┐  ┌────────┐
  │ CREAR  │  │ UNIRSE │
  └───┬────┘  └────┬───┘
      │            │
      └────┬───────┘
           ▼
    ┌─────────────┐
    │ SALA ESPERA │
    │ (Polling)   │
    └──────┬──────┘
           │ 2 jugadores + Iniciar
           ▼
    ┌─────────────┐
    │   TABLERO   │
    │ (Jugando)   │
    │  Polling 3s │
    └──────┬──────┘
           │ Barco alcanza meta
           ▼
    ┌─────────────┐
    │  FINALIZADA │
    │  (Ganador)  │
    └──────┬──────┘
           │
           ▼
    [Volver al Lobby]
```

## 🚀 Próximos Pasos Sugeridos

### Mejoras Opcionales
- [ ] Soporte para más de 2 jugadores (3-4 jugadores)
- [ ] Chat en tiempo real durante partida
- [ ] Historial de partidas completadas
- [ ] Rankings y estadísticas de jugadores
- [ ] Reconexión automática si se pierde conexión
- [ ] WebSockets en lugar de polling (más eficiente)
- [ ] Notificaciones cuando es tu turno
- [ ] Temporizador por turno (30 segundos límite)
- [ ] Modo espectador (observar partidas)
- [ ] Replay de partidas

## 📝 Notas de Implementación

- **Polling**: Se usa intervalo de 3 segundos en juego, 2 en lobby
- **Códigos**: 6 caracteres alfanuméricos (mayúsculas), ej: "A1B2C3"
- **Turnos**: Basados en número de ronda (0, 1, 2...), no tiempo
- **Sincronización**: Pull-based (cliente pide actualizaciones)
- **Estado**: Manejado por backend, cliente es stateless
- **Persistencia**: JPA guarda todo en base de datos

## ✨ Conclusión

Tu juego ahora soporta completamente el modo multijugador usando REST API. Los jugadores pueden:
- Crear partidas y compartir códigos
- Unirse a partidas existentes
- Jugar por turnos con sincronización automática
- Ver en tiempo real los movimientos del oponente
- Competir hasta que alguien alcance la meta

¡Disfruta tu Regata Online multijugador! 🎮⛵
