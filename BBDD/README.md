Se han Creado dos Tablas Intermedias entre Formato_Torneo y Jugadores y entre Torneo y Staff aun siendo una relacion 1:N en ambos casos,
esta decicion fue tomada para evitar duplicar de usuarios ya registrados y evitar asi que en el mismo torneo se duplicase y dejar restricciones como unique en DNI y Correo.

En las Querys se han intentado usar todo lo que se ha visto a lo largo del 1º año del curso siendo un Total de 15 Querysse han usado
- INNER JOIN
- LEFT JOIN
- IN
- NOT IN
- COUNT()
- like '%'
- Subconsultas

  Y se crearon dos tipos de usarios con sus permisos
  - Admin
  - Usuario
