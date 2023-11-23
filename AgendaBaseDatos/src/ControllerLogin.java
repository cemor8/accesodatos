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
            opcion = devolverInteger("\n1. Login Administradores \n2. Login Usuarios");
            String usuario;
            String password;
            //dependiendo del login se buscan los datos en un tabla y luego se muestra el controller correspondiente
            switch (opcion) {
                case 1:
                    usuario = this.devolverString("\nIntroduce nombre de usuario");
                    password = this.devolverString("\nIntroduce contraseña");
                    if (!comprobarCredenciales(usuario, password, "administrador")) {
                        System.out.println("Credenciales incorrectos");
                        opcion = null;
                        break;
                    }
                    ControllerAdmin controllerAdmin = new ControllerAdmin(new Administrador(usuario, password));
                    controllerAdmin.mostrarMenu();
                    break;
                case 2:
                    usuario = this.devolverString("\nIntroduce nombre de usuario");
                    password = this.devolverString("\nIntroduce contraseña");
                    if (!comprobarCredenciales(usuario, password, "usuario")) {
                        System.out.println("Credenciales incorrectos");
                        opcion = null;
                        break;
                    }
                    ControllerUsuario controllerUsuario = new ControllerUsuario(new Usuario(usuario, password));
                    controllerUsuario.mostrarMenu();
                    break;
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

            String crearBaseDatos = "CREATE DATABASE IF NOT EXISTS gestionagenda";
            statement.executeUpdate(crearBaseDatos);


            String usarBaseDatos = "USE gestionagenda";
            statement.executeUpdate(usarBaseDatos);
            //crear tablas

            String[] createQueries = {
                    "CREATE TABLE IF NOT EXISTS usuario (nombre_usuario VARCHAR(15) NOT NULL, clave VARCHAR(15) NOT NULL, PRIMARY KEY(nombre_usuario))",
                    "CREATE TABLE IF NOT EXISTS administrador (nombre_usuario VARCHAR(15) NOT NULL, clave VARCHAR(15) NOT NULL, PRIMARY KEY(nombre_usuario))",
                    "CREATE TABLE IF NOT EXISTS agenda (id_agenda INT NOT NULL, nombre VARCHAR(30) NOT NULL, nombre_usuario VARCHAR(15), PRIMARY KEY(id_agenda), FOREIGN KEY(nombre_usuario) REFERENCES usuario(nombre_usuario))",
                    "CREATE TABLE IF NOT EXISTS contacto (nombre VARCHAR(20), apellidos VARCHAR(30), direccion VARCHAR(25), correo VARCHAR(30), telefono VARCHAR(12) NOT NULL, id_agenda INT NOT NULL, PRIMARY KEY(telefono), FOREIGN KEY(id_agenda) REFERENCES agenda(id_agenda))"
            };

            for (String query : createQueries) {
                statement.executeUpdate(query);
            }
            //crear usuarios y asignar permisos

            String[] queries = {
                    "create user 'usuarioValidarCredencial'@'localhost' identified with mysql_native_password by 'validarCredencial'",
                    "create user 'admin'@'localhost' identified with mysql_native_password by 'admin'",
                    "create user 'userListarAgendas'@'localhost' identified with mysql_native_password by 'listarAgendas'",
                    "grant select on gestionagenda.administrador to 'usuarioValidarCredencial'@'localhost'",
                    "grant select on gestionagenda.usuario to 'usuarioValidarCredencial'@'localhost'",
                    "grant all privileges on gestionagenda.* to 'admin'@'localhost'",
                    "grant select on gestionagenda.agenda to 'userListarAgendas'@'localhost'",
                    "grant select on gestionagenda.usuario to 'userListarAgendas'@'localhost'",
                    "insert into gestionagenda.administrador values ('admin', 'admin')",
                    "GRANT CREATE USER ON *.* TO 'admin'@'localhost'",
                    "GRANT GRANT OPTION ON *.* TO 'admin'@'localhost'",
                    "GRANT UPDATE ON gestionagenda.* TO 'admin'@'localhost'",
                    "GRANT DROP ON gestionagenda.* TO 'admin'@'localhost'",
                    "GRANT DROP USER ON *.* TO 'admin'@'localhost'",
                    "FLUSH PRIVILEGES"
            };

            for (String query : queries) {
                statement.executeUpdate(query);
            }
            System.out.println("Base de datos creada con éxito, administrador : ");
            System.out.println("Nombre usuario admin");
            System.out.println("Contraseña admin");
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
            connection = conexion.hacerConexion("usuarioValidarCredencial", "validarCredencial",false);
            String consulta = "SELECT clave from " + nombreTablaBuscar + " where nombre_usuario like ?";
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
