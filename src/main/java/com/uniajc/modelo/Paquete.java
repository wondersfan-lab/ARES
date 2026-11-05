package com.uniajc.modelo;


public class Paquete {
    private int id;
    private int idOperacion;
    private String nombreProducto;
    private String descripcion;
    private int cantidad = 1;

    public Paquete() {}

    public Paquete(int id, int idOperacion, String nombreProducto, String descripcion, int cantidad) {
        this.id = id;
        this.idOperacion = idOperacion;
        this.nombreProducto = nombreProducto;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdOperacion() { return idOperacion; }
    public void setIdOperacion(int idOperacion) { this.idOperacion = idOperacion; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}