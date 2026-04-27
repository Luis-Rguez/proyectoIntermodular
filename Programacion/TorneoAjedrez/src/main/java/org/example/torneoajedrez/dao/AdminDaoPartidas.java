package org.example.torneoajedrez.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.torneoajedrez.controller.VentanasController;
import org.example.torneoajedrez.database.ConexionBBDD;
import org.example.torneoajedrez.database.DBSchem;
import org.example.torneoajedrez.model.Jugador;
import org.example.torneoajedrez.model.Partida;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class AdminDaoPartidas {

    private Connection connection;

    private PreparedStatement preparedStatement;

    private ResultSet resultSet;

    private  VentanasController ventana;

    public ObservableList<Partida> cargarPartidas(int idFormato, String color)
    {
        ObservableList<Partida> listaPartidas = FXCollections.observableArrayList();

        connection = ConexionBBDD.getConnection();

        String query = String.format("SELECT p.%s, p.%s, jr.%s, st.%s, st.%s, j.%s, p.%s FROM %s jr\n" +
                        "INNER JOIN %s j ON j.%s = jr.%s\n" +
                        "INNER JOIN %s p ON p.%s = j.%s\n" +
                        "INNER JOIN %s st ON st.%s = p.%s\n" +
                        "INNER JOIN %s ft ON p.%s = ft.%s\n" +
                        "WHERE p.%s = ? AND j.%s = ?",
                DBSchem.ID_PARTIDA, DBSchem.ID_FORMATO_TORNEO, DBSchem.COL_NOMBRE_JUGADOR,
                DBSchem.COL_NOMBRE, DBSchem.COL_APELLIDO, DBSchem.COL_RESULTADO, DBSchem.COL_MESA,
                DBSchem.TAB_JUGADORES,
                DBSchem.TAB_JUEGAN, DBSchem.ID_JUGADOR, DBSchem.ID_JUGADOR,
                DBSchem.TAB_PARTIDAS, DBSchem.ID_PARTIDA, DBSchem.ID_PARTIDA,
                DBSchem.TAB_STAFF, DBSchem.ID_STAFF, DBSchem.ID_STAFF,
                DBSchem.TAB_FORMATO_TORNEO, DBSchem.ID_FORMATO_TORNEO, DBSchem.ID_FORMATO_TORNEO,
                DBSchem.ID_FORMATO_TORNEO,DBSchem.COL_COLOR);

        try {
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, idFormato);
            preparedStatement.setString(2,color);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {

                int idPartida = resultSet.getInt(DBSchem.ID_PARTIDA);
                int idFormtToeneo = resultSet.getInt(DBSchem.ID_FORMATO_TORNEO);
                String nombreJugador = resultSet.getString(DBSchem.COL_NOMBRE_JUGADOR);
                String nombreArbitro = resultSet.getString(DBSchem.COL_NOMBRE) + " " + resultSet.getString(DBSchem.COL_APELLIDO);
                String resultado = resultSet.getString(DBSchem.COL_RESULTADO);
                int mesa = resultSet.getInt(DBSchem.COL_MESA);

                if(color.equals("blancas"))
                {
                    listaPartidas.add(new Partida(idPartida, idFormtToeneo, nombreJugador, resultado, mesa, nombreArbitro));
                }else
                {
                    listaPartidas.add(new Partida(nombreJugador, resultado));
                }
            }

        } catch (SQLException e) {
            ventana = new VentanasController();
            ventana.ventanaError("No se ha podido Realizar la Consulta \n\nErro: \n" + e.getMessage());
        }
        return listaPartidas;
    }

    public void agregarPartida(Partida partida, int idArbitro) throws SQLException
    {
        connection = ConexionBBDD.getConnection();

        String query = String.format("INSERT INTO %s (%s, %s, %s) VALUES\n" +
                                    "(?, ?, ?)",
                DBSchem.TAB_PARTIDAS,
                DBSchem.ID_STAFF, DBSchem.ID_FORMATO_TORNEO, DBSchem.COL_MESA);

        preparedStatement = connection.prepareStatement(query);

        preparedStatement.setInt(1, idArbitro);
        preparedStatement.setInt(2,partida.getId_formato());
        preparedStatement.setInt(3, partida.getMesa());

        preparedStatement.executeUpdate();
    }

    public void borrarEmparejamiento(int idPartida)
    {
        connection = ConexionBBDD.getConnection();

        String query = String.format("DELETE FROM %s WHERE %s = ?",
                DBSchem.TAB_JUEGAN, DBSchem.ID_PARTIDA);

        try
        {
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, idPartida);

            preparedStatement.executeUpdate();

            borrarPartida(idPartida);
        } catch (SQLException e)
        {
            ventana = new VentanasController();
            ventana.ventanaError("No se ha podido borrar la partida\n\n" + e.getMessage());
        }

    }

    public void borrarPartida(int idPartida) throws SQLException
    {
        connection = ConexionBBDD.getConnection();

        String query = String.format("DELETE FROM %s WHERE %s = ?",
                DBSchem.TAB_PARTIDAS, DBSchem.ID_PARTIDA);

        preparedStatement = connection.prepareStatement(query);

        preparedStatement.setInt(1, idPartida);

        preparedStatement.executeUpdate();

    }

    public ArrayList<Jugador> jugadoresSinPartida(int idFormato)
    {
        ArrayList<Jugador> jugadoresSinPartida = new ArrayList<>();

        connection = ConexionBBDD.getConnection();

        String query = String.format("SELECT j.%s, j.%s FROM %s j\n" +
                        "INNER JOIN %s fj ON fj.%s = j.%s\n" +
                        "INNER JOIN %s ft ON ft.%s = fj.%s\n" +
                        "LEFT JOIN %s jgn ON jgn.%s = j.%s\n" +
                        "WHERE jgn.%s IS NULL AND ft.%s = ?;",
                DBSchem.ID_JUGADOR, DBSchem.COL_NOMBRE_JUGADOR, DBSchem.TAB_JUGADORES,
                DBSchem.TAB_JUGADOR_FORMATO, DBSchem.ID_JUGADOR, DBSchem.ID_JUGADOR,
                DBSchem.TAB_FORMATO_TORNEO, DBSchem.ID_FORMATO_TORNEO, DBSchem.ID_FORMATO_TORNEO,
                DBSchem.TAB_JUEGAN, DBSchem.ID_JUGADOR, DBSchem.ID_JUGADOR,

                DBSchem.ID_JUGADOR, DBSchem.ID_FORMATO_TORNEO);

        try
        {
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, idFormato);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {

                int idJugador = resultSet.getInt(DBSchem.ID_JUGADOR);
                String nombre = resultSet.getString(DBSchem.COL_NOMBRE_JUGADOR);

                jugadoresSinPartida.add(new Jugador(idJugador, nombre));
            }
        }catch (SQLException e)
        {
            ventana = new VentanasController();
            ventana.ventanaError("No se ha podido Emparejar Aleatoriamente \n\nError: \n" + e.getMessage());
        }
        return jugadoresSinPartida;
    }

    public int ultimoIDPartida () throws SQLException
    {
        int ulimoID = 0;

        connection = ConexionBBDD.getConnection();

        String query = String.format("SELECT MAX(%s) as %s FROM %s;",
                DBSchem.ID_PARTIDA, DBSchem.AS_ULTIMO_ID, DBSchem.TAB_PARTIDAS);

        preparedStatement = connection.prepareStatement(query);

        resultSet = preparedStatement.executeQuery();

        while (resultSet.next())
        {
            ulimoID = resultSet.getInt(DBSchem.AS_ULTIMO_ID);
        }
        return ulimoID;
    }

    public void agregarJuegan(int idJugador, int idPartida, String color, String resultado) throws SQLException
    {
        connection = ConexionBBDD.getConnection();

        String query = String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES\n" +
                        "(?, ?, ?, ?)",
                DBSchem.TAB_JUEGAN,
                DBSchem.ID_JUGADOR, DBSchem.ID_PARTIDA, DBSchem.COL_COLOR, DBSchem.COL_RESULTADO);

        preparedStatement = connection.prepareStatement(query);

        preparedStatement.setInt(1, idJugador);
        preparedStatement.setInt(2,idPartida);
        preparedStatement.setString(3,color);
        preparedStatement.setString(4,resultado);


        preparedStatement.executeUpdate();
    }
}
