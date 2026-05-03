package org.example.torneoajedrez.controller.user;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.torneoajedrez.DataSet.DatosAdmin;
import org.example.torneoajedrez.DataSet.DatosJugador;
import org.example.torneoajedrez.controller.VentanasController;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.example.torneoajedrez.dao.Usuario.UserDao;
import org.example.torneoajedrez.model.Clasificacion;
import org.example.torneoajedrez.model.Jugador;

public class UserController implements Initializable {

    @FXML
    private TableView<Clasificacion> tableViewHistorial;
    private ObservableList<Clasificacion> listaHistorial;

    @FXML
    private TableColumn<Clasificacion, String> colNombreTorneo, colFormato, colClasificacion;

    @FXML
    private TableColumn<Clasificacion, Integer> colPerdidas, colTablas, colGanadas;

    @FXML
    private TextField editDNI, editMail, editNombre, editPass, editTelef, editNewPass;

    @FXML
    private Text textNewPass;

    @FXML
    private MenuItem menuItemCerrar,menuItemPerfil, menuItemSalir;

    @FXML
    private Button btnBaja, btnEditar, btnSalir, btnAceptar;

    private String pathLogin;
    private VentanasController ventana;
    private UserDao userDao;
    private Jugador jugador;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        instancias();
        initGUI();
        acciones();
    }

    private void acciones()
    {
        //--------------------- BARRA MENU --------------------------------------------------------
        menuItemPerfil.setOnAction(event -> ventana.abrirVentanas(btnSalir,"user/UsuarioPerfil-view.fxml", "Registro de Torneos", true));
        menuItemSalir.setOnAction(event -> ventana.cerrarVentana(btnSalir));
        menuItemCerrar.setOnAction(event ->
        {
            if(ventana.ventanaConfirmacion("¿Cerrar Sesion?", "¿Esta seguro de querer cesarr la sesion?"))
            {
                ventana.abrirVentanas(btnSalir,pathLogin, "Login", true);
            }
        });

        //--------------------- ACIONES BOTONES --------------------------------------------------------
        btnSalir.setOnAction(event -> ventana.abrirVentanas(btnSalir,pathLogin, "Login", true));
        btnBaja.setOnAction(event ->
        {
            userDao.baja();
            ventana.abrirVentanas(btnSalir,pathLogin, "Login", true);
        });
        btnEditar.setOnAction(event ->
                {
                    activarEdit(true);
                });

        btnAceptar.setOnAction(event ->
                {
                    activarEdit(false);
                    String nombre = editNombre.getText();
                    String dni = editDNI.getText();
                    String telf = editTelef.getText();
                    String mail = editMail.getText();
                    String newPass = editNewPass.getText();

                    if(newPass.isEmpty())
                    {
                        newPass = editPass.getText();
                    }

                    Jugador jugador = new Jugador(nombre, dni, telf, mail, newPass);

                    userDao.actualizarDatos(jugador);
                });
    }

    private void activarEdit(boolean activar)
    {
        editNewPass.editableProperty().set(activar);
        editMail.editableProperty().set(activar);
        editTelef.editableProperty().set(activar);
        editNombre.editableProperty().set(activar);
        editDNI.editableProperty().set(activar);
        btnAceptar.disableProperty().set(!activar);
        btnEditar.disableProperty().set(activar);
    }

    private void initGUI()
    {
        editNombre.setText(jugador.getNombre());
        editDNI.setText(jugador.getDni());
        editTelef.setText(jugador.getTelf());
        editMail.setText(jugador.getMail());
        editPass.setText(jugador.getPass());

        tableViewHistorial.setItems(listaHistorial);
        colNombreTorneo.setCellValueFactory(new PropertyValueFactory<>("nombreTorneo"));
        colFormato.setCellValueFactory(new PropertyValueFactory<>("nombreFormato"));
        colClasificacion.setCellValueFactory(new PropertyValueFactory<>("puesto"));
        colGanadas.setCellValueFactory(new PropertyValueFactory<>("ganadas"));
        colPerdidas.setCellValueFactory(new PropertyValueFactory<>("perdidas"));
        colTablas.setCellValueFactory(new PropertyValueFactory<>("tablas"));
    }

    private void instancias()
    {
        pathLogin = "login-view.fxml";
        ventana = new VentanasController();
        userDao = new UserDao();

        jugador = userDao.cargarUsuario(DatosJugador.getIdJugador());
        listaHistorial= FXCollections.observableArrayList();
        listaHistorial.setAll(userDao.cargarClasificacion());
    }
}
