package org.example.torneoajedrez.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.torneoajedrez.controller.VentanasController;
import org.example.torneoajedrez.database.ConexionBBDD;
import org.example.torneoajedrez.database.DBSchem;
import org.example.torneoajedrez.model.Partida;
import org.example.torneoajedrez.model.Staff;
import org.example.torneoajedrez.model.Torneo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class AdminDaoPartidas {

    private Connection connection;

    private PreparedStatement preparedStatement;

    private ResultSet resultSet;

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
            VentanasController ventana = new VentanasController();
            ventana.ventanaError("No se ha podido Realizar la Consulta \n\nErro: \n" + e.getMessage());
        }
        return listaPartidas;
    }

    public void agregarPartida(Partida partida, int idTorneo, int idArbitro)
    {
      /*  connection = ConexionBBDD.getConnection();

        String query = String.format("INSERT INTO %s (%s, %s, %s) VALUES\n" +
                                    "(?, ?, ?)",
                DBSchem.TAB_PARTIDAS,
                DBSchem.ID_STAFF, DBSchem.ID_FORMATO_TORNEO, DBSchem.COL_MESA);

        preparedStatement = connection.prepareStatement(query);

        preparedStatement.setInt(1, idArbitro);
        preparedStatement.setInt(2,partida.getId_formato());
        preparedStatement.setInt(3, partida.getMesa());

        preparedStatement.executeUpdate();

        agregarJuegan(partida);*/

    }

    private void agregarJuegan(Partida partida)
    {
       /* connection = ConexionBBDD.getConnection();

        String query = String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES\n" +
                        "(?, ?, ?, ?)",
                DBSchem.TAB_JUEGAN,
                DBSchem.ID_JUGADOR, DBSchem.ID_PARTIDA, DBSchem.COL_COLOR, DBSchem.COL_RESULTADO);

        preparedStatement = connection.prepareStatement(query);

         // Mirar Bien como pasar los datos que no tiene partida como id de jugador y color
        preparedStatement.setInt(1, );

        preparedStatement.setInt(2,partida.get());
        preparedStatement.setString(3, );
        preparedStatement.setString(4, partida.getResulBlancas());

        preparedStatement.executeUpdate();*/
    }
}
