package org.example.torneoajedrez.model;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Torneo {

    private String nombre;
    private int idTorneo;
    private ObservableList<Formato> formatoTorneo;

    public Torneo(String torneo1, int idTorneo) {
        nombre=torneo1;
        this.idTorneo = idTorneo;
        formatoTorneo = FXCollections.observableArrayList();
    }

    public void agregarFormato(String nombre_Formato)
    {
        Formato nuevoFormato = new Formato(nombre_Formato);
        formatoTorneo.add(nuevoFormato);
    }

    @Override
    public String toString() {
        return nombre;
    }
}