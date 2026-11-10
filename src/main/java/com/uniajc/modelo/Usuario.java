package com.uniajc.modelo;


public class Usuario extends Persona {

    public static final String ADMINISTRADOR = "ADMINISTRADOR";
    public static final String OPERADOR = "OPERADOR";

    private String login;
    private String contrasena;
    private String rol;
    private boolean activo = true;

    public Usuario() {
        super();
    }

    public Usuario(int id, String nombre, String telefono, String email,
                   String login, String contrasena, String rol, boolean activo) {
        super(id, nombre, telefono, email);
        this.login = login;
        this.contrasena = contrasena;
        this.rol = rol;
        this.activo = activo;
    }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    
    @Override
    public String toString() {
        return "Usuario{id=" + getId() + ", nombre='" + getNombre()
                + "', login='" + login + "', rol='" + rol + "', activo=" + activo + "}";
    }
}
