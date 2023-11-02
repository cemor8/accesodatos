package org.example;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Conexion {
    private static Connection conexionStatica = null;
    public static Connection getConnection(String nombreBase,String puerto,String usuario,String passw) throws ClassNotFoundException {

        if (conexionStatica == null) {
            String jdbcUrl;
            if(nombreBase==null){
                 jdbcUrl = "jdbc:mysql://localhost:"+puerto+"/";
            }else {
                 jdbcUrl = "jdbc:mysql://localhost:"+puerto+"/"+nombreBase;
            }
            try (Connection connection = DriverManager.getConnection(jdbcUrl, usuario, passw)){
                conexionStatica=connection;
            } catch (Exception err) {
                System.out.println(err.getMessage());
            }
        }
        return conexionStatica;
    }

    public static void crearBaseDatos(){
        String puerto=Controller.pideString("introduce un puerto para conectarte");
        String usuario=Controller.pideString("Introduce un usuario");
        String passw=Controller.pideString("Introduce contraseña del usuario");
        try {
            Conexion.getConnection(null,puerto,usuario,passw);
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try(Statement statement = conexionStatica.createStatement()) {
            String nombreBase=Controller.pideString("introduce el nombre para la base de datos");
            String crearBaseDeDatosSQL = "CREATE DATABASE IF NOT EXISTS "+nombreBase;
            statement.executeUpdate(crearBaseDeDatosSQL);
            System.out.println("Base de datos "+nombreBase+" creada con éxito.");
            statement.execute("USE "+nombreBase);
            cerrarConexion();
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public static void crearTabla() throws SQLException {
        String puerto=Controller.pideString("introduce un puerto para conectarte");
        String usuario=Controller.pideString("Introduce un usuario");
        String passw=Controller.pideString("Introduce contraseña del usuario");
        String nombreBase=Controller.pideString("introduce el nombre para la base de datos");
        try {
            Conexion.getConnection(nombreBase,puerto,usuario,passw);
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
            cerrarConexion();
        }
        String nombre_tabla=Controller.pideString("Introduce un nombre para la tabla");
        String datos_tabla=Controller.pideString("Introduce las columnas de la tabla (por ejemplo: 'columna1 INT, columna2 VARCHAR(255)')");
        try(Statement statement=conexionStatica.createStatement()) {
            String crearTablaSQL = "CREATE TABLE " + nombre_tabla + " (" + datos_tabla + ")";
            statement.executeUpdate(crearTablaSQL);
            System.out.println("tabla "+nombre_tabla+" creada con exito");
            Conexion.cerrarConexion();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            cerrarConexion();
        }

    }
    public static void modificarTabla() throws SQLException {
        String puerto=Controller.pideString("introduce un puerto para conectarte");
        String usuario=Controller.pideString("Introduce un usuario");
        String passw=Controller.pideString("Introduce contraseña del usuario");
        String nombreBase=Controller.pideString("introduce el nombre para la base de datos");
        try {
            Conexion.getConnection(nombreBase,puerto,usuario,passw);
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
            cerrarConexion();
        }
        String nombre_tabla=Controller.pideString("Introduce un nombre para la tabla");
        String nombre_columna=Controller.pideString("Introduce el nombre de la columna");
        Integer opcion=Controller.pideInteger("1. Añadir una columna\n2. Eliminar una columna");
        String columnaSQL ;
        switch (opcion){
            case 1:
                columnaSQL = "ALTER TABLE "+nombre_tabla+" ADD COLUMN "+nombre_columna;
                break;
            case 2:
                String tipoDato=Controller.pideString("Introduce el tipo de dato de la nueva columna");
                if(tipoDato.equalsIgnoreCase("date") || (tipoDato.equalsIgnoreCase("int") || tipoDato.substring(0, 7).equalsIgnoreCase("varchar"))){
                    columnaSQL = "ALTER TABLE "+nombre_tabla+" "+nombre_columna+" "+tipoDato;
                    break;
                }
                System.out.println("tipo de dato no soportado");
                return;
            default:
                System.out.println("error al introducir la opcion, vuelva a empezar");
                return;
        }
        try (Statement statement = conexionStatica.createStatement();){
            statement.executeUpdate(columnaSQL);
            cerrarConexion();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            cerrarConexion();
        }
    }
    public static void introducirDatos() throws SQLException {
        String puerto=Controller.pideString("introduce un puerto para conectarte");
        String usuario=Controller.pideString("Introduce un usuario");
        String passw=Controller.pideString("Introduce contraseña del usuario");
        String nombreBase=Controller.pideString("introduce el nombre para la base de datos");
        try {
            Conexion.getConnection(nombreBase,puerto,usuario,passw);
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
            cerrarConexion();
        }
        String nombre_tabla=Controller.pideString("Introduce el nombre de la tabla");

        try  {
            DatabaseMetaData metaData = conexionStatica.getMetaData();

            ResultSet columnas = metaData.getColumns(null, null, nombre_tabla, null);
            StringBuilder insertSQL = new StringBuilder("INSERT INTO " + nombre_tabla + " VALUES (");
            while (columnas.next()) {
                String nombreColumna = columnas.getString("COLUMN_NAME");
                String tipoDato = columnas.getString("TYPE_NAME");
                String datoIntroducir=Controller.pideString("Introduce dato para " + nombreColumna+" escribe null para no introducir nada");
                if (datoIntroducir.equalsIgnoreCase("null") && tipoDato.equalsIgnoreCase("int")){
                    insertSQL.append(java.sql.Types.INTEGER);
                } else if (datoIntroducir.equalsIgnoreCase("null")) {
                    insertSQL.append("");
                }

                insertSQL.append(datoIntroducir);
                insertSQL.append(", ");
            }
            insertSQL.append(")");
        }catch (Exception err){
            System.out.println(err.getMessage());
        }
    }
    public static void cerrarConexion() throws SQLException {
        if(conexionStatica!=null){
            conexionStatica.close();
            conexionStatica=null;
        }

    }
}
