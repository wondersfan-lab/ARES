package com.uniajc.modelo;

/**
 * Operador con el que trabaja el negocio (Mercado Libre, Envía, Coordinadora...).
 * Corresponde a la tabla ares.transportadora.
 *
 * Decisión: tipoServicio indica qué operaciones hace el operador
 * (solo envíos, solo recepciones o ambas). Se usan constantes en vez de
 * escribir el texto a mano en cada lugar, para evitar errores de digitación.
 */
public class Transportadora {

    public static final String SERVICIO_ENVIO = "ENVIO";
    public static final String SERVICIO_RECEPCION = "RECEPCION";
    public static final String SERVICIO_AMBOS = "AMBOS";

    private int id;
    private String nombre;
    private String nit;
    private String telefonoContacto;
    private String direccion;
    private String tipoServicio = SERVICIO_AMBOS;
    private boolean activo = true;

    public Transportadora() {}

    public Transportadora(int id, String nombre, String nit, String telefonoContacto,
                          String direccion, String tipoServicio, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.nit = nit;
        this.telefonoContacto = telefonoContacto;
        this.direccion = direccion;
        this.tipoServicio = tipoServicio;
        this.activo = activo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getNit() { return nit; }
    public void setNit(String nit) { this.nit = nit; }

    public String getTelefonoContacto() { return telefonoContacto; }
    public void setTelefonoContacto(String telefonoContacto) { this.telefonoContacto = telefonoContacto; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTipoServicio() { return tipoServicio; }
    public void setTipoServicio(String tipoServicio) { this.tipoServicio = tipoServicio; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    @Override
    public String toString() {
        return "Transportadora{id=" + id + ", nombre='" + nombre + "', servicio='" + tipoServicio + "'}";
    }
}