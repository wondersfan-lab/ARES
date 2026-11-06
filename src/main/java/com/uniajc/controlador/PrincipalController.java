package com.uniajc.controlador;

import com.uniajc.dao.OperacionDAO;
import com.uniajc.dao.PaqueteDAO;
import com.uniajc.dao.TarifaDAO;
import com.uniajc.dao.TransportadoraDAO;
import com.uniajc.dao.UsuarioDAO;
import com.uniajc.modelo.Operacion;
import com.uniajc.modelo.Paquete;
import com.uniajc.modelo.Tarifa;
import com.uniajc.modelo.Transportadora;
import com.uniajc.modelo.Usuario;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class PrincipalController {

    // --- VARIABLES DE VENTANA (Arrastre) ---
    @FXML private HBox barraTitulo;
    private double xOffset = 0;
    private double yOffset = 0;

    // --- PERFIL DE USUARIO ---
    @FXML private Label lblInicialesUsuario;
    @FXML private Label lblNombreUsuario;
    @FXML private Label lblRolUsuario;
    @FXML private VBox boxAvatar;
    @FXML private Label lblEtiquetaConfiguracion;
    @FXML private Label lblBadgeEnvios;

    // --- NAVEGACIÓN ---
    @FXML private StackPane stackPantallas;
    @FXML private VBox vistaPrincipal;
    @FXML private VBox vistaEnvios;
    @FXML private VBox vistaPaquetes;
    @FXML private VBox vistaRecepcion;
    @FXML private VBox vistaTarifas;
    @FXML private VBox vistaUsuarios;

    @FXML private Button btnNavPrincipal;
    @FXML private Button btnNavEnvios;
    @FXML private Button btnNavPaquetes;
    @FXML private Button btnNavRecepcion;
    @FXML private Button btnNavTarifas;
    @FXML private Button btnNavUsuarios;

    // --- 1. PANEL PRINCIPAL ---
    @FXML private Label lblTotalEnviosHoy;
    @FXML private Label lblTotalPaquetes;
    @FXML private Label lblTotalTransportadoras;
    @FXML private Label lblIngresosTotales;
    @FXML private TableView<Operacion> tblOperaciones;
    @FXML private TableColumn<Operacion, Integer> colId;
    @FXML private TableColumn<Operacion, String> colCodigoSeguimiento;
    @FXML private TableColumn<Operacion, String> colTipoOperacion;
    @FXML private TableColumn<Operacion, String> colRemitente;
    @FXML private TableColumn<Operacion, String> colDestinatario;
    @FXML private TableColumn<Operacion, String> colEstado;
    @FXML private TableColumn<Operacion, BigDecimal> colCostoTotal;

    // --- 2. GESTIÓN DE ENVÍOS ---
    @FXML private ComboBox<Transportadora> cmbEnvioTransportadora;
    @FXML private TextField txtEnvioGuia;
    @FXML private TextField txtEnvioRemitente;
    @FXML private TextField txtEnvioDestinatario;
    @FXML private TextField txtEnvioPeso;
    @FXML private TextField txtEnvioCosto;
    @FXML private TextField txtEnvioObservaciones;
    @FXML private TableView<Operacion> tblEnvios;
    @FXML private TableColumn<Operacion, Integer> colEnvId;
    @FXML private TableColumn<Operacion, String> colEnvGuia;
    @FXML private TableColumn<Operacion, String> colEnvRemitente;
    @FXML private TableColumn<Operacion, String> colEnvDestinatario;
    @FXML private TableColumn<Operacion, BigDecimal> colEnvPeso;
    @FXML private TableColumn<Operacion, BigDecimal> colEnvCosto;
    @FXML private TableColumn<Operacion, String> colEnvEstado;

    // --- 3. PAQUETES Y GUÍAS ---
    @FXML private ComboBox<Operacion> cmbPaqueteOperacion;
    @FXML private TextField txtPaqueteProducto;
    @FXML private TextField txtPaqueteCantidad;
    @FXML private TextField txtPaqueteDescripcion;
    @FXML private TableView<Paquete> tblPaquetes;
    @FXML private TableColumn<Paquete, Integer> colPaqId;
    @FXML private TableColumn<Paquete, Integer> colPaqIdOperacion;
    @FXML private TableColumn<Paquete, String> colPaqProducto;
    @FXML private TableColumn<Paquete, String> colPaqDescripcion;
    @FXML private TableColumn<Paquete, Integer> colPaqCantidad;

    // --- 4. RECEPCIÓN Y ENTREGA ---
    @FXML private ComboBox<Transportadora> cmbRecepcionTransportadora;
    @FXML private TextField txtRecepcionGuia;
    @FXML private TextField txtRecepcionRemitente;
    @FXML private TextField txtRecepcionDestinatario;
    @FXML private TextField txtRecepcionObservaciones;
    @FXML private TableView<Operacion> tblRecepciones;
    @FXML private TableColumn<Operacion, Integer> colRecId;
    @FXML private TableColumn<Operacion, String> colRecGuia;
    @FXML private TableColumn<Operacion, String> colRecRemitente;
    @FXML private TableColumn<Operacion, String> colRecDestinatario;
    @FXML private TableColumn<Operacion, String> colRecObservaciones;
    @FXML private TableColumn<Operacion, String> colRecEstado;

    // --- 5. TRANSPORTADORAS Y TARIFAS ---
    @FXML private TextField txtTransNombre;
    @FXML private TextField txtTransNit;
    @FXML private TextField txtTransTelefono;
    @FXML private TextField txtTransDireccion;
    @FXML private ComboBox<String> cmbTransServicio;
    @FXML private TableView<Transportadora> tblTransportadoras;
    @FXML private TableColumn<Transportadora, Integer> colTransId;
    @FXML private TableColumn<Transportadora, String> colTransNombre;
    @FXML private TableColumn<Transportadora, String> colTransNit;
    @FXML private TableColumn<Transportadora, String> colTransTelefono;
    @FXML private TableColumn<Transportadora, String> colTransDireccion;
    @FXML private TableColumn<Transportadora, String> colTransServicio;

    @FXML private ComboBox<Transportadora> cmbTarifaTransportadora;
    @FXML private TextField txtTarifaPesoMin;
    @FXML private TextField txtTarifaPesoMax;
    @FXML private TextField txtTarifaValor;
    @FXML private TableView<Tarifa> tblTarifas;
    @FXML private TableColumn<Tarifa, Integer> colTarId;
    @FXML private TableColumn<Tarifa, Integer> colTarIdTransportadora;
    @FXML private TableColumn<Tarifa, BigDecimal> colTarPesoMin;
    @FXML private TableColumn<Tarifa, BigDecimal> colTarPesoMax;
    @FXML private TableColumn<Tarifa, BigDecimal> colTarValor;

    // --- 6. GESTIÓN DE USUARIOS ---
    @FXML private TextField txtUsuNombre;
    @FXML private TextField txtUsuLogin;
    @FXML private PasswordField txtUsuContrasena;
    @FXML private TextField txtUsuTelefono;
    @FXML private TextField txtUsuEmail;
    @FXML private ComboBox<String> cmbUsuRol;
    @FXML private TableView<Usuario> tblUsuarios;
    @FXML private TableColumn<Usuario, Integer> colUsuId;
    @FXML private TableColumn<Usuario, String> colUsuNombre;
    @FXML private TableColumn<Usuario, String> colUsuLogin;
    @FXML private TableColumn<Usuario, String> colUsuTelefono;
    @FXML private TableColumn<Usuario, String> colUsuEmail;
    @FXML private TableColumn<Usuario, String> colUsuRol;
    @FXML private TableColumn<Usuario, Boolean> colUsuActivo;

    // --- OBJETOS DAO ---
    private final OperacionDAO operacionDAO = new OperacionDAO();
    private final PaqueteDAO paqueteDAO = new PaqueteDAO();
    private final TransportadoraDAO transportadoraDAO = new TransportadoraDAO();
    private final TarifaDAO tarifaDAO = new TarifaDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    // --- LISTAS OBSERVABLES ---
    private final ObservableList<Operacion> listaOperaciones = FXCollections.observableArrayList();
    private final ObservableList<Operacion> listaEnvios = FXCollections.observableArrayList();
    private final ObservableList<Operacion> listaRecepciones = FXCollections.observableArrayList();
    private final ObservableList<Paquete> listaPaquetes = FXCollections.observableArrayList();
    private final ObservableList<Transportadora> listaTransportadoras = FXCollections.observableArrayList();
    private final ObservableList<Tarifa> listaTarifas = FXCollections.observableArrayList();
    private final ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configurarArrastreVentana();
        configurarPermisosRol();
        configurarCombos();
        configurarColumnasTablas();
        cargarDatosDesdeDAO();
    }

    private void configurarPermisosRol() {
        Usuario user = SesionActual.getInstancia().getUsuarioLogueado();
        if (user != null) {
            lblNombreUsuario.setText(user.getNombre());
            String rolText = user.getRol();
            lblRolUsuario.setText(rolText);

            if (Usuario.ADMINISTRADOR.equals(rolText)) {
                lblInicialesUsuario.setText("AD");
                boxAvatar.setStyle("-fx-background-color: #1DB954; -fx-background-radius: 500; -fx-min-width: 35; -fx-min-height: 35;");
                lblRolUsuario.setStyle("-fx-text-fill: #1ED760; -fx-background-color: #0F2E1B; -fx-padding: 2 6; -fx-background-radius: 4; -fx-font-size: 10px;");
            } else {
                lblInicialesUsuario.setText("OP");
                boxAvatar.setStyle("-fx-background-color: #F59B23; -fx-background-radius: 500; -fx-min-width: 35; -fx-min-height: 35;");
                lblRolUsuario.setStyle("-fx-text-fill: #F59B23; -fx-background-color: #33240A; -fx-padding: 2 6; -fx-background-radius: 4; -fx-font-size: 10px;");

                lblEtiquetaConfiguracion.setVisible(false);
                lblEtiquetaConfiguracion.setManaged(false);
                btnNavTarifas.setVisible(false);
                btnNavTarifas.setManaged(false);
                btnNavUsuarios.setVisible(false);
                btnNavUsuarios.setManaged(false);
            }
        }
    }

    private void configurarArrastreVentana() {
        if (barraTitulo != null) {
            barraTitulo.setOnMousePressed(event -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });
            barraTitulo.setOnMouseDragged(event -> {
                Stage stage = (Stage) barraTitulo.getScene().getWindow();
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            });
        }
    }

    private void configurarCombos() {
        cmbTransServicio.getItems().addAll(
                Transportadora.SERVICIO_ENVIO,
                Transportadora.SERVICIO_RECEPCION,
                Transportadora.SERVICIO_AMBOS
        );
        cmbTransServicio.setValue(Transportadora.SERVICIO_AMBOS);

        cmbUsuRol.getItems().addAll(Usuario.ADMINISTRADOR, Usuario.OPERADOR);
        cmbUsuRol.setValue(Usuario.OPERADOR);
    }

    private void configurarColumnasTablas() {
        // 1. Tabla Principal
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCodigoSeguimiento.setCellValueFactory(new PropertyValueFactory<>("guia"));
        colTipoOperacion.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colRemitente.setCellValueFactory(new PropertyValueFactory<>("remitente"));
        colDestinatario.setCellValueFactory(new PropertyValueFactory<>("destinatario"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colCostoTotal.setCellValueFactory(new PropertyValueFactory<>("costo"));
        tblOperaciones.setItems(listaOperaciones);

        // 2. Tabla Envíos
        colEnvId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colEnvGuia.setCellValueFactory(new PropertyValueFactory<>("guia"));
        colEnvRemitente.setCellValueFactory(new PropertyValueFactory<>("remitente"));
        colEnvDestinatario.setCellValueFactory(new PropertyValueFactory<>("destinatario"));
        colEnvPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));
        colEnvCosto.setCellValueFactory(new PropertyValueFactory<>("costo"));
        colEnvEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        tblEnvios.setItems(listaEnvios);

        // 3. Tabla Paquetes
        colPaqId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPaqIdOperacion.setCellValueFactory(new PropertyValueFactory<>("idOperacion"));
        colPaqProducto.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
        colPaqDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colPaqCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        tblPaquetes.setItems(listaPaquetes);

        // 4. Tabla Recepciones
        colRecId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colRecGuia.setCellValueFactory(new PropertyValueFactory<>("guia"));
        colRecRemitente.setCellValueFactory(new PropertyValueFactory<>("remitente"));
        colRecDestinatario.setCellValueFactory(new PropertyValueFactory<>("destinatario"));
        colRecObservaciones.setCellValueFactory(new PropertyValueFactory<>("observaciones"));
        colRecEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        tblRecepciones.setItems(listaRecepciones);

        // 5. Tablas Transportadoras y Tarifas
        colTransId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTransNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colTransNit.setCellValueFactory(new PropertyValueFactory<>("nit"));
        colTransTelefono.setCellValueFactory(new PropertyValueFactory<>("telefonoContacto"));
        colTransDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colTransServicio.setCellValueFactory(new PropertyValueFactory<>("tipoServicio"));
        tblTransportadoras.setItems(listaTransportadoras);

        colTarId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTarIdTransportadora.setCellValueFactory(new PropertyValueFactory<>("idTransportadora"));
        colTarPesoMin.setCellValueFactory(new PropertyValueFactory<>("pesoMin"));
        colTarPesoMax.setCellValueFactory(new PropertyValueFactory<>("pesoMax"));
        colTarValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        tblTarifas.setItems(listaTarifas);

        // 6. Tabla Usuarios
        colUsuId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUsuNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colUsuLogin.setCellValueFactory(new PropertyValueFactory<>("login"));
        colUsuTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colUsuEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colUsuRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        colUsuActivo.setCellValueFactory(new PropertyValueFactory<>("activo"));
        tblUsuarios.setItems(listaUsuarios);
    }

    // --- CARGA DE DATOS DESDE LOS DAO ---
    @FXML
    public void cargarDatosDesdeDAO() {
        try {
            // Transportadoras
            List<Transportadora> transportadorasBD = transportadoraDAO.listarTodos();
            listaTransportadoras.setAll(transportadorasBD);
            cmbEnvioTransportadora.setItems(listaTransportadoras);
            cmbRecepcionTransportadora.setItems(listaTransportadoras);
            cmbTarifaTransportadora.setItems(listaTransportadoras);

            // Tarifas y Usuarios
            listaTarifas.setAll(tarifaDAO.listarTodos());
            listaUsuarios.setAll(usuarioDAO.listarTodos());

            // Paquetes
            List<Paquete> paquetesBD = paqueteDAO.listarTodos();
            listaPaquetes.setAll(paquetesBD);

            // Operaciones (Envíos y Recepciones)
            List<Operacion> operacionesBD = operacionDAO.listarTodos();
            listaOperaciones.setAll(operacionesBD);
            cmbPaqueteOperacion.setItems(listaOperaciones);

            listaEnvios.clear();
            listaRecepciones.clear();
            double ingresos = 0;

            for (Operacion op : operacionesBD) {
                if (Operacion.ENVIO.equals(op.getTipo())) {
                    listaEnvios.add(op);
                } else {
                    listaRecepciones.add(op);
                }
                if (op.getCosto() != null) {
                    ingresos += op.getCosto().doubleValue();
                }
            }

            // Actualizar KPIs
            lblTotalEnviosHoy.setText(String.valueOf(operacionesBD.size()));
            lblTotalPaquetes.setText(String.valueOf(paquetesBD.size()));
            lblTotalTransportadoras.setText(transportadorasBD.size() + " Activas");
            lblBadgeEnvios.setText(String.valueOf(listaEnvios.size()));

            NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(Locale.of("es", "CO"));
            formatoMoneda.setMaximumFractionDigits(0);
            lblIngresosTotales.setText(formatoMoneda.format(ingresos));

        } catch (Exception e) {
            System.out.println("Error consultando la BD: " + e.getMessage());
        }
    }

    // --- NAVEGACIÓN MENÚ LATERAL ---
    private void cambiarVista(VBox vistaActiva, Button botonActivo) {
        VBox[] vistas = {vistaPrincipal, vistaEnvios, vistaPaquetes, vistaRecepcion, vistaTarifas, vistaUsuarios};
        for (VBox v : vistas) {
            boolean mostrar = (v == vistaActiva);
            v.setVisible(mostrar);
            v.setManaged(mostrar);
        }

        Button[] botones = {btnNavPrincipal, btnNavEnvios, btnNavPaquetes, btnNavRecepcion, btnNavTarifas, btnNavUsuarios};
        for (Button b : botones) {
            b.getStyleClass().remove("nav-btn-active");
        }
        botonActivo.getStyleClass().add("nav-btn-active");
    }

    @FXML private void mostrarVistaPrincipal() { cambiarVista(vistaPrincipal, btnNavPrincipal); }
    @FXML private void mostrarVistaEnvios() { cambiarVista(vistaEnvios, btnNavEnvios); }
    @FXML private void mostrarVistaPaquetes() { cambiarVista(vistaPaquetes, btnNavPaquetes); }
    @FXML private void mostrarVistaRecepcion() { cambiarVista(vistaRecepcion, btnNavRecepcion); }
    @FXML private void mostrarVistaTarifas() { cambiarVista(vistaTarifas, btnNavTarifas); }
    @FXML private void mostrarVistaUsuarios() { cambiarVista(vistaUsuarios, btnNavUsuarios); }

    // --- ACCIONES DE ENVÍOS ---
    @FXML
    private void calcularTarifaEnvio() {
        try {
            Transportadora t = cmbEnvioTransportadora.getValue();
            BigDecimal peso = new BigDecimal(txtEnvioPeso.getText().trim());
            if (t != null) {
                Tarifa tarifa = tarifaDAO.buscarTarifaParaPeso(t.getId(), peso);
                if (tarifa != null) {
                    txtEnvioCosto.setText(tarifa.getValor().toPlainString());
                } else {
                    mostrarMensaje("Sin tarifa", "No hay tarifa configurada para ese peso; digite el costo manualmente.");
                }
            }
        } catch (Exception e) {
            mostrarMensaje("Datos incompletos", "Seleccione una transportadora e ingrese un peso válido.");
        }
    }

    @FXML
    private void guardarEnvio() {
        try {
            Transportadora t = cmbEnvioTransportadora.getValue();
            BigDecimal peso = new BigDecimal(txtEnvioPeso.getText().trim());
            BigDecimal costo = new BigDecimal(txtEnvioCosto.getText().trim());

            Operacion op = new Operacion();
            op.setIdTransportadora(t.getId());
            op.setIdUsuario(obtenerIdUsuarioActual());
            op.setTipo(Operacion.ENVIO);
            op.setGuia(txtEnvioGuia.getText().trim());
            op.setRemitente(txtEnvioRemitente.getText().trim());
            op.setDestinatario(txtEnvioDestinatario.getText().trim());
            op.setPeso(peso);
            op.setCosto(costo);
            op.setObservaciones(txtEnvioObservaciones.getText().trim());

            operacionDAO.insertar(op);
            txtEnvioGuia.clear();
            txtEnvioRemitente.clear();
            txtEnvioDestinatario.clear();
            txtEnvioPeso.clear();
            txtEnvioCosto.clear();
            txtEnvioObservaciones.clear();
            cargarDatosDesdeDAO();
        } catch (Exception e) {
            mostrarMensaje("Error al registrar envío", "Verifique todos los campos: " + e.getMessage());
        }
    }

    @FXML
    private void entregarEnvioSeleccionado() {
        Operacion op = tblEnvios.getSelectionModel().getSelectedItem();
        if (op != null) {
            op.setEstado(Operacion.ENTREGADO);
            operacionDAO.actualizar(op);
            cargarDatosDesdeDAO();
        }
    }

    @FXML
    private void eliminarEnvioSeleccionado() {
        Operacion op = tblEnvios.getSelectionModel().getSelectedItem();
        if (op != null) {
            operacionDAO.eliminar(op.getId());
            cargarDatosDesdeDAO();
        }
    }

    // --- ACCIONES DE PAQUETES ---
    @FXML
    private void guardarPaquete() {
        try {
            Operacion op = cmbPaqueteOperacion.getValue();
            int cantidad = Integer.parseInt(txtPaqueteCantidad.getText().trim());

            Paquete p = new Paquete();
            p.setIdOperacion(op.getId());
            p.setNombreProducto(txtPaqueteProducto.getText().trim());
            p.setCantidad(cantidad);
            p.setDescripcion(txtPaqueteDescripcion.getText().trim());

            paqueteDAO.insertar(p);
            txtPaqueteProducto.clear();
            txtPaqueteCantidad.setText("1");
            txtPaqueteDescripcion.clear();
            cargarDatosDesdeDAO();
        } catch (Exception e) {
            mostrarMensaje("Error al guardar paquete", "Seleccione una operación y complete los datos: " + e.getMessage());
        }
    }

    @FXML
    private void eliminarPaqueteSeleccionado() {
        Paquete p = tblPaquetes.getSelectionModel().getSelectedItem();
        if (p != null) {
            paqueteDAO.eliminar(p.getId());
            cargarDatosDesdeDAO();
        }
    }

    // --- ACCIONES DE RECEPCIÓN Y ENTREGA ---
    @FXML
    private void guardarRecepcion() {
        try {
            Transportadora t = cmbRecepcionTransportadora.getValue();

            Operacion op = new Operacion();
            op.setIdTransportadora(t.getId());
            op.setIdUsuario(obtenerIdUsuarioActual());
            op.setTipo(Operacion.RECEPCION);
            op.setGuia(txtRecepcionGuia.getText().trim());
            op.setRemitente(txtRecepcionRemitente.getText().trim());
            op.setDestinatario(txtRecepcionDestinatario.getText().trim());
            op.setObservaciones(txtRecepcionObservaciones.getText().trim());

            operacionDAO.insertar(op);
            txtRecepcionGuia.clear();
            txtRecepcionRemitente.clear();
            txtRecepcionDestinatario.clear();
            txtRecepcionObservaciones.clear();
            cargarDatosDesdeDAO();
        } catch (Exception e) {
            mostrarMensaje("Error al registrar recepción", "Verifique los datos: " + e.getMessage());
        }
    }

    @FXML
    private void entregarRecepcionSeleccionada() {
        Operacion op = tblRecepciones.getSelectionModel().getSelectedItem();
        if (op != null) {
            op.setEstado(Operacion.ENTREGADO);
            operacionDAO.actualizar(op);
            cargarDatosDesdeDAO();
        }
    }

    @FXML
    private void cancelarRecepcionSeleccionada() {
        Operacion op = tblRecepciones.getSelectionModel().getSelectedItem();
        if (op != null) {
            op.setEstado(Operacion.CANCELADO);
            operacionDAO.actualizar(op);
            cargarDatosDesdeDAO();
        }
    }

    @FXML
    private void eliminarRecepcionSeleccionada() {
        Operacion op = tblRecepciones.getSelectionModel().getSelectedItem();
        if (op != null) {
            operacionDAO.eliminar(op.getId());
            cargarDatosDesdeDAO();
        }
    }

    // --- ACCIONES DE TRANSPORTADORAS Y TARIFAS ---
    @FXML
    private void guardarTransportadora() {
        try {
            Transportadora t = new Transportadora();
            t.setNombre(txtTransNombre.getText().trim());
            t.setNit(txtTransNit.getText().trim());
            t.setTelefonoContacto(txtTransTelefono.getText().trim());
            t.setDireccion(txtTransDireccion.getText().trim());
            t.setTipoServicio(cmbTransServicio.getValue());
            t.setActivo(true);

            transportadoraDAO.insertar(t);
            txtTransNombre.clear();
            txtTransNit.clear();
            txtTransTelefono.clear();
            txtTransDireccion.clear();
            cargarDatosDesdeDAO();
        } catch (Exception e) {
            mostrarMensaje("Error al guardar transportadora", e.getMessage());
        }
    }

    @FXML
    private void eliminarTransportadoraSeleccionada() {
        Transportadora t = tblTransportadoras.getSelectionModel().getSelectedItem();
        if (t != null) {
            try {
                transportadoraDAO.eliminar(t.getId());
                cargarDatosDesdeDAO();
            } catch (Exception e) {
                mostrarMensaje("No se puede eliminar", "Esta transportadora ya tiene operaciones asociadas.");
            }
        }
    }

    @FXML
    private void guardarTarifa() {
        try {
            Transportadora t = cmbTarifaTransportadora.getValue();
            Tarifa tarifa = new Tarifa();
            tarifa.setIdTransportadora(t.getId());
            tarifa.setPesoMin(new BigDecimal(txtTarifaPesoMin.getText().trim()));
            tarifa.setPesoMax(new BigDecimal(txtTarifaPesoMax.getText().trim()));
            tarifa.setValor(new BigDecimal(txtTarifaValor.getText().trim()));

            tarifaDAO.insertar(tarifa);
            txtTarifaPesoMin.clear();
            txtTarifaPesoMax.clear();
            txtTarifaValor.clear();
            cargarDatosDesdeDAO();
        } catch (Exception e) {
            mostrarMensaje("Error al guardar tarifa", e.getMessage());
        }
    }

    @FXML
    private void eliminarTarifaSeleccionada() {
        Tarifa t = tblTarifas.getSelectionModel().getSelectedItem();
        if (t != null) {
            tarifaDAO.eliminar(t.getId());
            cargarDatosDesdeDAO();
        }
    }

    // --- ACCIONES DE USUARIOS ---
    @FXML
    private void guardarUsuario() {
        try {
            Usuario u = new Usuario();
            u.setNombre(txtUsuNombre.getText().trim());
            u.setLogin(txtUsuLogin.getText().trim());
            u.setContrasena(txtUsuContrasena.getText());
            u.setTelefono(txtUsuTelefono.getText().trim());
            u.setEmail(txtUsuEmail.getText().trim());
            u.setRol(cmbUsuRol.getValue());
            u.setActivo(true);

            usuarioDAO.insertar(u);
            txtUsuNombre.clear();
            txtUsuLogin.clear();
            txtUsuContrasena.clear();
            txtUsuTelefono.clear();
            txtUsuEmail.clear();
            cargarDatosDesdeDAO();
        } catch (Exception e) {
            mostrarMensaje("Error al guardar usuario", e.getMessage());
        }
    }

    @FXML
    private void cambiarEstadoUsuarioSeleccionado() {
        Usuario u = tblUsuarios.getSelectionModel().getSelectedItem();
        if (u != null) {
            u.setActivo(!u.isActivo());
            usuarioDAO.actualizar(u);
            cargarDatosDesdeDAO();
        }
    }

    @FXML
    private void eliminarUsuarioSeleccionado() {
        Usuario u = tblUsuarios.getSelectionModel().getSelectedItem();
        if (u != null) {
            try {
                usuarioDAO.eliminar(u.getId());
                cargarDatosDesdeDAO();
            } catch (Exception e) {
                mostrarMensaje("No se puede eliminar", "El usuario tiene historial de operaciones; puede desactivarlo.");
            }
        }
    }

    // --- EVENTOS DE VENTANA Y SESIÓN ---
    @FXML
    private void minimizarVentana() {
        Stage stage = (Stage) barraTitulo.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void cerrarAplicacion() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar salida");
        alert.setHeaderText(null);
        alert.setContentText("¿Desea cerrar la aplicación ARES?");

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: #181818;");
        dialogPane.lookup(".content.label").setStyle("-fx-text-fill: white;");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Platform.exit();
            System.exit(0);
        }
    }

    @FXML
    private void cerrarSesion() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cerrar Sesión");
        alert.setHeaderText(null);
        alert.setContentText("¿Desea cerrar sesión y volver al inicio?");

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: #181818;");
        dialogPane.lookup(".content.label").setStyle("-fx-text-fill: white;");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            SesionActual.getInstancia().cerrarSesion();
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/vistas/login.fxml"));
                Stage stage = (Stage) barraTitulo.getScene().getWindow();
                stage.getScene().setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private int obtenerIdUsuarioActual() {
        Usuario u = SesionActual.getInstancia().getUsuarioLogueado();
        return (u != null && u.getId() > 0) ? u.getId() : 1;
    }

    private void mostrarMensaje(String titulo, String texto) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(texto);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: #181818;");
        dialogPane.lookup(".content.label").setStyle("-fx-text-fill: white;");
        alert.showAndWait();
    }
}
