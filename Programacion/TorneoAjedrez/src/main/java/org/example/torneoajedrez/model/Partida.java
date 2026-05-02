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

public class Partida {

    private String blancas, negras, arbitro, resulBlancas, resulNegras ;
    private int id, idArbitro, id_formato, mesa, ronda, idBlancas, idNegras, id_movimientos;
    private ObservableList<Movimientos> listaMovimiento;

    public Partida()
    {
        listaMovimiento = FXCollections.observableArrayList();
    }

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

    public Partida(int idJugadorB, String nombreJugadorB, int idJugadorN, String nombreJugadorN)
    {
        this.idBlancas = idJugadorB;
        this.idNegras = idJugadorN;
        this.blancas = nombreJugadorB;
        this.negras = nombreJugadorN;

        listaMovimiento = FXCollections.observableArrayList();
    }

    public Partida(int idPartida, int idJugadorB, String nombreJugadorB, int idJugadorN, String nombreJugadorN, int mesa, int ronda, int idFormato)
    {
        this.id = idPartida;
        this.idBlancas = idJugadorB;
        this.idNegras = idJugadorN;
        this.blancas = nombreJugadorB;
        this.negras = nombreJugadorN;
        this.mesa = mesa;
        this.ronda = ronda;
        this.id_formato = idFormato;
        listaMovimiento = FXCollections.observableArrayList();
    }

    public Partida(int id_formato, String blancas, String negras, String resulBlancas, String resulNegras, int mesa, int ronda) {
        this.id_formato = id_formato;
        this.blancas = blancas;
        this.negras = negras;
        this.resulBlancas = resulBlancas;
        this.resulNegras = resulNegras;
        this.mesa = mesa;
        this.ronda = ronda;
        listaMovimiento = FXCollections.observableArrayList();
    }

    public Partida(int idFormato, int mesa, int ronda)
    {
        this.id_formato = idFormato;
        this.mesa = mesa;
        this.ronda = ronda;
        listaMovimiento = FXCollections.observableArrayList();
    }

    public Partida(String negras, String resulNegras, int idJugador) {
        this.negras = negras;
        this.resulNegras = resulNegras;
        this.idNegras = idJugador;
    }

    @Override
    public String toString() {
        return blancas + " VS " + negras;
    }
}
