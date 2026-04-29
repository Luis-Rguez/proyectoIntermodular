package org.example.torneoajedrez.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Clasificacion {

    private int id, id_jugador, id_tipoTorneo, ganadas, perdidas, tablas;
    private String nombreTorneo, nombreFormato, puesto;
}
