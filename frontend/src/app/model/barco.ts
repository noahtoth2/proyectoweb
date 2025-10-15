export class Barco {
    public id?: number;
    public velocidadX: number = 0; // ✅ Requerido con valor por defecto
    public velocidadY: number = 0; // ✅ Requerido con valor por defecto
    public posicionId?: number;
    public modeloId?: number;
    public jugadorId?: number;
    public tableroId?: number;
    
    // Para compatibilidad con backend que usa 'velocidad'
    public velocidad?: number;
}
