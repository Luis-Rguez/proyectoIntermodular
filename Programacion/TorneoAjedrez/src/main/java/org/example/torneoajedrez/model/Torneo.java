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

    private String nombre, recinto, fechaInicio, fechaFin, formato;
    private int idTorneo,idFormato, numParticipantes;
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

    public Torneo(int idTorneo, String nombre, String fechaInicio, String fechaFin, String formato, int idFormato)
    {
        this.idTorneo = idTorneo;
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.formato = formato;
        this.idFormato = idFormato;
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