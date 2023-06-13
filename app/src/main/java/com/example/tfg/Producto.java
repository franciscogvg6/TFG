package com.example.tfg;

public class Producto {
    private String productoId;
    private String nombre;
    private String precio;
    private String foto;

    private String categoria;



    public Producto(String productoId, String nombre, String precio, String foto, String categoria) {
        this.productoId = productoId;
        this.nombre = nombre;
        this.precio = precio;
        this.foto = foto;
        this.categoria = categoria;

    }

    public String getProductoId() {
        return productoId;
    }

    public void setProductoId(String productoId) {
        this.productoId = productoId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
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
}

