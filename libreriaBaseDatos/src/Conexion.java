import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Conexion {
    private static Connection conexionStatica = null;
    private static String nombreBasePrivada = "libreria";

    public static Connection getConnection() {
        String usuario = Controller.pideString("Introduce un usuario");
        String passw = Controller.pideString("Introduce contraseña del usuario");
        String puerto = Controller.pideString("introduce un puerto para conectarte");
        if (conexionStatica == null) {
            String jdbcUrl;
            if (nombreBasePrivada == null) {
                jdbcUrl = "jdbc:mysql://localhost:" + puerto + "/";
            } else {
                jdbcUrl = "jdbc:mysql://localhost:" + puerto + "/" + nombreBasePrivada;
            }
            System.out.println(jdbcUrl);
            try {
                Connection connection = DriverManager.getConnection(jdbcUrl, usuario, passw);
                conexionStatica = connection;
            } catch (Exception err) {
                System.out.println(err.getMessage());
            }
        }
        return conexionStatica;
    }

    public static void crearBaseDatos() {
        try {
            Conexion.getConnection();
            Statement statement = conexionStatica.createStatement();
            String nombreBase = Controller.pideString("introduce el nombre para la base de datos");
            String crearBaseDeDatosSQL = "CREATE DATABASE IF NOT EXISTS " + nombreBase;
            statement.executeUpdate(crearBaseDeDatosSQL);
            System.out.println("Base de datos " + nombreBase + " creada con éxito.");
            nombreBasePrivada = nombreBase;
            crearTabla();
            cerrarConexion();
            statement.close();
        } catch (Exception err) {
            System.out.println(err.getMessage());
        }
    }

    public static void modificarTabla() {
        try {
            Conexion.getConnection();
            Statement statement = conexionStatica.createStatement();
            String modificarBaseDatos = Controller.pideString("Introduce sentencia para modificar tabla");
            statement.executeUpdate(modificarBaseDatos);
            System.out.println("Tabla libro modificada");
            cerrarConexion();
            statement.close();
        } catch (Exception err) {
            System.out.println(err.getMessage());
        }
    }

    public static void crearTabla() {
        try {
            Statement statement = conexionStatica.createStatement();
            statement.executeUpdate("USE "+nombreBasePrivada);
            String crearTablaSQL = "CREATE TABLE " + "libro" + " ( id int not null, titulo varchar(30), autor varchar(30), fecha_lanzamiento date, numero_paginas int, primary key (id))";
            statement.executeUpdate(crearTablaSQL);
            System.out.println("Crearda tabla libro con exito");
        } catch (Exception err) {
            System.out.println(err.getMessage());
        }

    }

    public static void introducirDatos() {
        try {
            System.out.println(nombreBasePrivada);
            Conexion.getConnection();
            Statement statement = conexionStatica.createStatement();
            DatabaseMetaData metaData = conexionStatica.getMetaData();
            ResultSet columnasLibro = metaData.getColumns(null, null, "libro", null);
            ResultSetMetaData metaDataColumnas = conexionStatica.createStatement().executeQuery("SELECT * FROM " + "libro" + " LIMIT 0").getMetaData();
            int numeroDeColumnas = metaDataColumnas.getColumnCount();
            StringBuilder insertSQL = new StringBuilder("INSERT INTO " + "libro" + " (titulo, autor, fecha_lanzamiento, numero_de_paginas) VALUES (");
            while (columnasLibro.next()) {
                String nombreColumna = columnasLibro.getString("COLUMN_NAME");
                String tipoDato = columnasLibro.getString("TYPE_NAME");
                String datoIntroducir = Controller.pideString("Introduce dato para " + nombreColumna + " escribe null para no introducir nada");
                if (datoIntroducir.equalsIgnoreCase("null")) {
                    if (tipoDato.toLowerCase().contains("varchar") ||tipoDato.toLowerCase().contains("date")) {
                        insertSQL.append("");

                    } else {
                        insertSQL.append("null");
                    }
                }else {
                    insertSQL.append("'").append(datoIntroducir).append("'");
                }
                if (numeroDeColumnas > 1) {
                    insertSQL.append(", ");
                    numeroDeColumnas -= 1;
                } else {
                    insertSQL.append(")");
                }

            }
            System.out.println(insertSQL);
            statement.executeUpdate(String.valueOf(insertSQL));
            System.out.println("datos introducidos correctamente");
            crearTabla();
            cerrarConexion();
        } catch (Exception err) {
            System.out.println(err.getMessage());
        }
    }
    public static void cerrarConexion () throws SQLException {
        if (conexionStatica != null) {
            conexionStatica.close();
            conexionStatica = null;
        }
    }

    public static void consulta() throws SQLException {
        ArrayList<ArrayList<Object>> libros = new ArrayList<>();
        try {
            Conexion.getConnection();
            String consulta = Controller.pideString("introduce consulta");
            Statement statement = conexionStatica.createStatement();
            ResultSet resultSet = statement.executeQuery(consulta);
            while (resultSet.next()) {
                int numeroColumnas = resultSet.getMetaData().getColumnCount();
                ArrayList<Object> filaDatos = new ArrayList<>();
                for (int i = 0; i < numeroColumnas; i++) {
                    filaDatos.add(resultSet.getObject(i));
                }
                libros.add(filaDatos);
            }
            for (ArrayList<Object> fila : libros) {
                for (Object valor : fila) {
                    System.out.println(valor + "\n");
                }
            }

        } catch (Exception err) {
            System.out.println(err.getMessage());
        }
    }
}