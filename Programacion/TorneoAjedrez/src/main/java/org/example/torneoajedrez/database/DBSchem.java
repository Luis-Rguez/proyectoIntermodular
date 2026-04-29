package org.example.torneoajedrez.database;

public interface DBSchem {

    // NOMBRE DE TABLAS
    String TAB_STAFF = "staff";
    String TAB_JUGADORES = "jugadores";
    String TAB_PATROCINADORES = "patrocinadores";
    String TAB_FORMATO_TORNEO = "formato_torneo";
    String TAB_TORNEOS = "torneo";
    String TAB_RECINTO = "recinto";
    String TAB_PARTIDAS = "partidas";
    String TAB_MOVIMIENTO = "movimientos";
    String TAB_CLASIFICACION = "clasificacion";
    String TAB_TORNEO_STAFF = "Torneo_Staff";
    String TAB_JUEGAN = "juegan";
    String TAB_JUGADOR_FORMATO = "torneo_inscritos_jugadores";

    // COLUMNAS COMUNES (JUGADORES, STAFF, CLUBS, RECINTO, PATROCINADORES)
    String COL_EMAIL = "mail";
    String COL_TELF = "telefono";
    String COL_NOMBRE = "nombre";

    // COLUMNAS COMUNES (JUGADORES, STAFF)
    String COL_PASS = "pass";
    String COL_EDAD = "edad";
    String COL_APELLIDO = "apellido";
    String COL_DNI = "dni";

    // UNICA STAFF
    String COL_SALARIO = "salario";
    String COL_NUMCUENTA = "nº_cuenta";
    String COL_ROL = "rol";
    String ID_STAFF = "id_staff";

    //FORMATO TORNEO
    String ID_TORNEO = "id_torneo";
    String ID_FORMATO_TORNEO ="id_tipo_torneo";
    String COL_CATEGORIA = "categoria";

    //PARTIDA
    String ID_PARTIDA = "id_partida";
    String COL_MESA = "mesa";
    String COL_COLOR = "color";
    String COL_RESULTADO = "resultado";
    String AS_ULTIMO_ID = "ultimoID";
    String COL_RONDA = "ronda";

    //JUGADORES
    String ID_JUGADOR = "id_jugador";
    String COL_NOMBRE_JUGADOR = "nombre_jugador";

    //CLUBS
    String ID_CLUB = "id_club";
}
