package org.example.torneoajedrez.model;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Partida {

    private String blancas, negras, arbitro, resulBlancas, resulNegras ;
    private int id, id_formato, mesa, ronda, idBlancas, idNegras;
    private ObservableList<Movimientos> listaMovimiento;

    public Partida(int id, int id_formato, String blancas, String resulBlancas, int mesa, String arbitro, int idJugador, int ronda) {
        this.id = id;
        this.id_formato = id_formato;
        this.blancas = blancas;
        this.resulBlancas = resulBlancas;
        this.mesa = mesa;
        this.arbitro = arbitro;
        this.idBlancas = idJugador;
        this.ronda = ronda;
        listaMovimiento = FXCollections.observableArrayList();
    }

    public Partida(int id_formato, String blancas, String negras, String resulBlancas, String resulNegras, int mesa, int ronda) {
        this.id = id;
        this.id_formato = id_formato;
        this.blancas = blancas;
        this.negras = negras;
        this.resulBlancas = resulBlancas;
        this.resulNegras = resulNegras;
        this.mesa = mesa;
        this.arbitro = arbitro;
        this.ronda = ronda;
        listaMovimiento = FXCollections.observableArrayList();
    }

    public Partida(String negras, String resulNegras, int idJugador) {
        this.negras = negras;
        this.resulNegras = resulNegras;
        this.idNegras = idJugador;
    }
}
