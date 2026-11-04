package com.uniajc.modelo;

public class Usuario extends Persona {
    private String password;
    private String rol; // 'ADMINISTRADOR' o 'OPERADOR'

    public Usuario() {
        super();
    }

    public Usuario(int id, String nombre, String telefono, String email, String password, String rol) {
        super(id, nombre, telefono, email);
        this.password = password;
        this.rol = rol;
    }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    @Override
    public String toString() {
        return "Usuario{id=" + getId() + ", nombre='" + getNombre() + "', rol='" + rol + "'}";
    }
}