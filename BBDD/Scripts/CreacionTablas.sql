CREATE DATABASE Torneo_Ajedrez;

USE Torneo_Ajedrez;

CREATE TABLE Torneo(
id_Torneo INT AUTO_INCREMENT PRIMARY KEY,
Nombre_Torneo VARCHAR(30) NOT NULL,
Fecha_INICIO DATE NOT NULL,
Fecha_FIN DATE NOT NULL); 

CREATE TABLE Recinto(
id_Recinto INT AUTO_INCREMENT PRIMARY KEY,
id_Torneo INT NOT NULL,
Nombre_Recinto VARCHAR(30) NOT NULL,
Nombre_Ciudad VARCHAR(30),
Direccion VARCHAR(40) NOT NULL,

CONSTRAINT FK_RECINTO_TORNEO
FOREIGN KEY (id_Torneo) REFERENCES Torneo (id_Torneo)
ON UPDATE CASCADE
ON DELETE RESTRICT
);

CREATE TABLE Patrocinadores(
id_Patrocinador INT AUTO_INCREMENT PRIMARY KEY,
Nombre_Patrocinador VARCHAR(30) NOT NULL,
Nº_Contacto varchar(14) NOT NULL,
Mail VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE Se_Patrocina(
id_Torneo INT NOT NULL,
id_Patrocinador INT NOT NULL,
PRIMARY KEY(id_Torneo, id_Patrocinador),

CONSTRAINT FK_PATROCINA_TORNEO
FOREIGN KEY (id_Torneo) REFERENCES Torneo (id_Torneo)
ON UPDATE CASCADE
ON DELETE RESTRICT,

CONSTRAINT FK_PATROCINA_PATROCINADOR
FOREIGN KEY (id_Patrocinador) REFERENCES Patrocinadores (id_Patrocinador)
ON UPDATE CASCADE
ON DELETE RESTRICT
);

CREATE TABLE Formato_Torneo(
id_Tipo_Torneo INT AUTO_INCREMENT PRIMARY KEY,
id_Torneo INT NOT NULL,
Categoria VARCHAR(30) NOT NULL,
Reglas VARCHAR(100) NOT NULL,

CONSTRAINT FK_FORMATO_TORNEO_TORNEO
FOREIGN KEY (id_Torneo) REFERENCES Torneo (id_Torneo)
ON UPDATE CASCADE
ON DELETE RESTRICT
);

CREATE TABLE Club(
id_Club INT AUTO_INCREMENT PRIMARY KEY,
id_Tipo_Torneo INT NOT NULL,
Nombre_Club VARCHAR(30) NOT NULL,
MAIL varchar(50) UNIQUE NOT NULL,
Nombre_Representante VARCHAR(40) NOT NULL,
Telf_Representante VARCHAR(14) NOT NULL UNIQUE,

CONSTRAINT FK_CLUB_TIPO_TORNEO
FOREIGN KEY (id_Tipo_Torneo) REFERENCES Formato_Torneo (id_Tipo_Torneo)
ON UPDATE CASCADE
ON DELETE RESTRICT 
);

-- Eliminamos el atributo de id_Tipo_Torneo y creamos una tabla auxiliar para evitar registros repetidos
CREATE TABLE Jugadores(
id_Jugador INT AUTO_INCREMENT PRIMARY KEY,
id_Club INT, -- Se Permite que sea nulo porque solo se Asocia si juega en los formato de clubs
Nombre_Jugador VARCHAR(40) NOT NULL,
DNI VARCHAR(10) UNIQUE NOT NULL,
MAIL VARCHAR(50) UNIQUE NOT NULL,
Telefono VARCHAR(14),

CONSTRAINT FK_JUGADORES_CLUB
FOREIGN KEY (id_Club) REFERENCES CLUB (id_Club)
ON UPDATE CASCADE
ON DELETE RESTRICT
);

-- Se ha decido crear la siguiente Tabla intermediaria (aunque un jugador solo pueda registrarse en una categoria del torneo siendo un 1:N)
-- para evitar repetir duplicados de registros si jugasen en en otras ediciones

CREATE TABLE Torneo_Inscritos_Jugadores(
id_Tipo_Torneo INT,
id_Jugador INT,
PRIMARY KEY (id_Tipo_Torneo, id_Jugador),

CONSTRAINT FK_TORNEO_INSCRITO_TIPO_TORNEO
FOREIGN KEY (id_Tipo_Torneo) REFERENCES Formato_Torneo (id_Tipo_Torneo)
ON UPDATE CASCADE
ON DELETE RESTRICT,

CONSTRAINT FK_TORNEO_INSCRITO_JUGADOR
FOREIGN KEY (id_Jugador) REFERENCES Jugadores (id_Jugador)
ON UPDATE CASCADE
ON DELETE RESTRICT
);

CREATE TABLE Clasificacion(
id_Clasificacion INT AUTO_INCREMENT PRIMARY KEY,
id_Tipo_Torneo INT NOT NULL,
id_Jugador INT NOT NULL,
Puntuacion INT,
Clasificatoria VARCHAR(30) NOT NULL,

CONSTRAINT FK_CLASIFICACION_TIPO_TORNEO
FOREIGN KEY (id_Tipo_Torneo) REFERENCES Formato_Torneo (id_Tipo_Torneo)
ON UPDATE CASCADE
ON DELETE RESTRICT,

CONSTRAINT FK_CLASIFICACION_Jugador
FOREIGN KEY (id_Jugador) REFERENCES Jugadores (id_Jugador)
ON UPDATE CASCADE
ON DELETE RESTRICT
);

CREATE TABLE Staff(
id_Staff INT AUTO_INCREMENT PRIMARY KEY,
Nombre_Staff VARCHAR(15) NOT NULL,
Apellido_Staff VARCHAR(30) NOT NULL,
DNI_Staff VARCHAR(10) UNIQUE NOT NULL,
Telefono_Staff VARCHAR(14) UNIQUE NOT NULL,
Rol VARCHAR(15) NOT NULL,
Salario DOUBLE NOT NULL,
Nº_Cuenta LONG NOT NULL
);

-- Ocurre lo mismo que en la tabla de Jugadores

CREATE TABLE Torneo_Staff(
id_Torneo INT NOT NULL,
id_Staff INT NOT NULL,
PRIMARY KEY (id_Torneo, id_Staff),

CONSTRAINT FK_TORNEOSTAFF_STAFF
FOREIGN KEY (id_Staff) REFERENCES Staff (id_Staff)
ON UPDATE CASCADE
ON DELETE RESTRICT,

CONSTRAINT FK_TORNEOSTAFF_TORNEO
FOREIGN KEY (id_Torneo) REFERENCES Torneo (id_Torneo)
ON UPDATE CASCADE
ON DELETE RESTRICT
);

CREATE TABLE Partidas(
id_Partida INT AUTO_INCREMENT PRIMARY KEY,
id_Staff INT NOT NULL,
id_Tipo_Torneo INT NOT NULL,
MESA INT NOT NULL,

CONSTRAINT FK_PARTIDAS_STAFF
FOREIGN KEY (id_Staff) REFERENCES Staff (id_Staff)
ON UPDATE CASCADE
ON DELETE RESTRICT,

CONSTRAINT FK_PARTIDAS_TIPO_TORNEO
FOREIGN KEY (id_Tipo_Torneo) REFERENCES Formato_Torneo (id_Tipo_Torneo)
ON UPDATE CASCADE
ON DELETE RESTRICT
);

CREATE TABLE Juegan(
id_Jugador INT NOT NULL,
id_Partida INT NOT NULL,
PRIMARY KEY (id_Jugador, id_Partida),

CONSTRAINT FK_JUEGAN_JUGADOR
FOREIGN KEY (id_Jugador) REFERENCES Jugadores (id_Jugador)
ON UPDATE CASCADE
ON DELETE RESTRICT,

CONSTRAINT FK_JUEGAN_PARTIDA
FOREIGN KEY (id_Partida) REFERENCES Partidas (id_Partida)
ON UPDATE CASCADE
ON DELETE RESTRICT
);

CREATE TABLE Movimientos(
id_Movimiento INT AUTO_INCREMENT PRIMARY KEY,
id_Partida INT NOT NULL,
Blancas VARCHAR(7) NOT NULL,
Negras VARCHAR(6), -- Las negras pueden ser nulo porque las blancas siempre empiezan, si hacen mate blancas las negras no hacen movimiento por ello se permite un nulo

CONSTRAINT FK_Movimientos_PARTIDA
FOREIGN KEY (id_Partida) REFERENCES Partidas (id_Partida)
ON UPDATE CASCADE
ON DELETE RESTRICT
);
