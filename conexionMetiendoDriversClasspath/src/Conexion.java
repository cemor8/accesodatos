import java.sql.*;
import java.util.ArrayList;

public class Conexion {
    private static Connection conexionStatica = null;
    public static Connection getConnection(String nombreBase,String puerto,String usuario,String passw) throws ClassNotFoundException {

        if (conexionStatica == null) {
            String jdbcUrl;
            if(nombreBase==null){
                 //jdbcUrl = "jdbc:mysql://localhost:"+puerto+"/";
                    jdbcUrl= "jdbc:mysql://localhost";
            }else {
                 //jdbcUrl = "jdbc:mysql://localhost:"+puerto+"/"+nombreBase;
                jdbcUrl= "jdbc:mysql://localhost";
            }
            try {
                Connection connection = DriverManager.getConnection(jdbcUrl, usuario, passw);
                conexionStatica=connection;
            } catch (Exception err) {
                System.out.println(err.getMessage());
            }
        }
        return conexionStatica;
    }

    public static void crearBaseDatos(){
        //String puerto=Controller.pideString("introduce un puerto para conectarte");
        String usuario=Controller.pideString("Introduce un usuario");
        String passw=Controller.pideString("Introduce contraseña del usuario");
        try{
            Conexion.getConnection(null,null,usuario,passw);
            Statement statement = conexionStatica.createStatement();
            String nombreBase=Controller.pideString("introduce el nombre para la base de datos");
            String crearBaseDeDatosSQL = "CREATE DATABASE IF NOT EXISTS "+nombreBase;
            statement.executeUpdate(crearBaseDeDatosSQL);
            System.out.println("Base de datos "+nombreBase+" creada con éxito.");
            statement.execute("USE "+nombreBase);
            cerrarConexion();
            statement.close();
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public static void crearTabla() throws SQLException {
        String puerto=Controller.pideString("introduce un puerto para conectarte");
        String usuario=Controller.pideString("Introduce un usuario");
        String passw=Controller.pideString("Introduce contraseña del usuario");
        String nombreBase=Controller.pideString("introduce el nombre para la base de datos");
        String nombre_tabla=Controller.pideString("Introduce un nombre para la tabla");
        String datos_tabla=Controller.pideString("Introduce las columnas de la tabla (por ejemplo: 'columna1 INT, columna2 VARCHAR(255)')");
        try {
            Conexion.getConnection(nombreBase,puerto,usuario,passw);
            Statement statement=conexionStatica.createStatement();
            String crearTablaSQL = "CREATE TABLE " + nombre_tabla + " (" + datos_tabla + ")";
            statement.executeUpdate(crearTablaSQL);
            System.out.println("tabla "+nombre_tabla+" creada con exito");
            Conexion.cerrarConexion();
            statement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            cerrarConexion();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
    public static void modificarTabla() throws SQLException {
        String puerto=Controller.pideString("introduce un puerto para conectarte");
        String usuario=Controller.pideString("Introduce un usuario");
        String passw=Controller.pideString("Introduce contraseña del usuario");
        String nombreBase=Controller.pideString("introduce el nombre para la base de datos");
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
        try {
            Conexion.getConnection(nombreBase,puerto,usuario,passw);
            Statement statement = conexionStatica.createStatement();
            statement.executeUpdate(columnaSQL);
            cerrarConexion();
            statement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            cerrarConexion();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static void introducirDatos() throws SQLException {
        String puerto=Controller.pideString("introduce un puerto para conectarte");
        String usuario=Controller.pideString("Introduce un usuario");
        String passw=Controller.pideString("Introduce contraseña del usuario");
        String nombreBase=Controller.pideString("introduce el nombre para la base de datos");
        String nombre_tabla=Controller.pideString("Introduce el nombre de la tabla");

        try  {
            Conexion.getConnection(nombreBase,puerto,usuario,passw);
            Statement statement=conexionStatica.createStatement();
            DatabaseMetaData metaData = conexionStatica.getMetaData();
            ResultSet columnas = metaData.getColumns(null, null, nombre_tabla, null);
            StringBuilder insertSQL = new StringBuilder("INSERT INTO " + nombre_tabla + " (titulo, autor, fecha_lanzamiento, numero_de_paginas) VALUES (");
            ResultSetMetaData metaDataColumnas = conexionStatica.createStatement().executeQuery("SELECT * FROM " + nombre_tabla + " LIMIT 0").getMetaData();
            int numeroDeColumnas = metaDataColumnas.getColumnCount();
            System.out.println(numeroDeColumnas);
            while (columnas.next()) {
                String nombreColumna = columnas.getString("COLUMN_NAME");
                String tipoDato = columnas.getString("TYPE_NAME");
                String datoIntroducir=Controller.pideString("Introduce dato para " + nombreColumna+" escribe null para no introducir nada");
                if (datoIntroducir.equalsIgnoreCase("null") && tipoDato.equalsIgnoreCase("int")){
                    insertSQL.append(Types.INTEGER);
                } else if (datoIntroducir.equalsIgnoreCase("null")) {
                    insertSQL.append("");
                }
                insertSQL.append("'").append(datoIntroducir).append("'");
                if (numeroDeColumnas>1){
                    insertSQL.append(", ");
                    numeroDeColumnas-=1;
                }else {
                    insertSQL.append(")");
                }

            }
            System.out.println(insertSQL);
            statement.executeUpdate(String.valueOf(insertSQL));
            System.out.println("datos introducidos correctamente");
        }catch (Exception err){
            System.out.println(err.getMessage());
        }
    }
    public static void realizarConsulta(){
        String puerto=Controller.pideString("introduce un puerto para conectarte");
        String usuario=Controller.pideString("Introduce un usuario");
        String passw=Controller.pideString("Introduce contraseña del usuario");
        String nombreBase=Controller.pideString("introduce el nombre para la base de datos");
        try  {
            String consulta = Controller.pideString("Introduce consulta");
            Conexion.getConnection(nombreBase,puerto,usuario,passw);
            Statement statement=conexionStatica.createStatement();
            ResultSet resultSet = statement.executeQuery(consulta);
            ArrayList<Object[]> resultados=new ArrayList<>();
            while (resultSet.next()){
                int numeroColumnas = resultSet.getMetaData().getColumnCount();
                Object[] fila = new Object[numeroColumnas];

                for (int i = 0; i < numeroColumnas; i++) {
                    fila[i] = resultSet.getObject(i);
                }
                resultados.add(fila);
            }
            for (Object[] fila : resultados) {
                for (Object valor : fila) {
                    System.out.print(valor + "\t");
                }
                System.out.println();
            }
        }catch (Exception err){
            System.out.println(err.getMessage());
        }
    }

    public  static  void  recibirLibros(){
        ArrayList<Libro> libros=new ArrayList<>();
        String puerto=Controller.pideString("introduce un puerto para conectarte");
        String usuario=Controller.pideString("Introduce un usuario");
        String passw=Controller.pideString("Introduce contraseña del usuario");
        String nombreBase=Controller.pideString("introduce el nombre para la base de datos");
        String nombre_tabla=Controller.pideString("Introduce el nombre de la tabla");
        try  {
            String consulta = "SELECT titulo, autor, fecha_lanzamiento, numero_de_paginas FROM "+nombre_tabla;
            Conexion.getConnection(nombreBase,puerto,usuario,passw);
            Statement statement=conexionStatica.createStatement();
            ResultSet resultSet = statement.executeQuery(consulta);
            while (resultSet.next()){
                Libro libro=new Libro(resultSet.getString("titulo"),
                        resultSet.getString("autor"),
                        resultSet.getInt("numero_de_paginas"),
                        resultSet.getString("fecha_lanzamiento"));
                libros.add(libro);

            }
            Controller.añadirLibro(libros);
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
