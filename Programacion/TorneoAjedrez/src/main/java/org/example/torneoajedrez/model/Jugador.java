package org.example.torneoajedrez.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Jugador extends Usuario{

    private int idClub;

    public Jugador(int id, String nombre) {
        super(id, nombre);
    }

    public Jugador(){}

    @Override
    public String toString() {
        return getNombre();
    }
}
