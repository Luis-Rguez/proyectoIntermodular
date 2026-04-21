package org.example.torneoajedrez.database;

import lombok.Getter;
import org.example.torneoajedrez.controller.VentanasController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Getter

public class ConexionBBDD {

    private static Connection connection;

    public  static Connection getConnection()
    {
        if(connection == null)
        {
            createConnection();
        }
        return connection;
    }

    private static void createConnection()
    {
        String user = "root";
        String pass = "admin";
        String url = "127.0.0.1";
        String port = "3306";
        String dbName = "torneo_ajedrez";

        // jdbc:mysql://127.0.0.1:3306/torneo_ajedrez
        String urrlJBDC = String.format("jdbc:mysql://%s:%s/%s", url, port, dbName);

        try {
            connection = DriverManager.getConnection(urrlJBDC, user, pass);
        } catch (SQLException e)
        {
            VentanasController ventanas = new VentanasController();
            ventanas.ventanaError("Error de Conexcion de tipo " + e.getMessage());
        }
    }
}
