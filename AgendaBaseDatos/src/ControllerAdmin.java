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
            put("clave", "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$");
            put("nombre", "^.{1,30}$");
        }

    };

    public ControllerAdmin(Administrador administradorEntrada) {
        this.administrador = administradorEntrada;
        this.recibirUsuarios();
        this.recibirAgendas();
    }

    /**
     * Método que muestra el menú del administrador.
     */

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
     */
    public void mostrarMenuAgenda() {
        Integer opcion;
        while (true) {
            System.out.println("""

                    1. Listar Agendas
                    2. Crear Agenda
                    3. Eliminar Agenda
                    4. Modificar Agenda
                    5. Volver Atrás
                                        
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
                    this.crearAgenda(null);
                    break;
                case 3:
                    this.eliminarAgenda();
                    break;
                case 4:
                    this.modificarAgenda();
                    break;
                case 5:
                    this.mostrarMenu();
                    return;

            }
        }
    }

    /**
     * Método que se encarga de recibir los usuarios y meterlos en una lista para modificarlos u operar con ellos
     */
    public void recibirUsuarios() {
        Conexion conexion = null;
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        //consulta para recibir todos los usuarios de la base de datos
        try {
            conexion = new Conexion();
            connection = conexion.hacerConexion(this.administrador.getNombre_usuario(), this.administrador.getClave_usuario(),false);
            String insertSQL = "select * from gestionagenda.usuario";
            preparedStatement = connection.prepareStatement(insertSQL);
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
        } finally {
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
     * Método que se encarga de crear un usuario nuevo para la base de datos,
     * tambien crea una cuenta de usuario en la base de datos.
     */
    public void crearUsuario() {

        String insertSQL = "INSERT INTO usuario (nombre_usuario, clave) VALUES (?, ?)";
        List<String> datos = new ArrayList<>();
        String mensajeExito = "Usuario añadido correctamente";
        boolean creado = this.ejecutarConsultaUsuario(insertSQL, datos, mensajeExito, null);
        if(!creado){
            return;
        }
        this.listaUsuarios.add(new Usuario(datos.get(0), datos.get(1)));
        Conexion conexion = null;
        Connection connection = null;
        //consulta para crear un usuario
        try {
            conexion = new Conexion();
            connection = conexion.hacerConexion(this.administrador.getNombre_usuario(), this.administrador.getClave_usuario(),false);
            Statement statement = connection.createStatement();
            ArrayList<String> consultas = new ArrayList<>(List.of(
                    "CREATE USER \"" + datos.get(0) + "\"@\"localhost\" IDENTIFIED BY '" + datos.get(1) + "'",
                    "grant select on gestionagenda.contacto to " + "\"" + datos.get(0) + "\"" + "@" + "\"localhost\"",
                    "grant insert on gestionagenda.contacto to " + "\"" + datos.get(0) + "\"" + "@" + "\"localhost\"",
                    "grant update on gestionagenda.contacto to " + "\"" + datos.get(0) + "\"" + "@" + "\"localhost\"",
                    "grant delete on gestionagenda.contacto to " + "\"" + datos.get(0) + "\"" + "@" + "\"localhost\"",
                    "grant insert on gestionagenda.agenda to " + "\"" + datos.get(0) + "\"" + "@" + "\"localhost\"",
                    "grant select on gestionagenda.agenda to " + "\"" + datos.get(0) + "\"" + "@" + "\"localhost\""
            ));
            for(String consulta : consultas){
                statement.executeUpdate(consulta);
            }
            this.crearAgenda(datos.get(0));

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
     * Método que muestra la lista de usuarios que hay en la base de datos
     */
    public boolean listarUsuarios() {
        //listar usuarios ordenados alfabeticamente
        if(this.listaUsuarios.isEmpty()){
            System.out.println("No hay usuarios");
            return false;
        }
        this.listaUsuarios.sort(Comparator.comparing(Usuario::getNombre_usuario));
        for (Usuario usuario : this.listaUsuarios) {
            System.out.println(usuario);
        }
        return true;
    }

    /**
     * Método que se encarga de eliminar un usuario de la base de datos, el registro de
     * la tabla usuario y la cuenta de la base de datos, tambien elimina sus agendas junto a los
     * contactos de estas.
     */
    public void eliminarUsuario() {
        //muestro usuarios y pido el nombre del usuario a eliminar
        if (!this.listarUsuarios()){
            return;
        }
        String nombreUsuarioEliminar = this.devolverString("Introduce el nombre de usuario del usuario a eliminar", this.columnasExpresiones.get("nombre_usuario"), true);
        Optional<Usuario> usuarioEncontrado = this.listaUsuarios.stream().filter(usuario -> usuario.getNombre_usuario().equalsIgnoreCase(nombreUsuarioEliminar)).findFirst();
        if (usuarioEncontrado.isEmpty()) {
            System.out.println("Nombre de usuario no encontrado");
            return;
        }
        Usuario usuarioRecibido = usuarioEncontrado.get();
        Conexion conexion = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ArrayList<Integer> idsAgendas = new ArrayList<>();
        //eliminar el usuario
        try {
            conexion = new Conexion();
            connection = conexion.hacerConexion(this.administrador.getNombre_usuario(), this.administrador.getClave_usuario(),false);
            String insertSQL = "delete from usuario where usuario.nombre_usuario = ?";
            preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setString(1, nombreUsuarioEliminar);
            int afectadas = preparedStatement.executeUpdate();
            if (afectadas > 0) {
                System.out.println("Usuario eliminado con exito");
            } else {
                throw new SQLException();
            }
            insertSQL = "select id_agenda from agenda where nombre_usuario = ?";
            preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setString(1,nombreUsuarioEliminar);
            ResultSet resultSet = preparedStatement.executeQuery();
            //eliminar las agendas del usuario
            while (resultSet.next()) {
                int idAgenda = resultSet.getInt("id_agenda");
                idsAgendas.add(idAgenda);
            }
            //eliminar los contactos de las agendas
            for(int cada_id : idsAgendas){
                insertSQL = "delete from contacto where id_agenda = ?";
                preparedStatement = connection.prepareStatement(insertSQL);
                preparedStatement.setInt(1,cada_id);
                preparedStatement.executeUpdate();
                insertSQL = "delete from agenda where id_agenda = ?";
                preparedStatement = connection.prepareStatement(insertSQL);
                preparedStatement.setInt(1,cada_id);
                preparedStatement.executeUpdate();
            }
            preparedStatement.close();
            conexion.cerrarConexion();
            this.listaUsuarios.remove(usuarioRecibido);
            this.listaAgendas = new ArrayList<>();
            this.recibirAgendas();
            System.out.println("Usuario eliminado con éxito");
        } catch (SQLException err) {
            System.out.println(err.getMessage());
        } finally {
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
     * Método que se encarga de modificar un usuario de la base de datos.
     */
    public void modificarUsuario() {
        if (!this.listarUsuarios()){
            return;
        }
        String nombreUsuario = this.devolverString("Introduce el nombre de usuario del usuario a modificar", this.columnasExpresiones.get("nombre_usuario"), true);
        Optional<Usuario> usuarioAmodificar = this.listaUsuarios.stream().filter(usuario -> usuario.getNombre_usuario().equalsIgnoreCase(nombreUsuario)).findFirst();
        if (usuarioAmodificar.isEmpty()) {
            System.out.println("Error contacto no encontrado");
            return;
        }

        Usuario usuarioRecibido = usuarioAmodificar.get();
        String nombreUsuarioAntiguo = usuarioRecibido.getNombre_usuario();
        ArrayList<String> datos = new ArrayList<>();
        String updateSQL = "UPDATE usuario SET nombre_usuario = ?, clave = ? WHERE nombre_usuario = ?";
        String mensajeExito = "Usuario modificado correctamente";
        //consulta para modificar el usuario
        boolean creado = this.ejecutarConsultaUsuario(updateSQL, datos, mensajeExito, usuarioRecibido.getNombre_usuario());
        if(!creado){
            return;
        }
        //actualizo el usuario
        usuarioRecibido.setNombre_usuario(datos.get(0));
        usuarioRecibido.setClave_usuario(datos.get(1));
        Conexion conexion = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            conexion = new Conexion();
            connection = conexion.hacerConexion(this.administrador.getNombre_usuario(), this.administrador.getClave_usuario(),false);
            Statement statement = connection.createStatement();
            String crearUsuarioSQL = "CREATE USER \"" + datos.get(0) + "\"@\"localhost\" IDENTIFIED BY '" + datos.get(1) + "'";
            statement.executeUpdate(crearUsuarioSQL);
            ArrayList<String> consultas = new ArrayList<>(List.of(
                    "grant select on gestionagenda.contacto to " + "?" + "@" + "\"localhost\"",
                    "grant insert on gestionagenda.contacto to " + "?" + "@" + "\"localhost\"",
                    "grant update on gestionagenda.contacto to " + "?" + "@" + "\"localhost\"",
                    "grant delete on gestionagenda.contacto to " + "?" + "@" + "\"localhost\"",
                    "grant select on gestionagenda.agenda to " + "?" + "@" + "\"localhost\"",
                    "grant insert on gestionagenda.agenda to " + "?" + "@" + "\"localhost\""

            ));
            for(String consulta : consultas){
                preparedStatement=connection.prepareStatement(consulta);
                preparedStatement.setString(1,datos.get(0));
                preparedStatement.executeUpdate();
            }
            //eliminar usuario anterior
            String eliminarUsuarioSQL = "DROP USER \"" + nombreUsuarioAntiguo + "\"@\"localhost\"";

            //modificar nombre de usuario de la agenda
            statement.executeUpdate(eliminarUsuarioSQL);
            String sql1 = "update agenda set nombre_usuario = ? where nombre_usuario = ?";
            preparedStatement = connection.prepareStatement(sql1);

            preparedStatement.setString(1,datos.get(0));
            preparedStatement.setString(2,nombreUsuarioAntiguo);
            preparedStatement.executeUpdate();
            //actualizo la lista de agendas
            this.recibirAgendas();
            System.out.println("Datos del usuario modificados");
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
     * Método que ejecuta una consulta para la manipulacion de los usuarios en la
     * base de datos
     *
     * @param consultaSQL   consulta a realizar
     * @param datos         lista donde se guardaran los datos
     * @param mensajeExito  mensaje que se enviara para verificar el exito de la creacion del usuario
     * @param nombreUsuario parametro que se introducira en la consulta si no es nulo
     */
    public boolean ejecutarConsultaUsuario(String consultaSQL, List<String> datos, String mensajeExito, String nombreUsuario) {
        Conexion conexion = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            conexion = new Conexion();
            connection = conexion.hacerConexion(this.administrador.getNombre_usuario(), this.administrador.getClave_usuario(),false);
            DatabaseMetaData infoDatabase = connection.getMetaData();
            ResultSet columnasContacto = infoDatabase.getColumns(null, null, "usuario", null);
            preparedStatement = connection.prepareStatement(consultaSQL);

            int i = 1;
            while (columnasContacto.next()) {
                String nombre_columna = columnasContacto.getString("COLUMN_NAME");
                String expresionRegular = this.columnasExpresiones.get(nombre_columna);
                String dato = null;
                if (expresionRegular != null) {
                    if (nombre_columna.equalsIgnoreCase("clave")) {
                        dato = this.devolverString("Introduce el dato para campo " + nombre_columna + ". Requisitos: 8 caracteres, al menos una letra minúscula, al menos una letra mayúscula, " +
                                "al menos un dígito, al menos un carácter especial", expresionRegular, true);
                    } else if (nombre_columna.equalsIgnoreCase("nombre_usuario")) {
                        dato = this.devolverString("Introduce el dato para campo " + nombre_columna + ", que comience con una letra, minimo 3 caracteres", expresionRegular, true);
                    }
                    preparedStatement.setString(i, dato);
                    datos.add(dato);
                    i++;
                }
            }
            if (nombreUsuario != null) {
                preparedStatement.setString(3, nombreUsuario);
            }
            preparedStatement.executeUpdate();
            System.out.println(mensajeExito);
            preparedStatement.close();
            conexion.cerrarConexion();
            return true;
        } catch (SQLException err) {
            System.out.println(err.getMessage());
            return false;
        } finally {
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
     * Método que guarda en una lista las agendas disponibles
     * para los usuarios que estan almacendas en la base de datos.
     */
    public void recibirAgendas() {
        this.listaAgendas = new ArrayList<>();
        Conexion conexion = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        //recibir todas las agendas
        try {
            conexion = new Conexion();
            connection = conexion.hacerConexion(this.administrador.getNombre_usuario(), this.administrador.getClave_usuario(),false);
            String insertSQL = "select * from agenda";
            preparedStatement = connection.prepareStatement(insertSQL);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id_agenda = resultSet.getInt("id_agenda");
                String nombre = resultSet.getString("nombre");
                String nombreUsuario = resultSet.getString("nombre_usuario");
                Agenda agenda = new Agenda(id_agenda, null);
                agenda.setNombre(nombre);
                agenda.setNombre_usuario(nombreUsuario);
                this.listaAgendas.add(agenda);
            }
            preparedStatement.close();
            conexion.cerrarConexion();
        } catch (SQLException err) {
            System.out.println(err.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (conexion != null) {
                    conexion.cerrarConexion();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException err) {
                System.out.println(err.getMessage());
            }
        }
    }

    /**
     * Método que se encarga de crear una nueva agenda para que los usuarios la puedan usar.
     */
    public void crearAgenda(String nombreUsuario) {
        if(nombreUsuario!=null){
            String insertSQL = "INSERT INTO agenda (id_agenda, nombre, nombre_usuario) VALUES (?, ?, "+"'"+nombreUsuario+"'"+")";
            List<String> datos = new ArrayList<>();
            String mensajeExito = "Agenda creada correctamente";

            boolean creado= this.ejecutarConsultaAgenda(insertSQL, datos, mensajeExito, null,true);
            if(!creado){
                return;
            }
            Agenda agenda = new Agenda(Integer.parseInt(datos.get(0)), null);
            agenda.setNombre(datos.get(1));
            agenda.setNombre_usuario(nombreUsuario);
            this.listaAgendas.add(agenda);
            return;
        }
        String insertSQL = "INSERT INTO agenda (id_agenda, nombre, nombre_usuario) VALUES (?, ?, ?)";
        List<String> datos = new ArrayList<>();
        String mensajeExito = "Agenda creada correctamente";

        boolean creado = this.ejecutarConsultaAgenda(insertSQL, datos, mensajeExito, null,false);
        if(!creado){
            return;
        }
        Agenda agenda = new Agenda(Integer.parseInt(datos.get(0)), null);
        agenda.setNombre(datos.get(1));
        agenda.setNombre_usuario(datos.get(2));
        this.listaAgendas.add(agenda);
    }

    /**
     * Método que muestra la lista de usuarios que hay en la base de datos
     */
    public boolean listarAgendas() {
        if(this.listaAgendas.isEmpty()){
            System.out.println("No hay agendas");
            return false;
        }
        this.listaAgendas.sort(Comparator.comparing(Agenda::getId_agenda));
        for (Agenda agenda : this.listaAgendas) {
            System.out.println(agenda);
        }
        return true;
    }

    /**
     * Método que se encarga de eliminar una agenda de la base de datos.
     */
    public void eliminarAgenda() {
        if(!this.listarAgendas()){
            return;
        }
        int idAgendaEliminar = this.devolverInteger("Introduce el id de la agenda a eliminar");
        Optional<Agenda> agendaEncontrada = this.listaAgendas.stream().filter(agenda -> agenda.getId_agenda() == idAgendaEliminar).findFirst();
        if (agendaEncontrada.isEmpty()) {
            System.out.println("Agenda no encontrada");
            return;
        }
        Agenda agendaRecibida = agendaEncontrada.get();
        Conexion conexion = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        //eliminar agenda
        try {
            conexion = new Conexion();
            connection = conexion.hacerConexion(this.administrador.getNombre_usuario(), this.administrador.getClave_usuario(),false);
            String insertSQL = "delete from agenda where agenda.id_agenda = ?";
            preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setInt(1, idAgendaEliminar);
            int afectadas = preparedStatement.executeUpdate();
            if (afectadas > 0) {
                System.out.println("Agenda eliminada con exito");
            } else {
                throw new SQLException();
            }
            preparedStatement.close();
            conexion.cerrarConexion();
            //eliminar contactos de la agenda
            this.ejecutarConsultaContacto(true, agendaRecibida.getId_agenda(), null);
            this.listaAgendas.remove(agendaRecibida);


        } catch (SQLException err) {
            System.out.println(err.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (conexion != null) {
                    conexion.cerrarConexion();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException err) {
                System.out.println(err.getMessage());
            }
        }
    }

    /**
     * Método que se encarga de modificar los datos de una agenda.
     */
    public void modificarAgenda() {
        if(!this.listarAgendas()){
            return;
        }
        int idAgendaEliminar = this.devolverInteger("Introduce el id de la agenda a modificar");
        Optional<Agenda> agendaEncontrada = this.listaAgendas.stream().filter(agenda -> agenda.getId_agenda() == idAgendaEliminar).findFirst();
        if (agendaEncontrada.isEmpty()) {
            System.out.println("Agenda no encontrada");
            return;
        }
        ArrayList<String> datos = new ArrayList<>();
        Agenda agendaRecibida = agendaEncontrada.get();
        String updateSQL = "UPDATE agenda SET id_agenda = ?, nombre = ?, nombre_usuario = ? WHERE id_agenda = ?";
        String mensajeExito = "Agenda modificada correctamente";
        boolean creado = this.ejecutarConsultaAgenda(updateSQL, datos, mensajeExito, agendaRecibida.getId_agenda(),false);
        if(!creado){
            return;
        }

        //modificar el id de la agenda de los contactos
        this.ejecutarConsultaContacto(false, agendaRecibida.getId_agenda(), Integer.parseInt(datos.get(0)));
        agendaRecibida.setId_agenda(Integer.parseInt(datos.get(0)));
        agendaRecibida.setNombre(datos.get(1));
        agendaRecibida.setNombre_usuario(datos.get(2));
    }

    /**
     * Método que se encarga de ejecutar una consulta para manipular las agendas de la base de datos.
     *
     * @param consultaSQL  consulta a realizar
     * @param datos        lista en la que se guardan los datos para luego crear los contactos
     * @param mensajeExito mensaje a mostrar por pantalla cuando se realiza todo correctamente
     * @param idAgenda     id de la agenda a manipular
     */

    public boolean ejecutarConsultaAgenda(String consultaSQL, List<String> datos, String mensajeExito, Integer idAgenda, boolean aUsuario) {
        Conexion conexion = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            conexion = new Conexion();
            connection = conexion.hacerConexion(this.administrador.getNombre_usuario(), this.administrador.getClave_usuario(),false);
            DatabaseMetaData infoDatabase = connection.getMetaData();
            ResultSet columnasContacto = infoDatabase.getColumns(null, null, "agenda", null);
            preparedStatement = connection.prepareStatement(consultaSQL);
            int i = 1;
            while (columnasContacto.next()) {
                String nombre_columna = columnasContacto.getString("COLUMN_NAME");
                String expresionRegular = this.columnasExpresiones.get(nombre_columna);
                String dato = null;
                if ((nombre_columna.equalsIgnoreCase("nombre_usuario") && !aUsuario) || nombre_columna.equalsIgnoreCase("nombre")) {
                    dato = this.devolverString("Introduce el dato para campo " + nombre_columna, expresionRegular, true);
                } else if (nombre_columna.equalsIgnoreCase("id_agenda")) {
                    Integer numero = this.devolverInteger("Introduce dato para " + nombre_columna);
                    preparedStatement.setInt(1, numero);
                    i++;
                    datos.add(String.valueOf(numero));
                    continue;
                }
                if(dato == null){
                    i++;
                    continue;
                }
                preparedStatement.setString(i, dato);
                datos.add(dato);
                i++;

            }
            if (idAgenda != null) {
                preparedStatement.setInt(4, idAgenda);
            }
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
            System.out.println(mensajeExito);
            preparedStatement.close();
            conexion.cerrarConexion();
            return true;
        } catch (SQLException err) {
            System.out.println(err.getMessage());
            return false;
        } finally {
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
     * Método que se encarga de ejecutar una constulra para manipular los contactos de la agenda correspondiente.
     *
     * @param eliminar    si hay que eliminar o modificar el contacto
     * @param id_agenda   id de la agenda donde se almaxena el contacto
     * @param nuevaAgenda nuevo id de la agenda del contacto
     */
    public void ejecutarConsultaContacto(boolean eliminar, int id_agenda, Integer nuevaAgenda) {
        Conexion conexion = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            conexion = new Conexion();
            connection = conexion.hacerConexion(this.administrador.getNombre_usuario(), this.administrador.getClave_usuario(),false);
            String consulta = null;
            if (eliminar) {
                consulta = "Delete from gestionagenda.contacto where contacto.id_agenda = ?";
                preparedStatement = connection.prepareStatement(consulta);
                preparedStatement.setInt(1, id_agenda);

            } else {
                consulta = "Update contacto set contacto.id_agenda = ? where contacto.id_agenda = ?";
                preparedStatement = connection.prepareStatement(consulta);
                preparedStatement.setInt(1, nuevaAgenda);
                preparedStatement.setInt(2, id_agenda);
            }
            preparedStatement.executeUpdate();

        } catch (SQLException err) {
            System.out.println(err.getMessage());
        } finally {
            try {
                if (conexion != null) {
                    conexion.cerrarConexion();
                }
                if (connection != null) {
                    connection.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
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

            } catch (Exception err) {
                System.out.println("Opcion inválida");
            }
        }
        return integerDevolver;
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
