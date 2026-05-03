Este repositorio contiene un sistema de representación de torneos de ajedrez utilizando XML y XML Schema (XSD) para validar la estructura de los datos.

 Descripción

El proyecto define la estructura de:

Torneos
Partidas
Jugadores
Movimientos
Usuarios del sistema

Todo ello mediante un esquema XML (.xsd) que garantiza la integridad de los datos.

 Estructura del XML

El sistema se divide en dos bloques principales:

1.  Torneos

Cada torneo contiene:

Nombre
Fecha de inicio y fin
Lista de partidas

Cada partida incluye:

Ronda
Mesa
Jugador de blancas
Jugador de negras
Árbitro
Movimientos

Cada movimiento contiene:

Jugada de blancas
Jugada de negras
2. 👤 Usuarios

Los usuarios representan a los jugadores registrados en el sistema.

Cada usuario tiene:

Nombre
Apellido
DNI
Email
Teléfono
Contraseña
Edad
