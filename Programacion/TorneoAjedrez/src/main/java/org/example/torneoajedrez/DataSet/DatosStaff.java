package org.example.torneoajedrez.DataSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import org.example.torneoajedrez.dao.Staff.StaffDao;
import org.example.torneoajedrez.model.Partida;

@Getter

public class DatosStaff
{
    private static StaffDao staffDao = new StaffDao();
    private static int idStaff;
    private static ObservableList<Partida> listaPartidas = FXCollections.observableArrayList();
/// ///////////////////////////////////////////////
    public static ObservableList<Partida> partidaPendientes()
    {
        listaPartidas.setAll(staffDao.cargarPartidasStaff(idStaff));
        return listaPartidas;
    }

    public static ObservableList<Partida> getListaPartidas() {
        return listaPartidas;
    }

    public static int getIdStaff()
    {
        return idStaff;
    }

    public static void setIdStaff(int idUsuario) {
        idStaff = idUsuario;
    }
}
