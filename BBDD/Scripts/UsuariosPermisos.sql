CREATE USER 'admin_ajedrez'@'localhost' IDENTIFIED BY 'admin';
CREATE USER 'usuario_ajedrez'@'%' IDENTIFIED BY 'usuario';
CREATE USER 'arbitro_ajedrez'@'%' IDENTIFIED BY 'arbitro';

GRANT ALL PRIVILEGES ON torneo_ajedrez.* TO 'admin_ajedrez';

GRANT SELECT ON torneo_ajedrez.* TO 'usuario_ajedrez';
GRANT SELECT ON torneo_ajedrez.partidas TO 'usuario_ajedrez';
GRANT SELECT ON torneo_ajedrez.formato_torneo TO 'usuario_ajedrez';
GRANT SELECT ON torneo_ajedrez.movimientos TO 'usuario_ajedrez';
GRANT SELECT ON torneo_ajedrez.juegan TO 'usuario_ajedrez';

GRANT SELECT ON torneo_ajedrez.juegan TO 'arbitro_ajedrez';
GRANT SELECT ON torneo_ajedrez.clasificacion TO 'arbitro_ajedrez';
GRANT SELECT, INSERT, UPDATE ON torneo_ajedrez.movimientos TO 'arbitro_ajedrez';
