package org.example.torneoajedrez.controller.staff;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import org.example.torneoajedrez.DataSet.DatosAdmin;
import org.example.torneoajedrez.controller.VentanasController;
import org.example.torneoajedrez.dao.Admin.AdminDaoPartidas;
import org.example.torneoajedrez.model.Jugador;
import org.example.torneoajedrez.model.Partida;

public class StaffController implements Initializable {

    @FXML
    private Button btnFinPartida, btnMovimiento, btnSalir;

    @FXML
    private TableView<Partida> tablaViewMovimientos;
    private ObservableList<Partida> tablaPartidas;

    @FXML
    private TableColumn<Partida, String> colBlancas, colNegras;

    @FXML
    private ComboBox<Partida> comboPartidas;
    private ObservableList<Partida> listaPartida;

    @FXML
    private TextField editMovBlancas, editMovNegras;

    @FXML
    private Text texJugadorN, textJugadorB, textBlancas, textNegras;

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
    }

    private void initGUI()
    {
    }

    private void instancias()
    {
        pathLogin = "login-view.fxml";
        ventana = new VentanasController();
        jugadorBlancas = new Jugador();
        jugadorNegras = new Jugador();
        rondaTerminada = false;
        adminDaoPartidas = new AdminDaoPartidas();
        filtroJugador = new ArrayList<>();
        tablaPartidas = FXCollections.observableArrayList();

        DatosAdmin.vaciarLista();

        listaPartida = FXCollections.observableArrayList();
    }
}
