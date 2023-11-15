create database gestionAgenda;
use gestionAgenda;
create table usuario(
    nombre_usuario varchar(15) not null,
    clave varchar(15) not null,
    primary key(nombre_usuario)
);
create table administrador(
    nombre_usuario varchar(15) not null,
    clave varchar(15) not null,
	primary key(nombre_usuario)
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
create user 'usuarioValidarCredencial'@'localhost' identified with mysql_native_password by 'validarCredencial';
create user 'admin'@'localhost' identified with mysql_native_password by'admin';
create user 'cmorbla'@'localhost' identified with mysql_native_password by 'cmorbla';
create user 'userListarAgendas'@'localhost' identified with mysql_native_password by 'listarAgendas';
grant select on gestionAgenda.administrador to "usuarioValidarCredencial"@"localhost";
grant select on gestionAgenda.usuario to "usuarioValidarCredencial"@"localhost";
grant select on gestionAgenda.contacto to "cmorbla"@"localhost";
grant insert on gestionAgenda.contacto to "cmorbla"@"localhost";
grant update on gestionAgenda.contacto to "cmorbla"@"localhost";
grant delete on gestionAgenda.contacto to "cmorbla"@"localhost";
grant file on *.* to "cmorbla"@"localhost";
grant select on gestionAgenda.agenda to "cmorbla"@"localhost";
#grant all privileges on gestionAgenda to "admin"@"localhost";
grant select on gestionAgenda.agenda to "userListarAgendas"@"localhost";
grant select on gestionAgenda.usuario to "userListarAgendas"@"localhost";




flush privileges;

insert into usuario values("cmorbla","cmorbla");
insert into administrador values("admin","admin");
insert into agenda values(1,"agenda personal de carlos morais","cmorbla");
insert into contacto values("Carlos","Morais Blanco","c/ Barros, 19","carlosmorais@gmail.com","615-79-83-30",1);