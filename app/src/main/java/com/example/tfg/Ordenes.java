package com.example.tfg;

public class Ordenes {

    private String nombre, estado, fecha, hora;

    public Ordenes(){}

    public Ordenes(String nombre, String estado, String fecha, String hora) {
        this.nombre = nombre;
        this.estado = estado;
        this.fecha = fecha;
        this.hora = hora;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
