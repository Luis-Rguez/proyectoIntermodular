package org.example.torneoajedrez.controller.admin;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.torneoajedrez.DataSet.DatosAdmin;
import org.example.torneoajedrez.controller.VentanasController;
import org.example.torneoajedrez.dao.Admin.AdminDaoJugadores;
import org.example.torneoajedrez.model.Formato;
import org.example.torneoajedrez.model.Jugador;
import org.example.torneoajedrez.model.Staff;
import org.example.torneoajedrez.model.Torneo;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class JugadoresController implements Initializable {

    @FXML
    private Button btnRegistrar, btnBorrar, btnEditar, btnSalir, btnDatos;

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
    private TableView<Jugador> tableViewJugadores;

    @FXML
    private TableColumn<Jugador, String> colNombre, colDNI, colTelf, colMail;

    @FXML
    private TextField editBuscar;

    @FXML
    private TextField editTelf, editDNI, editMail, editNombre, editPass;

    @FXML
    private ComboBox<Torneo> registroTorneo, comboTorneo;
    private ObservableList<Torneo> listRegistroTorneo, listFiltroTorneo;

    @FXML
    private ComboBox<Formato> registroFormato, comboFormato;
    private ObservableList<Formato> listRegistroFormato, listFilTroFormato;

    private String pathLogin;
    private VentanasController ventana;
    private AdminDaoJugadores adminDaoJugadores;
    private FilteredList<Jugador> listaFiltrada;
    private ObservableList<Jugador> listaJugador;

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
        menuItemClubs.setOnAction(event -> ventana.abrirVentanas(btnSalir,"admin/adminClubs-view.fxml", "Registro de Club", true));
        menuItemJugadores.setOnAction(event -> ventana.abrirVentanas(btnSalir,"admin/adminUsuario-view.fxml", "Registro de Jugadores", true));
        menuItemStaff.setOnAction(event -> ventana.abrirVentanas(btnSalir,"admin/adminStaff-view.fxml", "Registro de Personal", true));
        menuItemPatrocinador.setOnAction(event -> ventana.abrirVentanas(btnSalir,"admin/adminPatrocinador-view.fxml", "Registro de Patrocinadores", true));
        menuItemOrganizar.setOnAction(event -> ventana.abrirVentanas(btnSalir,"admin/adminPartidas-view.fxml", "Organizar Partidas", true));
        menuItemVer.setOnAction(event -> ventana.abrirVentanas(btnSalir,"verPartidas-view.fxml", "Partidas", true));
        menuItemClasificacion.setOnAction(event -> ventana.abrirVentanas(btnSalir,"admin/adminClasificacion-view.fxml", "Clasificacion", true));

        menuItemCerrarSesion.setOnAction(event ->
        {
            if(ventana.ventanaConfirmacion("¿Cerrar Sesion?", "¿Esta seguro de querer cesarr la sesion?"))
            {
                ventana.abrirVentanas(btnSalir,pathLogin, "Login", true);
            }
        });
        menuItemSalir.setOnAction(event -> ventana.cerrarVentana(btnSalir));

        //-------------------------ACCIONES BOTONES----------------------------------------------------

        btnRegistrar.setOnAction(event ->
        {
            Jugador jugador = new Jugador();
            if(!registroFormato.getSelectionModel().isEmpty())
            {
                registrarJugador(jugador, registroFormato.getSelectionModel().getSelectedItem().getIdFormatoTorneo(), false);
                vaciarCampos();
            }
        });

        btnEditar.setOnAction(event ->
        {
            Jugador jugador = new Jugador();
            registrarJugador(jugador, registroFormato.getSelectionModel().getSelectedItem().getIdFormatoTorneo(), true);
        });

        btnBorrar.setOnAction(event ->
        {
            Jugador jugador = tableViewJugadores.getSelectionModel().getSelectedItem();
            if(jugador != null)
            {
                if(ventana.ventanaConfirmacion("Borrar Usuario", String.format("¿Esta Seguro de querer Borrar al usuario %s?", jugador.getNombre())))
                {
                    DatosAdmin.borrarJugador(jugador);
                    vaciarCampos();
                }
            }
        });

        btnDatos.setOnAction(event -> vaciarCampos());

        btnSalir.setOnAction(event -> ventana.cerrarVentana(btnSalir));

        editBuscar.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                listaFiltrada.setPredicate(producto -> producto.getNombre().contains(t1));// producto.getTitle().equalsIgnoreCase(t1)) qeu sean iguales
            }
        });

        //------------------------- COMBO BOX ----------------------------------------------------

        comboTorneo.setOnAction(event ->
        {
            if (comboTorneo.getSelectionModel().getSelectedIndex() != 0 && comboTorneo.getSelectionModel().getSelectedIndex() != -1) {
                listFilTroFormato.clear();
                listFilTroFormato.add(new Formato("Todos Los Formatos", 0));
                listFilTroFormato.addAll(adminDaoJugadores.cargarFormato(comboTorneo.getSelectionModel().getSelectedItem().getIdTorneo()));
                comboFormato.setItems(listFilTroFormato);
                comboFormato.getSelectionModel().select(1);
            }else
            {
                comboFormato.getSelectionModel().selectFirst();
                listaJugador.setAll(adminDaoJugadores.cargarJugadores());
            }
        });

        registroTorneo.setOnAction(event ->
        {
            if (registroTorneo.getSelectionModel().getSelectedIndex() != 0 && registroTorneo.getSelectionModel().getSelectedIndex() != -1) {
                listRegistroFormato.clear();
                listRegistroFormato.add(new Formato("Todos Los Formatos", 0));
                listRegistroFormato.addAll(adminDaoJugadores.cargarFormato(registroTorneo.getSelectionModel().getSelectedItem().getIdTorneo()));
                registroFormato.setItems(listRegistroFormato);
                registroFormato.getSelectionModel().select(0);
            }
        });

        comboFormato.setOnAction(event ->
        {
            if (comboTorneo.getSelectionModel().getSelectedIndex() != 0 && comboTorneo.getSelectionModel().getSelectedIndex() != -1) {

                if(comboFormato.getSelectionModel().getSelectedIndex() != 0 && comboFormato.getSelectionModel().getSelectedIndex() != -1)
                {
                    listaJugador.setAll(adminDaoJugadores.cargarJugadoresFormato(comboFormato.getSelectionModel().getSelectedItem().getIdFormatoTorneo()));
                }else
                {
                    listaJugador.setAll(adminDaoJugadores.cargarJugadoresTorneo(comboTorneo.getSelectionModel().getSelectedItem().getIdTorneo()));
                }

            } else
            {
                listaJugador.setAll(DatosAdmin.getListaJugadores());
            }
        });

        tableViewJugadores.setOnMouseClicked(event ->
        {
            Jugador jugador = tableViewJugadores.getSelectionModel().getSelectedItem();
            cargarInfoUsuario(jugador);
            btnBorrar.disableProperty().set(false);
            btnEditar.disableProperty().set(false);
        });
    }

    private void initGUI()
    {
        registroTorneo.setItems(listRegistroTorneo);
        comboTorneo.setItems(listFiltroTorneo);

        tableViewJugadores.setItems(listaFiltrada);
        colDNI.setCellValueFactory(new PropertyValueFactory<>("dni"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colTelf.setCellValueFactory(new PropertyValueFactory<>("telf"));
        colMail.setCellValueFactory(new PropertyValueFactory<>("mail"));
    }

    private void instancias()
    {
        pathLogin = "login-view.fxml";
        ventana = new VentanasController();
        adminDaoJugadores = new AdminDaoJugadores();

        DatosAdmin.vaciarLista();

        listRegistroTorneo = DatosAdmin.getListaTorneo();
        listFiltroTorneo = DatosAdmin.getListaTorneo();
        listRegistroFormato = FXCollections.observableArrayList();
        listFilTroFormato = FXCollections.observableArrayList();

        listaJugador = DatosAdmin.getListaJugadores();
        listaFiltrada = new FilteredList<>(listaJugador, jugador -> true);
    }

    public void registrarJugador(Jugador jugador,int idFormato, boolean editado)
    {
        if(editNombre.getText().isEmpty() || editTelf.getText().isEmpty() || editPass.getText().isEmpty()
                || editMail.getText().isEmpty() || editDNI.getText().isEmpty()
                || registroFormato.getSelectionModel().isEmpty() || registroFormato.getSelectionModel().isEmpty())
        {
            ventana.ventanaWarning("¡Faltan Datos!", "¡Confirma que todos los Datos están Rellenos!");

        } else
        {
            jugador.setDni(editDNI.getText());
            jugador.setNombre(editNombre.getText());
            jugador.setTelf(editTelf.getText());
            jugador.setMail(editMail.getText());
            jugador.setPass(editPass.getText());

            if(!editado)
            {
                try
                {
                    if(idFormato > 0)
                    {
                        DatosAdmin.agregarJugador(adminDaoJugadores.registrarJugador(jugador));
                        jugador.setId(adminDaoJugadores.idNuevoJugador(jugador.getDni()));
                        adminDaoJugadores.insertFormatoJugador(idFormato, jugador.getId());
                    }
                } catch (SQLException e)
                {
                    ventana.ventanaError("Error, Existe un usuario con ese DNI o Email\n" + e.getMessage());
                }
            } else
            {
                try
                {

                    adminDaoJugadores.editarJugador(jugador);
                }catch (SQLException e)
                {
                    ventana.ventanaError("Error al editar al usuario "+ jugador.getNombre() + "\\n\\n" + e.getMessage());
                }
            }
            vaciarCampos();
        }
    }

    public void vaciarCampos()
    {
        editNombre.clear();
        editDNI.clear();
        editMail.clear();
        editPass.clear();
        editTelf.clear();

        registroTorneo.getSelectionModel().select(0);
        btnEditar.disableProperty().set(true);
        btnBorrar.disableProperty().set(true);
        btnRegistrar.disableProperty().set(false);
    }

    private void cargarInfoUsuario(Jugador jugador)
    {
        btnEditar.disableProperty().set(false);
        btnRegistrar.disableProperty().set(true);


        editNombre.setText(jugador.getNombre());
        editTelf.setText(jugador.getTelf());
        editMail.setText(jugador.getMail());
        editDNI.setText(jugador.getDni());
        editPass.setText(jugador.getPass());

        int [] idTorFor = adminDaoJugadores.selectTorneoJugador(jugador.getId());
        registroTorneo.getSelectionModel().select(idTorFor[0]);
        registroFormato.getSelectionModel().select(idTorFor[1]);
    }
}
