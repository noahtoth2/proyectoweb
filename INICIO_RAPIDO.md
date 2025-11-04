# 🚀 INICIO RÁPIDO - Regata Online Multijugador

## 📋 Requisitos Previos

- ✅ Java 17+ instalado
- ✅ Node.js 18+ instalado
- ✅ Maven 3.8+ (incluido en backend como mvnw)
- ✅ Angular CLI instalado globalmente (opcional)

---

## ⚡ Inicio Rápido (2 Minutos)

### 1️⃣ Iniciar Backend
```powershell
cd c:\Users\romer\proyectoweb\backend
.\mvnw spring-boot:run
```

Espera hasta ver:
```
Started DemoApplication in X.XXX seconds
```

🌐 **Backend listo en:** http://localhost:8080

---

### 2️⃣ Iniciar Frontend
```powershell
# En una NUEVA terminal
cd c:\Users\romer\proyectoweb\frontend
npm start
```

Espera hasta ver:
```
✔ Browser application bundle generation complete.
** Angular Live Development Server is listening on localhost:4200 **
```

🌐 **Frontend listo en:** http://localhost:4200

---

### 3️⃣ Probar la Aplicación

#### Opción A: Modo Local (1 Jugador)
```
1. Abrir navegador → http://localhost:4200
2. Login con cualquier usuario existente
3. Click "🎮 Juego Local"
4. Crear jugadores y barcos
5. Iniciar juego y jugar
```

#### Opción B: Modo Multijugador (2 Jugadores)
```
JUGADOR 1:
1. Abrir Chrome → http://localhost:4200
2. Login (usuario: "admin", password: "admin")
3. Click "🌐 Crear Partida Multijugador"
4. Ingresar nombre: "Juan"
5. Click "Crear"
6. Copiar código que aparece (ej: "ABC123")
7. Esperar a que se una el segundo jugador...

JUGADOR 2:
1. Abrir Chrome Incógnito → http://localhost:4200
2. Login (usuario: "user", password: "user")
3. Click "🔗 Unirse a Partida"
4. Ingresar código: "ABC123"
5. Ingresar nombre: "María"
6. Click "Unirse"

JUGADOR 1:
7. Ver que María se unió
8. Click "🚀 Iniciar Juego"

AMBOS JUGADORES:
9. Serán redirigidos al tablero
10. ¡Jugar por turnos!
```

---

## 🧪 Probar la API (Opcional)

```powershell
cd c:\Users\romer\proyectoweb
.\test-api-multijugador.ps1
```

Salida esperada:
```
1️⃣  Creando partida...
   ✅ Partida creada: ABC123

2️⃣  Segundo jugador uniéndose...
   ✅ María se unió. Jugadores: 2

3️⃣  Iniciando juego...
   ✅ Juego iniciado

...

✅ ¡TODAS LAS PRUEBAS COMPLETADAS EXITOSAMENTE!
```

---

## 🐛 Solución de Problemas

### Backend no inicia
```
❌ Error: puerto 8080 ya está en uso

Solución:
# Matar proceso en puerto 8080
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# O cambiar puerto en backend/src/main/resources/application.properties
server.port=8081
```

### Frontend no inicia
```
❌ Error: puerto 4200 ya está en uso

Solución:
# Opción 1: Matar proceso
netstat -ano | findstr :4200
taskkill /PID <PID> /F

# Opción 2: Usar otro puerto
ng serve --port 4201
```

### Error de compilación en Frontend
```
❌ npm install no ejecutado

Solución:
cd c:\Users\romer\proyectoweb\frontend
npm install
npm start
```

### Error de base de datos
```
❌ No se puede conectar a la base de datos

Solución:
El backend usa H2 en memoria por defecto, no requiere configuración.
Si persiste el error, verifica backend/src/main/resources/application.properties
```

---

## 📂 Estructura del Proyecto

```
proyectoweb/
│
├── backend/                    # Spring Boot API
│   ├── src/main/java/
│   │   └── com/proyecto/demo/
│   │       ├── models/         # Entidades JPA
│   │       ├── controllers/    # REST Controllers
│   │       ├── services/       # Lógica de negocio
│   │       ├── repository/     # JPA Repositories
│   │       ├── dto/            # Data Transfer Objects
│   │       └── mappers/        # Entity ↔ DTO
│   └── src/main/resources/
│       └── application.properties
│
├── frontend/                   # Angular 17
│   ├── src/app/
│   │   ├── auth/               # Login/Registro
│   │   ├── lobby/              # Gestión de partidas ⭐ NUEVO
│   │   ├── tablero/            # Juego principal
│   │   ├── barco/              # CRUD barcos
│   │   ├── model/              # Interfaces TypeScript
│   │   └── shared/             # Servicios HTTP
│   └── package.json
│
├── MULTIJUGADOR_README.md      # Documentación completa
├── TEST_API.md                 # Guía de pruebas
├── RESUMEN_INTEGRACION.md      # Resumen técnico
└── test-api-multijugador.ps1   # Script de pruebas
```

---

## 🎮 Flujo del Juego Multijugador

```
┌─────────────┐
│   LOGIN     │
│ (Auth)      │
└──────┬──────┘
       │
       ▼
┌─────────────┐
│   LOBBY     │ ⭐ NUEVO
│  (Menú)     │
└──────┬──────┘
       │
       ├──────────────┬──────────────┐
       │              │              │
┌──────▼──────┐ ┌────▼─────┐ ┌─────▼──────┐
│ Juego Local │ │  Crear   │ │  Unirse    │
└──────┬──────┘ │ Partida  │ │  Partida   │
       │        └────┬─────┘ └─────┬──────┘
       │             │              │
       │             └──────┬───────┘
       │                    ▼
       │             ┌─────────────┐
       │             │ Sala Espera │ ⭐ NUEVO
       │             │  (Polling)  │
       │             └──────┬──────┘
       │                    │
       └────────────┬───────┘
                    ▼
             ┌─────────────┐
             │   TABLERO   │
             │  (Jugando)  │
             └──────┬──────┘
                    │
                    ▼
             ┌─────────────┐
             │  GANADOR    │
             └─────────────┘
```

---

## 🔑 Usuarios de Prueba

Por defecto, el backend incluye estos usuarios:

| Usuario | Password | Rol |
|---------|----------|-----|
| admin   | admin    | ADMIN |
| user    | user     | USER |

---

## 📡 Endpoints API Principales

### Autenticación
- `POST /api/auth/login` - Iniciar sesión

### Partidas (Multijugador) ⭐ NUEVO
- `POST /api/partidas/` - Crear partida
- `POST /api/partidas/unirse` - Unirse a partida
- `GET /api/partidas/{id}` - Obtener estado
- `POST /api/partidas/{id}/iniciar` - Iniciar juego
- `POST /api/partidas/{id}/siguiente-turno` - Avanzar turno
- `POST /api/partidas/{id}/finalizar` - Finalizar con ganador

### Jugadores
- `GET /api/jugadores` - Listar todos
- `POST /api/jugadores` - Crear jugador
- `GET /api/jugadores/{id}` - Obtener por ID

### Barcos
- `GET /api/barcos` - Listar todos
- `POST /api/barcos` - Crear barco
- `PUT /api/barcos/{id}` - Actualizar barco
- `POST /api/barcos/{id}/mover` - Mover barco

### Tablero
- `GET /api/tableros` - Listar tableros
- `GET /api/tableros/{id}/celdas` - Obtener celdas

---

## ✨ Nuevas Características

### ✅ Sistema Multijugador
- Crear partidas con código único
- Unirse usando código
- Sala de espera con polling
- Sincronización en tiempo real
- Sistema de turnos
- Registro de ganador

### ✅ UI/UX Mejorada
- Lobby con 4 vistas
- Indicadores visuales de turno (🟢/🔴)
- Botón copiar código al portapapeles
- Auto-navegación al iniciar juego
- Mensajes de estado dinámicos

---

## 📚 Documentación Adicional

- **Guía Completa**: `MULTIJUGADOR_README.md`
- **Pruebas API**: `TEST_API.md`
- **Resumen Técnico**: `RESUMEN_INTEGRACION.md`

---

## 🎯 Checklist de Primera Ejecución

- [ ] Backend iniciado (http://localhost:8080)
- [ ] Frontend iniciado (http://localhost:4200)
- [ ] Login exitoso
- [ ] Crear partida multijugador funciona
- [ ] Copiar código funciona
- [ ] Unirse con código funciona
- [ ] Iniciar juego redirige a tablero
- [ ] Polling sincroniza estado
- [ ] Solo el jugador en turno puede mover
- [ ] Al ganar se muestra mensaje correcto

---

## 🆘 Soporte

### Logs del Backend
```
c:\Users\romer\proyectoweb\backend\
└── logs/ (si están configurados)
```

### Logs del Frontend
- Abrir DevTools del navegador (F12)
- Ir a la pestaña "Console"
- Ver mensajes de error en rojo

### Base de Datos (H2 Console)
```
http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:testdb
User: sa
Password: (dejar vacío)
```

---

## 🎉 ¡Listo!

Tu aplicación **Regata Online** con soporte multijugador está funcionando.

**Disfruta el juego!** 🎮⛵
