package org.example.torneoajedrez.controller.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import org.example.torneoajedrez.controller.VentanasController;
import org.example.torneoajedrez.dao.Admin.AdminDaoClasificacion;
import org.example.torneoajedrez.model.Clasificacion;
import org.example.torneoajedrez.model.Formato;
import org.example.torneoajedrez.model.Jugador;
import org.example.torneoajedrez.model.Torneo;

public class ClasificacionController implements Initializable {

    @FXML
    private Button btnSalir, btnGuardar;

    @FXML
    private TableView<Clasificacion> tableViewClasificacion;
    private ObservableList<Clasificacion> tablaClasificacion;

    @FXML
    private TableColumn<Clasificacion, String> colClasificacion;

    @FXML
    private TableColumn<Clasificacion, Integer> colGanadas;

    @FXML
    private TableColumn<Clasificacion, String> colJugador;

    @FXML
    private ComboBox<Formato> comboFormato;
    private ObservableList<Formato> listaFormato;

    @FXML
    private ComboBox<Torneo> comboTorneo;
    private ObservableList<Torneo> listaTorneos;

    // Item Menu
    @FXML
    private MenuItem menuItemTorneo, menuItemCerrarSesion, menuItemSalir;

    // Item Registros
    @FXML
    private MenuItem menuItemClubs, menuItemPatrocinador,menuItemJugadores, menuItemStaff;

    // Item Partidas
    @FXML
    private MenuItem menuItemOrganizar, menuItemVer, menuItemClasificacion;

    @FXML
    private Text textTitulo;

    private String pathLogin;
    private VentanasController ventana;
    private AdminDaoClasificacion adminDaoClasificacion;
    private ArrayList<Jugador> listaJugadores;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        instancias();
        initGUI();
        acciones();
    }

    private void initGUI()
    {
        comboTorneo.setItems(listaTorneos);
        comboFormato.setItems(listaFormato);

        tableViewClasificacion.setItems(tablaClasificacion);
        colClasificacion.setCellValueFactory(new PropertyValueFactory<>("puesto"));
        colJugador.setCellValueFactory(new PropertyValueFactory<>("nombreJugador"));
        colGanadas.setCellValueFactory(new PropertyValueFactory<>("ganadas"));
    }

    private void instancias()
    {
        pathLogin = "login-view.fxml";
        ventana = new VentanasController();
        adminDaoClasificacion = new AdminDaoClasificacion();

        listaTorneos = FXCollections.observableArrayList();
        listaFormato = FXCollections.observableArrayList();
        tablaClasificacion = FXCollections.observableArrayList();
        listaJugadores = new ArrayList<>();

        listaTorneos.setAll(adminDaoClasificacion.cargarTorneo());
    }

    private void acciones() {
        // Acciones de los Items de la Barra Menu
        menuItemTorneo.setOnAction(event -> ventana.abrirVentanas(btnSalir, "admin/adminTorneo-view.fxml", "Registro de Torneos", true));
        menuItemClubs.setOnAction(event -> ventana.abrirVentanas(btnSalir, "admin/adminClubs-view.fxml", "Registro de Club", true));
        menuItemJugadores.setOnAction(event -> ventana.abrirVentanas(btnSalir, "admin/adminUsuario-view.fxml", "Registro de Jugadores", true));
        menuItemStaff.setOnAction(event -> ventana.abrirVentanas(btnSalir, "admin/adminStaff-view.fxml", "Registro de Personal", true));
        menuItemPatrocinador.setOnAction(event -> ventana.abrirVentanas(btnSalir, "admin/adminPatrocinador-view.fxml", "Registro de Patrocinadores", true));
        menuItemOrganizar.setOnAction(event -> ventana.abrirVentanas(btnSalir, "admin/adminPartidas-view.fxml", "Organizar Partidas", true));
        menuItemVer.setOnAction(event -> ventana.abrirVentanas(btnSalir, "verPartidas-view.fxml", "Partidas", true));
        menuItemClasificacion.setOnAction(event -> ventana.abrirVentanas(btnSalir, "admin/adminClasificacion-view.fxml", "Clasificacion", true));

        menuItemCerrarSesion.setOnAction(event ->
        {
            if (ventana.ventanaConfirmacion("¿Cerrar Sesion?", "¿Esta seguro de querer cesarr la sesion?")) {
                ventana.abrirVentanas(btnSalir, pathLogin, "Login", true);
            }
        });
        menuItemSalir.setOnAction(event -> ventana.cerrarVentana(btnSalir));

        // -------- ACCIONES COMBOS ----------------
        comboTorneo.setOnAction(event -> listaFormato.setAll(adminDaoClasificacion.cargarFormatos(comboTorneo.getSelectionModel().getSelectedItem().getIdTorneo())));
        comboFormato.setOnAction(event -> cargarTabla());


        // -------- BOTONES ----------------
        btnSalir.setOnAction(event -> ventana.cerrarVentana(btnSalir));

        btnGuardar.setOnAction(event ->
        {
            if(ventana.ventanaConfirmacion("Guardado", "¿Esta Seguro de querer guardar la clasificacion?"))
            {
                comprobarGuardado();
            }
        });
    }

    public void cargarTabla ()
    {
        if(!comboFormato.getSelectionModel().isEmpty())
        {
            tablaClasificacion.addAll(adminDaoClasificacion.jugadoresResultados(comboFormato.getSelectionModel().getSelectedItem().getIdFormatoTorneo(), "Ganan"));
            tablaClasificacion.sort((a, b) -> Integer.compare(b.getGanadas(), a.getGanadas()));

            for(int i = 1; i <= tablaClasificacion.size(); i++)
            {
                String puesto = i + "º Puesto";
                tablaClasificacion.get(i-1).setPuesto(puesto);
            }
        }
    }

    public void comprobarGuardado()
    {
        ArrayList<Clasificacion> listaGuardado = new ArrayList<>();
        listaGuardado.addAll(adminDaoClasificacion.cargarClasificacion(comboFormato.getSelectionModel().getSelectedItem().getIdFormatoTorneo()));

        for(int i = 1; i <= tablaClasificacion.size(); i++)
        {
            if(listaGuardado.isEmpty())
            {
                adminDaoClasificacion.agregarClasificacion(tablaClasificacion.get(i-1));
            }
            else if(listaGuardado.get(i-1).getId_jugador() != tablaClasificacion.get(i-1).getId_jugador())
            {
                adminDaoClasificacion.agregarClasificacion(tablaClasificacion.get(i-1));
            }
        }
    }
}
