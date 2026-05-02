package org.example.torneoajedrez.dao.Staff;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.torneoajedrez.controller.VentanasController;
import org.example.torneoajedrez.database.ConexionBBDD;
import org.example.torneoajedrez.database.DBSchem;
import org.example.torneoajedrez.model.Movimientos;
import org.example.torneoajedrez.model.Partida;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class StaffDao {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    private VentanasController ventana;

    public List<Partida> cargarPartidasStaff(int idStaff)
    {
        ObservableList<Partida> listaPartidas = FXCollections.observableArrayList();

        connection = ConexionBBDD.getConnection();

        String query = String.format("SELECT p.%s, p.%s, p.%s, p.%s, jb.%s as %s, jb.%s as %s, jn.%s as %s, jn.%s as %s FROM %s p\n" +
                        "INNER JOIN %s jgb ON p.%s = jgb.%s AND jgb.%s = ?\n" +
                        "INNER JOIN %s jb ON jb.%s = jgb.%s\n" +
                        "INNER JOIN %s jgn ON p.%s = jgn.%s AND jgn.%s = ?\n" +
                        "INNER JOIN %s jn ON jn.%s = jgn.%s\n" +
                        "INNER JOIN %s st ON st.%s = p.%s\n" +
                        "WHERE st.%s = ? AND jgn.%s = ? AND jgb.%s = ?;",
                DBSchem.ID_PARTIDA, DBSchem.COL_MESA, DBSchem.COL_RONDA, DBSchem.ID_FORMATO_TORNEO, DBSchem.COL_NOMBRE_JUGADOR,"Blancas", DBSchem.ID_JUGADOR, "idBlancas",
                DBSchem.COL_NOMBRE_JUGADOR, "Negras", DBSchem.ID_JUGADOR, "idNegras",
                DBSchem.TAB_PARTIDAS,
                DBSchem.TAB_JUEGAN, DBSchem.ID_PARTIDA, DBSchem.ID_PARTIDA, DBSchem.COL_COLOR,
                DBSchem.TAB_JUGADORES, DBSchem.ID_JUGADOR, DBSchem.ID_JUGADOR,
                DBSchem.TAB_JUEGAN, DBSchem.ID_PARTIDA, DBSchem.ID_PARTIDA, DBSchem.COL_COLOR,
                DBSchem.TAB_JUGADORES, DBSchem.ID_JUGADOR, DBSchem.ID_JUGADOR,
                DBSchem.TAB_STAFF, DBSchem.ID_STAFF, DBSchem.ID_STAFF,
                DBSchem.ID_STAFF, DBSchem.COL_RESULTADO, DBSchem.COL_RESULTADO);

        try {
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, "Blancas");
            preparedStatement.setString(2, "Negras");
            preparedStatement.setInt(3, idStaff);
            preparedStatement.setString(4,"Pendiente");
            preparedStatement.setString(5,"Pendiente");

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                int idPartida = resultSet.getInt(DBSchem.ID_PARTIDA);
                int idJugadorB = resultSet.getInt("idBlancas");
                int idJugadorN = resultSet.getInt("idNegras");
                int mesa = resultSet.getInt(DBSchem.COL_MESA);
                int ronda = resultSet.getInt(DBSchem.COL_RONDA);
                int idFormato = resultSet.getInt(DBSchem.ID_FORMATO_TORNEO);
                String nombreJugadorB = resultSet.getString("Blancas");
                String nombreJugadorN = resultSet.getString("Negras");

                listaPartidas.add(new Partida(idPartida, idJugadorB, nombreJugadorB, idJugadorN, nombreJugadorN, mesa, ronda, idFormato));
            }

        } catch (SQLException e) {
            ventana = new VentanasController();
            ventana.ventanaError("No se ha podido Realizar la Consulta \n\nErro: \n" + e.getMessage());
        }
        return listaPartidas;
    }

    public void insertarMovimientos(int idPartida, String movBlancas, String movNegras)
    {
        connection = ConexionBBDD.getConnection();

        String query = String.format("INSERT INTO  %s (%s, %s, %s) " +
                        "VALUES (?, ?, ?);",
                DBSchem.TAB_MOVIMIENTO,
                DBSchem.ID_PARTIDA, DBSchem.COL_BLANCAS, DBSchem.COL_NEGRAS);
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idPartida);
            preparedStatement.setString(2, movBlancas);
            preparedStatement.setString(3, movNegras);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            ventana.ventanaError("Ha Surgido un error al registrar El movimiento\n\n" + e.getMessage());
        }
    }

    public void actualizarResultado(int idPartida, int idJugador, String resultado)
    {
        connection = ConexionBBDD.getConnection();

        String query = String.format("UPDATE %s\n" +
                        "SET %s = ? WHERE %s = ? AND %s = ?;",
                DBSchem.TAB_JUEGAN,
                DBSchem.COL_RESULTADO, DBSchem.ID_PARTIDA, DBSchem.ID_JUGADOR);
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, resultado);
            preparedStatement.setInt(2, idPartida);
            preparedStatement.setInt(3, idJugador);


            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            ventana.ventanaError("Ha Surgido un error al registrar El movimiento\n\n" + e.getMessage());
        }
    }

    public ObservableList<Movimientos> cargarMovimientos (int idPartida)
    {
        ObservableList<Movimientos> listaMovimientos = FXCollections.observableArrayList();

        connection = ConexionBBDD.getConnection();

        String query = String.format("SELECT %s, %s FROM %s p\n" +
                        "WHERE %s = ?;",
                DBSchem.COL_BLANCAS, DBSchem.COL_NEGRAS,DBSchem.TAB_MOVIMIENTO,
                DBSchem.ID_PARTIDA);

        try {
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, idPartida);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                String movimientoBlancas = resultSet.getString(DBSchem.COL_BLANCAS);
                String movimientoNegras = resultSet.getString(DBSchem.COL_NEGRAS);

                listaMovimientos.add(new Movimientos(idPartida, movimientoBlancas, movimientoNegras));
            }

        } catch (SQLException e) {
            ventana = new VentanasController();
            ventana.ventanaError("No se ha podido Realizar la Consulta \n\nError: \n" + e.getMessage());
        }
        return listaMovimientos;
    }

}
