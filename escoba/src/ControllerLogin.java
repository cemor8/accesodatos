import java.sql.*;
import java.util.Scanner;

public class ControllerLogin {
    /**
     * Método que se encarga de mostrar el login y comprobar la existencia del usuario o administrador para
     * seguir usando la aplicación.
     */
    public void mostrarOpcionesLogin() {
        Integer opcion = null;
        while (opcion == null) {
            opcion = devolverInteger("\n1. Login \n2. Registrarse");
            String usuario;
            String password;
            //dependiendo del login se buscan los datos en un tabla y luego se muestra el controller correspondiente
            switch (opcion) {
                case 1:
                    usuario = this.devolverString("\nIntroduce nombre de usuario");
                    password = this.devolverString("\nIntroduce contraseña");
                    if (!comprobarCredenciales(usuario, password, "usuario")) {
                        System.out.println("Credenciales incorrectos");
                        opcion = null;
                        break;
                    }
                    ControllerUsuario controllerUsuario = new ControllerUsuario(new Usuario(usuario, password,null));
                    controllerUsuario.mostrarMenu();
                    break;
                case 2:
                    ControllerRegistrarse controllerRegistrarse = new ControllerRegistrarse();
                    controllerRegistrarse.registrarse();
                default:
                    opcion = null;
                    break;
            }
        }
    }

    /**
     * Método que se encarga de verificar la existencia de una base de datos con el nombre
     * gestionagenda para operar con ella, devuelve true o false en funcion de si existe o no.
     */
    public boolean verificarExistenciaBase() {
        Conexion conexion = new Conexion();
        Connection connection = conexion.hacerConexion("root", "",false);
        return connection != null;
    }

    /**
     * Método que se encarga de crear la base de datos si no existe en el sistema.
     */
    public void crearBase() {
        Conexion conexion = null;
        Connection connection = null;
        Statement statement = null;

        try {
            conexion = new Conexion();
            connection = conexion.hacerConexion("root", "",true);

            statement = connection.createStatement();

            String crearBaseDatos = "CREATE DATABASE IF NOT EXISTS escoba";
            statement.executeUpdate(crearBaseDatos);


            String usarBaseDatos = "USE escoba";
            statement.executeUpdate(usarBaseDatos);

            //crear y usuarios

            String[] createQueries = {
                    "CREATE DATABASE IF NOT EXISTS escoba",
                    "USE escoba",
                    "CREATE TABLE IF NOT EXISTS usuario (nombre_usuario VARCHAR(15) NOT NULL, clave VARCHAR(15) NOT NULL, PRIMARY KEY(nombre_usuario))",
                    "CREATE TABLE IF NOT EXISTS clasificacion (id_clasificacion INT AUTO_INCREMENT, partidas_ganadas INT NOT NULL, puntos_oros INT NOT NULL, puntos_escobas INT NOT NULL, puntos_velo INT NOT NULL, puntos_sietes INT NOT NULL, puntos_cantidad_cartas INT NOT NULL, nombre_usuario VARCHAR(15) NOT NULL, PRIMARY KEY(id_clasificacion), FOREIGN KEY(nombre_usuario) REFERENCES usuario(nombre_usuario))",
                    "CREATE USER IF NOT EXISTS 'admin_escoba'@'localhost' IDENTIFIED BY 'admin'",
                    "GRANT ALL PRIVILEGES ON *.* TO 'admin_escoba'@'localhost'",
                    "GRANT GRANT OPTION ON *.* TO 'admin_escoba'@'localhost'",
                    "GRANT CREATE USER ON *.* TO 'admin_escoba'@'localhost' WITH GRANT OPTION",
                    "GRANT DROP ON *.* TO 'admin_escoba'@'localhost'",
                    "FLUSH PRIVILEGES"
            };

            for (String query : createQueries) {
                statement.executeUpdate(query);
            }
            System.out.println("Base de datos creada con éxito");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {

                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
                if (conexion != null) {
                    conexion.cerrarConexion();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Método que se encarga de comprobar las credenciales del usuario introducido para validar que existe.
     *
     * @param nombreUsuario     nombre del usuario a validar
     * @param passwordUsuario   contraseña a validar
     * @param nombreTablaBuscar donde buscar el usuario
     */
    public boolean comprobarCredenciales(String nombreUsuario, String passwordUsuario, String nombreTablaBuscar) {
        Conexion conexion = null;
        Connection connection = null;
        try {
            //obtener los usuarios de la tabla admin si se va a iniciar sesion con un admin o de la tabla usuarios si se va a usar un usuario
            conexion = new Conexion();
            connection = conexion.hacerConexion("admin_escoba", "admin",false);
            String consulta = "SELECT clave from escoba." + nombreTablaBuscar + " where nombre_usuario like ?";
            PreparedStatement statement = connection.prepareStatement(consulta);
            statement.setString(1, "%" + nombreUsuario + "%");
            ResultSet resultados = statement.executeQuery();
            if (resultados.next()) {
                String clave = resultados.getString("clave");
                if (!clave.equals(passwordUsuario)) {
                    return false;
                }
                conexion.cerrarConexion();
                statement.close();
                return true;
            }
        } catch (SQLException err) {
            System.out.println(err.getMessage());
        } finally {
            if (conexion != null) {
                conexion.cerrarConexion();
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException err) {
                    System.out.println(err.getMessage());
                }

            }
        }
        return false;
    }

    /**
     * Método que se encarga de pedir un integer por terminal y devolverlo
     *
     * @param texto texto a mostrar
     */
    public Integer devolverInteger(String texto) {
        Integer integerDevolver = null;
        while (integerDevolver == null) {
            System.out.println(texto);
            Scanner integerDevolverIn = new Scanner(System.in);
            try {
                integerDevolver = integerDevolverIn.nextInt();
            } catch (Exception err) {
                System.out.println("Opcion inválida");
            }
        }
        return integerDevolver;
    }

    /**
     * Método que se encarga de pedir una string por terminal y devolverla
     *
     * @param texto texto a mostrar.
     */
    public String devolverString(String texto) {
        String stringDevolver = null;
        while (stringDevolver == null) {
            System.out.println(texto);
            Scanner stringDevolverIn = new Scanner(System.in);
            try {
                stringDevolver = stringDevolverIn.nextLine();
            } catch (Exception err) {
                System.out.println("Contenido inválido");
            }
        }
        return stringDevolver;
    }

}
