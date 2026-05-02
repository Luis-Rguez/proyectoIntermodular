package org.example.torneoajedrez.controller.staff;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import org.example.torneoajedrez.DataSet.DatosAdmin;
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
    private Text textJugadorN, textJugadorB, textVS;

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
            int idBlancas = comboPartidas.getSelectionModel().getSelectedItem().getIdBlancas();
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
                    staffDao.actualizarResultado(idPartida, idBlancas, comboGanan.getSelectionModel().getSelectedItem());
                    staffDao.actualizarResultado(idPartida, idNegras, comboGanan.getSelectionModel().getSelectedItem());
                    partidaDesempate(idBlancas, idNegras);
                    instancias();
                    initGUI();
                }

                case "Blancas" ->
                {
                    staffDao.actualizarResultado(idPartida,idBlancas, "Ganan");
                    staffDao.actualizarResultado(idPartida,idNegras, "Pierden");
                }

                case "Negras" ->
                {
                    staffDao.actualizarResultado(idPartida,idNegras, "Ganan");
                    staffDao.actualizarResultado(idPartida,idBlancas, "Pierden");
                }
            }
        });

        //------------------------- COMBOS BOTONES ----------------------------------------------------
        comboPartidas.setOnAction(event ->
        {
            cargarTextosTabla();
        });
    }

    private void partidaDesempate(int idBlancas, int idNegras)
    {
        int idFormato = comboPartidas.getSelectionModel().getSelectedItem().getId_formato();
        int mesa = comboPartidas.getSelectionModel().getSelectedItem().getMesa();
        int ronda = comboPartidas.getSelectionModel().getSelectedItem().getMesa();
        int idPartida = comboPartidas.getSelectionModel().getSelectedItem().getId();
        String nomBlancas = comboPartidas.getSelectionModel().getSelectedItem().getBlancas();
        String nomNegras = comboPartidas.getSelectionModel().getSelectedItem().getNegras();

        Partida partida = new Partida(idPartida, idBlancas, nomBlancas, idNegras, nomNegras, mesa, ronda, idFormato);
        partida.setResulBlancas("Pendiente");
        partida.setResulNegras("Pendiente");

        // Para el desempate ahora las blancas juegan con negras y las negras con blancas, por ese en el campo de id Blancas
        // poner el idNegras al llamar a la funcion de DatosAdmin para crear una nueva partida
        DatosAdmin.agregarPartida(partida,DatosStaff.getIdStaff(),idNegras, idBlancas);
    }

    private void cargarTextosTabla()
    {
        if(!listaPartida.isEmpty())
        {
            if(comboPartidas.getSelectionModel().getSelectedItem() != null)
            {
                textJugadorB.setText(comboPartidas.getSelectionModel().getSelectedItem().getBlancas());
                textJugadorN.setText(comboPartidas.getSelectionModel().getSelectedItem().getNegras());

                comboPartidas.getSelectionModel().selectFirst();
                tablaMovimientos.setAll(staffDao.cargarMovimientos(comboPartidas.getSelectionModel().getSelectedItem().getId()));
                tablaViewMovimientos.refresh();
            }

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
