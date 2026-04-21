package org.example.torneoajedrez.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.example.torneoajedrez.AppTorneoAjedrez;

import java.util.Optional;

public class VentanasController {

    public void ventanaError(String frase)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(frase);
        alert.show();
    }

    public boolean ventanaConfirmacion(String titulo, String frase)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setContentText(frase);
        Optional<ButtonType> respuesta = alert.showAndWait();

        if(respuesta.get() == ButtonType.OK)
        {
            return true;
        }
        return false;
    }

    public void ventanaWarning(String titulo, String frase)
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setContentText(frase);
        alert.show();
    }

    public void abrirVentanas(Button btnSalir, String rutaVentana, String titulo, boolean cerrarVentana)
    {
        Stage stage = new Stage();
        Scene scene = null;
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(AppTorneoAjedrez.class.getResource(rutaVentana));
            scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.setTitle(titulo);

            stage.show();

            if(cerrarVentana)
            {
                cerrarVentana(btnSalir);
            }

        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void cerrarVentana(Button btnCerrar)
    {
        ((Stage) btnCerrar.getScene().getWindow()).close();
    }
}
