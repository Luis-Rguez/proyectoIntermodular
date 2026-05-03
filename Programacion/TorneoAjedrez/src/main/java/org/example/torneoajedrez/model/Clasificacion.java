package org.example.torneoajedrez.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Clasificacion {

    private int id, id_jugador, id_tipoTorneo, ganadas, perdidas, tablas;
    private String nombreTorneo, nombreFormato, puesto, nombreJugador;

    public Clasificacion(int id_tipoTorneo, String puesto, int id_jugador) {
        this.id_tipoTorneo = id_tipoTorneo;
        this.puesto = puesto;
        this.id_jugador = id_jugador;
    }

    public Clasificacion(int id, int id_tipoTorneo, int id_jugador) {
        this.id = id;
        this.id_tipoTorneo = id_tipoTorneo;
        this.id_jugador = id_jugador;
    }

    public Clasificacion(int id_tipoTorneo, int id_jugador, String nombreJugador, int ganadas) {
        this.id_tipoTorneo = id_tipoTorneo;
        this.id_jugador = id_jugador;
        this.nombreJugador = nombreJugador;
        this.ganadas = ganadas;
    }

    public Clasificacion(String nombreTorneo, String nombreFormato, String puesto, int ganadas, int perdidas, int tablas) {
        this.nombreTorneo = nombreTorneo;
        this.nombreFormato = nombreFormato;
        this.puesto = puesto;
        this.ganadas = ganadas;
        this.perdidas = perdidas;
        this.tablas = tablas;
    }
}
