import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.invoke.ConstantCallSite;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControllerUsuario {
    Usuario usuario;
    Map<String, String> columnasExpresiones = new HashMap<String, String>() {
        {
            put("nombre", "^[A-Z][a-z]{2,19}$");
            put("apellidos", "^[A-Z][a-z]+(\\s[A-Z][a-z]+)?$");
            put("direccion", "^(c/|av.|p.|pza.)\\s[A-Z][a-z]{3,15},\\s\\d{1,3}$");
            put("correo", "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
            put("telefono", "^\\d{3}-\\d{2}-\\d{2}-\\d{2}$");
        }

    };

    public ControllerUsuario(Usuario usuarioEntrada) {
        this.usuario = usuarioEntrada;
    }

    /**
     * Método que muestra un menú en el que se le da la opcion al usuario de volver al login,
     * salir o elegir la agenda con la que quiere trabajar
     */
    public void mostrarMenu() {
        Integer opcionElegirAgenda = null;
        while (opcionElegirAgenda == null) {
            opcionElegirAgenda = this.devolverInteger("\n1. Elegir Agenda\n2. Volver Login \n3. Salir");
            switch (opcionElegirAgenda) {
                case 1:
                    this.elegirAgenda();
                    break;
                case 2:
                    ControllerLogin controllerLogin = new ControllerLogin();
                    controllerLogin.mostrarOpcionesLogin();
                    return;
                case 3:
                    System.exit(0);
                default:
                    System.out.println("opcion inválida");
                    opcionElegirAgenda = null;
            }
        }
        Integer opcion;
        while (true) {
            System.out.println("""

                    1. Introducir nuevo contacto
                    2. Listar contactos
                    3. Modificar contacto
                    4. Eliminar contacto
                    5. Backup agenda
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
                    this.introducirContactoEnAgenda();
                    break;
                case 2:
                    this.listarContactos();
                    break;
                case 3:
                    this.modificarContacto();
                    break;
                case 4:
                    this.eliminarContacto();
                    break;
                case 5:
                    this.backUp();
                    break;
                case 6:
                    System.exit(0);
                    break;
            }
        }
    }

    /**
     * Método que indica la agenda con la que quiere trabajar el usuario
     */
    public void elegirAgenda() {
        Conexion conexion = new Conexion();
        StringBuilder mostrarAgendas = new StringBuilder();
        Connection connection = null;
        try {
            connection = conexion.hacerConexion("userListarAgendas", "listarAgendas",false);
            String sql = "Select agenda.* from agenda inner join usuario on agenda.nombre_usuario = usuario.nombre_usuario where usuario.nombre_usuario like ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "%" + this.usuario.getNombre_usuario() + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id_agenda = resultSet.getInt("id_agenda");
                String nombre_agenda = resultSet.getString("nombre");
                mostrarAgendas.append(id_agenda).append(" ").append(nombre_agenda).append("\n");
            }

        } catch (SQLException err) {
            System.out.println(err.getMessage());
            try {
                connection.close();
            } catch (SQLException err_close) {
                System.out.println(err_close.getMessage());
                return;
            }
            return;
        }
        if (mostrarAgendas.isEmpty()) {
            try {
                connection.close();
            } catch (SQLException err) {
                System.out.println(err.getMessage());
                return;
            }
            return;
        }

        Integer agendaElegida = null;
        while (agendaElegida == null) {
            agendaElegida = devolverInteger(String.valueOf(mostrarAgendas));
            try {
                String sql = "SELECT COUNT(*) FROM agenda WHERE id_agenda = ? and agenda.nombre_usuario like ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, agendaElegida);
                preparedStatement.setString(2, "%" + this.usuario.getNombre_usuario() + "%");
                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                int count = resultSet.getInt(1);
                if (count > 0) {
                    System.out.println("La agenda con ID " + agendaElegida + " seleccionada.");
                    this.usuario.setAgendaSeleccionada(new Agenda(agendaElegida, this.recibirContactos(agendaElegida)));
                } else {
                    System.out.println("La agenda con ID " + agendaElegida + " no existe, selecciona una existente.");
                    agendaElegida = null;
                }
            } catch (SQLException err) {
                System.out.println(err.getMessage());
            }
        }

    }

    /**
     * Método que se encarga de introducir un contacto en la agenda del usuario, pide por terminal los datos
     * del contacto y valida que esten en el formato adecuado, si hay algun problema al meter los datos,
     * el metodo no hace nada.
     **/
    public void introducirContactoEnAgenda() {
        if (this.usuario.getAgendaSeleccionada() == null) {
            System.out.println("Selecciona una agenda");
            return;
        }
        ArrayList<String> datos = new ArrayList<>();

        try {
            Conexion conexion = new Conexion();
            Connection connection = conexion.hacerConexion(this.usuario.getNombre_usuario(), this.usuario.getClave_usuario(),false);
            DatabaseMetaData infoDatabase = connection.getMetaData();
            ResultSet columnasContacto = infoDatabase.getColumns(null, null, "contacto", null);
            String insertSQL = "INSERT INTO contacto (nombre, apellidos, direccion, correo, telefono, id_agenda) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            int i = 1;
            while (columnasContacto.next()) {
                String nombre_columna = columnasContacto.getString("COLUMN_NAME");
                String expresionRegular = this.columnasExpresiones.get(nombre_columna);
                String dato = null;
                if (nombre_columna.equalsIgnoreCase("telefono")) {
                    dato = this.devolverString("Introduce el dato para campo " + nombre_columna + " formato xxx-xx-xx-xx", expresionRegular, true);
                } else if (expresionRegular != null) {
                    dato = this.devolverString("Introduce el dato para campo " + nombre_columna + ", introduce null para dejar en blanco", expresionRegular, false);
                }
                if (dato != null && dato.equalsIgnoreCase("null")) {
                    preparedStatement.setString(i, "");
                    datos.add("");
                } else {
                    preparedStatement.setString(i, dato);
                    datos.add(dato);
                }
                i++;
            }
            preparedStatement.setInt(6, this.usuario.getAgendaSeleccionada().getId_agenda());
            preparedStatement.executeUpdate();
            this.usuario.getAgendaSeleccionada().getListaContactos().add(new Contacto(datos.get(0), datos.get(1), datos.get(2), datos.get(3), datos.get(4)));
            this.usuario.getAgendaSeleccionada().ordenarContactos();
            System.out.println("Contacto añadido correctamente");
            preparedStatement.close();
            conexion.cerrarConexion();
        } catch (SQLException err) {
            System.out.println(err.getMessage());
        }

    }

    /**
     * Método que se encarga de recibir los contactos de la agenda que ha seleccionado
     * el usuario para trabajar con ellos.
     *
     * @param idAgenda id de la agenda de los contactos
     */
    public ArrayList<Contacto> recibirContactos(int idAgenda) {
        ArrayList<Contacto> listaDeAgenda = new ArrayList<>();
        try {
            Conexion conexion = new Conexion();
            Connection connection = conexion.hacerConexion(this.usuario.getNombre_usuario(), this.usuario.getClave_usuario(),false);
            String insertSQL = "select * from contacto inner join agenda on contacto.id_agenda = agenda.id_agenda where agenda.id_agenda = ? order by contacto.nombre asc, contacto.apellidos asc";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setInt(1, idAgenda);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String nombre = resultSet.getString("nombre");
                String apellidos = resultSet.getString("apellidos");
                String direccion = resultSet.getString("direccion");
                String correo = resultSet.getString("correo");
                String telefono = resultSet.getString("telefono");
                listaDeAgenda.add(new Contacto(nombre, apellidos, direccion, correo, telefono));

            }
            preparedStatement.close();
            conexion.cerrarConexion();
            return listaDeAgenda;
        } catch (SQLException err) {
            System.out.println(err.getMessage());
        }
        return listaDeAgenda;
    }

    /**
     * Método que ordena por nombre y apellidos una lista de contactos
     */
    public void listarContactos() {
        if(this.usuario.getAgendaSeleccionada().getListaContactos().isEmpty()){
            System.out.println("La agenda seleccionada no tiene contactos");
            return;
        }
        this.usuario.getAgendaSeleccionada().ordenarContactos();
        for (Contacto contacto : this.usuario.getAgendaSeleccionada().getListaContactos()) {
            System.out.println(contacto);
        }
    }

    /**
     * Método que muestra los contactos para pedir el numero de telefono del contacto
     * que se desea eliminar de la agenda.
     */
    public void eliminarContacto() {
        this.listarContactos();
        String numeroContacto = this.devolverString("Introduce el numero del contacto a eliminar", this.columnasExpresiones.get("numero"), true);
        Optional<Contacto> contactoEncontrado = this.usuario.getAgendaSeleccionada().getListaContactos().stream().filter(contacto -> contacto.getTelefono().equalsIgnoreCase(numeroContacto)).findFirst();
        if (contactoEncontrado.isEmpty()) {
            System.out.println("Error contacto no encontrado");
            return;
        }
        Contacto contactoRecibido = contactoEncontrado.get();
        Conexion conexion = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            conexion = new Conexion();
            connection = conexion.hacerConexion(this.usuario.getNombre_usuario(), this.usuario.getClave_usuario(),false);
            String insertSQL = "delete from contacto where contacto.telefono = ?";
            preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setString(1, numeroContacto);
            int afectadas = preparedStatement.executeUpdate();
            if (afectadas > 0) {
                System.out.println("Contacto eliminado con exito");
            } else {
                throw new SQLException();
            }
            this.usuario.getAgendaSeleccionada().getListaContactos().remove(contactoRecibido);
            preparedStatement.close();
            conexion.cerrarConexion();
        } catch (SQLException err) {
            System.out.println(err.getMessage());
        } finally {
            if (conexion != null) {
                conexion.cerrarConexion();
            }
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
        }

    }

    /**
     * Método que permite modificar un contacto de la agenda, vuelve a pedir todos los
     * datos para el nuevo contacto, lo modifica en la base de datos y en la lista de
     * contactos a la vez.
     */
    public void modificarContacto() {
        this.listarContactos();
        String numeroContacto = this.devolverString("Introduce el numero del contacto a modificar", this.columnasExpresiones.get("numero"), true);
        Optional<Contacto> contactoModificar = this.usuario.getAgendaSeleccionada().getListaContactos().stream().filter(contacto -> contacto.getTelefono().equalsIgnoreCase(numeroContacto)).findFirst();
        if (contactoModificar.isEmpty()) {
            System.out.println("Error contacto no encontrado");
            return;
        }
        Contacto contactoRecibido = contactoModificar.get();
        Conexion conexion = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            conexion = new Conexion();
            connection = conexion.hacerConexion(this.usuario.getNombre_usuario(), this.usuario.getClave_usuario(),false);
            DatabaseMetaData infoDatabase = connection.getMetaData();
            ResultSet columnasContacto = infoDatabase.getColumns(null, null, "contacto", null);
            String insertSQL = "UPDATE contacto SET nombre = ?, apellidos = ?, direccion = ?, correo = ?, telefono = ?, id_agenda = ? WHERE telefono = ?";
            preparedStatement = connection.prepareStatement(insertSQL);
            int i = 1;
            ArrayList<String> datos = new ArrayList<>();
            while (columnasContacto.next()) {
                String nombre_columna = columnasContacto.getString("COLUMN_NAME");
                String expresionRegular = this.columnasExpresiones.get(nombre_columna);
                String dato = null;
                if (nombre_columna.equalsIgnoreCase("telefono")) {
                    dato = this.devolverString("Introduce el dato para campo " + nombre_columna + " formato xxx-xx-xx-xx", expresionRegular, true);
                } else if (expresionRegular != null) {
                    dato = this.devolverString("Introduce el dato para campo " + nombre_columna + ", introduce null para dejar en blanco", expresionRegular, false);
                }
                if (dato != null && dato.equalsIgnoreCase("null")) {
                    preparedStatement.setString(i, "");
                    datos.add("");
                } else {
                    preparedStatement.setString(i, dato);
                    datos.add(dato);
                }
                i++;
            }
            preparedStatement.setInt(6, this.usuario.getAgendaSeleccionada().getId_agenda());
            preparedStatement.setString(7, numeroContacto);
            //modifico los datos del contacto
            contactoRecibido.setNombre(datos.get(0));
            contactoRecibido.setApellidos(datos.get(1));
            contactoRecibido.setDireccion(datos.get(2));
            contactoRecibido.setCorreo(datos.get(3));
            contactoRecibido.setTelefono(datos.get(4));
            preparedStatement.executeUpdate();
            this.usuario.getAgendaSeleccionada().ordenarContactos();
            System.out.println("Contacto añadido correctamente");
            preparedStatement.close();
            conexion.cerrarConexion();
        } catch (SQLException err) {
            System.out.println(err.getMessage());
        } finally {
            if (conexion != null) {
                conexion.cerrarConexion();
            }
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
        }
    }

    /**
     * Método que exporta los datos de una agenda para guardarlos en un fichero sql y así no perder los datos de la agenda,
     * crea un archivo sql con consultas para crear en la base de datos la agenda y sus contactos.
     */
    public void backUp() {
        // Comrpobar si ya hay un archivo con el nombre existente para modificarle el nombre.
        int contador = 1;
        String nombreArchivo = "archivos/backup.sql";
        File archivo = new File(nombreArchivo);
        while (archivo.exists()) {
            nombreArchivo = "archivos/backup_" + contador + ".sql";
            archivo = new File(nombreArchivo);
            contador++;
        }


        Conexion conexion = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultados = null;
        try {
            conexion = new Conexion();
            connection = conexion.hacerConexion(this.usuario.getNombre_usuario(), this.usuario.getClave_usuario(),false);
            String query = "SELECT * FROM gestionagenda.agenda where agenda.id_agenda = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, this.usuario.getAgendaSeleccionada().getId_agenda());
            resultados = preparedStatement.executeQuery();
            PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo), false);
            while (resultados.next()) {
                Integer id_agenda = resultados.getInt("id_agenda");
                String nombre = resultados.getString("nombre");
                String nombre_usuario = resultados.getString("nombre_usuario");

                writer.println("INSERT INTO tabla (id, nombre, nombre_usuario) VALUES ("
                        + id_agenda + ", "
                        + "'" + nombre + "', "
                        + "'" + nombre_usuario + "') " +
                        "ON DUPLICATE KEY UPDATE " +
                        "nombre = VALUES(nombre), " +
                        "nombre_usuario = VALUES(nombre_usuario);");
            }
            query = "SELECT * FROM gestionagenda.contacto where contacto.id_agenda = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, this.usuario.getAgendaSeleccionada().getId_agenda());
            resultados = preparedStatement.executeQuery();
            while (resultados.next()) {
                String nombre = resultados.getString("nombre");
                String apellidos = resultados.getString("apellidos");
                String direccion = resultados.getString("direccion");
                String correo = resultados.getString("correo");
                String telefono = resultados.getString("telefono");
                int id_agenda = resultados.getInt("id_agenda");


                writer.println("INSERT INTO gestionagenda.contacto (nombre, apellidos, direccion, correo, telefono, id_agenda) VALUES ("
                        + "'" + nombre + "', "
                        + "'" + apellidos + "', "
                        + "'" + direccion + "', "
                        + "'" + correo + "', "
                        + "'" + telefono + "', "
                        + id_agenda + ") " +
                        "ON DUPLICATE KEY UPDATE " +
                        "nombre = VALUES(nombre), " +
                        "apellidos = VALUES(apellidos), " +
                        "direccion = VALUES(direccion), " +
                        "correo = VALUES(correo), " +
                        "telefono = VALUES(telefono);");
            }
            writer.close();
            System.out.println("Datos exportados con éxito");
        } catch (SQLException err) {
            System.out.println(err.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (conexion != null) {
                conexion.cerrarConexion();
            }
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
        }
    }


    /**
     * Método que se encarga de validar una expresion regular en un texto enviado
     *
     * @param patronCumplir expresion regular a cumplor
     * @param textoBuscar   string en la que se busca la expresion regular
     */
    public boolean validarDatos(String patronCumplir, String textoBuscar) {
        Pattern patron = Pattern.compile(patronCumplir);
        Matcher matcher = patron.matcher(textoBuscar);
        return matcher.matches();
    }

    /**
     * Método que se encarga de pedir una string por terminal, mostrará el texto
     * que se le pase, comprobará que se cumpla la expresion regular en la string introducida y si no se cumple,
     * solo podrá ser por que la string es "null", si requerido es true, tampoco valdra con la string "null", ya
     * que el parametro es obligatorio en la base de datos.
     *
     * @param texto     texto a mostrar por pantalla
     * @param patron    expresion regular a cumplir
     * @param requerido si es obligatorio en la base de datos o no.
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
     * Método que se encarga de pedir un integer por terminal y devolverlo.
     *
     * @param texto texto a mostrar por pantalla
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
}
