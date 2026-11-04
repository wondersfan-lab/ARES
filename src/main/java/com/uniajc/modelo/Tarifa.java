package com.uniajc.modelo;

public class Tarifa {
    private int id;
    private int transportadoraId;
    private double costoBase;
    private double valorKiloAdicional;

    public Tarifa() {}

    public Tarifa(int id, int transportadoraId, double costoBase, double valorKiloAdicional) {
        this.id = id;
        this.transportadoraId = transportadoraId;
        this.costoBase = costoBase;
        this.valorKiloAdicional = valorKiloAdicional;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getTransportadoraId() { return transportadoraId; }
    public void setTransportadoraId(int transportadoraId) { this.transportadoraId = transportadoraId; }

    public double getCostoBase() { return costoBase; }
    public void setCostoBase(double costoBase) { this.costoBase = costoBase; }

    public double getValorKiloAdicional() { return valorKiloAdicional; }
    public void setValorKiloAdicional(double valorKiloAdicional) { this.valorKiloAdicional = valorKiloAdicional; }
}