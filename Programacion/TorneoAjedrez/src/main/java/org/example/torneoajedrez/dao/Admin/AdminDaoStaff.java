package org.example.torneoajedrez.dao.Admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.torneoajedrez.controller.VentanasController;
import org.example.torneoajedrez.database.ConexionBBDD;
import org.example.torneoajedrez.database.DBSchem;
import org.example.torneoajedrez.model.Staff;

import java.sql.*;


public class AdminDaoStaff {

    private Connection connection;

    private PreparedStatement preparedStatement;

    private ResultSet resultSet;

    private VentanasController ventana;

    public ObservableList<Staff> cargarStaff()
    {
        ObservableList<Staff> listaUsuarios = FXCollections.observableArrayList();
        connection = ConexionBBDD.getConnection();

        String query = "SELECT * FROM " + DBSchem.TAB_STAFF;

        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                int id = resultSet.getInt(DBSchem.ID_STAFF);
                String nombre = resultSet.getString(DBSchem.COL_NOMBRE);
                String apellido = resultSet.getString(DBSchem.COL_APELLIDO);
                String dni = resultSet.getString(DBSchem.COL_DNI);
                int edad = resultSet.getInt(DBSchem.COL_EDAD);
                String telf = resultSet.getString(DBSchem.COL_TELF);
                String rol = resultSet.getString(DBSchem.COL_ROL);
                double salario = resultSet.getDouble(DBSchem.COL_SALARIO);
                Long cuenta = resultSet.getLong(DBSchem.COL_NUMCUENTA);
                String pass = resultSet.getString(DBSchem.COL_PASS);
                String mail = resultSet.getString(DBSchem.COL_EMAIL);

                listaUsuarios.add(new Staff(id, dni, nombre, apellido, edad,  telf, mail, pass, cuenta, rol, salario));
            }

        } catch (SQLException e) {
            ventana = new VentanasController();
            ventana.ventanaError("No se ha podido Realizar la Consulta \n\nError: \n" + e.getMessage());
        }
        return listaUsuarios;
    }

    public Staff agregarStaff(Staff staff) throws SQLException
    {
        connection = ConexionBBDD.getConnection();

        String query = String.format("INSERT INTO  %s (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
                DBSchem.TAB_STAFF,
                DBSchem.COL_NOMBRE, DBSchem.COL_APELLIDO, DBSchem.COL_DNI, DBSchem.COL_TELF,
                DBSchem.COL_ROL, DBSchem.COL_SALARIO, DBSchem.COL_NUMCUENTA, DBSchem.COL_EDAD, DBSchem.COL_EMAIL, DBSchem.COL_PASS);

        preparedStatement = connection.prepareStatement(query);

        preparedStatement.setString(1, staff.getNombre());
        preparedStatement.setString(2, staff.getApellido());
        preparedStatement.setString(3, staff.getDni());
        preparedStatement.setString(4, staff.getTelf());
        preparedStatement.setString(5, staff.getRol());
        preparedStatement.setDouble(6, staff.getSalario());
        preparedStatement.setLong(7, staff.getCuenta());
        preparedStatement.setInt(8, staff.getEdad());
        preparedStatement.setString(9, staff.getMail());
        preparedStatement.setString(10, staff.getPass());

        preparedStatement.executeUpdate();
        return staff;
    }

    public void editarStaff(Staff staff) throws SQLException
    {
        connection = ConexionBBDD.getConnection();

        String query = String.format("UPDATE  %s SET %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, " +
                                                        "%s = ?, %s = ?, %s = ?, %s = ?, %s = ?" +
                                                         " WHERE %s = ? ",
                DBSchem.TAB_STAFF,
                DBSchem.COL_NOMBRE, DBSchem.COL_APELLIDO, DBSchem.COL_DNI, DBSchem.COL_EDAD, DBSchem.COL_TELF,
                DBSchem.COL_EMAIL, DBSchem.COL_PASS, DBSchem.COL_ROL, DBSchem.COL_SALARIO, DBSchem.COL_NUMCUENTA,
                DBSchem.ID_STAFF);

            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, staff.getNombre());
            preparedStatement.setString(2, staff.getApellido());
            preparedStatement.setString(3, staff.getDni());
            preparedStatement.setInt(4, staff.getEdad());
            preparedStatement.setString(5, staff.getTelf());
            preparedStatement.setString(6, staff.getMail());
            preparedStatement.setString(7, staff.getPass());
            preparedStatement.setString(8, staff.getRol());
            preparedStatement.setDouble(9, staff.getSalario());
            preparedStatement.setLong(10, staff.getCuenta());
            preparedStatement.setInt(11, staff.getId());

            preparedStatement.executeUpdate();
    }

    public boolean borrarStaff(Staff staff)
    {
        connection = ConexionBBDD.getConnection();

        String query = String.format("DELETE FROM %s " +
                                    "WHERE %s = ?",
                DBSchem.TAB_TORNEO_STAFF,
                DBSchem.ID_STAFF);
        try
        {
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, staff.getId());

            preparedStatement.executeUpdate();

            borrar(staff);
        } catch(SQLException e)
        {
            ventana = new VentanasController();
            ventana.ventanaError(String.format("Error al borrar al usuario %s %s\n\n %s",staff.getNombre(), staff.getApellido(), e.getMessage()));
            return false;
        }
        return true;
    }

    private void borrar(Staff staff) throws SQLException
    {

        connection = ConexionBBDD.getConnection();

        String query = String.format("DELETE FROM %s " +
                        "WHERE %s = ?",
                DBSchem.TAB_STAFF,
                DBSchem.COL_DNI);

        preparedStatement = connection.prepareStatement(query);

        preparedStatement.setString(1, staff.getDni());

        preparedStatement.executeUpdate();
    }

    public ObservableList<Staff> filtroTorneoStaff(int torneo)
    {
        ObservableList<Staff> listaFiltrada= FXCollections.observableArrayList();

        connection = ConexionBBDD.getConnection();

        String query = String.format("SELECT %s, %s, %s, %s, %s, %s, %s, %s,%s,%s,%s FROM %s " +
                                        "WHERE  %s IN " +
                                             "(SELECT %s FROM %s " +
                                                 "WHERE %s = ?)",
                DBSchem.ID_STAFF, DBSchem.COL_NOMBRE, DBSchem.COL_APELLIDO, DBSchem.COL_DNI, DBSchem.COL_EDAD, DBSchem.COL_TELF,
                DBSchem.COL_EMAIL, DBSchem.COL_PASS, DBSchem.COL_ROL, DBSchem.COL_SALARIO, DBSchem.COL_NUMCUENTA,
                DBSchem.TAB_STAFF,
                DBSchem.ID_STAFF,
                DBSchem.ID_STAFF,
                DBSchem.TAB_TORNEO_STAFF,
                DBSchem.ID_TORNEO);

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, torneo);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                int id = resultSet.getInt(DBSchem.ID_STAFF);
                String nombre = resultSet.getString(DBSchem.COL_NOMBRE);
                String apellido = resultSet.getString(DBSchem.COL_APELLIDO);
                String dni = resultSet.getString(DBSchem.COL_DNI);
                int edad = resultSet.getInt(DBSchem.COL_EDAD);
                String telf = resultSet.getString(DBSchem.COL_TELF);
                String rol = resultSet.getString(DBSchem.COL_ROL);
                double salario = resultSet.getDouble(DBSchem.COL_SALARIO);
                Long cuenta = resultSet.getLong(DBSchem.COL_NUMCUENTA);
                String pass = resultSet.getString(DBSchem.COL_PASS);
                String mail = resultSet.getString(DBSchem.COL_EMAIL);

                listaFiltrada.add(new Staff(id, dni, nombre, apellido, edad,  telf, mail, pass, cuenta, rol, salario));
            }

        } catch (SQLException e) {
            ventana = new VentanasController();
            ventana.ventanaError("Error en el filtado\n\nError: \n" + e.getMessage());
        }
        return listaFiltrada;
    }

    public  ObservableList<Staff> filtroTorneoArbitro(int idTorneo, String rol)
    {
        ObservableList<Staff> listaFiltrada = FXCollections.observableArrayList();

        connection = ConexionBBDD.getConnection();

        String query = String.format("SELECT %s, %s, %s FROM %s " +
                        "WHERE  %s = ? AND %s IN " +
                        "(SELECT %s FROM %s " +
                        "WHERE %s = ?)",
                DBSchem.ID_STAFF, DBSchem.COL_NOMBRE, DBSchem.COL_APELLIDO,
                DBSchem.TAB_STAFF,
                DBSchem.COL_ROL, DBSchem.ID_STAFF,
                DBSchem.ID_STAFF,
                DBSchem.TAB_TORNEO_STAFF,
                DBSchem.ID_TORNEO);

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, rol);
            preparedStatement.setInt(2, idTorneo);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                int id = resultSet.getInt(DBSchem.ID_STAFF);
                String nombre = resultSet.getString(DBSchem.COL_NOMBRE);
                String apellido = resultSet.getString(DBSchem.COL_APELLIDO);

                listaFiltrada.add(new Staff(id, nombre, apellido));
            }

        } catch (SQLException e)
        {
            ventana.ventanaError("Error en el filtado\n\nError: \n" + e.getMessage());
        }
        return listaFiltrada;
    }

    public void insertTorneoStaff(int idTorneo, int idStaff) throws SQLException
    {
        connection = ConexionBBDD.getConnection();

        String query = String.format("INSERT INTO  %s (%s, %s) " +
                        "VALUES (?, ?);",
                DBSchem.TAB_TORNEO_STAFF,
                DBSchem.ID_TORNEO, DBSchem.ID_STAFF);

        preparedStatement = connection.prepareStatement(query);

        preparedStatement.setInt(1, idTorneo);
        preparedStatement.setInt(2, idStaff);

        preparedStatement.executeUpdate();
    }

    public int idNuevoStaff(String dni) throws SQLException
    {
        int idUsuario =0;
        connection = ConexionBBDD.getConnection();

        String query = String.format("SELECT %s FROM %s " +
                                        "WHERE %S = ?;",
                                    DBSchem.ID_STAFF, DBSchem.TAB_STAFF,
                                    DBSchem.COL_DNI);

        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, dni);

        resultSet = preparedStatement.executeQuery();

        while (resultSet.next())
        {
            idUsuario = resultSet.getInt(DBSchem.ID_STAFF);
        }

        return idUsuario;
    }

    public int selectTorneoStaff(int idStaff)
    {
        int idTorneo =0;

        connection = ConexionBBDD.getConnection();

        String query = String.format("SELECT %s FROM %s  " +
                        "WHERE %s = ?;",
                DBSchem.ID_TORNEO, DBSchem.TAB_TORNEO_STAFF,
                DBSchem.ID_STAFF);

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idStaff);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                idTorneo = resultSet.getInt(DBSchem.ID_TORNEO);
            }

        } catch (SQLException e) {
            ventana = new VentanasController();
            ventana.ventanaError("Error de consulta " + e.getMessage());
        }
        return idTorneo;
    }
}
