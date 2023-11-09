create database gestionAgenda;
use gestionAgenda;
create table usuario(
    nombre_usuario varchar(15) not null,
    clave_usuario varchar(15) not null,
    primary key(nombre_usuario)
);
create table administrador(
    nombre_administrador varchar(15) not null,
    clave_administrador varchar(15) not null,
	primary key(nombre_administrador)
);
create table agenda(
	id_agenda int not null,
    nombre varchar(30) not null,
    nombre_usuario varchar(15) null,
    primary key(id_agenda),
    foreign key (nombre_usuario) references usuario(nombre_usuario)
);
create table contacto(
    nombre varchar(20),
    apellidos varchar(30),
    direccion varchar(25),
    correo varchar(30),
    telefono varchar(12) not null,
    id_agenda int not null,
    primary key (telefono),
    foreign key (id_agenda) references agenda(id_agenda)
);
create user 'usuarioValidarCredencial'@'localhost' identified by 'validarCredencial';

grant select on gestionAgenda.administrador to usuarioValidarCredencial;
grant select on gestionAgenda.usuario to usuarioValidarCredencial;

insert into usuario values("cmorbla","cmorbla");
insert into administrador values("admin","admin");
create user 'administradorPrincipal'@'localhost' identified by 'admin';
grant all privileges on gestionAgenda to administradorPrincipal;
create user 'cmorbla'@'localhost' identified by 'cmorbla';
grant select , insert, update, delete, file on gestionAgenda.contacto to cmorbla;
flush privileges;