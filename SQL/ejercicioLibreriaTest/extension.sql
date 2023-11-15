#CREATE USER "Carlos""@""localhost" IDENTIFIED BY '12q12q12Q'
#show grants for "admin"@"localhost"
#delete from gestionagenda.usuario where usuario.nombre_usuario = "Carlos"
#GRANT GRANT OPTION ON *.* TO 'admin'@'localhost';
#GRANT FILE ON *.* TO  "admin"@"localhost";
#grant all privileges on *.* to "admin"@"localhost";
delete from gestionagenda.usuario where nombre_usuario = "Prueba2";

show grants for "admin"@"localhost";