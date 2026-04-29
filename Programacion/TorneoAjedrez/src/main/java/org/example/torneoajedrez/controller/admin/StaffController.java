package org.example.torneoajedrez.controller.admin;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.torneoajedrez.DataSet.DatosAdmin;
import org.example.torneoajedrez.controller.VentanasController;
import org.example.torneoajedrez.dao.Admin.AdminDaoStaff;
import org.example.torneoajedrez.model.Staff;
import org.example.torneoajedrez.model.Torneo;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class StaffController implements Initializable {

    // Item Menu
    @FXML
    private MenuItem menuItemTorneo, menuItemCerrarSesion, menuItemSalir;

    // Item Registros
    @FXML
    private MenuItem menuItemClubs, menuItemPatrocinador,menuItemJugadores, menuItemStaff;

    // Item Partidas
    @FXML
    private MenuItem menuItemOrganizar, menuItemVer;

    @FXML
    private Button btnBusqueda, btnEditar, btnRegistrar, btnBorrar, btnCancelar, btnSalir;

    @FXML
    private ComboBox<Torneo> comboTorneo;
    private ObservableList<Torneo> comboFiltro;

    @FXML
    private TableView<Staff> tableViewStaff;

    @FXML
    private TableColumn<Staff, String> colNombre, colApellido, colCuenta, colDNI;

    @FXML
    private TableColumn<Staff, String> colMail, colTelf, colRol;

    @FXML
    private TextField editCuenta, editSalario, editBuscar, editTelf, editDNI;

    @FXML
    private TextField editEdad, editMail, editNombre, editApellido, editPass;

    @FXML
    private ComboBox<Torneo> registroTorneo;

    @FXML
    private RadioButton radioArbitro, radioProduccion;
    private ToggleGroup grupoRol;

    private String pathLogin;
    private VentanasController ventana;
    private ObservableList<Torneo> listaTorneos;
    private FilteredList<Staff> listaFiltrada;
    private ObservableList<Staff> listaStaff;

    private AdminDaoStaff adminDaoStaff;

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
            Staff staff = new Staff();
            int idTorneo = registroTorneo.getSelectionModel().getSelectedItem().getIdTorneo();
            ingresarDatos(staff, idTorneo, false);
        });

        btnEditar.setOnAction(event ->
        {
            Staff staff = tableViewStaff.getSelectionModel().getSelectedItem();
            int idTorneo = registroTorneo.getSelectionModel().getSelectedItem().getIdTorneo();
            ingresarDatos(staff, idTorneo, true);
            tableViewStaff.refresh();
        });

        btnSalir.setOnAction(event -> ventana.cerrarVentana(btnSalir));

        btnBorrar.setOnAction(event ->
        {
            Staff staff = tableViewStaff.getSelectionModel().getSelectedItem();
            if(staff != null)
            {
                if(ventana.ventanaConfirmacion("Borrar Usuario", String.format("¿Esta Seguro de querer Borrar al usuario %s %s?", staff.getNombre(), staff.getApellido())))
                {
                    DatosAdmin.borrarUsuario(staff);
                    vaciarCampos();
                }
            }
        });

        btnCancelar.setOnAction(event -> vaciarCampos());

        btnBusqueda.setOnAction(event ->
        {
            editBuscar.clear();
            DatosAdmin.getListaStaff().clear();

            comboTorneo.getSelectionModel().selectFirst();
        });


        //---------------------ACIONES TABLA Y COMBO--------------------------------------------------------
        tableViewStaff.setOnMouseClicked(event->
        {
            Staff staff = tableViewStaff.getSelectionModel().getSelectedItem();
            cargarInfoUsuario(staff);
        });

        comboTorneo.setOnAction(event ->
        {
            if (comboTorneo.getSelectionModel().getSelectedIndex() != 0 && comboTorneo.getSelectionModel().getSelectedIndex() != -1) {
                int id = comboTorneo.getSelectionModel().getSelectedItem().getIdTorneo();

                if(id !=0)
                {
                    listaStaff.setAll(DatosAdmin.filtrarUsuarioPorTorneo(id));
                }

            } else
            {
                listaStaff.setAll(DatosAdmin.getListaStaff());
            }
        });

        editBuscar.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                listaFiltrada.setPredicate(producto -> producto.getNombre().contains(t1));// producto.getTitle().equalsIgnoreCase(t1)) qeu sean iguales
            }
        });
    }

    private void initGUI()
    {
        grupoRol.getToggles().addAll(radioProduccion, radioArbitro);
        registroTorneo.setItems(listaTorneos);
        registroTorneo.getSelectionModel().select(0);

        comboTorneo.setItems(comboFiltro);
        comboTorneo.getSelectionModel().select(0);

        tableViewStaff.setItems(listaFiltrada);
        colDNI.setCellValueFactory(new PropertyValueFactory<>("dni"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colTelf.setCellValueFactory(new PropertyValueFactory<>("telf"));
        colMail.setCellValueFactory(new PropertyValueFactory<>("mail"));
        colCuenta.setCellValueFactory(new PropertyValueFactory<>("cuenta"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
    }

    private void instancias()
    {
        pathLogin = "login-view.fxml";
        ventana = new VentanasController();
        adminDaoStaff = new AdminDaoStaff();
        grupoRol = new ToggleGroup();

        DatosAdmin.vaciarLista();

        listaStaff = DatosAdmin.getListaStaff();
        listaFiltrada = new FilteredList<>(listaStaff, staff -> true);

        listaTorneos = DatosAdmin.getListaTorneo();
        comboFiltro = DatosAdmin.getListaTorneo();
    }

    public void vaciarCampos()
    {
        editNombre.clear();
        editApellido.clear();
        editDNI.clear();
        editMail.clear();
        editPass.clear();
        editSalario.clear();
        editCuenta.clear();
        editTelf.clear();
        editEdad.clear();

        grupoRol.getSelectedToggle().setSelected(false);
        registroTorneo.getSelectionModel().select(0);
        btnEditar.disableProperty().set(true);
        btnRegistrar.disableProperty().set(false);
        btnCancelar.visibleProperty().set(false);
    }

    public void ingresarDatos(Staff staff,int idTorneo, boolean editado)
    {
        if(editNombre.getText().isEmpty() || editApellido.getText().isEmpty()
                || editTelf.getText().isEmpty() || editPass.getText().isEmpty()
                || editMail.getText().isEmpty() || grupoRol.getSelectedToggle() == null
                || editEdad.getText().isEmpty() || editCuenta.getText().isEmpty()
                || editDNI.getText().isEmpty()  || editSalario.getText().isEmpty())
        {
            ventana.ventanaWarning("Datos", "¡Confirma que todos los Datos están Rellenos!");

        } else
        {
            staff.setDni(editDNI.getText());
            staff.setNombre(editNombre.getText());
            staff.setApellido(editApellido.getText());
            staff.setTelf(editTelf.getText());
            staff.setMail(editMail.getText());
            staff.setEdad(Integer.parseInt(editEdad.getText()));
            staff.setCuenta(Long.parseLong(editCuenta.getText()));
            staff.setSalario(Double.parseDouble(editSalario.getText()));
            staff.setPass(editPass.getText());
            staff.setRol(((RadioButton) grupoRol.getSelectedToggle()).getText());

            if(!editado)
            {
                try
                {
                    DatosAdmin.agregarUsuario(adminDaoStaff.agregarUsuario(staff));
                    staff.setId(adminDaoStaff.idNuevoUsuario(Integer.parseInt(staff.getDni())));
                    if(idTorneo > 0)
                    {
                        adminDaoStaff.insertTorneoStaff(idTorneo, staff.getId());
                    }
                } catch (SQLException e)
                {
                    ventana.ventanaError("Error, Existe un usuario con ese DNI o Email\n" + e.getMessage());
                }
            } else
            {
                try
                {
                    DatosAdmin.editarUsuario(staff);
                    if(idTorneo >0)
                    {
                        adminDaoStaff.insertTorneoStaff(idTorneo, staff.getId());
                    }
                }catch (SQLException e)
                {
                    ventana.ventanaError("Error al editar al usuario "+staff.getNombre() + " " + staff.getApellido() + "\\n\\n" + e.getMessage());
                }
            }
            vaciarCampos();
        }
    }

    private void cargarInfoUsuario(Staff staff)
    {
        btnEditar.disableProperty().set(false);
        btnRegistrar.disableProperty().set(true);
        btnCancelar.visibleProperty().set(true);

        editNombre.setText(staff.getNombre());
        editApellido.setText(staff.getApellido());
        editTelf.setText(staff.getTelf());
        editMail.setText(staff.getMail());
        editDNI.setText(staff.getDni());
        editEdad.setText(String.valueOf(staff.getEdad()));
        editCuenta.setText(String.valueOf(staff.getCuenta()));
        editSalario.setText(String.valueOf(staff.getSalario()));
        editPass.setText(staff.getPass());
        if(staff.getRol().equals("Arbitro"))
        {
            radioArbitro.setSelected(true);
        }else
        {
            radioProduccion.setSelected(true);
        }

        int idTorneo = adminDaoStaff.selectTorneoStaff(staff.getId());
        registroTorneo.getSelectionModel().select(DatosAdmin.busquedaTorneo(idTorneo).getIdTorneo());
    }
}
