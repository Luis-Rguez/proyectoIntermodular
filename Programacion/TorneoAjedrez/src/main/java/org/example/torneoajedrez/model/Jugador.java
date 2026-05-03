package org.example.torneoajedrez.model;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

public class Jugador extends Usuario{

    private int idClub;
    private List<Torneo> listaTorneo;

    public Jugador(){listaTorneo = new ArrayList<>();}

    public Jugador(int id, String nombre)
    {
        super(id, nombre);
        listaTorneo = new ArrayList<>();
    }

    public Jugador(int id, int idClub, String nombre, String dni, String mail, String telf, String pass) {
        super(id, dni, nombre, telf, mail, pass);
        this.idClub = idClub;
        listaTorneo = new ArrayList<>();
    }

    public Jugador(String nombre, String dni, String telf, String mail, String pass) {
        super(nombre, dni, telf, mail, pass);
    }

    @Override
    public String toString() {
        return getNombre();
    }
}
