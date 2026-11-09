package com.uniajc.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PaqueteTest {

    @Test
    void constructorCompletoGuardaLosDatosRecibidos() {
        Paquete paquete = new Paquete(
                7, 12, "Teclado", "Teclado inalámbrico", 3);

        assertEquals(7, paquete.getId());
        assertEquals(12, paquete.getIdOperacion());
        assertEquals("Teclado", paquete.getNombreProducto());
        assertEquals("Teclado inalámbrico", paquete.getDescripcion());
        assertEquals(3, paquete.getCantidad());
    }

    @Test
    void constructorVacioIniciaLaCantidadEnUno() {
        Paquete paquete = new Paquete();

        assertEquals(1, paquete.getCantidad());
    }

    @Test
    void settersPermitenActualizarLosDatos() {
        Paquete paquete = new Paquete();

        paquete.setId(5);
        paquete.setIdOperacion(9);
        paquete.setNombreProducto("Libro");
        paquete.setDescripcion("Libro de estudio");
        paquete.setCantidad(2);

        assertEquals(5, paquete.getId());
        assertEquals(9, paquete.getIdOperacion());
        assertEquals("Libro", paquete.getNombreProducto());
        assertEquals("Libro de estudio", paquete.getDescripcion());
        assertEquals(2, paquete.getCantidad());
    }
}
