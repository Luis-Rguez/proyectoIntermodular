package org.example.torneoajedrez.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.torneoajedrez.AppTorneoAjedrez;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private Button btnLogin,  btnCerrar;

    @FXML
    private TextField editDNI;

    @FXML
    private PasswordField editPass;    @FXML

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
            if(editDNI.getText().equals(""))
            {
                ventanasAciones.abrirVentanas(btnCerrar, ventanaAdmin, "Panel Principal", true);
            }else if(editDNI.getText().equals("2"))
            {
                ventanasAciones.abrirVentanas(btnCerrar, ventanaUsuario, "Panel Principal", true);
            }else
            {
                ventanasAciones.abrirVentanas(btnCerrar, ventanaArbitro, "Panel Principal", true);
            }
        });

        btnCerrar.setOnAction(event -> ventanasAciones.cerrarVentana(btnCerrar));
    }
}
