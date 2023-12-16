create database escoba;
use escoba;
create table usuario(
	nombre_usuario varchar(15) not null,
    clave varchar(15) not null,
    primary key(nombre_usuario)
);
create table clasificacion(
	id_clasificacion INT AUTO_INCREMENT,
    partidas_ganadas int not null,
    puntos_oros int not null,
    puntos_escobas int not null,
    puntos_velo int not null,
    puntos_sietes int not null,
    puntos_cantidad_cartas int not null,
    nombre_usuario varchar(15) not null,
    primary key(id_clasificacion),
    foreign key (nombre_usuario) references usuario(nombre_usuario)
);

create user 'admin_escoba'@'localhost' identified with mysql_native_password by'admin';
grant all privileges on *.* to "admin_escoba"@"localhost";
GRANT GRANT OPTION ON *.* TO 'admin'@'localhost';
GRANT CREATE USER ON *.* TO 'admin_escoba'@'localhost' WITH GRANT OPTION;
GRANT DROP ON *.* TO 'admin_escoba'@'localhost';
FLUSH PRIVILEGES;