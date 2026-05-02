package org.example.torneoajedrez.dao;

import org.example.torneoajedrez.controller.VentanasController;
import org.example.torneoajedrez.database.ConexionBBDD;
import org.example.torneoajedrez.database.DBSchem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDao {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    private VentanasController ventana;

    public int getLogin(String mail, String pass, String tabla, String id)
    {
        int idUsuario = 0;

        String query = String.format("SELECT %s FROM %s WHERE %s = ? AND %s = ?;",
                id, tabla, DBSchem.COL_EMAIL, DBSchem.COL_PASS);

        connection = ConexionBBDD.getConnection();

        try{

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, mail);
            preparedStatement.setString(2, pass);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                idUsuario = resultSet.getInt(id);
            }
        } catch (SQLException e) {
            ventana = new VentanasController();
            ventana.ventanaError("Se ha Producido un Error: \n\n" + e.getMessage());
        }
        return idUsuario;
    }
}
