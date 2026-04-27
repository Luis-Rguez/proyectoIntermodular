package org.example.torneoajedrez.dao;

import org.example.torneoajedrez.controller.VentanasController;
import org.example.torneoajedrez.database.ConexionBBDD;
import org.example.torneoajedrez.database.DBSchem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDaoJugadores {

    private Connection connection;

    private PreparedStatement preparedStatement;

    private ResultSet resultSet;

    public void insertJugadoresFormato(int idJugador, int idFormato)
    {
        connection = ConexionBBDD.getConnection();

        String query = String.format("INSERT INTO %s (%s, %s) VALUES\n" +
                        "(?, ?)",
                DBSchem.TAB_JUGADOR_FORMATO,
                DBSchem.ID_FORMATO_TORNEO, DBSchem.ID_JUGADOR);
        try
        {
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, idFormato);
            preparedStatement.setInt(2,idJugador);

            preparedStatement.executeUpdate();
        }catch (SQLException e)
        {
            VentanasController ventana = new VentanasController();
            ventana.ventanaError("No se ha podido Registrar al Jugador \n\nError: \n" + e.getMessage());
        }
    }
}
