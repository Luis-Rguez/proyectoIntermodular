package org.example.torneoajedrez.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Formato {

    private String categoria;
    private int idFormatoTorneo, idTorneo;
    private ObservableList<Partida> listaPartidas;
    private List <Clasificacion> clasificacion;

    public Formato(String categoria) {
        this.categoria = categoria;
        listaPartidas = FXCollections.observableArrayList();
    }

    public Formato(String categoria, int idTorneo, int idFormatoTorneo)
    {
        this.categoria = categoria;
        this.idTorneo = idTorneo;
        this.idFormatoTorneo = idFormatoTorneo;
        listaPartidas = FXCollections.observableArrayList();
    }

    @Override
    public String toString() {
        return categoria;
    }
}
