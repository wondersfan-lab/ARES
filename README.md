# Proyecto Integrador ARES - Sistema de Gestión Logística

## 1. Introducción
ARES es un software de gestión logística desarrollado en Java 21 con Maven y conectado a Neon PostgreSQL. El sistema implementa una arquitectura modular por capas (MVC + DAO + Servicios), aplicando Programación Orientada a Objetos (POO) y control de versiones con Git. La interfaz gráfica se trabaja con JavaFX y Scene Builder.

## 2. Participación y Roles del Equipo

1. Emmanuel Ortega (Capa Modelo y Base de Datos)
Me encargué de estructurar la base de datos en Neon PostgreSQL y desarrollar la Capa Modelo en Java. La decisión principal fue unificar envíos y recepciones en una sola clase llamada Operacion, además de aplicar herencia creando la clase abstracta Persona y su extensión Usuario. Todo con sus constructores y encapsulamiento.

2. Adrián Andrade (Capa DAO)
Se encarga de la persistencia de los datos. Desarrolla las clases de Acceso a Datos (DAO) usando JDBC y sentencias preparadas (PreparedStatement) para conectar de forma segura nuestros objetos Java con la base de datos.

3. Esteban Muñoz (Capa Visual y Controladores)
Se encarga del frontend del sistema. Su trabajo es diseñar la interfaz de usuario en JavaFX con Scene Builder, aplicar los estilos CSS y programar los controladores que conectan las ventanas con los métodos de los DAOs.

4. Jhonatan Morales (Pruebas y Testing)
Se encarga de validar que todo funcione. Ejecuta las pruebas funcionales de la interfaz, prueba la conexión a la base de datos y revisa que los datos se guarden correctamente sin errores.