package org.example.torneoajedrez.dao.Admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.torneoajedrez.controller.VentanasController;
import org.example.torneoajedrez.database.ConexionBBDD;
import org.example.torneoajedrez.database.DBSchem;
import org.example.torneoajedrez.model.Formato;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDaoFormatos {

    private Connection connection;

    private PreparedStatement preparedStatement;

    private ResultSet resultSet;

    public ObservableList<Formato> cargarFormatoTorneo(int idTorneo)
    {
        ObservableList<Formato> formatoTorneo = FXCollections.observableArrayList();

        connection = ConexionBBDD.getConnection();

        String query = String.format("SELECT %s, %s, %s FROM %s WHERE %s = ?",

                DBSchem.ID_TORNEO,DBSchem.ID_FORMATO_TORNEO, DBSchem.COL_CATEGORIA, DBSchem.TAB_FORMATO_TORNEO,
                DBSchem.ID_TORNEO);

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idTorneo);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                String categoria = resultSet.getString(DBSchem.COL_CATEGORIA);
                int id = resultSet.getInt(DBSchem.ID_TORNEO);
                int idFormato = resultSet.getInt(DBSchem.ID_FORMATO_TORNEO);

                formatoTorneo.add(new Formato(categoria,id, idFormato));
            }

        } catch (SQLException e) {
            VentanasController ventana = new VentanasController();
            ventana.ventanaError("No se ha podido Realizar la Consulta \n\nErro: \n" + e.getMessage());
        }

        return formatoTorneo;
    }
}
