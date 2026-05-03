package org.example.torneoajedrez.controller.admin;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.torneoajedrez.controller.VentanasController;
import org.example.torneoajedrez.dao.Admin.AdminDaoTorneos;
import org.example.torneoajedrez.model.Formato;
import org.example.torneoajedrez.model.Torneo;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TorneosController implements Initializable {

    @FXML
    private Button btnSalir, btnBorrar, btnNuevoFormato, btnRegistrar;

    @FXML
    private TextField editFechaFin, editFechaInicio, editFormatoTorneo, editNombre;

    @FXML
    private TableView<Torneo> tablaViewTorneo;
    private ObservableList<Torneo> tablaTorneo;

    @FXML
    private TableColumn<Torneo, String> colFechaFin, colFormato, colNombre, colInicio;

    // Item Menu
    @FXML
    private MenuItem menuItemTorneo, menuItemCerrarSesion, menuItemSalir;

    // Item Registros
    @FXML
    private MenuItem menuItemJugadores, menuItemStaff;

    // Item Partidas
    @FXML
    private MenuItem menuItemOrganizar, menuItemClasificacion;

    private String pathLogin;
    private VentanasController ventana;
    private Torneo torneo;
    private ObservableList<Formato> listFormato;
    private AdminDaoTorneos adminDaoTorneos;
    private ArrayList<Formato> listaFormato;

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
        menuItemTorneo.setOnAction(event -> ventana.abrirVentanas(btnSalir,"admin/adminTorneo-view.fxml", "Registro de Torneos", true));
        menuItemJugadores.setOnAction(event -> ventana.abrirVentanas(btnSalir,"admin/adminUsuario-view.fxml", "Registro de Jugadores", true));
        menuItemStaff.setOnAction(event -> ventana.abrirVentanas(btnSalir,"admin/adminStaff-view.fxml", "Registro de Personal", true));
        menuItemOrganizar.setOnAction(event -> ventana.abrirVentanas(btnSalir,"admin/adminPartidas-view.fxml", "Organizar Partidas", true));
        menuItemClasificacion.setOnAction(event -> ventana.abrirVentanas(btnSalir,"admin/adminClasificacion-view.fxml", "Clasificacion", true));
        menuItemCerrarSesion.setOnAction(event ->
        {
            if(ventana.ventanaConfirmacion("¿Cerrar Sesion?", "¿Esta seguro de querer cesarr la sesion?"))
            {
                ventana.abrirVentanas(btnSalir,pathLogin, "Login", true);
            }
        });
        menuItemSalir.setOnAction(event -> ventana.cerrarVentana(btnSalir));

        btnSalir.setOnAction(event -> ventana.cerrarVentana(btnSalir));

        btnNuevoFormato.setOnAction(event ->
        {
            Formato nuevoFormato = new Formato(editFormatoTorneo.getText());
            listFormato.add(nuevoFormato);
            btnRegistrar.disableProperty().set(false);
        });

        btnRegistrar.setOnAction(event ->
        {
            if(editNombre.getText().isEmpty())
            {
                ventana.ventanaWarning("Añade Torneo", "Primero ha indicar el nombre del Torneo");
            }else if(listFormato.isEmpty() && editFormatoTorneo.getText().isEmpty())
            {
                ventana.ventanaWarning("Añade Formato", "Ha de añadie almenos un Formato");
            } else
            {
                registrarPartida();
                int idTorneo = adminDaoTorneos.cargarIDTorneoNuevo();
                listFormato.forEach(formato -> adminDaoTorneos.agregarFormato(formato, idTorneo));
            }
            btnRegistrar.disableProperty().set(true);
            vaciarCampos();
            cargarTabla();
        });

        btnBorrar.setOnAction(event ->
        {
            if(!tablaViewTorneo.getSelectionModel().isEmpty())
            {
                if(ventana.ventanaConfirmacion("Quiere Borrar", "¿Esta seguro de borar?\n\nTenga en cuenta que si el un torneo ha comenzado no se podra borrar."))
                {
                    int jugadores = adminDaoTorneos.numerosJugadores(tablaViewTorneo.getSelectionModel().getSelectedItem().getIdFormato());

                    if(jugadores == 0)
                    {
                        adminDaoTorneos.borrarFormato(tablaViewTorneo.getSelectionModel().getSelectedItem().getIdFormato());
                        tablaTorneo.remove(tablaViewTorneo.getSelectionModel().getSelectedItem());
                    }else
                    {
                        ventana.ventanaInformation("Primer Paso", "Para eliminar esta modalidad primero tiene que eliminar los registros de los jugadores.");
                    }
                }
            }
            vaciarCampos();
        });
    }

    private void cargarTabla()
    {
        tablaTorneo.setAll(adminDaoTorneos.cargarTorneo());
    }

    private void registrarPartida()
    {
        if(editNombre.getText().isEmpty() || editFormatoTorneo.getText().isEmpty()
                || editFechaInicio.getText().isEmpty() || editFechaFin.getText().isEmpty())
        {
            ventana.ventanaWarning("Datos", "¡Confirma que todos los Datos están Rellenos!");

        } else
        {
            torneo.setNombre(editNombre.getText());
            torneo.setFormatoTorneo(listFormato);
            torneo.setFechaInicio(editFechaInicio.getText());
            torneo.setFechaFin(editFechaFin.getText());

            adminDaoTorneos.agregarTorneo(torneo);

            vaciarCampos();
        }
    }

    private void vaciarCampos()
    {
        editNombre.setText("");
        editFormatoTorneo.setText("");
        editFechaInicio.setText("");
        editFechaFin.setText("");
    }

    private void initGUI()
    {
        tablaViewTorneo.setItems(tablaTorneo);

        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colFormato.setCellValueFactory(new PropertyValueFactory<>("formato"));
        colInicio.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        colFechaFin.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));
    }

    private void instancias()
    {
        pathLogin = "login-view.fxml";
        ventana = new VentanasController();
        torneo = new Torneo();
        listFormato = FXCollections.observableArrayList();

        tablaTorneo = FXCollections.observableArrayList();
        adminDaoTorneos = new AdminDaoTorneos();
        cargarTabla();
    }
}
