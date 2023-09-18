package com.example.tfg;

public class Producto {
    private String Nombre;
    private double Precio, Cantidad;
    private String Foto;

    private String Categoria;

    private String pid;

    public Producto(){}

    public Producto(String Nombre, double Precio, String Foto, String Categoria, String pid, double Cantidad) {
        this.Nombre = Nombre;
        this.Precio = Precio;
        this.Foto = Foto;
        this.Categoria = Categoria;
        this.pid = pid;

        this.Cantidad = Cantidad;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public double getPrecio() {
        return Precio;
    }

    public void setPrecio(double precio) {
        Precio = precio;
    }

    public String getFoto() {
        return Foto;
    }

    public void setFoto(String foto) {
        Foto = foto;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public double getCantidad() {
        return Cantidad;
    }

    public void setCantidad(double cantidad) {
        Cantidad = cantidad;
    }
}

