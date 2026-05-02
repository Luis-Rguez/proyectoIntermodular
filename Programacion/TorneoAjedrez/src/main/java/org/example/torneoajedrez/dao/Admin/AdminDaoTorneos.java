package org.example.torneoajedrez.dao.Admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.torneoajedrez.controller.VentanasController;
import org.example.torneoajedrez.database.ConexionBBDD;
import org.example.torneoajedrez.database.DBSchem;
import org.example.torneoajedrez.model.Torneo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDaoTorneos {

    private Connection connection;

    private PreparedStatement preparedStatement;

    private ResultSet resultSet;

    public ObservableList<Torneo> cargarTorneoIdNom()
    {
        ObservableList<Torneo> listaTorneo = FXCollections.observableArrayList();
        connection = ConexionBBDD.getConnection();

        String query = String.format("SELECT %s, %s FROM %s", DBSchem.ID_TORNEO, DBSchem.COL_NOMBRE, DBSchem.TAB_TORNEOS);

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
            ventana.ventanaError("No se ha podido Realizar la Consulta \n\nErro: \n" + e.getMessage());
        }
        return listaTorneo;
    }
}
