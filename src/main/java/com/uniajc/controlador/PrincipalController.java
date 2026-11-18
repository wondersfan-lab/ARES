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
import com.uniajc.servicios.ExportadorExcel;
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

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
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

    // --- 2. GESTIÓN DE ENVÍOS (ComboBox<String> para mostrar nombres limpios) ---
    @FXML private ComboBox<String> cmbEnvioTransportadora;
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
    @FXML private ComboBox<String> cmbPaqueteOperacion;
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
    @FXML private ComboBox<String> cmbRecepcionTransportadora;
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

    @FXML private ComboBox<String> cmbTarifaTransportadora;
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

    // Objetos seleccionados para edición simple
    private Operacion envioEditando = null;
    private Operacion recepcionEditando = null;
    private Paquete paqueteEditando = null;
    private Transportadora transportadoraEditando = null;
    private Tarifa tarifaEditando = null;
    private Usuario usuarioEditando = null;

    @FXML
    public void initialize() {
        configurarArrastreVentana();
        configurarPermisosRol();
        configurarCombosFijos();
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

    private void configurarCombosFijos() {
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

    // --- CARGA OPTIMIZADA POR MÓDULO (RÁPIDA Y LIMPIA) ---
    @FXML
    public void cargarDatosDesdeDAO() {
        cargarTransportadoras();
        cargarTarifas();
        cargarUsuarios();
        cargarPaquetes();
        cargarOperaciones();
    }

    private void cargarTransportadoras() {
        try {
            List<Transportadora> transportadorasBD = transportadoraDAO.listarTodos();
            listaTransportadoras.setAll(transportadorasBD);

            cmbEnvioTransportadora.getItems().clear();
            cmbRecepcionTransportadora.getItems().clear();
            cmbTarifaTransportadora.getItems().clear();

            for (Transportadora t : transportadorasBD) {
                cmbTarifaTransportadora.getItems().add(t.getNombre());
                if (t.isActivo()) {
                    if (Transportadora.SERVICIO_ENVIO.equals(t.getTipoServicio())
                            || Transportadora.SERVICIO_AMBOS.equals(t.getTipoServicio())) {
                        cmbEnvioTransportadora.getItems().add(t.getNombre());
                    }
                    if (Transportadora.SERVICIO_RECEPCION.equals(t.getTipoServicio())
                            || Transportadora.SERVICIO_AMBOS.equals(t.getTipoServicio())) {
                        cmbRecepcionTransportadora.getItems().add(t.getNombre());
                    }
                }
            }
            lblTotalTransportadoras.setText(transportadorasBD.size() + " Activas");
        } catch (Exception e) {
            System.out.println("Error cargando transportadoras: " + e.getMessage());
        }
    }

    private void cargarOperaciones() {
        try {
            List<Operacion> operacionesBD = operacionDAO.listarTodos();
            listaOperaciones.setAll(operacionesBD);

            listaEnvios.clear();
            listaRecepciones.clear();
            cmbPaqueteOperacion.getItems().clear();
            double ingresos = 0;

            for (Operacion op : operacionesBD) {
                // Texto limpio para el desplegable de Paquetes y Guías
                cmbPaqueteOperacion.getItems().add(formatearOperacionParaCombo(op));

                if (Operacion.ENVIO.equals(op.getTipo())) {
                    listaEnvios.add(op);
                } else {
                    listaRecepciones.add(op);
                }
                if (op.getCosto() != null && !Operacion.CANCELADO.equals(op.getEstado())) {
                    ingresos += op.getCosto().doubleValue();
                }
            }

            lblTotalEnviosHoy.setText(String.valueOf(operacionesBD.size()));
            lblBadgeEnvios.setText(String.valueOf(listaEnvios.size()));

            NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(Locale.of("es", "CO"));
            formatoMoneda.setMaximumFractionDigits(0);
            lblIngresosTotales.setText(formatoMoneda.format(ingresos));

            // Sincronización automática en tiempo real con Registros_ARES.xlsx
            actualizarExcelEnSegundoPlano();
        } catch (Exception e) {
            System.out.println("Error cargando operaciones: " + e.getMessage());
        }
    }

    private void actualizarExcelEnSegundoPlano() {
        List<Operacion> copiaOperaciones = new ArrayList<>(listaOperaciones);
        Thread hiloExcel = new Thread(() -> {
            try {
                ExportadorExcel.exportarOperaciones(copiaOperaciones);
            } catch (IOException e) {
                Platform.runLater(() -> mostrarMensaje(
                        "Archivo Excel Abierto",
                        "La operación se guardó en PostgreSQL, pero no se pudo actualizar Registros_ARES.xlsx porque está abierto en Excel.\n\nPor favor cierre el archivo Excel."
                ));
            }
        });
        hiloExcel.setDaemon(true);
        hiloExcel.start();
    }

    @FXML
    private void exportarExcelManual() {
        try {
            File archivo = ExportadorExcel.exportarOperaciones(listaOperaciones);
            mostrarMensaje(
                    "Exportación a Excel Exitosa",
                    "El archivo se guardó y actualizó correctamente en:\n" + archivo.getAbsolutePath()
            );
        } catch (IOException e) {
            mostrarMensaje(
                    "Archivo Excel Abierto",
                    "No se pudo guardar Registros_ARES.xlsx porque lo tiene abierto en Microsoft Excel.\n\nPor favor cierre el archivo e intente nuevamente."
            );
        }
    }

    private void cargarPaquetes() {
        try {
            List<Paquete> paquetesBD = paqueteDAO.listarTodos();
            listaPaquetes.setAll(paquetesBD);
            lblTotalPaquetes.setText(String.valueOf(paquetesBD.size()));
        } catch (Exception e) {
            System.out.println("Error cargando paquetes: " + e.getMessage());
        }
    }

    private void cargarTarifas() {
        try {
            listaTarifas.setAll(tarifaDAO.listarTodos());
        } catch (Exception e) {
            System.out.println("Error cargando tarifas: " + e.getMessage());
        }
    }

    private void cargarUsuarios() {
        try {
            listaUsuarios.setAll(usuarioDAO.listarTodos());
        } catch (Exception e) {
            System.out.println("Error cargando usuarios: " + e.getMessage());
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

    // --- 1. ACCIONES PANEL PRINCIPAL ---
    @FXML
    private void entregarOperacionPrincipal() {
        Operacion op = tblOperaciones.getSelectionModel().getSelectedItem();
        if (op != null) {
            op.setEstado(Operacion.ENTREGADO);
            operacionDAO.actualizar(op);
            cargarOperaciones();
        }
    }

    @FXML
    private void cancelarOperacionPrincipal() {
        Operacion op = tblOperaciones.getSelectionModel().getSelectedItem();
        if (op != null) {
            op.setEstado(Operacion.CANCELADO);
            operacionDAO.actualizar(op);
            cargarOperaciones();
        }
    }

    @FXML
    private void eliminarOperacionPrincipal() {
        Operacion op = tblOperaciones.getSelectionModel().getSelectedItem();
        if (op != null) {
            operacionDAO.eliminar(op.getId());
            cargarOperaciones();
            cargarPaquetes();
        }
    }

    // --- 2. ACCIONES DE ENVÍOS ---
    @FXML
    private void calcularTarifaEnvio() {
        try {
            Transportadora t = buscarTransportadoraPorNombre(cmbEnvioTransportadora.getValue());
            BigDecimal peso = new BigDecimal(txtEnvioPeso.getText().trim());
            if (t != null) {
                Tarifa tarifa = tarifaDAO.buscarTarifaParaPeso(t.getId(), peso);
                if (tarifa != null) {
                    txtEnvioCosto.setText(tarifa.getValor().toPlainString());
                } else {
                    mostrarMensaje("Sin tarifa", "No hay tarifa configurada para ese peso; digite el costo manualmente.");
                }
            } else {
                mostrarMensaje("Transportadora requerida", "Seleccione primero una transportadora.");
            }
        } catch (Exception e) {
            mostrarMensaje("Peso inválido", "Ingrese un peso numérico válido (ej. 3.5).");
        }
    }

    @FXML
    private void guardarEnvio() {
        try {
            Transportadora t = buscarTransportadoraPorNombre(cmbEnvioTransportadora.getValue());
            BigDecimal peso = new BigDecimal(txtEnvioPeso.getText().trim());
            BigDecimal costo = new BigDecimal(txtEnvioCosto.getText().trim());

            if (envioEditando == null) {
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
            } else {
                envioEditando.setIdTransportadora(t.getId());
                envioEditando.setGuia(txtEnvioGuia.getText().trim());
                envioEditando.setRemitente(txtEnvioRemitente.getText().trim());
                envioEditando.setDestinatario(txtEnvioDestinatario.getText().trim());
                envioEditando.setPeso(peso);
                envioEditando.setCosto(costo);
                envioEditando.setObservaciones(txtEnvioObservaciones.getText().trim());
                operacionDAO.actualizar(envioEditando);
            }

            limpiarCamposEnvio();
            cargarOperaciones();
        } catch (Exception e) {
            mostrarMensaje("Error al guardar envío", "Verifique todos los campos: " + e.getMessage());
        }
    }

    @FXML
    private void cargarEnvioSeleccionado() {
        Operacion op = tblEnvios.getSelectionModel().getSelectedItem();
        if (op != null) {
            envioEditando = op;
            cmbEnvioTransportadora.setValue(obtenerNombreTransportadora(op.getIdTransportadora()));
            txtEnvioGuia.setText(op.getGuia());
            txtEnvioRemitente.setText(op.getRemitente());
            txtEnvioDestinatario.setText(op.getDestinatario());
            txtEnvioPeso.setText(op.getPeso() != null ? op.getPeso().toPlainString() : "");
            txtEnvioCosto.setText(op.getCosto() != null ? op.getCosto().toPlainString() : "");
            txtEnvioObservaciones.setText(op.getObservaciones() != null ? op.getObservaciones() : "");
        }
    }

    @FXML
    private void limpiarCamposEnvio() {
        envioEditando = null;
        txtEnvioGuia.clear();
        txtEnvioRemitente.clear();
        txtEnvioDestinatario.clear();
        txtEnvioPeso.clear();
        txtEnvioCosto.clear();
        txtEnvioObservaciones.clear();
    }

    @FXML
    private void entregarEnvioSeleccionado() {
        Operacion op = tblEnvios.getSelectionModel().getSelectedItem();
        if (op != null) {
            op.setEstado(Operacion.ENTREGADO);
            operacionDAO.actualizar(op);
            cargarOperaciones();
        }
    }

    @FXML
    private void cancelarEnvioSeleccionado() {
        Operacion op = tblEnvios.getSelectionModel().getSelectedItem();
        if (op != null) {
            op.setEstado(Operacion.CANCELADO);
            operacionDAO.actualizar(op);
            cargarOperaciones();
        }
    }

    @FXML
    private void eliminarEnvioSeleccionado() {
        Operacion op = tblEnvios.getSelectionModel().getSelectedItem();
        if (op != null) {
            operacionDAO.eliminar(op.getId());
            cargarOperaciones();
            cargarPaquetes();
        }
    }

    // --- 3. ACCIONES DE PAQUETES ---
    @FXML
    private void guardarPaquete() {
        try {
            Operacion op = buscarOperacionPorTextoCombo(cmbPaqueteOperacion.getValue());
            int cantidad = Integer.parseInt(txtPaqueteCantidad.getText().trim());

            if (paqueteEditando == null) {
                Paquete p = new Paquete();
                p.setIdOperacion(op.getId());
                p.setNombreProducto(txtPaqueteProducto.getText().trim());
                p.setCantidad(cantidad);
                p.setDescripcion(txtPaqueteDescripcion.getText().trim());
                paqueteDAO.insertar(p);
            } else {
                if (op != null) {
                    paqueteEditando.setIdOperacion(op.getId());
                }
                paqueteEditando.setNombreProducto(txtPaqueteProducto.getText().trim());
                paqueteEditando.setCantidad(cantidad);
                paqueteEditando.setDescripcion(txtPaqueteDescripcion.getText().trim());
                paqueteDAO.actualizar(paqueteEditando);
            }

            limpiarCamposPaquete();
            cargarPaquetes();
        } catch (Exception e) {
            mostrarMensaje("Error al guardar paquete", "Seleccione una guía y complete los datos: " + e.getMessage());
        }
    }

    @FXML
    private void cargarPaqueteSeleccionado() {
        Paquete p = tblPaquetes.getSelectionModel().getSelectedItem();
        if (p != null) {
            paqueteEditando = p;
            txtPaqueteProducto.setText(p.getNombreProducto());
            txtPaqueteCantidad.setText(String.valueOf(p.getCantidad()));
            txtPaqueteDescripcion.setText(p.getDescripcion() != null ? p.getDescripcion() : "");
            for (Operacion op : listaOperaciones) {
                if (op.getId() == p.getIdOperacion()) {
                    cmbPaqueteOperacion.setValue(formatearOperacionParaCombo(op));
                    break;
                }
            }
        }
    }

    @FXML
    private void limpiarCamposPaquete() {
        paqueteEditando = null;
        txtPaqueteProducto.clear();
        txtPaqueteCantidad.setText("1");
        txtPaqueteDescripcion.clear();
    }

    @FXML
    private void filtrarPaquetesPorGuia() {
        Operacion op = buscarOperacionPorTextoCombo(cmbPaqueteOperacion.getValue());
        if (op != null) {
            listaPaquetes.setAll(paqueteDAO.listarPorOperacion(op.getId()));
        } else {
            mostrarMensaje("Seleccione una guía", "Elija primero una guía en el desplegable superior.");
        }
    }

    @FXML
    private void verTodosLosPaquetes() {
        cargarPaquetes();
    }

    @FXML
    private void eliminarPaqueteSeleccionado() {
        Paquete p = tblPaquetes.getSelectionModel().getSelectedItem();
        if (p != null) {
            paqueteDAO.eliminar(p.getId());
            cargarPaquetes();
        }
    }

    // --- 4. ACCIONES DE RECEPCIÓN Y ENTREGA ---
    @FXML
    private void guardarRecepcion() {
        try {
            Transportadora t = buscarTransportadoraPorNombre(cmbRecepcionTransportadora.getValue());

            if (recepcionEditando == null) {
                Operacion op = new Operacion();
                op.setIdTransportadora(t.getId());
                op.setIdUsuario(obtenerIdUsuarioActual());
                op.setTipo(Operacion.RECEPCION);
                op.setGuia(txtRecepcionGuia.getText().trim());
                op.setRemitente(txtRecepcionRemitente.getText().trim());
                op.setDestinatario(txtRecepcionDestinatario.getText().trim());
                op.setObservaciones(txtRecepcionObservaciones.getText().trim());
                operacionDAO.insertar(op);
            } else {
                recepcionEditando.setIdTransportadora(t.getId());
                recepcionEditando.setGuia(txtRecepcionGuia.getText().trim());
                recepcionEditando.setRemitente(txtRecepcionRemitente.getText().trim());
                recepcionEditando.setDestinatario(txtRecepcionDestinatario.getText().trim());
                recepcionEditando.setObservaciones(txtRecepcionObservaciones.getText().trim());
                operacionDAO.actualizar(recepcionEditando);
            }

            limpiarCamposRecepcion();
            cargarOperaciones();
        } catch (Exception e) {
            mostrarMensaje("Error al guardar recepción", "Verifique los datos: " + e.getMessage());
        }
    }

    @FXML
    private void cargarRecepcionSeleccionada() {
        Operacion op = tblRecepciones.getSelectionModel().getSelectedItem();
        if (op != null) {
            recepcionEditando = op;
            cmbRecepcionTransportadora.setValue(obtenerNombreTransportadora(op.getIdTransportadora()));
            txtRecepcionGuia.setText(op.getGuia());
            txtRecepcionRemitente.setText(op.getRemitente());
            txtRecepcionDestinatario.setText(op.getDestinatario());
            txtRecepcionObservaciones.setText(op.getObservaciones() != null ? op.getObservaciones() : "");
        }
    }

    @FXML
    private void limpiarCamposRecepcion() {
        recepcionEditando = null;
        txtRecepcionGuia.clear();
        txtRecepcionRemitente.clear();
        txtRecepcionDestinatario.clear();
        txtRecepcionObservaciones.clear();
    }

    @FXML
    private void entregarRecepcionSeleccionada() {
        Operacion op = tblRecepciones.getSelectionModel().getSelectedItem();
        if (op != null) {
            op.setEstado(Operacion.ENTREGADO);
            operacionDAO.actualizar(op);
            cargarOperaciones();
        }
    }

    @FXML
    private void cancelarRecepcionSeleccionada() {
        Operacion op = tblRecepciones.getSelectionModel().getSelectedItem();
        if (op != null) {
            op.setEstado(Operacion.CANCELADO);
            operacionDAO.actualizar(op);
            cargarOperaciones();
        }
    }

    @FXML
    private void eliminarRecepcionSeleccionada() {
        Operacion op = tblRecepciones.getSelectionModel().getSelectedItem();
        if (op != null) {
            operacionDAO.eliminar(op.getId());
            cargarOperaciones();
            cargarPaquetes();
        }
    }

    // --- 5. ACCIONES DE TRANSPORTADORAS Y TARIFAS ---
    @FXML
    private void guardarTransportadora() {
        try {
            if (transportadoraEditando == null) {
                Transportadora t = new Transportadora();
                t.setNombre(txtTransNombre.getText().trim());
                t.setNit(txtTransNit.getText().trim());
                t.setTelefonoContacto(txtTransTelefono.getText().trim());
                t.setDireccion(txtTransDireccion.getText().trim());
                t.setTipoServicio(cmbTransServicio.getValue());
                t.setActivo(true);
                transportadoraDAO.insertar(t);
            } else {
                transportadoraEditando.setNombre(txtTransNombre.getText().trim());
                transportadoraEditando.setNit(txtTransNit.getText().trim());
                transportadoraEditando.setTelefonoContacto(txtTransTelefono.getText().trim());
                transportadoraEditando.setDireccion(txtTransDireccion.getText().trim());
                transportadoraEditando.setTipoServicio(cmbTransServicio.getValue());
                transportadoraDAO.actualizar(transportadoraEditando);
            }

            limpiarCamposTransportadora();
            cargarTransportadoras();
        } catch (Exception e) {
            mostrarMensaje("Error al guardar transportadora", e.getMessage());
        }
    }

    @FXML
    private void cargarTransportadoraSeleccionada() {
        Transportadora t = tblTransportadoras.getSelectionModel().getSelectedItem();
        if (t != null) {
            transportadoraEditando = t;
            txtTransNombre.setText(t.getNombre());
            txtTransNit.setText(t.getNit() != null ? t.getNit() : "");
            txtTransTelefono.setText(t.getTelefonoContacto() != null ? t.getTelefonoContacto() : "");
            txtTransDireccion.setText(t.getDireccion() != null ? t.getDireccion() : "");
            cmbTransServicio.setValue(t.getTipoServicio());
        }
    }

    @FXML
    private void limpiarCamposTransportadora() {
        transportadoraEditando = null;
        txtTransNombre.clear();
        txtTransNit.clear();
        txtTransTelefono.clear();
        txtTransDireccion.clear();
        cmbTransServicio.setValue(Transportadora.SERVICIO_AMBOS);
    }

    @FXML
    private void eliminarTransportadoraSeleccionada() {
        Transportadora t = tblTransportadoras.getSelectionModel().getSelectedItem();
        if (t != null) {
            try {
                transportadoraDAO.eliminar(t.getId());
                cargarTransportadoras();
            } catch (Exception e) {
                mostrarMensaje("No se puede eliminar", "Esta transportadora ya tiene operaciones asociadas.");
            }
        }
    }

    @FXML
    private void guardarTarifa() {
        try {
            Transportadora t = buscarTransportadoraPorNombre(cmbTarifaTransportadora.getValue());

            if (tarifaEditando == null) {
                Tarifa tarifa = new Tarifa();
                tarifa.setIdTransportadora(t.getId());
                tarifa.setPesoMin(new BigDecimal(txtTarifaPesoMin.getText().trim()));
                tarifa.setPesoMax(new BigDecimal(txtTarifaPesoMax.getText().trim()));
                tarifa.setValor(new BigDecimal(txtTarifaValor.getText().trim()));
                tarifaDAO.insertar(tarifa);
            } else {
                tarifaEditando.setIdTransportadora(t.getId());
                tarifaEditando.setPesoMin(new BigDecimal(txtTarifaPesoMin.getText().trim()));
                tarifaEditando.setPesoMax(new BigDecimal(txtTarifaPesoMax.getText().trim()));
                tarifaEditando.setValor(new BigDecimal(txtTarifaValor.getText().trim()));
                tarifaDAO.actualizar(tarifaEditando);
            }

            limpiarCamposTarifa();
            cargarTarifas();
        } catch (Exception e) {
            mostrarMensaje("Error al guardar tarifa", e.getMessage());
        }
    }

    @FXML
    private void cargarTarifaSeleccionada() {
        Tarifa t = tblTarifas.getSelectionModel().getSelectedItem();
        if (t != null) {
            tarifaEditando = t;
            cmbTarifaTransportadora.setValue(obtenerNombreTransportadora(t.getIdTransportadora()));
            txtTarifaPesoMin.setText(t.getPesoMin().toPlainString());
            txtTarifaPesoMax.setText(t.getPesoMax().toPlainString());
            txtTarifaValor.setText(t.getValor().toPlainString());
        }
    }

    @FXML
    private void limpiarCamposTarifa() {
        tarifaEditando = null;
        txtTarifaPesoMin.clear();
        txtTarifaPesoMax.clear();
        txtTarifaValor.clear();
    }

    @FXML
    private void eliminarTarifaSeleccionada() {
        Tarifa t = tblTarifas.getSelectionModel().getSelectedItem();
        if (t != null) {
            tarifaDAO.eliminar(t.getId());
            cargarTarifas();
        }
    }

    // --- 6. ACCIONES DE USUARIOS ---
    @FXML
    private void guardarUsuario() {
        try {
            if (usuarioEditando == null) {
                Usuario u = new Usuario();
                u.setNombre(txtUsuNombre.getText().trim());
                u.setLogin(txtUsuLogin.getText().trim());
                u.setContrasena(txtUsuContrasena.getText());
                u.setTelefono(txtUsuTelefono.getText().trim());
                u.setEmail(txtUsuEmail.getText().trim());
                u.setRol(cmbUsuRol.getValue());
                u.setActivo(true);
                usuarioDAO.insertar(u);
            } else {
                usuarioEditando.setNombre(txtUsuNombre.getText().trim());
                usuarioEditando.setLogin(txtUsuLogin.getText().trim());
                usuarioEditando.setContrasena(txtUsuContrasena.getText());
                usuarioEditando.setTelefono(txtUsuTelefono.getText().trim());
                usuarioEditando.setEmail(txtUsuEmail.getText().trim());
                usuarioEditando.setRol(cmbUsuRol.getValue());
                usuarioDAO.actualizar(usuarioEditando);
            }

            limpiarCamposUsuario();
            cargarUsuarios();
        } catch (Exception e) {
            mostrarMensaje("Error al guardar usuario", e.getMessage());
        }
    }

    @FXML
    private void cargarUsuarioSeleccionado() {
        Usuario u = tblUsuarios.getSelectionModel().getSelectedItem();
        if (u != null) {
            usuarioEditando = u;
            txtUsuNombre.setText(u.getNombre());
            txtUsuLogin.setText(u.getLogin());
            txtUsuContrasena.setText(u.getContrasena());
            txtUsuTelefono.setText(u.getTelefono() != null ? u.getTelefono() : "");
            txtUsuEmail.setText(u.getEmail() != null ? u.getEmail() : "");
            cmbUsuRol.setValue(u.getRol());
        }
    }

    @FXML
    private void limpiarCamposUsuario() {
        usuarioEditando = null;
        txtUsuNombre.clear();
        txtUsuLogin.clear();
        txtUsuContrasena.clear();
        txtUsuTelefono.clear();
        txtUsuEmail.clear();
        cmbUsuRol.setValue(Usuario.OPERADOR);
    }

    @FXML
    private void cambiarEstadoUsuarioSeleccionado() {
        Usuario u = tblUsuarios.getSelectionModel().getSelectedItem();
        if (u != null) {
            u.setActivo(!u.isActivo());
            usuarioDAO.actualizar(u);
            cargarUsuarios();
        }
    }

    @FXML
    private void eliminarUsuarioSeleccionado() {
        Usuario u = tblUsuarios.getSelectionModel().getSelectedItem();
        if (u != null) {
            try {
                usuarioDAO.eliminar(u.getId());
                cargarUsuarios();
            } catch (Exception e) {
                mostrarMensaje("No se puede eliminar", "El usuario tiene historial de operaciones; puede desactivarlo.");
            }
        }
    }

    // --- MÉTODOS AUXILIARES BÁSICOS (POO) ---
    private String formatearOperacionParaCombo(Operacion op) {
        String guia = op.getGuia() != null ? op.getGuia().trim() : "";
        String destinatario = op.getDestinatario() != null ? op.getDestinatario().trim() : "";
        if (guia.toUpperCase().startsWith("DEMO-") || guia.toUpperCase().contains("DEMO")) {
            return op.getTipo() + " - (" + destinatario + ")";
        } else {
            return "Guía " + guia + " - (" + destinatario + ")";
        }
    }

    private Operacion buscarOperacionPorTextoCombo(String textoCombo) {
        if (textoCombo == null) return null;
        for (Operacion op : listaOperaciones) {
            if (formatearOperacionParaCombo(op).equals(textoCombo)) {
                return op;
            }
        }
        return null;
    }

    private Transportadora buscarTransportadoraPorNombre(String nombre) {
        if (nombre == null) return null;
        for (Transportadora t : listaTransportadoras) {
            if (t.getNombre().equals(nombre)) {
                return t;
            }
        }
        return null;
    }

    private String obtenerNombreTransportadora(int idTransportadora) {
        for (Transportadora t : listaTransportadoras) {
            if (t.getId() == idTransportadora) {
                return t.getNombre();
            }
        }
        return "";
    }

    private int obtenerIdUsuarioActual() {
        Usuario u = SesionActual.getInstancia().getUsuarioLogueado();
        return (u != null && u.getId() > 0) ? u.getId() : 1;
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
