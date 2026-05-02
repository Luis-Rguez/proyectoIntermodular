package org.example.torneoajedrez.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.torneoajedrez.AppTorneoAjedrez;
import org.example.torneoajedrez.DataSet.DatosJugador;
import org.example.torneoajedrez.DataSet.DatosStaff;
import org.example.torneoajedrez.dao.UsuarioDao;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private Button btnLogin,  btnCerrar;

    @FXML
    private TextField editMail;

    @FXML
    private PasswordField editPass;

    private VentanasController ventanasAciones;
    private String ventanaAdmin;
    private String ventanaJugador;
    private String ventanaArbitro;
    private UsuarioDao usuarioDao;

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
        ventanaJugador = "user/UsuarioPerfil-view.fxml";
        ventanaArbitro = "staff/staffRegistroPartida-view.fxml";
        usuarioDao = new UsuarioDao();
    }

    private void actions()
    {
        btnLogin.setOnAction(event ->
        {
            int dato;

            if ((dato = usuarioDao.getLogin(editMail.getText(), editPass.getText(), "Staff","id_Staff")) !=0)
            {
                DatosStaff.setIdStaff(dato);
                abrirVentana(ventanaArbitro);
            } else if((dato = usuarioDao.getLogin(editMail.getText(), editPass.getText(), "Jugadores","id_Jugador")) !=0)
            {
                abrirVentana(ventanaJugador);
            } else if(editMail.getText().equals("1"))
            {
                DatosJugador.setIdJugador(dato);
                abrirVentana(ventanaAdmin);
            } else
            {
                ventanasAciones.ventanaWarning("Usuario Incorrecto", "Por favor, Verifique su Contraseña y su Password.");
            }
        });

        btnCerrar.setOnAction(event -> ventanasAciones.cerrarVentana(btnCerrar));
    }

    public void abrirVentana(String ruta)
    {
        FXMLLoader loader = null;
        loader = new FXMLLoader(AppTorneoAjedrez.class.getResource(ruta));

        try {

            Parent root = loader.load();
            Stage ventana = new Stage();
            Scene scene = new Scene(root);
            ventana.setScene(scene);
            ventana.setTitle("Principal");
            ventana.show();
            ((Stage)btnCerrar.getScene().getWindow()).close();

        } catch (IOException e) {
            ventanasAciones.ventanaError("Ha ocurrido un Incoveniente. \n\n " + e.getMessage());
        }
    }
}
