package org.example.torneoajedrez.DataSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import org.example.torneoajedrez.dao.Usuario.UserDao;
import org.example.torneoajedrez.model.Torneo;

@Getter

public class DatosJugador {

    private static int idJugador;

    public static int getIdJugador() {return idJugador;}

    public static void setIdJugador(int idJugador) {DatosJugador.idJugador = idJugador;}
}
