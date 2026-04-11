CREATE USER 'admin_ajedrez'@'localhost' IDENTIFIED BY 'admin';
CREATE USER 'usuario_ajedrez'@'%' IDENTIFIED BY 'usuario';

GRANT ALL PRIVILEGES ON torneo_ajedrez.* TO 'admin_ajedrez';
GRANT SELECT ON torneo_ajedrez.* TO 'usuario_ajedrez';