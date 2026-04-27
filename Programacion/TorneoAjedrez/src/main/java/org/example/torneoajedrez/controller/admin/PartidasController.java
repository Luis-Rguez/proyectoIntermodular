package org.example.torneoajedrez.controller.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;
import org.example.torneoajedrez.DataSet.Datos;
import org.example.torneoajedrez.controller.VentanasController;
import org.example.torneoajedrez.dao.AdminDaoPartidas;
import org.example.torneoajedrez.model.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class PartidasController implements Initializable {

    // Item Menu
    @FXML
    private MenuItem menuItemTorneo, menuItemCerrarSesion, menuItemSalir;

    // Item Registros
    @FXML
    private MenuItem menuItemClubs, menuItemPatrocinador,menuItemJugadores, menuItemStaff;

    // Item Partidas
    @FXML
    private MenuItem menuItemOrganizar, menuItemVer;

    // Botones
    @FXML
    private Button btnBorrar, btnEmparejar, btnRegistrar, btnSalir, btnLimpiar;

    //Tabla
    @FXML
    private TableView<Partida> tableViewPartidas;
    private ObservableList<Partida> tablaPartidas;

    @FXML
    private TableColumn<Partida, String> colBlancas, colNegras;

    @FXML
    private TableColumn<Partida, String> colArbitro;

    @FXML
    private TableColumn<Partida, Integer> colMesa;

    // Combo
    @FXML
    private ComboBox<Staff> comboArbitro;
    private ObservableList<Staff> listaStaff;

    @FXML
    private ComboBox<Formato> comboFormato;
    private ObservableList<Formato> listaFormato;

    @FXML
    private ComboBox<Torneo> comboTorneo;
    private ObservableList<Torneo> listaTorneos;

    // Casillas
    @FXML
    private TextField editBlancas, editNegras;

    @FXML
    private Spinner<Integer> spinnerMesa;
    private SpinnerValueFactory.IntegerSpinnerValueFactory mesa;


    private String pathLogin;
    private VentanasController ventana;
    private ArrayList<Jugador> filtroJugador;

    private AdminDaoPartidas adminDaoPartidas;
    private Jugador jugadorBlancas;
    private Jugador jugadorNegras;

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
        btnSalir.setOnAction(event -> ventana.cerrarVentana(btnSalir));

        btnBorrar.setOnAction(event ->
        {
            Partida partida = tableViewPartidas.getSelectionModel().getSelectedItem();
            if(partida.getResulBlancas().equals("Pendiente"))
            {
                Datos.borrarPartida(partida.getId());
                tablaPartidas.remove(partida);
                desactivarBotonesLimpiar(true);
            } else
            {
                ventana.ventanaWarning("¡PARTIDA JUGADA!", "La Partida ya ha sido Jugada.\n\n¡NO SE PUEDE ELIMINAR!");
            }
        });

        btnLimpiar.setOnAction(event -> desactivarBotonesLimpiar(true));

        btnEmparejar.setOnAction(event ->
        {
            if(comboTorneo.getSelectionModel().getSelectedIndex() !=-1)
            {
                emparejar(comboFormato.getSelectionModel().getSelectedItem().getIdFormatoTorneo());
            } else
            {
                ventana.ventanaWarning("¡Cuidado!", "Seleccione un Torneo y un Formato de Torneo primero");
            }
        });

        btnRegistrar.setOnAction(event ->
                {
                    registrarPartida();
                    btnRegistrar.disableProperty().set(true);
                    editBlancas.setText("");
                    editNegras.setText("");
                });

        //---------------------ACIONES TABLA Y COMBO--------------------------------------------------------
        comboTorneo.setOnAction(event ->
        {
            int idTorneo = comboTorneo.getSelectionModel().getSelectedItem().getIdTorneo();
            int indicelista = comboTorneo.getSelectionModel().getSelectedIndex();

            listaStaff.setAll(Datos.staffTorneoActivo(idTorneo, "arbitro"));

            if(listaStaff.isEmpty())
            {
                ventana.ventanaWarning("¡Cuidado!",String.format("Para el Torneo %s tiene que contratar a Arbitros", listaTorneos.get(indicelista).getNombre()));
            }

            if(listaTorneos.get(indicelista).getFormatoTorneo().isEmpty())
            {
                ventana.ventanaWarning("¡Cuidado!",String.format("Para el Torneo %s tiene que crear un Formato de Competicion", listaTorneos.get(indicelista).getNombre()));
            }else
            {
                listaFormato.setAll(listaTorneos.get(indicelista).getFormatoTorneo());
                comboFormato.getSelectionModel().selectFirst();
            }
            if(listaStaff.isEmpty() || listaTorneos.get(indicelista).getFormatoTorneo().isEmpty())
            {
                btnRegistrar.disableProperty().set(true);
            }
        });

        comboFormato.setOnAction(event ->
        {
            int indicelista = comboFormato.getSelectionModel().getSelectedIndex();
            if((listaFormato.get(indicelista).getListaPartidas().isEmpty()))
            {
                tablaPartidas.clear();
            } else
            {
                tablaPartidas.setAll(comboFormato.getSelectionModel().getSelectedItem().getListaPartidas());
                tableViewPartidas.setItems(tablaPartidas);
                tableViewPartidas.getSelectionModel().clearSelection();
            }
        });

        tableViewPartidas.setOnMouseClicked(event ->
        {
            tableViewPartidas.getSelectionModel().select(tableViewPartidas.getFocusModel().getFocusedIndex());

            int index = tableViewPartidas.getSelectionModel().getSelectedIndex();

            if (index == -1) return;

            Partida partida = tableViewPartidas.getSelectionModel().getSelectedItem();
            System.out.println(partida.getBlancas());
            editBlancas.setText(partida.getBlancas());
            editNegras.setText(partida.getNegras());

            btnBorrar.disableProperty().set(false);
        });
    }

    private void initGUI()
    {
        spinnerMesa.setValueFactory(mesa);
        comboTorneo.setItems(listaTorneos);
        comboArbitro.setItems(listaStaff);
        comboFormato.setItems(listaFormato);
        tableViewPartidas.setItems(tablaPartidas);
        colBlancas.setCellValueFactory(new PropertyValueFactory<>("blancas"));
        colNegras.setCellValueFactory(new PropertyValueFactory<>("negras"));
        colMesa.setCellValueFactory(new PropertyValueFactory<>("mesa"));
        colArbitro.setCellValueFactory(new PropertyValueFactory<>("arbitro"));
    }

    private void instancias()
    {
        pathLogin = "login-view.fxml";
        ventana = new VentanasController();
        jugadorBlancas = new Jugador();
        jugadorNegras = new Jugador();
        adminDaoPartidas = new AdminDaoPartidas();
        filtroJugador = new ArrayList<>();
        tablaPartidas = FXCollections.observableArrayList();
        mesa = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,20,1,1);

        Datos.vaciarLista();

        listaStaff = FXCollections.observableArrayList();
        listaTorneos = Datos.cargarTorneosPartidas();
        listaFormato = FXCollections.observableArrayList();
    }

    private void desactivarBotonesLimpiar(boolean desactivar)
    {
        btnBorrar.disableProperty().set(desactivar);
        editBlancas.setText("");
        editNegras.setText("");
    }

    public void registrarPartida()
    {
        if(comboArbitro.getSelectionModel().isEmpty() || comboArbitro.getSelectionModel().isEmpty()
                || comboFormato.getSelectionModel().isEmpty() || editBlancas.getText().isEmpty()
                || editNegras.getText().isEmpty())
        {
            ventana.ventanaWarning("Faltan Datos", "Por favor, asegurese de que todos los campos estan rellenos");
        }else
        {
            int idFormato = comboFormato.getSelectionModel().getSelectedItem().getIdFormatoTorneo();
            int idArbitro = comboArbitro.getSelectionModel().getSelectedItem().getId();
            int mesa = spinnerMesa.getValue();

            String blancas = editBlancas.getText();
            String negras = editNegras.getText();
            String resultado = "Pendiente";
            Partida partida = new Partida(idFormato, blancas, negras, resultado, resultado, mesa);
            Datos.agregarPartida(partida, idArbitro, jugadorBlancas.getId(), jugadorNegras.getId());
            Datos.cargarTorneosPartidas();
            tablaPartidas.setAll(Datos.getListaTorneo().get(comboTorneo.getSelectionModel().getSelectedIndex()).getFormatoTorneo()
                                            .get(comboFormato.getSelectionModel().getSelectedIndex()).getListaPartidas());
            tableViewPartidas.refresh();
        }
    }

    public  void emparejar(int idFormato)
    {
        filtroJugador = adminDaoPartidas.jugadoresSinPartida(idFormato); //Retorna una lista de Jugadores sin Partidas

        if(filtroJugador.isEmpty())
        {
            ventana.ventanaInformation("Emparejamiento Completado", "Ya estan todos los emparejamientos posibles");
            return;
        } else
        {
            btnRegistrar.disableProperty().set(false);
        }

        if (filtroJugador.size() % 2 == 0)
        {
            if(!filtroJugador.isEmpty())
            {
                int numNegras;
                int numBlancas = (int)(Math.random() * filtroJugador.size());
                do
                {
                    numNegras = (int)(Math.random() * filtroJugador.size());
                }while(numBlancas == numNegras);

                editBlancas.setText(filtroJugador.get(numBlancas).getNombre());
                jugadorBlancas.setNombre(filtroJugador.get(numBlancas).getNombre());
                jugadorBlancas.setId(filtroJugador.get(numBlancas).getId());

                editNegras.setText(filtroJugador.get(numNegras).getNombre());
                jugadorNegras.setNombre(filtroJugador.get(numNegras).getNombre());
                jugadorNegras.setId(filtroJugador.get(numNegras).getId());
            }
        }else
        {
            VentanasController ventana = new VentanasController();
            ventana.ventanaWarning("¡Falta un Jugador!", "Para que el Torneo se pueda realizar tiene " +
                    "que dar de baja a un jugador o dar de alta.\n\n" +
                    "Actualmente son ¡IMPARES!");
        }
    }
}
