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

    public Torneo(String torneo, int idTorneo) {
        nombre=torneo;
        this.idTorneo = idTorneo;
        formatoTorneo = FXCollections.observableArrayList();
    }

    public Torneo(int idTorneo, String nombre)
    {
        this.idTorneo = idTorneo;
        this.nombre = nombre;
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