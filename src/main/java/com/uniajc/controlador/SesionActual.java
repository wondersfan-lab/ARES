package com.uniajc.controlador;

import com.uniajc.modelo.Usuario;

public class SesionActual {
    private static SesionActual instancia;
    private Usuario usuarioLogueado;

    private SesionActual() {}

    public static SesionActual getInstancia() {
        if (instancia == null) {
            instancia = new SesionActual();
        }
        return instancia;
    }

    public void setUsuarioLogueado(Usuario usuario) {
        this.usuarioLogueado = usuario;
    }

    public Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public void cerrarSesion() {
        this.usuarioLogueado = null;
    }
}