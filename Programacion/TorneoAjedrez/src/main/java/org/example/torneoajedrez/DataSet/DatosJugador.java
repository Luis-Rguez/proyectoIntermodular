package org.example.torneoajedrez.DataSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import org.example.torneoajedrez.dao.Usuario.UserDao;
import org.example.torneoajedrez.model.Torneo;

@Getter

public class DatosJugador {

    private static int idJugador;
    private static UserDao userDao = new UserDao();
    private static ObservableList<Torneo> listaTorneoJugador = FXCollections.observableArrayList();

    public static ObservableList<Torneo> filtroTorneoJugador(int idJugador)
    {
        listaTorneoJugador.setAll(userDao.cargarTorneos(idJugador));
        //listaTorneoJugador.setAll(userDao.cargarFormato(idJugador));
        //listaTorneoJugador.setAll(userDao.cargarPartidas(idJugador));
        //listaTorneoJugador.setAll(userDao.cargarMovimientos(idJugador));
        return listaTorneoJugador;
    }

    public static int getIdJugador() {return idJugador;}

    public static void setIdJugador(int idJugador) {DatosJugador.idJugador = idJugador;}
}
