package org.example.torneoajedrez.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Movimientos {

    private String moveBlancas, moveNegras;
    private int id, idPartida;

    public Movimientos(int idPartida, String moveBlancas, String moveNegras) {
        this.idPartida = idPartida;
        this.moveBlancas = moveBlancas;
        this.moveNegras = moveNegras;
    }
}
