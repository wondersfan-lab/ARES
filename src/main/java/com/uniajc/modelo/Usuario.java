package com.uniajc.modelo;

/**
 * Usuario del sistema. Corresponde a la tabla ares.usuario.
 *
 * Hereda de Persona: id (id_usuario), nombre, telefono y email.
 *
 * Decisiones:
 *  - El nombre de acceso se llama "login" en Java porque en la tabla la columna
 *    se llama "usuario" y una clase Usuario con un atributo "usuario" confunde.
 *  - El rol se guarda como String con dos valores válidos (constantes abajo).
 *    La base de datos los valida con un CHECK, así que no hace falta repetir
 *    la validación aquí.
 *  - "activo" permite desactivar a un usuario sin borrarlo (así se conserva
 *    el historial de operaciones que registró).
 */
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

    // La contraseña NO se incluye en toString para no mostrarla por accidente.
    @Override
    public String toString() {
        return "Usuario{id=" + getId() + ", nombre='" + getNombre()
                + "', login='" + login + "', rol='" + rol + "', activo=" + activo + "}";
    }
}
