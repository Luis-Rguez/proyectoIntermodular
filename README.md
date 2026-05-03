# proyectoIntermodular

Proyecto final de 1º DAM

Evidencias de su funcionamiento
https://www.dropbox.com/scl/fo/tqhfwd1lcaifq80ezd9ya/AElDNRUSXeAUmovPd3FKr10?rlkey=ta0beed0vry0hyfuw1bwx2gb7&st=tn3qhye1&dl=0

Esta aplicación permite gestionar torneos de ajedrez mediante un sistema de eliminación directa, controlando automáticamente las partidas, rondas y resultados.

Descripción general

La app organiza torneos donde:

Las partidas se generan de forma aleatoria
Los jugadores avanzan mediante eliminación directa
Los resultados son registrados únicamente por el árbitro
El torneo continúa por rondas hasta que queda un único ganador

Funcionamiento del sistema ->
Generación de partidas
Al iniciar el torneo, los emparejamientos se crean automáticamente de forma aleatoria.
No hay intervención manual en la asignación de rivales.
Rol del árbitro

El árbitro es el único autorizado para ->
Registrar los movimientos de cada partida
Indicar el resultado (victoria o derrota)

Esto asegura la integridad del torneo.

Sistema de rondas ->
El torneo está dividido en rondas
No se puede avanzar a la siguiente ronda hasta que todas las partidas de la ronda actual hayan finalizado
Cada ronda elimina a los jugadores que pierden

Eliminación directa ->
El torneo sigue un formato de knockout (eliminación directa)
Solo los ganadores de cada partida avanzan a la siguiente ronda
El proceso continúa hasta que queda un solo jugador ganador

Reglas importantes ->
El número de jugadores debe ser múltiplo de 4
Esto evita que jugadores pasen automáticamente de ronda (byes)
Todas las partidas deben tener resultado antes de avanzar
No existen empates: siempre debe haber un ganador

Flujo del torneo ->
Registro de jugadores
Verificación del número de participantes (múltiplo de 4)
Generación aleatoria de partidas
Clasificion final automatica

Desarrollo de la ronda ->
Registro de movimientos
Registro de resultados por el árbitro
Finalización de la ronda
Paso a la siguiente ronda con los ganadores
Repetir hasta obtener un campeón

Objetivo ->
Garantizar una gestión estructurada, justa y automatizada de torneos de ajedrez, evitando errores humanos en emparejamientos y controlando el avance correcto de las rondas.
