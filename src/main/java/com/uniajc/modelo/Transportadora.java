package com.uniajc.modelo;

public class Transportadora {
    private int id;
    private String nit;
    private String razonSocial;
    private String telefono;
    private boolean estadoActivo;

    public Transportadora() {}

    public Transportadora(int id, String nit, String razonSocial, String telefono, boolean estadoActivo) {
        this.id = id;
        this.nit = nit;
        this.razonSocial = razonSocial;
        this.telefono = telefono;
        this.estadoActivo = estadoActivo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNit() { return nit; }
    public void setNit(String nit) { this.nit = nit; }

    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public boolean isEstadoActivo() { return estadoActivo; }
    public void setEstadoActivo(boolean estadoActivo) { this.estadoActivo = estadoActivo; }
}