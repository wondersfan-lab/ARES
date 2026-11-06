package com.uniajc.vista;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.net.URL;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL rutaFxml = getClass().getResource("/vistas/login.fxml");
        if (rutaFxml == null) {
            System.err.println("CRÍTICO: No se encontró login.fxml");
            System.exit(1);
        }

        Parent root = FXMLLoader.load(rutaFxml);
        Scene scene = new Scene(root, 1280, 820);

        // Vuelve el fondo nativo de la escena transparente para que se vean los bordes redondeados del CSS
        scene.setFill(Color.TRANSPARENT);

        primaryStage.setTitle("ARES LOGÍSTICA");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1050);
        primaryStage.setMinHeight(680);

        // Quita la ventana cuadrada de Windows por completo
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
