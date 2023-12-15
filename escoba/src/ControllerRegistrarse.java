import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControllerRegistrarse {
    private String nombre_admin = "admin_escoba";
    private String password_admin = "admin";
    private ArrayList<String> nombresUsuarios = new ArrayList<>();
    Map<String, String> columnasExpresiones = new HashMap<String, String>() {
        {
            put("nombre_usuario", "^[a-zA-Z0-9_]{3,15}$");
            put("clave", "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$");
            put("nombre", "^.{1,30}$");
        }

    };
    public void registrarse() {
        this.obtenerNombresUsuarios();
        while (true){
            String nombreUsuario = this.devolverString("Introduce el nombre para tu usauario ", this.columnasExpresiones.get("nombre_usuario"), true);
            if(this.nombresUsuarios.isEmpty()){
                this.crearUsuario(nombreUsuario);
                return;
            }else {
                Optional<String> nombreOptional = this.nombresUsuarios.stream().filter(s -> s.equalsIgnoreCase(nombreUsuario)).findAny();
                if(nombreOptional.isPresent()){
                    System.out.println("Nombre de usuario existente");
                }else {
                    this.crearUsuario(nombreUsuario);
                }
            }
        }

    }
    public void crearUsuario(String nombreUsuario){
        String passwordUsuario = this.devolverString("Introduce la contraseña ", this.columnasExpresiones.get("clave"), true);

        Conexion conexion = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            conexion = new Conexion();
            connection = conexion.hacerConexion(this.nombre_admin, this.password_admin,false);

            String insertSQL = "INSERT INTO usuario (nombre_usuario, clave) VALUES (?, ?)";

            preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setString(1,nombreUsuario);
            preparedStatement.setString(2,passwordUsuario);
            preparedStatement.executeUpdate();

             insertSQL = "INSERT INTO clasificacion (partidas_ganadas, puntos_oros, puntos_escobas, " +
                     "puntos_velo, puntos_cantidad_cartas, nombre_usuario) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";;

            preparedStatement = connection.prepareStatement(insertSQL);

            preparedStatement.setInt(1, 0);
            preparedStatement.setInt(2, 0);
            preparedStatement.setInt(3, 0);
            preparedStatement.setInt(4, 0);
            preparedStatement.setInt(5, 0);
            preparedStatement.setString(6, nombreUsuario);
            preparedStatement.executeUpdate();
            System.out.println("Usuario creado con éxito");
            preparedStatement.close();
            conexion.cerrarConexion();
            ControllerLogin controllerLogin = new ControllerLogin();
            controllerLogin.mostrarOpcionesLogin();
        }catch (SQLException err){
            System.out.println(err.getErrorCode());
        }finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException err) {
                System.out.println(err.getMessage());
            }

            if (conexion != null) {
                conexion.cerrarConexion();
            }
        }
    }

    public void obtenerNombresUsuarios(){

        Conexion conexion = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            conexion = new Conexion();
            connection = conexion.hacerConexion(this.nombre_admin, this.password_admin,false);
            String insertSQL = "select nombre_usuario from escoba.usuario";
            preparedStatement = connection.prepareStatement(insertSQL);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String usuario = resultSet.getString("nombre_usuario");
                this.nombresUsuarios.add(usuario);

            }
            preparedStatement.close();
            conexion.cerrarConexion();
        }catch (SQLException err){
            System.out.println(err.getErrorCode());
        }finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException err) {
                System.out.println(err.getMessage());
            }

            if (conexion != null) {
                conexion.cerrarConexion();
            }
        }
    }

    /**
     * Método que pide una string por pantalla, si hay patron comprueba que el
     * texto cumpla los requisitos, pero si el texto introducido es null continua, aun que si
     * el texto introducido es null pero requerido es true, no valdra y tendras que cumplir los requisitos
     *
     * @param patron    expresion regular a validar
     * @param texto     texto a mostrar por pantalla
     * @param requerido si es texto debe cumplir los requisitos si este es null.
     */
    public String devolverString(String texto, String patron, boolean requerido) {
        String stringDevolver = null;
        while (stringDevolver == null) {
            System.out.println(texto);
            Scanner stringDevolverIn = new Scanner(System.in);
            try {
                stringDevolver = stringDevolverIn.nextLine();
                if (requerido && patron != null && !validarDatos(patron, stringDevolver)) {
                    throw new Exception("Contenido invalido");
                } else if ((patron != null && !validarDatos(patron, stringDevolver)) && !stringDevolver.equalsIgnoreCase("null")) {
                    throw new Exception("Contenido invalido");
                }
            } catch (Exception err) {
                System.out.println("Contenido inválido");
                stringDevolver = null;
            }
        }
        return stringDevolver;
    }
    /**
     * Método que se encarga de validar los datos para que se cumpla la
     * expresion regular.
     *
     * @param patronCumplir patron a cumplir
     * @param textoBuscar   string donde buscar el patron
     */
    public boolean validarDatos(String patronCumplir, String textoBuscar) {
        Pattern patron = Pattern.compile(patronCumplir);
        Matcher matcher = patron.matcher(textoBuscar);
        return matcher.matches();
    }
}
