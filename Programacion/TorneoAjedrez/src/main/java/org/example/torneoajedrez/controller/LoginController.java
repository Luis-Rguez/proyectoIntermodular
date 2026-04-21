package org.example.torneoajedrez.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.example.torneoajedrez.AppTorneoAjedrez;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnCerrar;

    private VentanasController ventanasAciones;
    private String ventanaAdmin;
    private String ventanaUsuario;
    private String ventanaArbitro;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        inicializacion();
        actions();
    }

    private void inicializacion()
    {
        ventanasAciones = new VentanasController();
        ventanaAdmin = "admin/adminPrincipal-view.fxml";
        ventanaUsuario = "user/UsuarioPerfil-view.fxml";
        ventanaArbitro = "staff/staffRegistroPartida-view.fxml";
    }

    private void actions()
    {
        btnLogin.setOnAction(event ->
        {
            ventanasAciones.abrirVentanas(btnCerrar, ventanaAdmin, "Panel Principal", true);
        });

        btnCerrar.setOnAction(event -> ventanasAciones.cerrarVentana(btnCerrar));
    }
}
