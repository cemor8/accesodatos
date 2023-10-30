CREATE DATABASE libreria;
USE libreria;
CREATE TABLE libro(
	id_libro int not null,
    titulo varchar(30),
    num_paginas int,
    fecha_lanzamiento date,
    codigo_autor int,
    primary key (id_libro),
    foreign key(codigo_autor)references autor(nombre)
);
CREATE TABLE autor(
	id_autor int not null,
    nombre varchar(30),
    num_paginas int,
    fecha_nacimiento date,
    primary key (id_autor)
);