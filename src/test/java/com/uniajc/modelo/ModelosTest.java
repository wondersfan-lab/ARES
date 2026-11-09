package com.uniajc.modelo;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ModelosTest {

    @Nested
    class PersonaTest {

        @Test
        void constructorGuardaLosDatosHeredados() {
            Persona persona = new Persona(4, "Ana Torres", "3001234567", "ana@example.com") {};

            assertAll(
                    () -> assertEquals(4, persona.getId()),
                    () -> assertEquals("Ana Torres", persona.getNombre()),
                    () -> assertEquals("3001234567", persona.getTelefono()),
                    () -> assertEquals("ana@example.com", persona.getEmail()));
        }

        @Test
        void settersActualizanLosDatosHeredados() {
            Persona persona = new Persona() {};

            persona.setId(8);
            persona.setNombre("Luis Pérez");
            persona.setTelefono("3109876543");
            persona.setEmail("luis@example.com");

            assertAll(
                    () -> assertEquals(8, persona.getId()),
                    () -> assertEquals("Luis Pérez", persona.getNombre()),
                    () -> assertEquals("3109876543", persona.getTelefono()),
                    () -> assertEquals("luis@example.com", persona.getEmail()));
        }
    }

    @Nested
    class OperacionTest {

        @Test
        void constructorCompletoGuardaTodosLosDatos() {
            LocalDate fecha = LocalDate.of(2026, 5, 12);
            LocalTime hora = LocalTime.of(14, 30, 15);
            BigDecimal peso = new BigDecimal("2.50");
            BigDecimal costo = new BigDecimal("18000.00");
            Operacion operacion = new Operacion(
                    1, 2, 3, Operacion.ENVIO, "GUIA-001", fecha, hora,
                    Operacion.REGISTRADO, "Ana", "Luis", peso, costo, "Frágil");

            assertAll(
                    () -> assertEquals(1, operacion.getId()),
                    () -> assertEquals(2, operacion.getIdTransportadora()),
                    () -> assertEquals(3, operacion.getIdUsuario()),
                    () -> assertEquals(Operacion.ENVIO, operacion.getTipo()),
                    () -> assertEquals("GUIA-001", operacion.getGuia()),
                    () -> assertEquals(fecha, operacion.getFecha()),
                    () -> assertEquals(hora, operacion.getHora()),
                    () -> assertEquals(Operacion.REGISTRADO, operacion.getEstado()),
                    () -> assertEquals("Ana", operacion.getRemitente()),
                    () -> assertEquals("Luis", operacion.getDestinatario()),
                    () -> assertEquals(peso, operacion.getPeso()),
                    () -> assertEquals(costo, operacion.getCosto()),
                    () -> assertEquals("Frágil", operacion.getObservaciones()));
        }

        @Test
        void constructorVacioInicializaFechaHoraYEstado() {
            LocalDate fechaAntesDeCrear = LocalDate.now(Operacion.ZONA);
            Operacion operacion = new Operacion();
            LocalDate fechaDespuesDeCrear = LocalDate.now(Operacion.ZONA);

            assertAll(
                    () -> assertEquals(Operacion.REGISTRADO, operacion.getEstado()),
                    () -> assertTrue(
                            operacion.getFecha().equals(fechaAntesDeCrear)
                                    || operacion.getFecha().equals(fechaDespuesDeCrear)),
                    () -> assertNotNull(operacion.getHora()),
                    () -> assertEquals(0, operacion.getHora().getNano()));
        }

        @Test
        void settersActualizanTodosLosDatos() {
            Operacion operacion = new Operacion();
            LocalDate fecha = LocalDate.of(2026, 6, 1);
            LocalTime hora = LocalTime.of(9, 45);
            BigDecimal peso = new BigDecimal("1.25");
            BigDecimal costo = new BigDecimal("9500");

            operacion.setId(10);
            operacion.setIdTransportadora(20);
            operacion.setIdUsuario(30);
            operacion.setTipo(Operacion.RECEPCION);
            operacion.setGuia("GUIA-XYZ");
            operacion.setFecha(fecha);
            operacion.setHora(hora);
            operacion.setEstado(Operacion.ENTREGADO);
            operacion.setRemitente("Carlos");
            operacion.setDestinatario("Marta");
            operacion.setPeso(peso);
            operacion.setCosto(costo);
            operacion.setObservaciones("Entregar en portería");

            assertAll(
                    () -> assertEquals(10, operacion.getId()),
                    () -> assertEquals(20, operacion.getIdTransportadora()),
                    () -> assertEquals(30, operacion.getIdUsuario()),
                    () -> assertEquals(Operacion.RECEPCION, operacion.getTipo()),
                    () -> assertEquals("GUIA-XYZ", operacion.getGuia()),
                    () -> assertEquals(fecha, operacion.getFecha()),
                    () -> assertEquals(hora, operacion.getHora()),
                    () -> assertEquals(Operacion.ENTREGADO, operacion.getEstado()),
                    () -> assertEquals("Carlos", operacion.getRemitente()),
                    () -> assertEquals("Marta", operacion.getDestinatario()),
                    () -> assertEquals(peso, operacion.getPeso()),
                    () -> assertEquals(costo, operacion.getCosto()),
                    () -> assertEquals("Entregar en portería", operacion.getObservaciones()));
        }

        @Test
        void toStringIncluyeInformacionPrincipal() {
            Operacion operacion = new Operacion();
            operacion.setId(5);
            operacion.setTipo(Operacion.ENVIO);
            operacion.setGuia("GUIA-005");
            operacion.setEstado(Operacion.CANCELADO);

            String resultado = operacion.toString();

            assertAll(
                    () -> assertTrue(resultado.contains("id=5")),
                    () -> assertTrue(resultado.contains("tipo='ENVIO'")),
                    () -> assertTrue(resultado.contains("guia='GUIA-005'")),
                    () -> assertTrue(resultado.contains("estado='CANCELADO'")));
        }
    }

    @Nested
    class TarifaTest {

        @Test
        void constructorCompletoGuardaTodosLosDatos() {
            BigDecimal pesoMin = new BigDecimal("0.50");
            BigDecimal pesoMax = new BigDecimal("5.00");
            BigDecimal valor = new BigDecimal("12000.00");
            Tarifa tarifa = new Tarifa(6, 9, pesoMin, pesoMax, valor);

            assertAll(
                    () -> assertEquals(6, tarifa.getId()),
                    () -> assertEquals(9, tarifa.getIdTransportadora()),
                    () -> assertEquals(pesoMin, tarifa.getPesoMin()),
                    () -> assertEquals(pesoMax, tarifa.getPesoMax()),
                    () -> assertEquals(valor, tarifa.getValor()));
        }

        @Test
        void settersActualizanTodosLosDatos() {
            Tarifa tarifa = new Tarifa();
            BigDecimal pesoMin = new BigDecimal("1");
            BigDecimal pesoMax = new BigDecimal("10");
            BigDecimal valor = new BigDecimal("25000");

            tarifa.setId(2);
            tarifa.setIdTransportadora(3);
            tarifa.setPesoMin(pesoMin);
            tarifa.setPesoMax(pesoMax);
            tarifa.setValor(valor);

            assertAll(
                    () -> assertEquals(2, tarifa.getId()),
                    () -> assertEquals(3, tarifa.getIdTransportadora()),
                    () -> assertEquals(pesoMin, tarifa.getPesoMin()),
                    () -> assertEquals(pesoMax, tarifa.getPesoMax()),
                    () -> assertEquals(valor, tarifa.getValor()));
        }
    }

    @Nested
    class TransportadoraTest {

        @Test
        void constructorCompletoGuardaTodosLosDatos() {
            Transportadora transportadora = new Transportadora(
                    1, "Envíos ARES", "900123456", "6011234567",
                    "Calle 10 # 20-30", Transportadora.SERVICIO_ENVIO, false);

            assertAll(
                    () -> assertEquals(1, transportadora.getId()),
                    () -> assertEquals("Envíos ARES", transportadora.getNombre()),
                    () -> assertEquals("900123456", transportadora.getNit()),
                    () -> assertEquals("6011234567", transportadora.getTelefonoContacto()),
                    () -> assertEquals("Calle 10 # 20-30", transportadora.getDireccion()),
                    () -> assertEquals(Transportadora.SERVICIO_ENVIO, transportadora.getTipoServicio()),
                    () -> assertFalse(transportadora.isActivo()));
        }

        @Test
        void constructorVacioUsaServicioAmbosYActivo() {
            Transportadora transportadora = new Transportadora();

            assertAll(
                    () -> assertEquals(Transportadora.SERVICIO_AMBOS, transportadora.getTipoServicio()),
                    () -> assertTrue(transportadora.isActivo()));
        }

        @Test
        void settersActualizanTodosLosDatos() {
            Transportadora transportadora = new Transportadora();

            transportadora.setId(4);
            transportadora.setNombre("Mensajería Central");
            transportadora.setNit("800555111");
            transportadora.setTelefonoContacto("3005551111");
            transportadora.setDireccion("Carrera 7 # 8-9");
            transportadora.setTipoServicio(Transportadora.SERVICIO_RECEPCION);
            transportadora.setActivo(false);

            assertAll(
                    () -> assertEquals(4, transportadora.getId()),
                    () -> assertEquals("Mensajería Central", transportadora.getNombre()),
                    () -> assertEquals("800555111", transportadora.getNit()),
                    () -> assertEquals("3005551111", transportadora.getTelefonoContacto()),
                    () -> assertEquals("Carrera 7 # 8-9", transportadora.getDireccion()),
                    () -> assertEquals(Transportadora.SERVICIO_RECEPCION, transportadora.getTipoServicio()),
                    () -> assertFalse(transportadora.isActivo()));
        }

        @Test
        void toStringIncluyeNombreYTipoDeServicio() {
            Transportadora transportadora = new Transportadora(
                    3, "Operador Norte", "900111222", "3001112222",
                    "Calle 1", Transportadora.SERVICIO_AMBOS, true);

            assertEquals(
                    "Transportadora{id=3, nombre='Operador Norte', servicio='AMBOS'}",
                    transportadora.toString());
        }
    }

    @Nested
    class UsuarioTest {

        @Test
        void constructorCompletoGuardaDatosPropiosYHeredados() {
            Usuario usuario = new Usuario(
                    7, "Sofía Gómez", "3004445566", "sofia@example.com",
                    "sgomez", "clave-prueba", Usuario.ADMINISTRADOR, false);

            assertAll(
                    () -> assertEquals(7, usuario.getId()),
                    () -> assertEquals("Sofía Gómez", usuario.getNombre()),
                    () -> assertEquals("3004445566", usuario.getTelefono()),
                    () -> assertEquals("sofia@example.com", usuario.getEmail()),
                    () -> assertEquals("sgomez", usuario.getLogin()),
                    () -> assertEquals("clave-prueba", usuario.getContrasena()),
                    () -> assertEquals(Usuario.ADMINISTRADOR, usuario.getRol()),
                    () -> assertFalse(usuario.isActivo()));
        }

        @Test
        void constructorVacioIniciaActivo() {
            Usuario usuario = new Usuario();

            assertTrue(usuario.isActivo());
        }

        @Test
        void settersActualizanDatosPropiosYHeredados() {
            Usuario usuario = new Usuario();

            usuario.setId(12);
            usuario.setNombre("Diego Ruiz");
            usuario.setTelefono("3112223344");
            usuario.setEmail("diego@example.com");
            usuario.setLogin("druiz");
            usuario.setContrasena("otra-clave");
            usuario.setRol(Usuario.OPERADOR);
            usuario.setActivo(false);

            assertAll(
                    () -> assertEquals(12, usuario.getId()),
                    () -> assertEquals("Diego Ruiz", usuario.getNombre()),
                    () -> assertEquals("3112223344", usuario.getTelefono()),
                    () -> assertEquals("diego@example.com", usuario.getEmail()),
                    () -> assertEquals("druiz", usuario.getLogin()),
                    () -> assertEquals("otra-clave", usuario.getContrasena()),
                    () -> assertEquals(Usuario.OPERADOR, usuario.getRol()),
                    () -> assertFalse(usuario.isActivo()));
        }

        @Test
        void toStringNoIncluyeLaContrasena() {
            Usuario usuario = new Usuario(
                    2, "Elena Díaz", "3001112233", "elena@example.com",
                    "ediaz", "clave-que-no-debe-aparecer", Usuario.OPERADOR, true);

            String resultado = usuario.toString();

            assertAll(
                    () -> assertTrue(resultado.contains("login='ediaz'")),
                    () -> assertTrue(resultado.contains("rol='OPERADOR'")),
                    () -> assertFalse(resultado.contains("clave-que-no-debe-aparecer")));
        }
    }
}
