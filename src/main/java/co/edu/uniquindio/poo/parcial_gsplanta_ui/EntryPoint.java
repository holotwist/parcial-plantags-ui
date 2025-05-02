package co.edu.uniquindio.poo.parcial_gsplanta_ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class EntryPoint extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(EntryPoint.class.getResource("main-view.fxml")));
        Scene scene = new Scene(root); // El tamaño se toma de BorderPane prefWidth/prefHeight
        stage.setTitle("GS Planta - Sistema de Gestión");
        stage.setMinWidth(1100);
        stage.setMinHeight(700);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}