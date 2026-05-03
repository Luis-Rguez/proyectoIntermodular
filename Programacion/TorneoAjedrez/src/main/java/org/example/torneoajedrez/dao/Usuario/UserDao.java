package org.example.torneoajedrez.dao.Usuario;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.torneoajedrez.DataSet.DatosJugador;
import org.example.torneoajedrez.controller.VentanasController;
import org.example.torneoajedrez.database.ConexionBBDD;
import org.example.torneoajedrez.database.DBSchem;
import org.example.torneoajedrez.model.Clasificacion;
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

    public ObservableList<Clasificacion> cargarClasificacion()
    {
        ObservableList<Clasificacion> listaClasificacion = FXCollections.observableArrayList();

        connection = ConexionBBDD.getConnection();

        String query = String.format("SELECT t.%s, ft.%s, c.%s,\n" +
                        "SUM(CASE WHEN jg.%s = ? THEN 1 ELSE 0 END) AS %s,\n" +
                        "SUM(CASE WHEN jg.%s = ? THEN 1 ELSE 0 END) AS %s, \n" +
                        "SUM(CASE WHEN jg.%s = ? THEN 1 ELSE 0 END) AS %s FROM %s j\n" +
                        "INNER JOIN %s tj ON tj.%s = j.%s\n" +
                        "INNER JOIN %s ft ON ft.%s = tj.%s\n" +
                        "INNER JOIN %s c ON c.%s = j.%s AND c.%s = ft.%s\n" +
                        "INNER JOIN %s t ON t.%s = ft.%s\n" +
                        "INNER JOIN %s jg ON jg.%s = j.%s\n" +
                        "WHERE j.%s = ?\n" +
                        "GROUP BY t.%s, ft.%s, c.%s;",
                DBSchem.COL_NOMBRE, DBSchem.COL_CATEGORIA, DBSchem.COL_PUESTO,
                DBSchem.COL_RESULTADO, "Ganadas",
                DBSchem.COL_RESULTADO, "Perdidas",
                DBSchem.COL_RESULTADO, "Tablas", DBSchem.TAB_JUGADORES,
                DBSchem.TAB_JUGADOR_FORMATO, DBSchem.ID_JUGADOR, DBSchem.ID_JUGADOR,
                DBSchem.TAB_FORMATO_TORNEO, DBSchem.ID_FORMATO_TORNEO, DBSchem.ID_FORMATO_TORNEO,
                DBSchem.TAB_CLASIFICACION, DBSchem.ID_JUGADOR, DBSchem.ID_JUGADOR, DBSchem.ID_FORMATO_TORNEO, DBSchem.ID_FORMATO_TORNEO,
                DBSchem.TAB_TORNEOS, DBSchem.ID_TORNEO, DBSchem.ID_TORNEO,
                DBSchem.TAB_JUEGAN, DBSchem.ID_JUGADOR, DBSchem.ID_JUGADOR,
                DBSchem.ID_JUGADOR,
                DBSchem.COL_NOMBRE,DBSchem.COL_CATEGORIA, DBSchem.COL_PUESTO);
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "Ganan");
            preparedStatement.setString(2, "Pierden");
            preparedStatement.setString(3, "Tablas");
            preparedStatement.setInt(4, DatosJugador.getIdJugador());

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                String torneo = resultSet.getString(DBSchem.COL_NOMBRE);
                String formato = resultSet.getString(DBSchem.COL_CATEGORIA);
                String puesto = resultSet.getString(DBSchem.COL_PUESTO);
                int ganadas = resultSet.getInt("Ganadas");
                int perdidas = resultSet.getInt("Perdidas");
                int tablas = resultSet.getInt("Tablas");
                listaClasificacion.add(new Clasificacion(torneo, formato, puesto, ganadas, perdidas, tablas));
            }
        } catch (SQLException e) {
            VentanasController ventana = new VentanasController();
            ventana.ventanaError("No se ha podido Realizar la Consulta \n\nError: \n" + e.getMessage());
        }
        return listaClasificacion;
    }

    public void actualizarDatos(Jugador jugador)
    {
        connection = ConexionBBDD.getConnection();

        String query = String.format("UPDATE %s SET %s = ?, %s = ?, %s = ?, %s = ?, %s = ?  WHERE %s = ?;",
                DBSchem.TAB_JUGADORES,
                DBSchem.COL_DNI, DBSchem.COL_EMAIL, DBSchem.COL_TELF, DBSchem.COL_PASS, DBSchem.COL_NOMBRE_JUGADOR,
                DBSchem.ID_JUGADOR);

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, jugador.getDni());
            preparedStatement.setString(2, jugador.getMail());
            preparedStatement.setString(3, jugador.getTelf());
            preparedStatement.setString(4, jugador.getPass());
            preparedStatement.setString(5, jugador.getNombre());
            preparedStatement.setInt(6, DatosJugador.getIdJugador());

            int actualizado =  preparedStatement.executeUpdate();

        } catch (SQLException e) {
            VentanasController ventana = new VentanasController();
            ventana.ventanaError("No se ha podido Realizar la Consulta \n\nError: \n" + e.getMessage());
        }
    }

    public void baja()
    {
        connection = ConexionBBDD.getConnection();

        String query = String.format("UPDATE %s SET %s = ?, %s = ?, %s = ?, %s = ?  WHERE %s = ?;",
                DBSchem.TAB_JUGADORES,
                DBSchem.COL_DNI, DBSchem.COL_EMAIL, DBSchem.COL_TELF, DBSchem.COL_PASS,
                DBSchem.ID_JUGADOR);

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "xxxxxx");
            preparedStatement.setString(2, "xxxxxx");
            preparedStatement.setString(3, "xxxxxx");
            preparedStatement.setString(4, "xxxxxx");
            preparedStatement.setInt(5, DatosJugador.getIdJugador());

            int eliminado =  preparedStatement.executeUpdate();


        } catch (SQLException e) {
            VentanasController ventana = new VentanasController();
            ventana.ventanaError("No se ha podido Realizar la Consulta \n\nError: \n" + e.getMessage());
        }
    }
}
