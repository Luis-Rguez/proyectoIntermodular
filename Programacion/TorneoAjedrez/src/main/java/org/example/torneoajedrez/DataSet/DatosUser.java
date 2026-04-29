package org.example.torneoajedrez.DataSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.torneoajedrez.dao.Usuario.UserDao;
import org.example.torneoajedrez.model.Torneo;

public class DatosUser {

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
}
