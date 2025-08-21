package com.edu.proyecto.demo.dto;

public class TableroDTO{
        private Long id;
        private Barco barco;
        private int[][] tablero;


    public TableroDTO(){

    }
    public TableroDTO(Barco barco,int filas, int columnas){
        this.barco=barco;
        tthis.tablero = new int[filas][columnas];
    }

    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id=id
    }
    public int[][] getTablero(){
        return tablero;
    }
    public void setTablero(int[][] tablero){
        this.tablero=tablero;
    }
   
    
}