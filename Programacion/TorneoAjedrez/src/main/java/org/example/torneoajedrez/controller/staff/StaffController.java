package org.example.torneoajedrez.controller.staff;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import org.example.torneoajedrez.DataSet.DatosStaff;
import org.example.torneoajedrez.controller.VentanasController;
import org.example.torneoajedrez.dao.Staff.StaffDao;
import org.example.torneoajedrez.model.Movimientos;
import org.example.torneoajedrez.model.Partida;

public class StaffController implements Initializable {

    @FXML
    private Button btnFinPartida, btnMovimiento, btnCerrar, btnSalir;

    @FXML
    private TableView<Movimientos> tablaViewMovimientos;
    private ObservableList<Movimientos> tablaMovimientos;

    @FXML
    private TableColumn<Movimientos, String> colBlancas, colNegras;

    @FXML
    private ComboBox<Partida> comboPartidas;
    private ObservableList<Partida> listaPartida;

    @FXML
    private ComboBox<String> comboGanan;
    private ObservableList<String> listaGanan;

    @FXML
    private TextField editMovBlancas, editMovNegras;

    @FXML
    private Text textJugadorN, textJugadorB, textVS, textBlancas, textNegras;

    private VentanasController ventana;
    private StaffDao staffDao;
    private String pathLogin;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        instancias();
        initGUI();
        acciones();
    }

    private void acciones()
    {
        //------------------------- ACCIONES BOTONES ----------------------------------------------------
        btnSalir.setOnAction(event -> ventana.cerrarVentana(btnSalir));
        btnCerrar.setOnAction(event ->
        {
        if(ventana.ventanaConfirmacion("¿Cerrar Sesion?", "¿Esta seguro de querer cerrar la Sesion?"))
        {
            ventana.abrirVentanas(btnSalir,pathLogin, "Login", true);
        }
        });

        btnMovimiento.setOnAction(event ->
        {
            if((editMovBlancas.getText().isEmpty() && !editMovNegras.getText().isEmpty()
                    || (editMovNegras.getText().isEmpty() && editMovBlancas.getText().isEmpty())))
            {
                ventana.ventanaWarning("FALTA MOVIMIENTO", "Falta ingresar el movimiento de las Blancas");
            }else
            {
                staffDao.insertarMovimientos(comboPartidas.getSelectionModel().getSelectedItem().getId(), editMovBlancas.getText(), editMovNegras.getText());

                tablaMovimientos.add(new Movimientos(comboPartidas.getSelectionModel().getSelectedItem().getId(),
                        editMovBlancas.getText(), editMovNegras.getText()));

                editMovBlancas.setText("");
                editMovNegras.setText("");
            }
        });

        btnFinPartida.setOnAction(event ->
        {
            int idPartida = comboPartidas.getSelectionModel().getSelectedItem().getId();
            int idBlanca = comboPartidas.getSelectionModel().getSelectedItem().getIdBlancas();
            int idNegras = comboPartidas.getSelectionModel().getSelectedItem().getIdNegras();

            if (comboGanan.getSelectionModel().getSelectedIndex() ==-1)
            {
                ventana.ventanaWarning("FALTA RESULTADO", "Ha indicar el resultado de la partida.");
                return;
            }

            switch (comboGanan.getSelectionModel().getSelectedItem())
            {
                case "Tablas" ->
                {
                    staffDao.actualizarResultado(idPartida, idBlanca, comboGanan.getSelectionModel().getSelectedItem());
                    staffDao.actualizarResultado(idPartida, idNegras, comboGanan.getSelectionModel().getSelectedItem());
                }

                case "Blancas" ->
                {
                    staffDao.actualizarResultado(idPartida,idBlanca, "Ganan");
                    staffDao.actualizarResultado(idPartida,idNegras, "Pierden");
                }

                case "Negras" ->
                {
                    staffDao.actualizarResultado(idPartida,idNegras, "Ganan");
                    staffDao.actualizarResultado(idPartida,idBlanca, "Pierden");
                }
            }
            cargarPartidasListado();
            cargarTextosTabla();
        });

        //------------------------- COMBOS BOTONES ----------------------------------------------------
        comboPartidas.setOnAction(event ->
        {
            cargarTextosTabla();
        });

    }

    private void cargarTextosTabla()
    {
        if(!listaPartida.isEmpty())
        {
            textJugadorB.setText(comboPartidas.getSelectionModel().getSelectedItem().getBlancas());
            textJugadorN.setText(comboPartidas.getSelectionModel().getSelectedItem().getNegras());

            comboPartidas.getSelectionModel().selectFirst();
            tablaMovimientos.setAll(staffDao.cargarMovimientos(comboPartidas.getSelectionModel().getSelectedItem().getId()));
            tablaViewMovimientos.refresh();
        }else
        {
            textVS.setText("Ronda Finalizada");
            textJugadorB.visibleProperty().set(false);
            textJugadorN.visibleProperty().set(false);
            btnFinPartida.disableProperty().set(true);
            btnMovimiento.disableProperty().set(true);
            listaPartida.clear();
            tablaViewMovimientos.getItems().clear();
            tablaViewMovimientos.refresh();
        }
    }

    private void initGUI()
    {
        comboPartidas.setItems(listaPartida);
        comboPartidas.getSelectionModel().selectFirst();

        comboGanan.setItems(listaGanan);

        tablaViewMovimientos.setItems(tablaMovimientos);
        colBlancas.setCellValueFactory(new PropertyValueFactory<>("moveBlancas"));
        colNegras.setCellValueFactory(new PropertyValueFactory<>("moveNegras"));
        cargarTextosTabla();
    }

    private void instancias()
    {
        pathLogin = "login-view.fxml";
        ventana = new VentanasController();
        staffDao = new StaffDao();
        tablaMovimientos = FXCollections.observableArrayList();
        listaPartida = FXCollections.observableArrayList();
        listaGanan = FXCollections.observableArrayList("Blancas", "Negras", "Tablas");

        cargarPartidasListado();
    }

    private void cargarPartidasListado()
    {
        DatosStaff.partidaPendientes();
        listaPartida.setAll(DatosStaff.getListaPartidas());
    }
}
