package com.example.tfg;

import java.util.HashMap;

public class Ordenes {

    private String id, nombre, estado, mesa;
    private HashMap<String, ProductoOrden> productos;

    public Ordenes(){}

    public Ordenes(String nombre, String estado, HashMap<String, ProductoOrden> productos, String mesa) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estado;
        this.productos = productos;
        this.mesa = mesa;
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

    public String getMesa() {
        return mesa;
    }

    public void setMesa(String mesa) {
        this.mesa = mesa;
    }


    public HashMap<String, ProductoOrden> getProductos() {
        return productos;
    }

    public void setProductos(HashMap<String, ProductoOrden> productos) {
        this.productos = productos;
    }
}
