package com.example.tfg;

public class Producto {
    private String Nombre;
    private double Precio;
    private String Foto;

    private String Categoria;

    private String pid, fecha, hora;

    public Producto(){}

    public Producto(String Nombre, double Precio, String Foto, String Categoria, String pid, String fecha, String hora) {
        this.Nombre = Nombre;
        this.Precio = Precio;
        this.Foto = Foto;
        this.Categoria = Categoria;
        this.pid = pid;
        this.fecha = fecha;
        this.hora = hora;
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

