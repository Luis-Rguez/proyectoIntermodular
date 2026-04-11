ALTER TABLE juegan
ADD Color VARCHAR(7) NOT NULL,
ADD Resultado VARCHAR(15); -- el Resultado Puede ser Nulo por si no se ha emparejado pero aun no se ha disputado la Partida

INSERT INTO torneo (Nombre_Torneo, Fecha_INICIO, Fecha_FIN) VALUES
('Torneo Chess Master', '2026-05-05', '2026-05-06'),
('Torneo Toronto Chess', '2026-05-05', '2026-10-04'),
('Torneo Chess GM', '2026-05-05', '2028-05-06');

INSERT INTO recinto (id_Torneo, Nombre_Ciudad, Nombre_Recinto, Direccion) VALUES
(1, 'Santa Cruz de Tenerife', 'Pabellon Quico Cabrera', 'C/ Fernando Barajas Prat'),
(2, 'Madrid', 'Parque El Retiro', 'Plaza de la Independencia, 7'),
(3, 'Barcelona', 'Nou Camp', 'C/ Arístides Maillol, 15');

INSERT INTO patrocinadores (Nombre_Patrocinador, Nº_Contacto, Mail) VALUES
('Galletas MAria S.L.', '963258741', 'galletasmaria@example.com'),
('Galletas Fontaneda S.A.', '689123658', 'fontaneda@example.com'),
('Papel Baño  S.L.', '916236441', 'pepelitofino@example.com'),
('Macanico Todares S.L.', '999999999', 'todaresmecanico@example.com'),
('Pizza Hutt S.A.', '654123654', 'pizzahutt@example.com');

INSERT INTO se_patrocina (id_Torneo, id_Patrocinador) VALUES
(1,1),
(1,2),
(1,4),
(2,1),
(3,3);

INSERT INTO formato_torneo (id_Torneo, Categoria, Reglas) VALUES
(1, 'Eliminatoria Individual', 'Mayores de 16 años'),
(1, 'Eliminatoria Clubs', 'Solo Clubs'),
(1, 'Metodo Suizo ', 'Abierto Individual'),
(2, 'Metodo Suizo ', 'Abierto Clubs'),
(3, 'Toreneo Cerrado GM', 'Solo Categoria Gran Maestro');

INSERT INTO staff (Nombre_Staff, Apellido_Staff, DNI_Staff, Telefono_Staff, Rol, Salario, Nº_Cuenta) VALUES
('Miguel', 'Rosales', '74165456M', '987426351', 'Arbitro', 200, 12346789123465),
('Maria', 'Lopez', '15963215P', '651302135', 'Arbitro', 200, 12346789123465),
('Santiago', 'Segura', '23165498K', '72134568', 'Producion', 200, 12346789123465),
('Juana', 'Vera', '89461325C', '666666666', 'Arbitro', 200, 12346789123465),
('Manolo', 'Rodriguez', '12365482N', '622222222', 'Arbitro', 200, 12346789123465),
('Moises', 'Xin', '15948672Z', '675312846', 'Producion', 200, 12346789123465);

INSERT INTO  Torneo_Staff (id_Torneo, id_Staff) VALUES
(1,1),
(1,2),
(1,3),
(2,4),
(2,5),
(2,6);

INSERT INTO  club (id_Tipo_Torneo, Nombre_Club, MAIL, Nombre_Representante, Telf_Representante) VALUES
(2, 'Club Chees Chirigota', 'chesschirigota@example.com', 'Pepe Mariachi', '645789654'),
(2, 'Club Hiperdino', 'chesshiperdino@example.com', 'Popeye EL Marino', '684635123'),
(2, 'Club Rotanda Alta', 'rotondaajedrez@example.com', 'Micky Mouse', '600888777'),
(2, 'Club Chess Matados', 'matadosclub@example.com', 'Olivia Matusalen', '678456369'),
(2, 'Club Pato Chess', 'patochess@example.com', 'Pato Donal', '666555444'),
(4, 'Club Forzen', 'frozen@example.com', 'Olaf', '630000111'),
(4, 'Club Chess Aventura', 'chessaventura@example.com', 'Tio Gilito', '675848979'),
(5, 'Club GM', 'chessclubGMa@example.com', 'Garry Kasparov', '671033299');

INSERT INTO jugadores (id_Club, Nombre_jugador, DNI, MAIL, Telefono) VALUES
(null, 'Juan Gonzalez', '11122233P','juan@example.es','666999666'),
(null, 'Mario Kart', '77777777P','mario@example.es','666666666'),
(null, 'Gonzalez Rosales', '11111111Z','rosales@example.es','699999999'),
(null, 'Luigui', '22222222O','luigui@example.es','612333555'),
(3, 'Joshie Mario', '74125412L','joshie@example.es','644555681'),
(4, 'Don King Kong', '88888888J','donking@example.es','674747845'),
(6, 'Zelda Ds', '99955661F','zelda@example.es','632000010'),
(7, 'Moises Gonzalez', '11133333P','moy@example.es','666999666'),
(null, 'Bobby Fische', '87412365L','bobby@example.es','600000001'),
(null, 'Raúl Capablanca', '85214774S','capablanca@example.es','695620315');

INSERT INTO Torneo_Inscritos_Jugadores (id_Tipo_Torneo, id_Jugador) VALUES
(1,1),
(1,2),
(1,3),
(1,4),
(2,5),
(2,6),
(4,7),
(4,8),
(3,9),
(3,10);

INSERT INTO partidas (id_Staff, id_Tipo_Torneo, Mesa) VALUES
(1,1,8),
(2,1,6),
(1,2,5);

alter table juegan change column Color Color ENUM('Blancas', 'Negras') not null; 
alter table juegan change column Resultado Resultado ENUM('Ganan', 'Pierden', 'Tablas', 'Pendiente') not null;
INSERT INTO juegan (id_Partida, id_Jugador, Color, Resultado) VALUES
(1,1, 'Blancas', 'Ganan'),
(1,2, 'Negras', 'Pierden'),
(2,3, 'Blancas', 'Pierden'),
(2,4, 'Negras', 'Ganan'),
(3,5, 'Blancas', 'Pendiente'),
(3,6, 'Negras', 'Pendiente');

INSERT INTO movimientos (id_Partida, Blancas, Negras) VALUES
(1,'e4', 'e5'),
(1,'Dh5', 'Cc6'),
(1,'Ac4', 'Cf6'),
(1,'e4#', null),
(2,'f3', 'e5'),
(2,'g4', 'Dh4#');

INSERT INTO clasificacion (id_Tipo_Torneo, id_Jugador, Puntuacion, Clasificatoria) VALUES
(1, 1, null, 'Primero Puesto'),
(1, 4, null, 'Segundo Puesto'),
(4, 7, 100, 'Primer Puesto'),
(4, 8, 80, 'Segundo Puesto');

select * from jugadores;