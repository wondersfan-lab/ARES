package com.uniajc.controlador;

import com.uniajc.dao.UsuarioDAO;
import com.uniajc.modelo.Usuario;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblError;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @FXML
    private void iniciarSesion(ActionEvent event) {
        lblError.setVisible(false);
        String usr = txtUsuario.getText().trim();
        String pass = txtPassword.getText();

        if (usr.isBlank() || pass.isBlank()) {
            lblError.setText("Debe ingresar usuario y contraseña.");
            lblError.setVisible(true);
            return;
        }

        try {
            Usuario usuarioValido = usuarioDAO.autenticar(usr, pass);

            if (usuarioValido != null) {
                SesionActual.getInstancia().setUsuarioLogueado(usuarioValido);
                cargarPantallaPrincipal(event);
            } else {
                lblError.setText("Usuario o contraseña incorrectos.");
                lblError.setVisible(true);
            }
        } catch (Exception e) {
            lblError.setText("Error conectando a la BD: " + e.getMessage());
            lblError.setVisible(true);
        }
    }

    private void cargarPantallaPrincipal(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/vistas/principal.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            Throwable causa = e.getCause() != null ? e.getCause() : e;
            lblError.setText("Error al cargar vista: " + causa.getMessage());
            lblError.setVisible(true);
            e.printStackTrace();
        }
    }

    @FXML
    private void cerrarApp() {
        Platform.exit();
        System.exit(0);
    }
}
