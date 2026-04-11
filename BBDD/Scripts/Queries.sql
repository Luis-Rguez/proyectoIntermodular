-- Vamos a ver la 1º Partida jugada con sus Jugadores de la Partida 1 y sus Partidas y el color
SELECT jr.nombre_jugador, j.color, m.blancas, m.negras FROM jugadores jr
INNER JOIN juegan j ON j.id_jugador = jr.id_jugador
INNER JOIN partidas p ON p.id_Partida = j.id_Partida
INNER JOIN movimientos m ON m.id_partida = p.id_partida
WHERE p.id_Partida = 1;

-- Mostramos los Jugadores y el tipo de Torneo Que no esten en un club
SELECT t.categoria, j.nombre_jugador FROM formato_torneo t
INNER JOIN torneo_inscritos_jugadores ins ON t.id_tipo_torneo = ins.id_tipo_torneo
INNER JOIN jugadores j ON j.id_jugador = ins.id_jugador
WHERE j.id_club IS NULL;

-- Mostramos los Clubes y el tipo de Torneo que estan inscritos
SELECT t.categoria, c.nombre_club FROM formato_torneo t
INNER JOIN torneo_inscritos_jugadores ins ON ins.id_tipo_torneo = t.id_tipo_torneo 
INNER JOIN jugadores j ON ins.id_jugador = j.id_jugador
INNER JOIN club c ON c.id_club = j.id_club;

-- Mostramos el Fomato de Torneo y su Clasificacion del Todos los Torneos
SELECT tr.nombre_torneo, t.categoria, c.clasificatoria, j.nombre_jugador, c.puntuacion, cl.nombre_club FROM torneo tr
INNER JOIN formato_torneo t ON t.id_torneo = tr.id_torneo
INNER JOIN torneo_inscritos_jugadores ins ON ins.id_tipo_torneo = t.id_tipo_torneo 
INNER JOIN jugadores j ON ins.id_jugador = j.id_jugador
INNER JOIN clasificacion c ON c.id_jugador = j.id_jugador AND c.id_tipo_torneo = ins.id_tipo_torneo
LEFT JOIN club cl ON cl.id_club = j.id_club;

-- Mostramos el Fomato de Torneo y su Clasificacion del Torneo 1
SELECT tr.nombre_torneo, t.categoria, c.clasificatoria, j.nombre_jugador, c.puntuacion, cl.nombre_club FROM torneo tr
INNER JOIN formato_torneo t ON t.id_torneo = tr.id_torneo
INNER JOIN torneo_inscritos_jugadores ins ON ins.id_tipo_torneo = t.id_tipo_torneo 
INNER JOIN jugadores j ON ins.id_jugador = j.id_jugador
INNER JOIN clasificacion c ON c.id_jugador = j.id_jugador
LEFT JOIN club cl ON cl.id_club = j.id_club
WHERE tr.id_torneo = 1;

-- Mostramos el total de Partidas incritos en Eliminatoria Individual del torneo 1
SELECT COUNT(id_partida) AS Num_Partidas FROM partidas
WHERE id_tipo_torneo IN (SELECT id_tipo_torneo FROM formato_torneo
WHERE categoria = 'Eliminatoria Individual');

-- Mostramos las Partrocinadores que patrocinan los torneos
SELECT p.nombre_patrocinador, t.nombre_torneo FROM patrocinadores p
INNER JOIN se_patrocina sp ON sp.id_patrocinador = p.id_patrocinador
INNER JOIN torneo t ON sp.id_torneo = t.id_torneo;

-- Mostramos los Patrocinadoes del Torneo 'Torneo Toronto Chess'
SELECT p.nombre_patrocinador, t.nombre_torneo FROM patrocinadores p
INNER JOIN se_patrocina sp ON sp.id_patrocinador = p.id_patrocinador
INNER JOIN torneo t ON sp.id_torneo = t.id_torneo
WHERE t.nombre_torneo = 'Torneo Toronto Chess';

-- Contamos los patrocinadores que hay 'Torneo Chess Master'
SELECT COUNT(p.id_Patrocinador) as NUM_Patrocinadores_Torneo_Chess_Master FROM patrocinadores p
INNER JOIN se_patrocina sp ON sp.id_patrocinador = p.id_patrocinador
INNER JOIN torneo t ON sp.id_torneo = t.id_torneo
WHERE t.nombre_torneo = 'Torneo Chess Master';

-- Mostramos Partidas Pendientes de Jugar
SELECT p.id_partida, jr.nombre_jugador, j.color FROM jugadores jr
INNER JOIN juegan j ON j.id_jugador = jr.id_jugador
INNER JOIN partidas p ON p.id_Partida = j.id_Partida
WHERE j.resultado LIKE 'Pend%';

-- Mostramos al equipo del Staff que no estan asigandos a ninguna mesa del Toreno 1
SELECT nombre_staff, apellido_staff, rol FROM staff
WHERE id_staff IN (SELECT id_staff FROM Torneo_Staff WHERE id_torneo = 1)
AND
id_staff NOT IN (SELECT id_staff FROM partidas);

-- Mostramos al equipo del Staff que si estan asigandos a ninguna mesa del Toreno 1
SELECT nombre_staff, apellido_staff, rol FROM staff
WHERE id_staff IN (SELECT id_staff FROM Torneo_Staff WHERE id_torneo = 1)
AND
id_staff IN (SELECT id_staff FROM partidas);

-- Mostramos al equipo del Staff que es Produccion del Torneo 2
SELECT nombre_staff, apellido_staff, rol FROM staff
WHERE id_staff IN (SELECT id_staff FROM Torneo_Staff WHERE id_torneo = 2);

-- Mostramos a los Jugadores que se Presentan de forma Individual
SELECT nombre_Jugador FROM jugadores
WHERE id_club IS NULL;

-- Mostramos a los Jugadores que se Presentan de forma Individual
SELECT COUNT(nombre_Jugador) FROM jugadores
WHERE id_club IS NULL;