package org.example.torneoajedrez.dao.Admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.torneoajedrez.controller.VentanasController;
import org.example.torneoajedrez.database.ConexionBBDD;
import org.example.torneoajedrez.database.DBSchem;
import org.example.torneoajedrez.model.Formato;
import org.example.torneoajedrez.model.Torneo;

import java.sql.*;
import java.util.ArrayList;

public class AdminDaoTorneos {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    private VentanasController ventana;

    public ObservableList<Torneo> cargarTorneoIdNom()
    {
        ObservableList<Torneo> listaTorneo = FXCollections.observableArrayList();
        connection = ConexionBBDD.getConnection();

        String query = String.format("SELECT %s, %s FROM %s;", DBSchem.ID_TORNEO, DBSchem.COL_NOMBRE, DBSchem.TAB_TORNEOS);

        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {

                String nombre = resultSet.getString(DBSchem.COL_NOMBRE);
                int id = resultSet.getInt(DBSchem.ID_TORNEO);

                listaTorneo.add(new Torneo(nombre,id));
            }

        } catch (SQLException e) {
            VentanasController ventana = new VentanasController();
            ventana.ventanaError("No se ha podido Realizar la Consulta \n\nError: \n" + e.getMessage());
        }
        return listaTorneo;
    }

    public void agregarTorneo(Torneo torneo)
    {
        connection = ConexionBBDD.getConnection();

        String query = String.format("INSERT INTO  %s (%s, %s, %s) VALUES (?, ?, ?);",
                DBSchem.TAB_TORNEOS, DBSchem.COL_NOMBRE, DBSchem.COL_INICIO, DBSchem.COL_FIN);

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, torneo.getNombre());
            preparedStatement.setDate(2, java.sql.Date.valueOf(torneo.getFechaInicio()));
            preparedStatement.setDate(3, java.sql.Date.valueOf(torneo.getFechaFin()));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            ventana = new VentanasController();
            ventana.ventanaError("Ha ocurrido un error. Posiblemente formato de fecha incorrecta.\n\n" + e.getMessage());
        }
    }

    public void agregarFormato(Formato formato, int idTorneo)
    {
        connection = ConexionBBDD.getConnection();

        String query = String.format("INSERT INTO  %s (%s, %s) VALUES (?, ?);",
                DBSchem.TAB_FORMATO_TORNEO, DBSchem.ID_TORNEO, DBSchem.COL_CATEGORIA);

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idTorneo);
            preparedStatement.setString(2, formato.getCategoria());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            ventana = new VentanasController();
            ventana.ventanaError("Ha ocurrido un error.\n\n" + e.getMessage());
        }
    }

    public int cargarIDTorneoNuevo()
    {
        int idTorneo =0;

        connection = ConexionBBDD.getConnection();

        String query = String.format("SELECT MAX(%s) AS %s FROM %s;", DBSchem.ID_TORNEO, "ID_Ultimo", DBSchem.TAB_TORNEOS);

        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next())
            {
                idTorneo = resultSet.getInt("Id_Ultimo");
            }

        } catch (SQLException e) {
            VentanasController ventana = new VentanasController();
            ventana.ventanaError("No se ha podido Realizar la Consulta \n\nErro: \n" + e.getMessage());
        }
        return idTorneo;
    }

    public ObservableList<Torneo> cargarTorneo() {
        ObservableList<Torneo> listaTorneos = FXCollections.observableArrayList();

        connection = ConexionBBDD.getConnection();

        String query = String.format("SELECT t.%s, t.%s, t.%s, t.%s, f.%s, f.%s FROM %s t\n" +
                        "INNER JOIN %s f ON f.%s = t.%s;",
                DBSchem.ID_TORNEO, DBSchem.COL_NOMBRE, DBSchem.COL_INICIO, DBSchem.COL_FIN,
                DBSchem.COL_CATEGORIA, DBSchem.ID_FORMATO_TORNEO,
                DBSchem.TAB_TORNEOS,
                DBSchem.TAB_FORMATO_TORNEO, DBSchem.ID_TORNEO, DBSchem.ID_TORNEO);

        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {

                String nombre = resultSet.getString(DBSchem.COL_NOMBRE);
                int idTorneo = resultSet.getInt(DBSchem.ID_TORNEO);
                String inicio = resultSet.getString(DBSchem.COL_INICIO);
                String fin = resultSet.getString(DBSchem.COL_FIN);
                String formato = resultSet.getString(DBSchem.COL_CATEGORIA);
                int idFormato = resultSet.getInt(DBSchem.ID_FORMATO_TORNEO);

                listaTorneos.add(new Torneo(idTorneo, nombre, inicio, fin, formato, idFormato));
            }

        } catch (SQLException e) {
            VentanasController ventana = new VentanasController();
            ventana.ventanaError("No se ha podido Realizar la Consulta \n\nError: \n" + e.getMessage());
        }
        return listaTorneos;
    }

    public void borrarFormato(int idFormato)
    {
        connection = ConexionBBDD.getConnection();
        String query = String.format("DELETE FROM %s WHERE %s = ?;",
                DBSchem.TAB_FORMATO_TORNEO, DBSchem.ID_FORMATO_TORNEO);

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idFormato);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            ventana = new VentanasController();
            ventana.ventanaError("Ha ocurrido un error.\n\n" + e.getMessage());
        }
    }

    public int numerosJugadores(int idFormato)
    {
        int numJugadores =0;

        connection = ConexionBBDD.getConnection();

        String query = String.format("SELECT COUNT(%s) AS %s FROM %s WHERE %s = ?;",
                DBSchem.ID_JUGADOR, "Num_Jugadores", DBSchem.TAB_JUGADOR_FORMATO, DBSchem.ID_FORMATO_TORNEO);

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idFormato);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                numJugadores = resultSet.getInt("Num_Jugadores");
            }

        } catch (SQLException e) {
            VentanasController ventana = new VentanasController();
            ventana.ventanaError("No se ha podido Realizar la Consulta \n\nError: \n" + e.getMessage());
        }
        return numJugadores;
    }
}
