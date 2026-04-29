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
    private static int idStaff =0;
    private static ObservableList<Partida> listaPartidas = FXCollections.observableArrayList();
/// ///////////////////////////////////////////////
    public static ObservableList<Partida> partidaPendientes()
    {
        listaPartidas.setAll(staffDao.cargarPartidasStaff(1));
        return listaPartidas;
    }

    public static ObservableList<Partida> getListaPartidas() {
        return listaPartidas;
    }

    public static void setIdStaff(int idUsuario) {
        idStaff = idStaff;
    }
}
