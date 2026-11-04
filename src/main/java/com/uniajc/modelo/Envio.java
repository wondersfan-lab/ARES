package com.uniajc.modelo;

import java.time.LocalDateTime;

public class Envio {
    private int id;
    private String codigoSeguimiento;
    private int usuarioId;
    private int transportadoraId;
    private double costoTotal;
    private String estado;
    private String destinatario;
    private LocalDateTime createdAt;

    public Envio() {}

    public Envio(int id, String codigoSeguimiento, int usuarioId, int transportadoraId, double costoTotal, String estado, String destinatario, LocalDateTime createdAt) {
        this.id = id;
        this.codigoSeguimiento = codigoSeguimiento;
        this.usuarioId = usuarioId;
        this.transportadoraId = transportadoraId;
        this.costoTotal = costoTotal;
        this.estado = estado;
        this.destinatario = destinatario;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCodigoSeguimiento() { return codigoSeguimiento; }
    public void setCodigoSeguimiento(String codigoSeguimiento) { this.codigoSeguimiento = codigoSeguimiento; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public int getTransportadoraId() { return transportadoraId; }
    public void setTransportadoraId(int transportadoraId) { this.transportadoraId = transportadoraId; }

    public double getCostoTotal() { return costoTotal; }
    public void setCostoTotal(double costoTotal) { this.costoTotal = costoTotal; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getDestinatario() { return destinatario; }
    public void setDestinatario(String destinatario) { this.destinatario = destinatario; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}