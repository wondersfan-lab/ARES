package com.uniajc.modelo;

import java.time.LocalDateTime;

public class Recepcion {
    private int id;
    private int envioId;
    private String personaRecibe;
    private LocalDateTime fechaRecepcion;
    private String observaciones;

    public Recepcion() {}

    public Recepcion(int id, int envioId, String personaRecibe, LocalDateTime fechaRecepcion, String observaciones) {
        this.id = id;
        this.envioId = envioId;
        this.personaRecibe = personaRecibe;
        this.fechaRecepcion = fechaRecepcion;
        this.observaciones = observaciones;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getEnvioId() { return envioId; }
    public void setEnvioId(int envioId) { this.envioId = envioId; }

    public String getPersonaRecibe() { return personaRecibe; }
    public void setPersonaRecibe(String personaRecibe) { this.personaRecibe = personaRecibe; }

    public LocalDateTime getFechaRecepcion() { return fechaRecepcion; }
    public void setFechaRecepcion(LocalDateTime fechaRecepcion) { this.fechaRecepcion = fechaRecepcion; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}