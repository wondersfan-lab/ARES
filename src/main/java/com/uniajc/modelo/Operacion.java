package com.uniajc.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;


public class Operacion {

    public static final String ENVIO = "ENVIO";
    public static final String RECEPCION = "RECEPCION";

    public static final String REGISTRADO = "REGISTRADO";
    public static final String ENTREGADO = "ENTREGADO";
    public static final String CANCELADO = "CANCELADO";

    
    public static final ZoneId ZONA = ZoneId.of("America/Bogota");

    private int id;
    private int idTransportadora;
    private int idUsuario;
    private String tipo;
    private String guia;
    private LocalDate fecha = LocalDate.now(ZONA);
    private LocalTime hora = LocalTime.now(ZONA).withNano(0);
    private String estado = REGISTRADO;
    private String remitente;
    private String destinatario;
    private BigDecimal peso;
    private BigDecimal costo;
    private String observaciones;

    public Operacion() {}

    public Operacion(int id, int idTransportadora, int idUsuario, String tipo, String guia,
                     LocalDate fecha, LocalTime hora, String estado, String remitente,
                     String destinatario, BigDecimal peso, BigDecimal costo, String observaciones) {
        this.id = id;
        this.idTransportadora = idTransportadora;
        this.idUsuario = idUsuario;
        this.tipo = tipo;
        this.guia = guia;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
        this.remitente = remitente;
        this.destinatario = destinatario;
        this.peso = peso;
        this.costo = costo;
        this.observaciones = observaciones;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdTransportadora() { return idTransportadora; }
    public void setIdTransportadora(int idTransportadora) { this.idTransportadora = idTransportadora; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getGuia() { return guia; }
    public void setGuia(String guia) { this.guia = guia; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) { this.hora = hora; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getRemitente() { return remitente; }
    public void setRemitente(String remitente) { this.remitente = remitente; }

    public String getDestinatario() { return destinatario; }
    public void setDestinatario(String destinatario) { this.destinatario = destinatario; }

    public BigDecimal getPeso() { return peso; }
    public void setPeso(BigDecimal peso) { this.peso = peso; }

    public BigDecimal getCosto() { return costo; }
    public void setCosto(BigDecimal costo) { this.costo = costo; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    @Override
    public String toString() {
        return "Operacion{id=" + id + ", tipo='" + tipo + "', guia='" + guia + "', fecha=" + fecha
                + ", hora=" + hora + ", estado='" + estado + "'}";
    }
}
