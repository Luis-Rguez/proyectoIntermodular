package org.example.torneoajedrez.dao.Admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.torneoajedrez.controller.VentanasController;
import org.example.torneoajedrez.database.ConexionBBDD;
import org.example.torneoajedrez.database.DBSchem;
import org.example.torneoajedrez.model.Formato;
import org.example.torneoajedrez.model.Jugador;
import org.example.torneoajedrez.model.Staff;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDaoJugadores {

    private Connection connection;

    private PreparedStatement preparedStatement;

    private ResultSet resultSet;

    private VentanasController ventana;

    public ObservableList<Jugador> cargarJugadores()
    {
        ObservableList<Jugador> listaJugadores = FXCollections.observableArrayList();
        connection = ConexionBBDD.getConnection();

        String query = String.format("SELECT * FROM %s",
                                        DBSchem.TAB_JUGADORES);

        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                int id = resultSet.getInt(DBSchem.ID_JUGADOR);
                int idClub = resultSet.getInt(DBSchem.ID_CLUB);
                String nombre = resultSet.getString(DBSchem.COL_NOMBRE_JUGADOR);
                String dni = resultSet.getString(DBSchem.COL_DNI);
                String telf = resultSet.getString(DBSchem.COL_TELF);
                String pass = resultSet.getString(DBSchem.COL_PASS);
                String mail = resultSet.getString(DBSchem.COL_EMAIL);

                listaJugadores.add(new Jugador(id, idClub, nombre, dni, mail, telf, pass));
            }

        } catch (SQLException e) {
            VentanasController ventana = new VentanasController();
            ventana.ventanaError("No se ha podido Realizar la Consulta \n\nError: \n" + e.getMessage());
        }
        return listaJugadores;
    }

    public ObservableList<Jugador> cargarJugadoresFormato(int idFormato)
    {
        ObservableList<Jugador> listaJugadores = FXCollections.observableArrayList();
        connection = ConexionBBDD.getConnection();

        String query = String.format("SELECT %s, %s, %s, %s, %s, %s, %s FROM %s " +
                        "WHERE  %s IN " +
                        "(SELECT %s FROM %s " +
                        "WHERE %s = ?)",
                DBSchem.ID_JUGADOR, DBSchem.ID_CLUB, DBSchem.COL_NOMBRE_JUGADOR, DBSchem.COL_DNI, DBSchem.COL_TELF,
                DBSchem.COL_EMAIL, DBSchem.COL_PASS,
                DBSchem.TAB_JUGADORES,
                DBSchem.ID_JUGADOR,
                DBSchem.ID_JUGADOR,
                DBSchem.TAB_JUGADOR_FORMATO,
                DBSchem.ID_FORMATO_TORNEO);

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idFormato);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                int id = resultSet.getInt(DBSchem.ID_JUGADOR);
                int idClub = resultSet.getInt(DBSchem.ID_CLUB);
                String nombre = resultSet.getString(DBSchem.COL_NOMBRE_JUGADOR);
                String dni = resultSet.getString(DBSchem.COL_DNI);
                String telf = resultSet.getString(DBSchem.COL_TELF);
                String pass = resultSet.getString(DBSchem.COL_PASS);
                String mail = resultSet.getString(DBSchem.COL_EMAIL);

                listaJugadores.add(new Jugador(id, idClub, nombre, dni, mail, telf, pass));
            }

        } catch (SQLException e) {
            ventana = new VentanasController();
            ventana.ventanaError("No se ha podido Realizar la Consulta \n\nError: \n" + e.getMessage());
        }
        return listaJugadores;
    }

    public ObservableList<Jugador> cargarJugadoresTorneo(int idTorneo)
    {
        ObservableList<Jugador> listaJugadores = FXCollections.observableArrayList();
        connection = ConexionBBDD.getConnection();

        String query = String.format("SELECT j.%s, j.%s, j.%s, j.%s, j.%s, j.%s, j.%s FROM %s j\n" +
                        "INNER JOIN %s ins ON j.%s = ins.%s\n" +
                        "INNER JOIN %s ft ON ft.%s = ins.%s\n" +
                        "INNER JOIN %s t ON t.%s = ft.%s\n" +
                        "WHERE t.%s = ?;",
                DBSchem.ID_JUGADOR, DBSchem.ID_CLUB, DBSchem.COL_NOMBRE_JUGADOR, DBSchem.COL_DNI, DBSchem.COL_TELF,
                DBSchem.COL_EMAIL, DBSchem.COL_PASS,
                DBSchem.TAB_JUGADORES,
                DBSchem.TAB_JUGADOR_FORMATO, DBSchem.ID_JUGADOR, DBSchem.ID_JUGADOR,
                DBSchem.TAB_FORMATO_TORNEO,DBSchem.ID_FORMATO_TORNEO, DBSchem.ID_FORMATO_TORNEO,
                DBSchem.TAB_TORNEOS,DBSchem.ID_TORNEO, DBSchem.ID_TORNEO,
                DBSchem.ID_TORNEO);

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idTorneo);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                int id = resultSet.getInt(DBSchem.ID_JUGADOR);
                int idClub = resultSet.getInt(DBSchem.ID_CLUB);
                String nombre = resultSet.getString(DBSchem.COL_NOMBRE_JUGADOR);
                String dni = resultSet.getString(DBSchem.COL_DNI);
                String telf = resultSet.getString(DBSchem.COL_TELF);
                String pass = resultSet.getString(DBSchem.COL_PASS);
                String mail = resultSet.getString(DBSchem.COL_EMAIL);

                listaJugadores.add(new Jugador(id, idClub, nombre, dni, mail, telf, pass));
            }

        } catch (SQLException e) {
            ventana = new VentanasController();
            ventana.ventanaError("No se ha podido Realizar la Consulta \n\nError: \n" + e.getMessage());
        }
        return listaJugadores;
    }

    public boolean borarJugadorFormato(Jugador jugador)
    {
        connection = ConexionBBDD.getConnection();

        String query = String.format("DELETE FROM %s " +
                        "WHERE %s = ?",
                DBSchem.TAB_JUGADOR_FORMATO,
                DBSchem.ID_JUGADOR);
        try
        {
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, jugador.getId());

            preparedStatement.executeUpdate();

        } catch(SQLException e)
        {
            ventana = new VentanasController();
            ventana.ventanaError(String.format("Error al borrar al usuario %s %s\n\n %s",jugador.getNombre(), jugador.getApellido(), e.getMessage()));
            return false;
        }
        return true;
    }


    public boolean borrarJugador(Jugador jugador)
    {
        connection = ConexionBBDD.getConnection();

        String query = String.format("DELETE FROM %s " +
                        "WHERE %s = ?",
                DBSchem.TAB_JUGADORES,
                DBSchem.ID_JUGADOR);
        try
        {
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, jugador.getId());

            preparedStatement.executeUpdate();

        } catch(SQLException e)
        {
            ventana = new VentanasController();
            ventana.ventanaError(String.format("Error al borrar al usuario %s %s\n\n %s",jugador.getNombre(), jugador.getApellido(), e.getMessage()));
            return false;
        }
        return true;
    }

    public ObservableList<Formato> cargarFormato(int idTorneo)
    { ObservableList<Formato> listaFormatos = FXCollections.observableArrayList();
        connection = ConexionBBDD.getConnection();

        String query = String.format("SELECT %s, %s, %s FROM %s WHERE %s = ?",
                DBSchem.ID_TORNEO ,DBSchem.ID_FORMATO_TORNEO, DBSchem.COL_CATEGORIA,
                DBSchem.TAB_FORMATO_TORNEO,
                DBSchem.ID_TORNEO);

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idTorneo);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                int idFormato = resultSet.getInt(DBSchem.ID_FORMATO_TORNEO);
                String categoria = resultSet.getString(DBSchem.COL_CATEGORIA);

                listaFormatos.add(new Formato(categoria, idTorneo, idFormato));
            }

        } catch (SQLException e) {
            ventana.ventanaError("No se ha podido Realizar la Consulta \n\nError: \n" + e.getMessage());
        }
        return listaFormatos;
    }

    public Jugador registrarJugador (Jugador jugador) throws SQLException
    {
        connection = ConexionBBDD.getConnection();

        String query = String.format("INSERT INTO  %s (%s, %s, %s, %s, %s) " +
                        "VALUES (?, ?, ?, ?, ?);",
                DBSchem.TAB_JUGADORES,
                DBSchem.COL_NOMBRE_JUGADOR, DBSchem.COL_DNI, DBSchem.COL_TELF,
                DBSchem.COL_EMAIL, DBSchem.COL_PASS);

        preparedStatement = connection.prepareStatement(query);

        preparedStatement.setString(1, jugador.getNombre());
        preparedStatement.setString(2, jugador.getDni());
        preparedStatement.setString(3, jugador.getTelf());
        preparedStatement.setString(4, jugador.getMail());
        preparedStatement.setString(5, jugador.getPass());

        preparedStatement.executeUpdate();

        return jugador;
    }

    public int idNuevoJugador(String dni) throws SQLException
    {
        int idJugador =0;
        connection = ConexionBBDD.getConnection();

        String query = String.format("SELECT %s FROM %s " +
                        "WHERE %S = ?;",
                DBSchem.ID_JUGADOR, DBSchem.TAB_JUGADORES,
                DBSchem.COL_DNI);

        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, dni);

        resultSet = preparedStatement.executeQuery();

        while (resultSet.next())
        {
            idJugador = resultSet.getInt(DBSchem.ID_JUGADOR);
        }

        return idJugador;
    }

    public void insertFormatoJugador(int idFormato, int idJugador) throws SQLException
    {
        connection = ConexionBBDD.getConnection();

        String query = String.format("INSERT INTO  %s (%s, %s) " +
                        "VALUES (?, ?);",
                DBSchem.TAB_JUGADOR_FORMATO,
                DBSchem.ID_FORMATO_TORNEO, DBSchem.ID_JUGADOR);

        preparedStatement = connection.prepareStatement(query);

        preparedStatement.setInt(1, idFormato);
        preparedStatement.setInt(2, idJugador);

        preparedStatement.executeUpdate();
    }

    public void editarJugador(Jugador jugador) throws SQLException
    {
        connection = ConexionBBDD.getConnection();

        String query = String.format("UPDATE  %s SET %s = ?, %s = ?, %s = ?, %s = ?, %s = ? " +
                        "WHERE %s = ?;",
                DBSchem.TAB_JUGADORES,
                DBSchem.COL_NOMBRE_JUGADOR, DBSchem.COL_DNI, DBSchem.COL_TELF,
                DBSchem.COL_EMAIL, DBSchem.COL_PASS,
                DBSchem.COL_DNI);


        preparedStatement = connection.prepareStatement(query);

        preparedStatement.setString(1, jugador.getNombre());
        preparedStatement.setString(2, jugador.getDni());
        preparedStatement.setString(3, jugador.getTelf());
        preparedStatement.setString(4, jugador.getMail());
        preparedStatement.setString(5, jugador.getPass());
        preparedStatement.setString(6, jugador.getDni());

        preparedStatement.executeUpdate();
    }

    public int [] selectTorneoJugador(int idJugador)
    {
        int [] idTorFor = new int[2];

        connection = ConexionBBDD.getConnection();

        String query = String.format("SELECT f.%s, f.%s FROM %s f " +
                        "INNER JOIN %s fb ON f.%s = fb.%s " +
                        "WHERE fb.%s = ?;",
                DBSchem.ID_TORNEO, DBSchem.ID_FORMATO_TORNEO,
                DBSchem.TAB_FORMATO_TORNEO,
                DBSchem.TAB_JUGADOR_FORMATO,
                DBSchem.ID_FORMATO_TORNEO, DBSchem.ID_FORMATO_TORNEO,
                DBSchem.ID_JUGADOR);

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idJugador);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                idTorFor[0] = resultSet.getInt(DBSchem.ID_TORNEO);
                idTorFor[1] = resultSet.getInt(DBSchem.ID_FORMATO_TORNEO);
            }

        } catch (SQLException e) {
            ventana = new VentanasController();
            ventana.ventanaError("Error de consulta " + e.getMessage());
        }
        return idTorFor;
    }
}
