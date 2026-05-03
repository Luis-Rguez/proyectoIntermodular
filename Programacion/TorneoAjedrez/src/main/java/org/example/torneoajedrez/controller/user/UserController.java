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
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.example.torneoajedrez.dao.Usuario.UserDao;
import org.example.torneoajedrez.model.Clasificacion;
import org.example.torneoajedrez.model.Formato;
import org.example.torneoajedrez.model.Jugador;
import org.example.torneoajedrez.model.Torneo;

public class UserController implements Initializable {

    @FXML
    private TableView<Clasificacion> tableViewHistorial;
    private ObservableList listaHistorial;

    @FXML
    private TableColumn<Clasificacion, String> colNombreTorneo, colFormato, colClasificacion;

    @FXML
    private TableColumn<Clasificacion, Integer> colPerdidas, colTablas, colGanadas;

    @FXML
    private ComboBox<Formato> comboFormato;
    private ObservableList<Formato> listaFormato;

    @FXML
    private ComboBox<Torneo> comboToreno;
    private ObservableList<Torneo> listaTorneos;

    @FXML
    private TextField editDNI,editEdad, editMail, editNombre, editPass, editTelef, editNewPass;

    @FXML
    private Text textNewPass;

    @FXML
    private MenuItem menuItemCerrar, menuItemVer, menuItemPerfil, menuItemSalir;

    @FXML
    private Button btnBaja, btnEditar, btnSalir, btnVerPartida, btnAceptar;

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
        menuItemVer.setOnAction(event -> ventana.abrirVentanas(btnSalir,"verPartidas-view.fxml", "Partidas", true));
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

        //--------------------- ACIONES TABLA Y COMBO --------------------------------------------------------


    }

    private void initGUI()
    {
        comboToreno.setItems(listaTorneos);
        comboFormato.getSelectionModel().select(0);

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

        DatosAdmin.vaciarLista();

        jugador = userDao.cargarUsuario(DatosJugador.getIdJugador());
        listaTorneos = DatosJugador.filtroTorneoJugador(DatosJugador.getIdJugador());
        listaFormato = FXCollections.observableArrayList();
    }
}
