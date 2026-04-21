package org.example.torneoajedrez.controller.admin;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import org.example.torneoajedrez.controller.VentanasController;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    @FXML
    private Button btnClasificacion, btnVer, btnClubs, btnPartidas, btnProveedores, btnSalir;

    @FXML
    private Button btnStaff, btnTroenos, btnUsuarios;

    @FXML
    private MenuBar menuBarra;

    // Item Menu
    @FXML
    private MenuItem menuItemTorneo, menuItemCerrarSesion, menuItemSalir;

    // Item Registros
    @FXML
    private MenuItem menuItemClubs, menuItemPatrocinador,menuItemJugadores, menuItemStaff;

    // Item Partidas
    @FXML
    private MenuItem menuItemOrganizar, menuItemVer;

    private String pathLogin;
    private VentanasController ventana;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
       instancias();
       initGUI();
       acciones();
    }

    private void acciones()
    {
        // Acciones de los Items de la Barra Menu
        menuItemTorneo.setOnAction(event -> ventana.abrirVentanas(btnSalir,"admin/adminTorneo-view.fxml", "Registro de Torneos", false));
        menuItemClubs.setOnAction(event -> ventana.abrirVentanas(btnSalir,"admin/adminClubs-view.fxml", "Registro de Club", false));
        menuItemJugadores.setOnAction(event -> ventana.abrirVentanas(btnSalir,"admin/adminUsuario-view.fxml", "Registro de Jugadores", false));
        menuItemStaff.setOnAction(event -> ventana.abrirVentanas(btnSalir,"admin/adminStaff-view.fxml", "Registro de Personal", false));
        menuItemPatrocinador.setOnAction(event -> ventana.abrirVentanas(btnSalir,"admin/adminPatrocinador-view.fxml", "Registro de Patrocinadores", false));
        menuItemOrganizar.setOnAction(event -> ventana.abrirVentanas(btnSalir,"admin/adminPartidas-view.fxml", "Organizar Partidas", false));
        menuItemVer.setOnAction(event -> ventana.abrirVentanas(btnSalir,"verPartidas-view.fxml", "Partidas", false));

        menuItemCerrarSesion.setOnAction(event ->
        {
            if(ventana.ventanaConfirmacion("¿Cerrar Sesion?", "¿Esta seguro de querer cesarr la sesion?"))
            {
                ventana.abrirVentanas(btnSalir,pathLogin, "Login", true);
            }
        });
        menuItemSalir.setOnAction(event -> ventana.cerrarVentana(btnSalir));

        // Acciones de los Botones de la Pantalla Principal
        btnProveedores.setOnAction(event -> ventana.abrirVentanas(btnSalir,"admin/adminPatrocinador-view.fxml", "Registro de Patrocinadores", false));
        btnUsuarios.setOnAction(event -> ventana.abrirVentanas(btnSalir,"admin/adminUsuario-view.fxml", "Registro de Usuarios", false));
        btnStaff.setOnAction(event -> ventana.abrirVentanas(btnSalir,"admin/adminStaff-view.fxml", "Registro de Personal", false));
        btnClubs.setOnAction(event -> ventana.abrirVentanas(btnSalir,"admin/adminClubs-view.fxml", "Registro de Club", false));
        btnTroenos.setOnAction(event -> ventana.abrirVentanas(btnSalir,"admin/adminTorneo-view.fxml", "Registro de Torneos", false));
        btnPartidas.setOnAction(event -> ventana.abrirVentanas(btnSalir,"admin/adminPartidas-view.fxml", "Resgistro de Partidas", false));
        btnVer.setOnAction(event -> ventana.abrirVentanas(btnSalir,"verPartidas-view.fxml", "Partidas", false));

        btnSalir.setOnAction(event -> ventana.cerrarVentana(btnSalir));
    }

    private void initGUI()
    {
    }

    private void instancias()
    {
        pathLogin = "login-view.fxml";
        ventana = new VentanasController();
    }
}
