package com.uniajc.modelo;

public class Paquete {
    private int id;
    private int envioId;
    private double pesoKg;
    private double volumen;
    private String descripcion;

    public Paquete() {}

    public Paquete(int id, int envioId, double pesoKg, double volumen, String descripcion) {
        this.id = id;
        this.envioId = envioId;
        this.pesoKg = pesoKg;
        this.volumen = volumen;
        this.descripcion = descripcion;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getEnvioId() { return envioId; }
    public void setEnvioId(int envioId) { this.envioId = envioId; }

    public double getPesoKg() { return pesoKg; }
    public void setPesoKg(double pesoKg) { this.pesoKg = pesoKg; }

    public double getVolumen() { return volumen; }
    public void setVolumen(double volumen) { this.volumen = volumen; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}