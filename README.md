Proyecto Integrador ARES - Sistema de Gestión Logística

1. Introducción

ARES es un software de gestión logística desarrollado en Java 21 con Maven y conectado a Neon PostgreSQL. El sistema implementa una arquitectura modular por capas (MVC + DAO + Servicios), aplicando Programación Orientada a Objetos (POO) y control de versiones con Git.

2. Participación de Emmanuel Ortega (Capa Modelo)

Me encargué de desarrollar la Capa Modelo en la rama feature/capa-modelo, creando las clases POJO que representan las entidades del negocio alineadas con la base de datos:

Herencia y Encapsulamiento: Creación de la clase abstracta Persona y su extensión Usuario (con roles de ADMINISTRADOR y OPERADOR), manteniendo atributos privados con sus respectivos getters, setters y constructores.

Entidades del Sistema: Implementación de Transportadora, Tarifa, Envio, Paquete y Recepcion para gestionar toda la información en memoria del sistema logístico.