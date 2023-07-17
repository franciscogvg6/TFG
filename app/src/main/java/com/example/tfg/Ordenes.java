package com.example.tfg;

import java.util.HashMap;

public class Ordenes {

    private String id, nombre, estado, fecha, hora;
    private HashMap<String, ProductoOrden> productos;

    public Ordenes(){}

    public Ordenes(String nombre, String estado, String fecha, String hora, HashMap<String, ProductoOrden> productos) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estado;
        this.fecha = fecha;
        this.hora = hora;
        this.productos = productos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public HashMap<String, ProductoOrden> getProductos() {
        return productos;
    }

    public void setProductos(HashMap<String, ProductoOrden> productos) {
        this.productos = productos;
    }
}
