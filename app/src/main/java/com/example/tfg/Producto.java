package com.example.tfg;

public class Producto {
    private String nombre;
    private double precio;
    private String foto;

    private String categoria;

    private String pid, fecha, hora;



    public Producto(String nombre, double precio, String foto, String categoria, String pid, String fecha, String hora) {
        this.nombre = nombre;
        this.precio = precio;
        this.foto = foto;
        this.categoria = categoria;
        this.pid=pid;
        this.fecha=fecha;
        this.hora=hora;

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
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

