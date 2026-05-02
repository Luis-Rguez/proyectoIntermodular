package org.example.torneoajedrez.DataSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;
import org.example.torneoajedrez.controller.VentanasController;
import org.example.torneoajedrez.dao.Admin.*;
import org.example.torneoajedrez.model.*;

import java.sql.SQLException;

@Getter
@Setter

public class DatosAdmin {


    private static ObservableList<Torneo> listaTorneo = FXCollections.observableArrayList();
    private static ObservableList<Staff> listaStaff = FXCollections.observableArrayList();
    private static ObservableList<Jugador> listaJugadores = FXCollections.observableArrayList();

    private static AdminDaoStaff adminDaoStaff = new AdminDaoStaff();
    private static AdminDaoTorneos adminDaoTorneos = new AdminDaoTorneos();
    private static AdminDaoFormatos adminDaoFormatos = new AdminDaoFormatos();
    private static AdminDaoPartidas adminDaoPartidas = new AdminDaoPartidas();
    private static AdminDaoJugadores adminDaoJugadores = new AdminDaoJugadores();

    // ---------------------- Staff -----------------------------------------------------------------------------------
    public static boolean agregarStaff(Usuario usuario)
    {
        return listaStaff.add((Staff) usuario);
    }

    public static void editarStaff(Usuario usuario) throws SQLException
    {
        adminDaoStaff.editarStaff((Staff) usuario);
    }

    public static boolean borrarStaff(Usuario usuario)
    {
        if(adminDaoStaff.borrarStaff((Staff) usuario))
        {
            listaStaff.remove(usuario);
            return true;
        }
        return false;
    }

    public static ObservableList<Staff> filtrarUsuarioPorTorneo(int filtro)
    {
        listaStaff = adminDaoStaff.filtroTorneoStaff(filtro);
        return listaStaff;
    }

    public static ObservableList<Staff> getListaStaff() {

        if(listaTorneo.isEmpty())
        {
            listaTorneo.add(new Torneo("Torneos", 0));
            listaTorneo.addAll(adminDaoTorneos.cargarTorneoIdNom());
        }

        if (listaStaff.isEmpty())
        {
            listaStaff.setAll(adminDaoStaff.cargarStaff());
        }
        return listaStaff;
    }

    public static Torneo busquedaTorneo (int idTorneo)
    {
        return listaTorneo.stream().filter(item -> item.getIdTorneo() == idTorneo).findFirst().orElse(null);
    }

    // ---------------------- JUGADORES -----------------------------------------------------------------------------------

    public static ObservableList<Jugador> getListaJugadores() {

        if(listaTorneo.isEmpty())
        {
            listaTorneo.add(new Torneo("Torneos", 0));
            listaTorneo.addAll(adminDaoTorneos.cargarTorneoIdNom());
        }
        if (listaJugadores.isEmpty())
        {
            listaJugadores.setAll(adminDaoJugadores.cargarJugadores());
        }

        return listaJugadores;
    }

    public static boolean borrarJugador(Jugador jugador)
    {
        if(adminDaoJugadores.borarJugadorFormato(jugador))
        {
            if(adminDaoJugadores.borrarJugador(jugador))
            {
                listaJugadores.remove(jugador);
                return true;
            }
        }
        return false;
    }

    public static boolean agregarJugador(Jugador jugador)
    {
        return listaJugadores.add(jugador);
    }

    // ---------------------- PARTIDAS -----------------------------------------------------------------------------------

    public static ObservableList<Torneo> cargarTorneosPartidas()
{
    listaTorneo = adminDaoTorneos.cargarTorneoIdNom();

    for(int i=0; i<listaTorneo.size(); i++)
    {
        listaTorneo.get(i).setFormatoTorneo(adminDaoFormatos.cargarFormatoTorneo(listaTorneo.get(i).getIdTorneo()));

        for(int j=0; j<listaTorneo.get(i).getFormatoTorneo().size(); j++)
        {
            // Cargamos primero la partida de blancas y luego negras y añadimos al jugador negro y su resultado
            listaTorneo.get(i).getFormatoTorneo().get(j).setListaPartidas
                    (adminDaoPartidas.cargarPartidas(listaTorneo.get(i).getFormatoTorneo().get(j).getIdFormatoTorneo(), "blancas"));

            for(int z=0; z < listaTorneo.get(i).getFormatoTorneo().get(j).getListaPartidas().size(); z++)
            {
               ObservableList<Partida> negras = FXCollections.observableArrayList();
                negras.setAll(adminDaoPartidas.cargarPartidas(listaTorneo.get(i).getFormatoTorneo().get(j).getIdFormatoTorneo(), "negras"));
                listaTorneo.get(i).getFormatoTorneo().get(j).getListaPartidas().get(z).setNegras(negras.get(z).getNegras());
                listaTorneo.get(i).getFormatoTorneo().get(j).getListaPartidas().get(z).setResulNegras(negras.get(z).getResulNegras());
            }
        }
    }
    return listaTorneo;
}

    public static void agregarPartida(Partida partida, int idArbitro, int idBlancas, int idNegras)
{
    try
    {
        adminDaoPartidas.agregarPartida(partida, idArbitro);

        //Obtenemos el idPartida de la tabla Partidas que acabamos de crear para usartla en la tabla de Juegan
        int idPartida = adminDaoPartidas.ultimoIDPartida();

        // ingresamos registro para el jugador de blancas
        adminDaoPartidas.agregarJuegan(idBlancas, idPartida, "Blancas", partida.getResulBlancas());

        // ingresamos registro para el jugador de negras
        adminDaoPartidas.agregarJuegan(idNegras, idPartida, "Negras", partida.getResulNegras());

    } catch(SQLException e) {
        VentanasController ventana = new VentanasController();
        ventana.ventanaError("No se ha podido Agregar la Partida \n\nError: \n" + e.getMessage());
    }
}

    public static ObservableList<Staff> staffTorneoActivo(int idTorneo, String rol)
{
    return adminDaoStaff.filtroTorneoArbitro(idTorneo, rol);
}

    public static void borrarPartida(int partida)
    {
        adminDaoPartidas.borrarEmparejamiento(partida);
        cargarTorneosPartidas();
    }


    // ---------------------- TORNEOS -----------------------------------------------------------------------------------
    public static void vaciarLista()
    {
        listaStaff.clear();
        listaTorneo.clear();
        listaJugadores.clear();
        listaJugadores.clear();
    }

    public static ObservableList<Torneo> getListaTorneo() {
        return listaTorneo;
    }
}
