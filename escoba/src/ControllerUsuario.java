import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControllerUsuario {
    Map<String, String> columnasExpresiones = new HashMap<String, String>() {
        {
            put("nombre_usuario", "^[a-zA-Z0-9_]{3,15}$");
            put("clave", "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$");
        }

    };
    private Usuario usuario;
    private ArrayList<Clasificacion> clasificaciones = new ArrayList<>();

    public ControllerUsuario(Usuario usuario) {
        this.usuario = usuario;
        this.obtenerClasificaciones();
    }
    public void mostrarMenu(){
        Integer opcion = null;
        while (opcion == null){
            boolean borrar = false;
            Scanner opcionIN = new Scanner(System.in);
            System.out.println("1. Ver perfil");
            System.out.println("2. Ver ranking");
            System.out.println("3. Jugar");
            System.out.println("4. Borrar Cuenta");
            System.out.println("5. Salir");
            try {
                opcion = opcionIN.nextInt();

            }catch (Exception err){
                System.out.println(err.getMessage());
                continue;
            }
            switch (opcion){
                case 1:
                    this.mostrarPerfil();
                    opcion = null;
                    break;
                case 2:
                    this.opcionesRanking();
                    opcion = null;
                    break;
                case 3:
                    this.jugar();
                    this.obtenerClasificaciones();
                    opcion = null;
                    break;
                case 4:
                    this.borrarCuenta();
                    borrar = true;
                    break;
                case 5:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opcion Inválida");
                    opcion = null;
                    break;
            }
            if(borrar){
                break;
            }
        }
        ControllerLogin controllerLogin = new ControllerLogin();
        controllerLogin.mostrarOpcionesLogin();
    }
    public void jugar(){
        Integer puntos = this.devolverInteger("Introduce los puntos para ganar la partida");
        IA ia = new IA(new ArrayList<>());
        Baraja baraja = new Baraja();
        Jugador jugador = new Jugador(this.usuario,new ArrayList<>());
        Mesa mesa = new Mesa(jugador,ia,baraja,puntos);
        mesa.iniciarPartida(new Clasificacion(this.usuario.getNombreUsuario(),0,0,0,0,0,0,this.usuario));
    }

    public void borrarCuenta(){
        Conexion conexion = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        this.clasificaciones = new ArrayList<>();
        try {
            conexion = new Conexion();
            connection = conexion.hacerConexion("admin_escoba", "admin",false);
            String insertSQL = "delete from esoba.usuario where esoba.usuario.nombre_usuario = ?";
            preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setString(1,this.usuario.getNombreUsuario());
            preparedStatement.executeUpdate();

            insertSQL = "delete from esoba.clasificacion where esoba.clasificacion.nombre_usuario = ?";
            preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setString(1,this.usuario.getNombreUsuario());
            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();
            conexion.cerrarConexion();

        } catch (SQLException err) {
            System.out.println(err.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException err) {
                    System.out.println(err.getMessage());
                }
            }
            if (conexion != null) {
                conexion.cerrarConexion();
            }
        }
    }


    public void opcionesRanking(){
        Integer opcion = null;
        while (opcion == null){
            Scanner opcionIN = new Scanner(System.in);
            System.out.println("1. Ver posicion en ranking y estadisticas");
            System.out.println("2. Ver ranking global");
            System.out.println("3. Volver atras");
            try {
                opcion = opcionIN.nextInt();

            }catch (Exception err){
                System.out.println(err.getMessage());
                continue;
            }
            switch (opcion){
                case 1:
                    this.mostrarClasificaciones(true);
                    opcion=null;
                    break;
                case 2:
                    this.mostrarClasificaciones(false);
                    opcion=null;
                    break;
                case 3:
                    this.mostrarMenu();
                    break;
                default:
                    System.out.println("Opcion Inválida");
                    opcion = null;
                    break;
            }
        }
    }

    public void mostrarClasificaciones(boolean sola){
        int i = 1;
        for(Clasificacion clasificacion: this.clasificaciones){
            if(sola){
                if(this.usuario.getClasificacion().equals(clasificacion)){
                    System.out.println("\t"+i +" "+clasificacion);
                    break;
                }
                i++;
                continue;
            }
            System.out.println("\t"+i +" "+clasificacion);
            i++;
        }
    }

    public void obtenerClasificaciones(){
        Conexion conexion = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        this.clasificaciones = new ArrayList<>();
        try {
            conexion = new Conexion();
            connection = conexion.hacerConexion("admin_escoba", "admin",false);
            String updateSQL = "Select * from escoba.clasificacion";
            preparedStatement = connection.prepareStatement(updateSQL);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                int partidasGanadas = resultSet.getInt("partidas_ganadas");
                int puntosOros = resultSet.getInt("puntos_oros");
                int puntosEscobas = resultSet.getInt("puntos_escobas");
                int puntosVelo = resultSet.getInt("puntos_velo");
                int puntosCantidadCartas = resultSet.getInt("puntos_cantidad_cartas");
                int puuntosSietes = resultSet.getInt("puntos_sietes");
                String nombreUsuario = resultSet.getString("nombre_usuario");
                this.clasificaciones.add(new Clasificacion(nombreUsuario,partidasGanadas,puntosOros,puntosEscobas,puntosVelo, puntosCantidadCartas,puuntosSietes,null));
            }
            Optional<Clasificacion> clasificacionOptional = this.clasificaciones.stream().filter(clasificacion -> clasificacion.getNombre_usuario().equalsIgnoreCase(this.usuario.getNombreUsuario())).findAny();
            clasificacionOptional.ifPresent(clasificacion -> this.usuario.setClasificacion(clasificacion));
            this.clasificaciones.sort(Comparator.comparing(Clasificacion::getPartidas_ganadas).thenComparing(Clasificacion::getPuntos_escobas).thenComparing(Clasificacion::getPuntos_velo).reversed());
            preparedStatement.close();
            connection.close();
            conexion.cerrarConexion();
        } catch (SQLException err) {
            System.out.println(err.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException err) {
                    System.out.println(err.getMessage());
                }
            }
            if (conexion != null) {
                conexion.cerrarConexion();
            }
        }
    }

    public void mostrarPerfil(){
        Integer opcion = null;
        while (opcion == null){
            Scanner opcionIN = new Scanner(System.in);
            System.out.println("1. Ver Datos");
            System.out.println("2. Modificar Datos");
            System.out.println("3. Volver atras");
            try {
                opcion = opcionIN.nextInt();

            }catch (Exception err){
                System.out.println(err.getMessage());
                continue;
            }
            switch (opcion){
                case 1:
                    System.out.println(this.usuario);
                    opcion = null;
                    break;
                case 2:
                    this.modificarDatos();
                    this.obtenerClasificaciones();
                    opcion = null;
                    break;
                case 3:
                    this.mostrarMenu();
                    break;
                default:
                    System.out.println("Opcion Inválida");
                    opcion = null;
                    break;
            }
        }
    }
    public void modificarDatos(){
        String nombreAntiguo = this.usuario.getNombreUsuario();
        String nuevoNombre = this.devolverString("Introduce el nombre para tu usauario ", this.columnasExpresiones.get("nombre_usuario"), true);
        String nuevapassword = this.devolverString("Introduce la contraseña ", this.columnasExpresiones.get("clave"), true);
        Conexion conexion = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            conexion = new Conexion();
            connection = conexion.hacerConexion("admin_escoba", "admin",false);
            String updateSQL = "UPDATE escoba.usuario SET nombre_usuario = ?, clave = ? WHERE nombre_usuario = ?";
            preparedStatement = connection.prepareStatement(updateSQL);
            preparedStatement.setString(1,nuevoNombre);
            preparedStatement.setString(2,nuevapassword);
            preparedStatement.setString(3,nombreAntiguo);

            updateSQL = "UPDATE escoba.clasificacion SET nombre_usuario = ? WHERE nombre_usuario = ?";
            preparedStatement = connection.prepareStatement(updateSQL);
            preparedStatement.setString(1,nuevoNombre);
            preparedStatement.setString(2,nombreAntiguo);

            Statement statement = connection.createStatement();
            String crearUsuarioSQL = "CREATE USER \"" + nuevoNombre + "\"@\"localhost\" IDENTIFIED BY '" + nuevapassword + "'";
            statement.executeUpdate(crearUsuarioSQL);
            ArrayList<String> consultas = new ArrayList<>(List.of(
                    "grant select on escoba.clasificacion to " + "?" + "@" + "\"localhost\"",
                    "grant insert on escoba.clasificacion to " + "?" + "@" + "\"localhost\"",
                    "grant update on escoba.clasificacion to " + "?" + "@" + "\"localhost\""
            ));

            for(String consulta : consultas){
                preparedStatement=connection.prepareStatement(consulta);
                preparedStatement.setString(1,nuevoNombre);
                preparedStatement.executeUpdate();
            }

            //eliminar usuario anterior
            String eliminarUsuarioSQL = "DROP USER \"" + nombreAntiguo + "\"@\"localhost\"";

            //modificar nombre de usuario de la agenda
            statement.executeUpdate(eliminarUsuarioSQL);

            System.out.println("Datos modificados con éxito");
            preparedStatement.close();
            connection.close();
            conexion.cerrarConexion();
        } catch (SQLException err) {
            System.out.println(err.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException err) {
                    System.out.println(err.getMessage());
                }
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
    /**
     * Método que pide un integer por terminal y lo devuelve.
     *
     * @param texto string a mostrar por pantalla
     */
    public Integer devolverInteger(String texto) {
        Integer integerDevolver = null;
        while (integerDevolver == null) {
            System.out.println(texto);
            Scanner integerDevolverIn = new Scanner(System.in);
            try {
                integerDevolver = integerDevolverIn.nextInt();
                if(integerDevolver < 3 || integerDevolver > 20){
                    throw  new Exception("error");
                }

            } catch (Exception err) {
                System.out.println("Opcion inválida");
                integerDevolver = null;
            }
        }
        return integerDevolver;
    }
}
