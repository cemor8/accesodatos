import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControllerAdmin {
    Administrador administrador;
    private ArrayList<Usuario> listaUsuarios = new ArrayList<>();
    private ArrayList<Agenda> listaAgendas = new ArrayList<>();
    Map<String, String> columnasExpresiones = new HashMap<String, String>() {
        {
            put("nombre_usuario", "^[a-zA-Z0-9_]{3,15}$");
            put("clave", "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\\\d)(?=.*[@#$%^&+=!]).{8,}$");
            put("nombre","^.{1,30}$");
        }

    };

    public ControllerAdmin(Administrador administradorEntrada) {

        this.administrador = administradorEntrada;
        this.recibirUsuarios();
        this.recibirAgendas();
    }
    /**
     * Método que muestra el menú del administrador.
     * */

    public void mostrarMenu() {
        Integer opcion;
        while (true) {
            System.out.println("""

                    1. Alta Usuarios
                    2. Listar Usuarios
                    3. Baja usuarios
                    4. Modificar usuario
                    5. Trabajar Agenda
                    6. Salir
                                        
                    """);
            Scanner opcionIn = new Scanner(System.in);
            try {
                opcion = opcionIn.nextInt();
            } catch (Exception err) {
                System.out.println("Error al introducir la opcion");
                continue;
            }
            switch (opcion) {
                case 1:
                    this.crearUsuario();
                    break;
                case 2:
                    this.listarUsuarios();
                    break;
                case 3:
                    this.eliminarUsuario();
                    break;
                case 4:
                    this.modificarUsuario();
                    break;
                case 5:
                    this.mostrarMenuAgenda();
                    break;
                case 6:
                    System.exit(0);
            }
        }
    }
    /**
     * Método que muestra el menu para trabajar con las agendas.
     * */
    public void mostrarMenuAgenda() {
        Integer opcion;
        while (true) {
            System.out.println("""

                    1. Listar Agendas
                    2. Crear Agenda
                    3. Eliminar Agenda
                    4. Asignar Agenda a Usuario
                    5. Modificar Agenda
                    6. Volver Atrás
                                        
                    """);
            Scanner opcionIn = new Scanner(System.in);
            try {
                opcion = opcionIn.nextInt();
            } catch (Exception err) {
                System.out.println("Error al introducir la opcion");
                continue;
            }
            switch (opcion) {
                case 1:
                    this.listarAgendas();
                    break;
                case 2:
                    this.crearAgenda();
                    break;
                case 3:
                    this.eliminarAgenda();
                    break;
                case 4:
                    this.asignarAgendaUsuario();
                    break;
                case 5:
                    this.modificarAgenda();
                    break;
                case 6:
                    this.mostrarMenu();
                    return;

            }
        }
    }
    /**
     * Método que se encarga de recibir los usuarios y meterlos en una lista para modificarlos u operar con ellos
     * */
    public void recibirUsuarios() {
        try {
            Conexion conexion = new Conexion();
            Connection connection = conexion.hacerConexion(this.administrador.getNombre_usuario(), this.administrador.getClave_usuario());
            String insertSQL = "select * from agenda.usuario";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String usuario = resultSet.getString("nombre_usuario");
                String clave = resultSet.getString("clave");
                this.listaUsuarios.add(new Usuario(usuario, clave));

            }
            preparedStatement.close();
            conexion.cerrarConexion();
        } catch (SQLException err) {
            System.out.println(err.getMessage());
        }
    }

    /**
     * Método que se encarga de crear un usuario nuevo para la base de datos,
     * tambien crea una cuenta de usuario en la base de datos.
     * */
    public void crearUsuario() {
        try {
            Conexion conexion = new Conexion();
            Connection connection = conexion.hacerConexion(this.administrador.getNombre_usuario(), this.administrador.getClave_usuario());
            DatabaseMetaData infoDatabase = connection.getMetaData();
            ResultSet columnasContacto = infoDatabase.getColumns(null, null, "contacto", null);
            String insertSQL = "INSERT INTO usuario (nombre_usuario, clave) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            int i = 1;
            ArrayList<String> datos = new ArrayList<>();
            while (columnasContacto.next()) {
                String nombre_columna = columnasContacto.getString("COLUMN_NAME");
                String expresionRegular = this.columnasExpresiones.get(nombre_columna);
                String dato = null;
                if (nombre_columna.equalsIgnoreCase("clave")) {
                    dato = this.devolverString("Introduce el dato para campo " + nombre_columna + ". Requisitos: 8 caracteres, al menos una letra minúscula, al menos una letra mayúscula, " +
                            "al menos un dígito, al menos un carácter especial", expresionRegular, true);
                } else if (expresionRegular != null && nombre_columna.equalsIgnoreCase("nombre_usuario")) {
                    dato = this.devolverString("Introduce el dato para campo " + nombre_columna + ", que comience con una letra, minimo 3 caracteres", expresionRegular, true);
                }
                preparedStatement.setString(i, dato);
                datos.add(dato);
                i++;
            }
            preparedStatement.executeUpdate();
            this.listaUsuarios.add(new Usuario(datos.get(0), datos.get(1)));
            System.out.println("Usuario añadido correctamente");
            preparedStatement.close();
            conexion.cerrarConexion();
        } catch (SQLException err) {
            System.out.println(err.getMessage());
        }
    }
    /**
     * Método que muestra la lista de usuarios que hay en la base de datos
     * */
    public void listarUsuarios(){
        this.listaUsuarios.sort(Comparator.comparing(Usuario::getNombre_usuario));
        for(Usuario usuario : this.listaUsuarios){
            System.out.println(usuario);
        }
    }
    /**
     * Método que se encarga de eliminar un usuario de la base de datos, el registro de
     * la tabla usuario y la cuenta de la base de datos.
     * */
    public void eliminarUsuario(){
        this.listarUsuarios();
        String nombreUsuarioEliminar=this.devolverString("Introduce el nombre de usuario del usuario a eliminar",this.columnasExpresiones.get("nombre_usuario"),true);
        Optional<Usuario> usuarioEncontrado = this.listaUsuarios.stream().filter(usuario -> usuario.getNombre_usuario().equalsIgnoreCase(nombreUsuarioEliminar)).findFirst();
        if(usuarioEncontrado.isEmpty()){
            System.out.println("Nombre de usuario no encontrado");
            return;
        }
        Usuario usuarioRecibido = usuarioEncontrado.get();
        try {
            Conexion conexion = new Conexion();
            Connection connection = conexion.hacerConexion(this.administrador.getNombre_usuario(), this.administrador.getClave_usuario());
            String insertSQL = "delete from usuario where usuario.nombre_usuario = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setString(1, nombreUsuarioEliminar);
            int afectadas = preparedStatement.executeUpdate();
            if(afectadas>0){
                System.out.println("Contacto eliminado con exito");
            }else{
                throw new SQLException();
            }
            preparedStatement.close();
            conexion.cerrarConexion();
            this.listaUsuarios.remove(usuarioRecibido);
        } catch (SQLException err) {
            System.out.println(err.getMessage());
        }
    }
    /**
     * Método que se encarga de modificar un usuario de la base de datos.
     * */
    public void modificarUsuario(){
        this.listarUsuarios();
        String nombreUsuario=this.devolverString("Introduce el nombre de usuario del usuario a modificar",this.columnasExpresiones.get("nombre_usuario"),true);
        Optional<Usuario> usuarioAmodificar= this.listaUsuarios.stream().filter(usuario -> usuario.getNombre_usuario().equalsIgnoreCase(nombreUsuario)).findFirst();
        if(usuarioAmodificar.isEmpty()){
            System.out.println("Error contacto no encontrado");
            return;
        }
        Usuario usuarioRecibido = usuarioAmodificar.get();
        try {
            Conexion conexion = new Conexion();
            Connection connection = conexion.hacerConexion(this.administrador.getNombre_usuario(), this.administrador.getClave_usuario());
            DatabaseMetaData infoDatabase = connection.getMetaData();
            ResultSet columnasContacto = infoDatabase.getColumns(null, null, "contacto", null);
            String insertSQL = "UPDATE usuario SET nombre_usuario = ?, clave = ? WHERE nombre_usuario = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            int i = 1;
            ArrayList<String> datos = new ArrayList<>();
            while (columnasContacto.next()) {
                String nombre_columna = columnasContacto.getString("COLUMN_NAME");
                String expresionRegular = this.columnasExpresiones.get(nombre_columna);
                String dato = null;
                if (nombre_columna.equalsIgnoreCase("clave")) {
                    dato = this.devolverString("Introduce el dato para campo " + nombre_columna + ". Requisitos: 8 caracteres, al menos una letra minúscula, al menos una letra mayúscula, " +
                            "al menos un dígito, al menos un carácter especial", expresionRegular, true);
                } else if (expresionRegular != null && nombre_columna.equalsIgnoreCase("nombre_usuario")) {
                    dato = this.devolverString("Introduce el dato para campo " + nombre_columna + ", que comience con una letra, minimo 3 caracteres", expresionRegular, true);
                }
                preparedStatement.setString(i, dato);
                datos.add(dato);
                i++;
            }
            preparedStatement.setString(3,nombreUsuario);
            usuarioRecibido.setNombre_usuario(datos.get(0));
            usuarioRecibido.setClave_usuario(datos.get(1));
            preparedStatement.executeUpdate();

            System.out.println("Contacto añadido correctamente");

            preparedStatement.close();
            conexion.cerrarConexion();
        } catch (SQLException err) {
            System.out.println(err.getMessage());
        }
    }
    /**
     * Método que guarda las agendas de todos los usuarios en la base de datos.
     * */
    public void recibirAgendas() {
        try {
            Conexion conexion = new Conexion();
            Connection connection = conexion.hacerConexion(this.administrador.getNombre_usuario(), this.administrador.getClave_usuario());
            String insertSQL = "select * from agenda";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id_agenda = resultSet.getInt("id_agenda");
                String nombre = resultSet.getString("nombre");
                String nombreUsuario = resultSet.getString("nombre_usuario");
                Agenda agenda = new Agenda(id_agenda,null);
                agenda.setNombre(nombre);
                agenda.setNombre_usuario(nombreUsuario);
                this.listaAgendas.add(agenda);


            }
            preparedStatement.close();
            conexion.cerrarConexion();
        } catch (SQLException err) {
            System.out.println(err.getMessage());
        }
    }
    /**
     * Método que se encarga de crear una nueva agenda para que los usuarios la puedan usar.
     * */
    public void crearAgenda() {
        try {
            Conexion conexion = new Conexion();
            Connection connection = conexion.hacerConexion(this.administrador.getNombre_usuario(), this.administrador.getClave_usuario());
            DatabaseMetaData infoDatabase = connection.getMetaData();
            ResultSet columnasContacto = infoDatabase.getColumns(null, null, "contacto", null);
            String insertSQL = "INSERT INTO agenda (id_agenda, nombre, nombre_usuario) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            int i = 1;
            ArrayList<String> datos = new ArrayList<>();
            while (columnasContacto.next()) {
                String nombre_columna = columnasContacto.getString("COLUMN_NAME");
                String expresionRegular = this.columnasExpresiones.get(nombre_columna);
                String dato = null;
                if (expresionRegular != null && nombre_columna.equalsIgnoreCase("nombre_usuario")) {
                    dato = this.devolverString("Introduce el dato para campo " + nombre_columna, expresionRegular, true);
                }else if(expresionRegular!=null && nombre_columna.equalsIgnoreCase("id_agenda")){
                    Integer numero = this.devolverInteger("Introduce dato para "+nombre_columna);
                    preparedStatement.setInt(1,numero);
                    i++;
                    datos.add(String.valueOf(numero));
                    continue;
                }
                preparedStatement.setString(i, dato);
                datos.add(dato);
                i++;
            }
            preparedStatement.executeUpdate();
            Agenda agendaCreada = new Agenda(Integer.parseInt(datos.get(0)),null);
            agendaCreada.setNombre_usuario(datos.get(2));
            agendaCreada.setNombre(datos.get(1));
            this.listaAgendas.add(agendaCreada);
            System.out.println("Agenda creada correctamente");
            preparedStatement.close();
            conexion.cerrarConexion();
        } catch (SQLException err) {
            System.out.println(err.getMessage());
        }
    }
    /**
     * Método que muestra la lista de usuarios que hay en la base de datos
     * */
    public void listarAgendas(){
        this.listaAgendas.sort(Comparator.comparing(Agenda::getId_agenda));
        for(Agenda agenda : this.listaAgendas){
            System.out.println(agenda);
        }
    }
    /**
     * Método que se encarga de eliminar una agenda de la base de datos.
     * */
    public void eliminarAgenda(){
        this.listarAgendas();
        int idAgendaEliminar=this.devolverInteger("Introduce el id de la agenda a eliminar");
        Optional<Agenda> agendaEncontrada = this.listaAgendas.stream().filter(agenda -> agenda.getId_agenda() == idAgendaEliminar).findFirst();
        if(agendaEncontrada.isEmpty()){
            System.out.println("Agenda no encontrada");
            return;
        }
        Agenda agendaRecibida = agendaEncontrada.get();
        try {
            Conexion conexion = new Conexion();
            Connection connection = conexion.hacerConexion(this.administrador.getNombre_usuario(), this.administrador.getClave_usuario());
            String insertSQL = "delete from agenda where agenda.id_agenda = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setInt(1, idAgendaEliminar);
            int afectadas = preparedStatement.executeUpdate();
            if(afectadas>0){
                System.out.println("Agenda eliminada con exito");
            }else{
                throw new SQLException();
            }
            preparedStatement.close();
            conexion.cerrarConexion();
            this.listaAgendas.remove(agendaRecibida);
        } catch (SQLException err) {
            System.out.println(err.getMessage());
        }
    }
    /**
     * Método que se encarga de asignar una agenda a un usuario para que este la pueda usar
     * */
    public void asignarAgendaUsuario(){
        this.listarAgendas();
        int idAgendaEliminar=this.devolverInteger("Introduce el id de la agenda a modificar");
        Optional<Agenda> agendaEncontrada = this.listaAgendas.stream().filter(agenda -> agenda.getId_agenda() == idAgendaEliminar).findFirst();
        if(agendaEncontrada.isEmpty()){
            System.out.println("Agenda no encontrada");
            return;
        }
        Agenda agendaRecibida = agendaEncontrada.get();
        this.listarUsuarios();
        String nombreUsuarioAsignar=this.devolverString("Introduce el nombre de usuario para la agenda",this.columnasExpresiones.get("nombre_usuario"),true);
        Optional<Usuario> usuarioEncontrado = this.listaUsuarios.stream().filter(usuario -> usuario.getNombre_usuario().equalsIgnoreCase(nombreUsuarioAsignar)).findFirst();
        if(usuarioEncontrado.isEmpty()){
            System.out.println("usuario no encontrado");
            return;
        }
        Usuario usuario = usuarioEncontrado.get();

        try {
            Conexion conexion = new Conexion();
            Connection connection = conexion.hacerConexion(this.administrador.getNombre_usuario(), this.administrador.getClave_usuario());
            String insertSQL = "UPDATE agenda SET nombre_usuario = ? WHERE id_agenda = )";

            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setString(1,nombreUsuarioAsignar);
            preparedStatement.setInt(2, idAgendaEliminar);
            int afectadas = preparedStatement.executeUpdate();
            if(afectadas>0){
                System.out.println("Modificacion correcta");
            }else{
                throw new SQLException();
            }
            agendaRecibida.setNombre_usuario(usuario.getNombre_usuario());
            preparedStatement.close();
            conexion.cerrarConexion();
        } catch (SQLException err) {
            System.out.println(err.getMessage());
        }
    }
    /**
     * Método que se encarga de modificar los datos de una agenda.
     * */
    public void modificarAgenda(){
        this.listarAgendas();
        int idAgendaEliminar=this.devolverInteger("Introduce el id de la agenda a modificar");
        Optional<Agenda> agendaEncontrada = this.listaAgendas.stream().filter(agenda -> agenda.getId_agenda() == idAgendaEliminar).findFirst();
        if(agendaEncontrada.isEmpty()){
            System.out.println("Agenda no encontrada");
            return;
        }
        Agenda agendaRecibida = agendaEncontrada.get();
        try {
            Conexion conexion = new Conexion();
            Connection connection = conexion.hacerConexion(this.administrador.getNombre_usuario(), this.administrador.getClave_usuario());
            DatabaseMetaData infoDatabase = connection.getMetaData();
            ResultSet columnasContacto = infoDatabase.getColumns(null, null, "contacto", null);
            String insertSQL = "INSERT INTO agenda (id_agenda, nombre, nombre_usuario) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            int i = 1;
            ArrayList<String> datos = new ArrayList<>();
            while (columnasContacto.next()) {
                String nombre_columna = columnasContacto.getString("COLUMN_NAME");
                String expresionRegular = this.columnasExpresiones.get(nombre_columna);
                String dato = null;
                if (expresionRegular != null && nombre_columna.equalsIgnoreCase("nombre_usuario")) {
                    dato = this.devolverString("Introduce el dato para campo " + nombre_columna, expresionRegular, true);
                }else if(expresionRegular!=null && nombre_columna.equalsIgnoreCase("id_agenda")){
                    Integer numero = this.devolverInteger("Introduce dato para "+nombre_columna);
                    preparedStatement.setInt(1,numero);
                    i++;
                    datos.add(String.valueOf(numero));
                    continue;
                }
                preparedStatement.setString(i, dato);
                datos.add(dato);
                i++;
            }
            preparedStatement.executeUpdate();
            agendaRecibida.setId_agenda(Integer.parseInt(datos.get(0)));
            agendaRecibida.setNombre_usuario(datos.get(2));
            agendaRecibida.setNombre(datos.get(1));
            System.out.println("Agenda modificada correctamente");
            preparedStatement.close();
            conexion.cerrarConexion();
        } catch (SQLException err) {
            System.out.println(err.getMessage());
        }
    }
    /**
     * Método que pide una string por pantalla, si hay patron comprueba que el
     * texto cumpla los requisitos, pero si el texto introducido es null continua, aun que si
     * el texto introducido es null pero requerido es true, no valdra y tendras que cumplir los requisitos
     * @param patron expresion regular a validar
     * @param texto texto a mostrar por pantalla
     * @param requerido si es texto debe cumplir los requisitos si este es null.
     * */
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
     * Método que pide un integer por terminal y lo devuelve.
     * @param texto string a mostrar por pantalla
     * */
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
     * Método que se encarga de validar los datos para que se cumpla la
     * expresion regular.
     * @param patronCumplir patron a cumplir
     * @param textoBuscar string donde buscar el patron
     * */
    public boolean validarDatos(String patronCumplir, String textoBuscar) {
        Pattern patron = Pattern.compile(patronCumplir);
        Matcher matcher = patron.matcher(textoBuscar);
        return matcher.matches();
    }

}
