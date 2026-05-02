package org.example.torneoajedrez.dao.Admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.torneoajedrez.controller.VentanasController;
import org.example.torneoajedrez.database.ConexionBBDD;
import org.example.torneoajedrez.database.DBSchem;
import org.example.torneoajedrez.model.Clasificacion;
import org.example.torneoajedrez.model.Formato;
import org.example.torneoajedrez.model.Torneo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AdminDaoClasificacion {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    private VentanasController ventana;

    public ArrayList<Torneo> cargarTorneo ()
    {
        ArrayList<Torneo> listaTorneos = new ArrayList<>();

        connection = ConexionBBDD.getConnection();

        String query = String.format("SELECT %s, %s from %s;",
                        DBSchem.ID_TORNEO, DBSchem.COL_NOMBRE, DBSchem.TAB_TORNEOS);

        try
        {
            preparedStatement = connection.prepareStatement(query);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {

                int idTorneo = resultSet.getInt(DBSchem.ID_TORNEO);
                String nombre = resultSet.getString(DBSchem.COL_NOMBRE);

                listaTorneos.add(new Torneo(idTorneo, nombre));
            }
        }catch (SQLException e)
        {
            ventana = new VentanasController();
            ventana.ventanaError("No se ha cargar los datos de los torneos. \n\nError: \n" + e.getMessage());
        }
        return listaTorneos;
    }

    public ArrayList<Formato> cargarFormatos (int idTorneo)
    {
        ArrayList<Formato> listaFormato = new ArrayList<>();

        connection = ConexionBBDD.getConnection();

        String query = String.format("SELECT %s, %s from %s\n" +
                                        "WHERE %s = ?;",
                DBSchem.ID_FORMATO_TORNEO, DBSchem.COL_CATEGORIA, DBSchem.TAB_FORMATO_TORNEO,
                DBSchem.ID_TORNEO);

        try
        {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idTorneo);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {

                int idFormato = resultSet.getInt(DBSchem.ID_FORMATO_TORNEO);
                String categoria = resultSet.getString(DBSchem.COL_CATEGORIA);

                listaFormato.add(new Formato(categoria, idTorneo, idFormato));
            }
        }catch (SQLException e)
        {
            ventana = new VentanasController();
            ventana.ventanaError("No se ha podido cargar los datos de los formatos. \n\nError: \n" + e.getMessage());
        }
        return listaFormato;
    }

    public ArrayList<Clasificacion> cargarClasificacion (int idFormato)
    {
        ArrayList<Clasificacion> listaClasificacion = new ArrayList<>();

        connection = ConexionBBDD.getConnection();

        String query = String.format("SELECT %s, %s from %s\n" +
                        "WHERE %s = ?;",
                DBSchem.ID_CLASIFICAION, DBSchem.ID_JUGADOR, DBSchem.TAB_CLASIFICACION,
                DBSchem.ID_FORMATO_TORNEO);

        try
        {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idFormato);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {

                int idClasificacion = resultSet.getInt(DBSchem.ID_CLASIFICAION);
                int idJugador = resultSet.getInt(DBSchem.ID_JUGADOR);

                listaClasificacion.add(new Clasificacion(idClasificacion, idFormato, idJugador));
            }
        }catch (SQLException e)
        {
            ventana = new VentanasController();
            ventana.ventanaError("No se ha podido cargar los datos de las clasificaciones. \n\nError: \n" + e.getMessage());
        }
        return listaClasificacion;
    }

    public ObservableList<Clasificacion> jugadoresResultados(int idFormato, String resultado)
    {
        ObservableList<Clasificacion> listaClasificacion = FXCollections.observableArrayList();

        connection = ConexionBBDD.getConnection();

        String query = String.format("SELECT j.%s, j.%s, SUM(CASE WHEN jg.%s = ? THEN 1 ELSE 0 END) AS %s FROM %s j\n" +
                        "INNER JOIN %s tj ON tj.%s = j.%s\n" +
                        "INNER JOIN %s p ON p.%s = tj.%s\n" +
                        "INNER JOIN %s jg ON jg.%s = p.%s AND jg.%s = j.%s\n" +
                        "WHERE tj.%s = ?\n" +
                        "GROUP BY j.%s, j.%s;\n",
                DBSchem.ID_JUGADOR, DBSchem.COL_NOMBRE_JUGADOR, DBSchem.COL_RESULTADO, "totalGanadas",
                DBSchem.TAB_JUGADORES,
                DBSchem.TAB_JUGADOR_FORMATO ,DBSchem.ID_JUGADOR,DBSchem.ID_JUGADOR,
                DBSchem.TAB_PARTIDAS, DBSchem.ID_FORMATO_TORNEO, DBSchem.ID_FORMATO_TORNEO,
                DBSchem.TAB_JUEGAN, DBSchem.ID_PARTIDA, DBSchem.ID_PARTIDA, DBSchem.ID_JUGADOR, DBSchem.ID_JUGADOR,
                DBSchem.ID_FORMATO_TORNEO,
                DBSchem.ID_JUGADOR, DBSchem.COL_NOMBRE_JUGADOR);
        try
        {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, resultado);
            preparedStatement.setInt(2, idFormato);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                int idJugador = resultSet.getInt(DBSchem.ID_JUGADOR);
                String nombre = resultSet.getString(DBSchem.COL_NOMBRE_JUGADOR);
                int ganadas = resultSet.getInt("totalGanadas");
                listaClasificacion.add(new Clasificacion(idFormato, idJugador,nombre, ganadas));

            }
        }catch (SQLException e)
        {
            ventana = new VentanasController();
            ventana.ventanaError("No se ha podido cargar los datos de los jugadores. \n\nError: \n" + e.getMessage());
        }
        return listaClasificacion;
    }

    public void agregarClasificacion(Clasificacion clasificacion)
    {
        connection = ConexionBBDD.getConnection();

        String query = String.format("INSERT INTO %s (%s, %s, %s) VALUES\n" +
                        "(?, ?, ?)",
                DBSchem.TAB_CLASIFICACION,
                DBSchem.ID_FORMATO_TORNEO, DBSchem.ID_JUGADOR, DBSchem.COL_PUESTO);

        try {
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, clasificacion.getId_tipoTorneo());
            preparedStatement.setInt(2,clasificacion.getId_jugador());
            preparedStatement.setString(3, clasificacion.getPuesto());


            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            ventana = new VentanasController();
            ventana.ventanaWarning("Error", "Ha ocurrido un error al registrar la clacificacion.\n\n" + e.getMessage());
        }
    }
}
