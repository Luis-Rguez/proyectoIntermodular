package org.example.torneoajedrez.dao.Usuario;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.torneoajedrez.controller.VentanasController;
import org.example.torneoajedrez.database.ConexionBBDD;
import org.example.torneoajedrez.database.DBSchem;
import org.example.torneoajedrez.model.Jugador;
import org.example.torneoajedrez.model.Torneo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    private Connection connection;

    private PreparedStatement preparedStatement;

    private ResultSet resultSet;

    public Jugador cargarUsuario(int idJugador)
    {
        Jugador jugador = new Jugador();

        connection = ConexionBBDD.getConnection();

        String query = String.format("SELECT * FROM %s WHERE %s = ?",
                DBSchem.TAB_JUGADORES, DBSchem.ID_JUGADOR);
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idJugador);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                jugador.setId(resultSet.getInt(DBSchem.ID_JUGADOR));
                jugador.setNombre(resultSet.getString(DBSchem.COL_NOMBRE_JUGADOR));
                jugador.setIdClub( resultSet.getInt(DBSchem.ID_CLUB));
                jugador.setTelf(resultSet.getString(DBSchem.COL_TELF));
                jugador.setMail(resultSet.getString(DBSchem.COL_EMAIL));
                jugador.setDni(resultSet.getString(DBSchem.COL_DNI));
                jugador.setPass(resultSet.getString(DBSchem.COL_PASS));
            }
        } catch (SQLException e) {
            VentanasController ventana = new VentanasController();
            ventana.ventanaError("No se ha podido Realizar la Consulta \n\nError: \n" + e.getMessage());
        }
        return jugador;
    }

    public ObservableList<Torneo> cargarTorneos(int idJugador)
    {
        ObservableList<Torneo> listaTorneo = FXCollections.observableArrayList();
/*
        connection = ConexionBBDD.getConnection();

        String query = String.format("SELECT %s, %s FROM %s WHERE %s = ?",
                DBSchem.TAB_JUGADORES, DBSchem.ID_JUGADOR);
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idJugador);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                //TODO

            }
        } catch (SQLException e) {
            VentanasController ventana = new VentanasController();
            ventana.ventanaError("No se ha podido Realizar la Consulta \n\nError: \n" + e.getMessage());
        }*/
        return listaTorneo;
    }
}
