# 🧪 Script de Pruebas - API Multijugador

## Prerrequisitos
- Backend corriendo en http://localhost:8080
- Herramienta: PowerShell, curl, o Postman

## 1. Crear Primera Partida

```powershell
$body = @{
    nombreJugador = "Juan"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/partidas/" `
    -Method POST `
    -ContentType "application/json" `
    -Body $body
```

**Respuesta Esperada:**
```json
{
  "id": 1,
  "codigo": "ABC123",
  "jugadores": [
    {
      "id": 1,
      "nombre": "Juan"
    }
  ],
  "iniciada": false,
  "finalizada": false,
  "turnoActualId": null
}
```

**IMPORTANTE:** Guarda el `codigo` (ej: "ABC123") para el siguiente paso.

---

## 2. Unirse a la Partida

```powershell
$body = @{
    codigo = "ABC123"  # ← Usa el código del paso anterior
    nombreJugador = "María"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/partidas/unirse" `
    -Method POST `
    -ContentType "application/json" `
    -Body $body
```

**Respuesta Esperada:**
```json
{
  "id": 1,
  "codigo": "ABC123",
  "jugadores": [
    {
      "id": 1,
      "nombre": "Juan"
    },
    {
      "id": 2,
      "nombre": "María"
    }
  ],
  "iniciada": false,
  "finalizada": false
}
```

---

## 3. Iniciar Juego

```powershell
$partidaId = 1  # ← ID de la partida creada

Invoke-RestMethod -Uri "http://localhost:8080/api/partidas/$partidaId/iniciar" `
    -Method POST
```

**Respuesta Esperada:**
```json
{
  "id": 1,
  "codigo": "ABC123",
  "jugadores": [...],
  "iniciada": true,
  "finalizada": false,
  "turnoActualId": 1,
  "turnoActualNombre": "Juan"
}
```

---

## 4. Obtener Estado Actual

```powershell
$partidaId = 1

Invoke-RestMethod -Uri "http://localhost:8080/api/partidas/$partidaId" `
    -Method GET
```

---

## 5. Avanzar Turno

```powershell
$partidaId = 1

Invoke-RestMethod -Uri "http://localhost:8080/api/partidas/$partidaId/siguiente-turno" `
    -Method POST
```

**Respuesta Esperada:**
```json
{
  "turnoActualId": 2,
  "turnoActualNombre": "María",
  "turnoNumero": 1
}
```

---

## 6. Finalizar Partida (Declarar Ganador)

```powershell
$partidaId = 1
$ganadorId = 1  # ID de Juan

Invoke-RestMethod -Uri "http://localhost:8080/api/partidas/$partidaId/finalizar?ganadorId=$ganadorId" `
    -Method POST
```

**Respuesta Esperada:**
```json
{
  "id": 1,
  "codigo": "ABC123",
  "finalizada": true,
  "ganadorId": 1,
  "ganadorNombre": "Juan"
}
```

---

## 7. Listar Partidas Activas

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/partidas/activas" `
    -Method GET
```

**Respuesta Esperada:**
```json
[
  {
    "id": 1,
    "codigo": "ABC123",
    "jugadores": [...],
    "iniciada": true,
    "finalizada": false
  }
]
```

---

## ✅ Checklist de Validación

- [ ] **Crear partida** genera código único de 6 caracteres
- [ ] **Unirse** con código válido agrega segundo jugador
- [ ] **Unirse** con código inválido devuelve error 404
- [ ] **Iniciar juego** con 1 jugador devuelve error 400
- [ ] **Iniciar juego** con 2 jugadores asigna primer turno
- [ ] **Siguiente turno** alterna entre jugadores correctamente
- [ ] **Finalizar** marca partida como terminada con ganador
- [ ] **Listar activas** solo muestra partidas no finalizadas

---

## 🐛 Errores Comunes

### 404 Not Found
```json
{
  "status": 404,
  "message": "Partida no encontrada con código: XXXX"
}
```
**Causa:** Código de partida incorrecto  
**Solución:** Verifica el código generado en el paso 1

---

### 400 Bad Request (Al iniciar)
```json
{
  "status": 400,
  "message": "Se requieren al menos 2 jugadores para iniciar"
}
```
**Causa:** Intentaste iniciar con solo 1 jugador  
**Solución:** Completa el paso 2 antes del paso 3

---

### 500 Internal Server Error
**Causa:** Backend no está corriendo o error en base de datos  
**Solución:** 
```powershell
cd c:\Users\romer\proyectoweb\backend
.\mvnw spring-boot:run
```

---

## 🔄 Script Completo (Flujo Automático)

```powershell
# 1. Crear partida
Write-Host "1️⃣ Creando partida..." -ForegroundColor Cyan
$partida = Invoke-RestMethod -Uri "http://localhost:8080/api/partidas/" `
    -Method POST `
    -ContentType "application/json" `
    -Body (@{ nombreJugador = "Juan" } | ConvertTo-Json)

Write-Host "✅ Partida creada: $($partida.codigo)" -ForegroundColor Green
$codigo = $partida.codigo
$partidaId = $partida.id

# 2. Unirse
Write-Host "`n2️⃣ Segundo jugador uniéndose..." -ForegroundColor Cyan
$partida = Invoke-RestMethod -Uri "http://localhost:8080/api/partidas/unirse" `
    -Method POST `
    -ContentType "application/json" `
    -Body (@{ codigo = $codigo; nombreJugador = "María" } | ConvertTo-Json)

Write-Host "✅ María se unió. Jugadores: $($partida.jugadores.Count)" -ForegroundColor Green

# 3. Iniciar
Write-Host "`n3️⃣ Iniciando juego..." -ForegroundColor Cyan
$partida = Invoke-RestMethod -Uri "http://localhost:8080/api/partidas/$partidaId/iniciar" `
    -Method POST

Write-Host "✅ Juego iniciado. Turno de: $($partida.turnoActualNombre)" -ForegroundColor Green

# 4. Avanzar turno
Write-Host "`n4️⃣ Avanzando turno..." -ForegroundColor Cyan
$partida = Invoke-RestMethod -Uri "http://localhost:8080/api/partidas/$partidaId/siguiente-turno" `
    -Method POST

Write-Host "✅ Nuevo turno de: $($partida.turnoActualNombre)" -ForegroundColor Green

# 5. Finalizar
Write-Host "`n5️⃣ Finalizando partida..." -ForegroundColor Cyan
$ganadorId = $partida.jugadores[0].id
$partida = Invoke-RestMethod -Uri "http://localhost:8080/api/partidas/$partidaId/finalizar?ganadorId=$ganadorId" `
    -Method POST

Write-Host "🏆 Ganador: $($partida.ganadorNombre)" -ForegroundColor Yellow

Write-Host "`n✅ ¡Todas las pruebas completadas!" -ForegroundColor Green
```

**Ejecutar:**
```powershell
cd c:\Users\romer\proyectoweb
.\test-api-multijugador.ps1
```

---

## 📊 Salida Esperada

```
1️⃣ Creando partida...
✅ Partida creada: A1B2C3

2️⃣ Segundo jugador uniéndose...
✅ María se unió. Jugadores: 2

3️⃣ Iniciando juego...
✅ Juego iniciado. Turno de: Juan

4️⃣ Avanzando turno...
✅ Nuevo turno de: María

5️⃣ Finalizando partida...
🏆 Ganador: Juan

✅ ¡Todas las pruebas completadas!
```
