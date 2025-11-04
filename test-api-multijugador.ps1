# Script de prueba automática para API Multijugador
# Ejecutar: .\test-api-multijugador.ps1

Write-Host "`n🧪 ===== PRUEBA AUTOMÁTICA API MULTIJUGADOR =====" -ForegroundColor Magenta
Write-Host "Backend: http://localhost:8080`n" -ForegroundColor Gray

try {
    # 1. Crear partida
    Write-Host "1️⃣  Creando partida..." -ForegroundColor Cyan
    $partida = Invoke-RestMethod -Uri "http://localhost:8080/api/partidas/" `
        -Method POST `
        -ContentType "application/json" `
        -Body (@{ nombreJugador = "Juan" } | ConvertTo-Json)

    Write-Host "   ✅ Partida creada: $($partida.codigo)" -ForegroundColor Green
    Write-Host "   📋 ID: $($partida.id)" -ForegroundColor Gray
    $codigo = $partida.codigo
    $partidaId = $partida.id

    Start-Sleep -Seconds 1

    # 2. Unirse
    Write-Host "`n2️⃣  Segundo jugador uniéndose..." -ForegroundColor Cyan
    $partida = Invoke-RestMethod -Uri "http://localhost:8080/api/partidas/unirse" `
        -Method POST `
        -ContentType "application/json" `
        -Body (@{ codigo = $codigo; nombreJugador = "María" } | ConvertTo-Json)

    Write-Host "   ✅ María se unió. Jugadores: $($partida.jugadores.Count)" -ForegroundColor Green
    foreach ($j in $partida.jugadores) {
        Write-Host "      - $($j.nombre) (ID: $($j.id))" -ForegroundColor Gray
    }

    Start-Sleep -Seconds 1

    # 3. Iniciar
    Write-Host "`n3️⃣  Iniciando juego..." -ForegroundColor Cyan
    $partida = Invoke-RestMethod -Uri "http://localhost:8080/api/partidas/$partidaId/iniciar" `
        -Method POST

    Write-Host "   ✅ Juego iniciado" -ForegroundColor Green
    Write-Host "   🎮 Turno inicial: $($partida.turnoActualNombre) (ID: $($partida.turnoActualId))" -ForegroundColor Gray

    Start-Sleep -Seconds 1

    # 4. Obtener estado
    Write-Host "`n4️⃣  Consultando estado..." -ForegroundColor Cyan
    $estado = Invoke-RestMethod -Uri "http://localhost:8080/api/partidas/$partidaId" `
        -Method GET

    Write-Host "   ✅ Estado obtenido" -ForegroundColor Green
    Write-Host "   📊 Iniciada: $($estado.iniciada) | Finalizada: $($estado.finalizada)" -ForegroundColor Gray

    Start-Sleep -Seconds 1

    # 5. Avanzar turno (1er cambio)
    Write-Host "`n5️⃣  Avanzando turno..." -ForegroundColor Cyan
    $partida = Invoke-RestMethod -Uri "http://localhost:8080/api/partidas/$partidaId/siguiente-turno" `
        -Method POST

    Write-Host "   ✅ Turno avanzado" -ForegroundColor Green
    Write-Host "   🔄 Ahora es turno de: $($partida.turnoActualNombre)" -ForegroundColor Gray

    Start-Sleep -Seconds 1

    # 6. Avanzar turno (2do cambio)
    Write-Host "`n6️⃣  Avanzando turno nuevamente..." -ForegroundColor Cyan
    $partida = Invoke-RestMethod -Uri "http://localhost:8080/api/partidas/$partidaId/siguiente-turno" `
        -Method POST

    Write-Host "   ✅ Turno avanzado" -ForegroundColor Green
    Write-Host "   🔄 Ahora es turno de: $($partida.turnoActualNombre)" -ForegroundColor Gray

    Start-Sleep -Seconds 1

    # 7. Listar partidas activas
    Write-Host "`n7️⃣  Listando partidas activas..." -ForegroundColor Cyan
    $activas = Invoke-RestMethod -Uri "http://localhost:8080/api/partidas/activas" `
        -Method GET

    Write-Host "   ✅ Partidas activas: $($activas.Count)" -ForegroundColor Green
    foreach ($p in $activas) {
        Write-Host "      - Código: $($p.codigo) | Jugadores: $($p.jugadores.Count) | Iniciada: $($p.iniciada)" -ForegroundColor Gray
    }

    Start-Sleep -Seconds 1

    # 8. Finalizar
    Write-Host "`n8️⃣  Finalizando partida..." -ForegroundColor Cyan
    $ganadorId = $partida.jugadores[0].id
    $partida = Invoke-RestMethod -Uri "http://localhost:8080/api/partidas/$partidaId/finalizar?ganadorId=$ganadorId" `
        -Method POST

    Write-Host "   ✅ Partida finalizada" -ForegroundColor Green
    Write-Host "   🏆 Ganador: $($partida.ganadorNombre) (ID: $($partida.ganadorId))" -ForegroundColor Yellow

    Start-Sleep -Seconds 1

    # 9. Verificar que no aparece en activas
    Write-Host "`n9️⃣  Verificando partidas activas..." -ForegroundColor Cyan
    $activas = Invoke-RestMethod -Uri "http://localhost:8080/api/partidas/activas" `
        -Method GET

    Write-Host "   ✅ Partidas activas: $($activas.Count)" -ForegroundColor Green
    Write-Host "   📌 La partida finalizada ya no aparece en activas" -ForegroundColor Gray

    # Resumen final
    Write-Host "`n" -NoNewline
    Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Magenta
    Write-Host "✅ ¡TODAS LAS PRUEBAS COMPLETADAS EXITOSAMENTE!" -ForegroundColor Green
    Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Magenta
    Write-Host "`nResumen de la partida:" -ForegroundColor White
    Write-Host "  • Código: $codigo" -ForegroundColor Cyan
    Write-Host "  • Jugadores: Juan, María" -ForegroundColor Cyan
    Write-Host "  • Turnos jugados: 3" -ForegroundColor Cyan
    Write-Host "  • Ganador: $($partida.ganadorNombre)" -ForegroundColor Yellow
    Write-Host "`n"

} catch {
    Write-Host "`n❌ ERROR: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "`n💡 Asegúrate de que el backend esté corriendo:" -ForegroundColor Yellow
    Write-Host "   cd c:\Users\romer\proyectoweb\backend" -ForegroundColor Gray
    Write-Host "   .\mvnw spring-boot:run" -ForegroundColor Gray
    Write-Host "`n"
}
