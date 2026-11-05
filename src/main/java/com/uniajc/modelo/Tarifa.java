package com.uniajc.modelo;

import java.math.BigDecimal;


public class Tarifa {
    private int id;
    private int idTransportadora;
    private BigDecimal pesoMin;
    private BigDecimal pesoMax;
    private BigDecimal valor;

    public Tarifa() {}

    public Tarifa(int id, int idTransportadora, BigDecimal pesoMin, BigDecimal pesoMax, BigDecimal valor) {
        this.id = id;
        this.idTransportadora = idTransportadora;
        this.pesoMin = pesoMin;
        this.pesoMax = pesoMax;
        this.valor = valor;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdTransportadora() { return idTransportadora; }
    public void setIdTransportadora(int idTransportadora) { this.idTransportadora = idTransportadora; }

    public BigDecimal getPesoMin() { return pesoMin; }
    public void setPesoMin(BigDecimal pesoMin) { this.pesoMin = pesoMin; }

    public BigDecimal getPesoMax() { return pesoMax; }
    public void setPesoMax(BigDecimal pesoMax) { this.pesoMax = pesoMax; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
}
