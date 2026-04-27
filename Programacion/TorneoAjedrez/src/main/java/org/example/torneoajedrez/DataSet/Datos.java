package org.example.torneoajedrez.DataSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;
import org.example.torneoajedrez.controller.VentanasController;
import org.example.torneoajedrez.dao.AdminDaoFormatos;
import org.example.torneoajedrez.dao.AdminDaoPartidas;
import org.example.torneoajedrez.dao.AdminDaoStaff;
import org.example.torneoajedrez.dao.AdminDaoTorneos;
import org.example.torneoajedrez.model.*;

import java.sql.SQLException;

@Getter
@Setter

public class Datos {


    private static ObservableList<Torneo> listaTorneo = FXCollections.observableArrayList();
    private static ObservableList<Staff> listaStaff = FXCollections.observableArrayList();
    private static ObservableList<Jugador> listaJugadores = FXCollections.observableArrayList();

    private static AdminDaoStaff adminDaoStaff = new AdminDaoStaff();
    private static AdminDaoTorneos adminDaoTorneos = new AdminDaoTorneos();
    private static AdminDaoFormatos adminDaoFormatos = new AdminDaoFormatos();
    private static AdminDaoPartidas adminDaoPartidas = new AdminDaoPartidas();

    // ---------------------- USUARIOS -----------------------------------------------------------------------------------
    public static boolean agregarUsuario(Usuario usuario)
    {
        return listaStaff.add((Staff) usuario);
    }

    public static void editarUsuario(Usuario usuario) throws SQLException
    {
        adminDaoStaff.editarUsuario((Staff) usuario);
    }

    public static boolean borrarUsuario(Usuario usuario)
    {
        if(adminDaoStaff.borrarUsuario((Staff) usuario))
        {
            listaStaff.remove(usuario);
            return true;
        }
        return false;
    }

    public static ObservableList<Staff> filtrarUsuarioPorTorneo(int filtro)
    {
        listaStaff = adminDaoStaff.filtroTorneoUsuario(filtro);
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
            listaStaff.setAll(adminDaoStaff.cargarUsuarios());
        }
        return listaStaff;
    }

    public static Torneo busquedaTorneo (int idTorneo)
    {
        return listaTorneo.stream().filter(item -> item.getIdTorneo() == idTorneo).findFirst().orElse(null);
    }

    public static void vaciarLista()
    {
        listaStaff.clear();
        listaTorneo.clear();
        listaJugadores.clear();
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
            // Cargarmos primero la partida de blancas y luego negras y añadimos al jugador negro y su resultado
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
        adminDaoPartidas.agregarJuegan(idNegras, idPartida, "Negras", partida.getResulBlancas());

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

    public static ObservableList<Torneo> getListaTorneo() {
        return listaTorneo;
    }
}
