package org.example.torneoajedrez.controller.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;
import org.example.torneoajedrez.DataSet.DatosAdmin;
import org.example.torneoajedrez.controller.VentanasController;
import org.example.torneoajedrez.dao.Admin.AdminDaoFormatos;
import org.example.torneoajedrez.dao.Admin.AdminDaoPartidas;
import org.example.torneoajedrez.model.*;

import java.net.URL;
import java.sql.SQLException;
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
    private MenuItem menuItemOrganizar, menuItemVer, menuItemClasificacion;

    // Botones
    @FXML
    private Button btnBorrar, btnEmparejar, btnRegistrar, btnSalir, btnLimpiar, btnRonda;

    //Tabla
    @FXML
    private TableView<Partida> tableViewPartidas;
    private ObservableList<Partida> tablaPartidas;

    @FXML
    private TableColumn<Partida, String> colBlancas, colNegras, colArbitro;

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

    @FXML private ComboBox<Integer> comboRonda;
    private ObservableList<Integer> listaRonda;

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
    private AdminDaoFormatos adminDaoFormatos;
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
        btnSalir.setOnAction(event -> ventana.cerrarVentana(btnSalir));

        btnBorrar.setOnAction(event ->
        {
            Partida partida = tableViewPartidas.getSelectionModel().getSelectedItem();
            if(partida.getResulBlancas().equals("Pendiente"))
            {
                DatosAdmin.borrarPartida(partida.getId());
                tablaPartidas.remove(partida);
                tableViewPartidas.refresh();
                desactivarBotonesLimpiar(true);
                cargarEmparejar();
            } else
            {
                ventana.ventanaWarning("¡PARTIDA JUGADA!", "La Partida ya ha sido Jugada.\n\n¡NO SE PUEDE ELIMINAR!");
            }
        });

        btnLimpiar.setOnAction(event -> desactivarBotonesLimpiar(true));

        btnEmparejar.setOnAction(event ->
        {
            cargarEmparejar();
            emparejar();
        });

        btnRegistrar.setOnAction(event ->
                {
                    registrarPartida();
                    cargarTabla();
                    btnRegistrar.disableProperty().set(true);
                    editBlancas.setText("");
                    editNegras.setText("");
                });

        btnRonda.setOnAction(event ->
        {
            if (tablaPartidas.isEmpty())
            {
                ventana.ventanaWarning("Seleciona Torneo", "Debe Selecionar Primero un Torneo y un Formato");
                return;
            }else if(comboRonda.getSelectionModel().getSelectedIndex() < listaRonda.size() -1)
            {
                ventana.ventanaWarning("¡Cuidado Partida Nueva!", "Ya hay una Partida Posterior");
            }

            for(Partida partida : tablaPartidas)
            {
                if(partida.getResulBlancas().equals("Pendiente") || partida.getResulNegras().equals("Pendiente"))
                {
                    ventana.ventanaWarning("Faltan Partidas","Aun no se han terminado de jugar todas las Partidas de esta Ronda");
                    return;
                }
            }

            listaRonda.add(listaRonda.getLast() + 1);
            comboRonda.getSelectionModel().selectLast();
            filtroJugador.clear();

            desactivarBotonesLimpiar(true);
            cargarTabla();
        });

        //---------------------ACIONES TABLA Y COMBO--------------------------------------------------------
        comboTorneo.setOnAction(event ->
        {
            if(!comboTorneo.getSelectionModel().isEmpty())
            {
                int idTorneo = comboTorneo.getSelectionModel().getSelectedItem().getIdTorneo();
                int indicelista = comboTorneo.getSelectionModel().getSelectedIndex();
                listaStaff.setAll(DatosAdmin.staffTorneoActivo(idTorneo, "arbitro"));
                listaRonda.add(1);
                comboRonda.getSelectionModel().selectFirst();

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
                    if(!listaFormato.isEmpty())
                    {
                        comboFormato.getSelectionModel().selectFirst();
                    }else
                    {
                       // listaFormato.setAll(adminDaoFormatos.cargarFormatoTorneo(comboTorneo.getSelectionModel().getSelectedItem().getIdTorneo()));
                    }
                }
                if(listaStaff.isEmpty() || listaTorneos.get(indicelista).getFormatoTorneo().isEmpty())
                {
                    btnRegistrar.disableProperty().set(true);
                }
                cargarTabla();
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
                int mayorRonda = adminDaoPartidas.totalRondas
                                    (comboFormato.getSelectionModel().getSelectedItem().getIdFormatoTorneo());
                listaRonda.clear();
                for(int i=0; i < mayorRonda; i++)
                {
                    listaRonda.add(i+1);
                }
                comboRonda.getSelectionModel().selectLast();
            }
        });

        comboRonda.setOnAction(event ->
        {
            cargarTabla();
        });

        tableViewPartidas.setOnMouseClicked(event ->
        {
            tableViewPartidas.getSelectionModel().select(tableViewPartidas.getFocusModel().getFocusedIndex());

            int index = tableViewPartidas.getSelectionModel().getSelectedIndex();

            if (index == -1) return;

            Partida partida = tableViewPartidas.getSelectionModel().getSelectedItem();
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
        comboRonda.setItems(listaRonda);
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
        adminDaoFormatos = new AdminDaoFormatos();
        filtroJugador = new ArrayList<>();
        tablaPartidas = FXCollections.observableArrayList();
        mesa = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,50,1,1);

        DatosAdmin.vaciarLista();

        listaStaff = FXCollections.observableArrayList();
        listaRonda = FXCollections.observableArrayList();
        listaTorneos = DatosAdmin.cargarTorneosPartidas();
        listaFormato = FXCollections.observableArrayList();
    }

    private void cargarTabla()
    {
        tablaPartidas.clear();

        if(!comboFormato.getSelectionModel().isEmpty())
        {
            if (!comboRonda.getSelectionModel().isEmpty())
            {
                comboFormato.getSelectionModel().getSelectedItem().getListaPartidas().forEach(item ->
                {
                    if (Integer.valueOf(item.getRonda()).equals(comboRonda.getSelectionModel().getSelectedItem()))
                    {
                        tablaPartidas.add(item);
                    }
                });

                tableViewPartidas.setItems(tablaPartidas);
                tableViewPartidas.refresh();
                tableViewPartidas.getSelectionModel().clearSelection();
            }
        }
    }

    private void desactivarBotonesLimpiar(boolean desactivar)
    {
        btnBorrar.disableProperty().set(desactivar);
        editBlancas.setText("");
        editNegras.setText("");
    }

    public void registrarPartida()
    {
        if(comboTorneo.getSelectionModel().isEmpty() || comboArbitro.getSelectionModel().isEmpty()
                || comboFormato.getSelectionModel().isEmpty() || editBlancas.getText().isEmpty()
                || editNegras.getText().isEmpty() || comboRonda.getSelectionModel().getSelectedIndex() == -1)
        {
            ventana.ventanaWarning("Faltan Datos", "Por favor, asegurese de que todos los campos estan rellenos");
        }else
        {
            int idFormato = comboFormato.getSelectionModel().getSelectedItem().getIdFormatoTorneo();
            int idArbitro = comboArbitro.getSelectionModel().getSelectedItem().getId();
            int ronda = comboRonda.getSelectionModel().getSelectedItem();
            int numMesa = spinnerMesa.getValue();

            String blancas = editBlancas.getText();
            String negras = editNegras.getText();
            String resultado = "Pendiente";
            Partida partida = new Partida(idFormato, blancas, negras, resultado, resultado, numMesa, ronda);
            DatosAdmin.agregarPartida(partida, idArbitro, jugadorBlancas.getId(), jugadorNegras.getId());

            partida.setIdArbitro(idArbitro);
            partida.setArbitro(comboArbitro.getSelectionModel().getSelectedItem().toString());
            partida.setIdBlancas(jugadorBlancas.getId());
            partida.setIdNegras(jugadorNegras.getId());

            listaFormato.get(comboFormato.getSelectionModel().getSelectedIndex()).getListaPartidas().add(partida);
            mesa.increment(+1);
        }
    }

    public  void emparejar()
    {
        if(filtroJugador.isEmpty())
        {
            ventana.ventanaInformation("Emparejamiento Completado", "Ya estan todos los emparejamientos posibles.\n\n Puede Empezar la siguiente Ronda.");
            return;
        } else
        {
            btnRegistrar.disableProperty().set(false);
        }

        if(filtroJugador.size() == 1)
        {
            ventana.ventanaInformation("No hay Partidas","Ya se genero la final.\n\n No se pueden generar mas partidas.");
        }else if (comprobarMultiplo())
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

                comboArbitro.getSelectionModel().select((int)(Math.random() * listaStaff.size()));
            }
        }else
        {
            ventana = new VentanasController();
            int faltante = numJugadoresFaltantes(filtroJugador.size()) - filtroJugador.size();
            int sobrante = -1 * (filtroJugador.size() - numJugadoresFaltantes(filtroJugador.size()));

            ventana.ventanaWarning("¡Faltan Jugadores!", "Para que el Torneo se pueda realizar tiene " +
                    "que dar de baja a " + sobrante + " o dar de alta " + faltante  + " jugadores.\n\n" +
                    "Actualmente no es un multiplo adecuado para este formato.");

            btnRegistrar.disableProperty().set(true);
            desactivarBotonesLimpiar(true);
        }
    }

    private boolean comprobarMultiplo()
    {
        int numJugadores = adminDaoPartidas.totalJugadoresFormato(comboFormato.getSelectionModel()
                .getSelectedItem().getIdFormatoTorneo());

        if (comprobarPotencia(numJugadores))
        {
            return true;

        } else
        {
            return false;
        }
    }

    public boolean comprobarPotencia(int numJugadores)
    {
        if(numJugadores >= 1)
        {
            while (numJugadores % 4 == 0)
            {
                numJugadores /= 4;
            }
        }

        if(numJugadores == 1)
        {
            return true;
        }
        return false;
    }

    public int numJugadoresFaltantes(int numJugadores)
    {
        int potencia = 1;

        while (potencia < numJugadores)
        {
            potencia *= 2;
        }
        return potencia;
    }

    public void cargarEmparejar()
    {
        if(comboTorneo.getSelectionModel().getSelectedIndex() !=-1)
        {
            if(comboRonda.getSelectionModel().getSelectedItem() <= 1)
            {
                filtroJugador = adminDaoPartidas.jugadoresSinPartida(comboFormato.getSelectionModel().getSelectedItem()
                        .getIdFormatoTorneo());

            }else if(comboRonda.getSelectionModel().getSelectedItem() > 1)
            {
                filtroJugador = adminDaoPartidas.cargarGanadores(comboFormato.getSelectionModel().getSelectedItem()
                        .getIdFormatoTorneo(), listaRonda.getLast()-1);
            }
        } else
        {
            ventana.ventanaWarning("¡Cuidado!", "Seleccione un Torneo y un Formato de Torneo primero");
        }
    }
}
