import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControllerUsuario {
    Usuario usuario;

    public ControllerUsuario(Usuario usuarioEntrada) {
        this.usuario = usuarioEntrada;
    }

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
                    this.introducirUsuarioEnAgenda();
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    System.exit(0);
                    break;
            }
        }
    }

    public void elegirAgenda() {
        Conexion conexion = new Conexion();
        StringBuilder mostrarAgendas = new StringBuilder();
        Connection connection = null;
        try {
            connection = conexion.hacerConexion("userListarAgendas", "listarAgendas");
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
            System.out.println("El usuario no tiene agendas asignadas, contacta con un administrador");
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
                    this.usuario.setAgendaSeleccionada(new Agenda(agendaElegida));
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
    public void introducirUsuarioEnAgenda() {
        if (this.usuario.getAgendaSeleccionada() == null) {
            System.out.println("Selecciona una agenda");
            return;
        }
        Map<String, String> columnasExpresiones = new HashMap<>();
        columnasExpresiones.put("nombre", "^[A-Z][a-z]{2,19}$");
        columnasExpresiones.put("apellidos", "^[A-Z][a-z]+(\\s[A-Z][a-z]+)?$");
        columnasExpresiones.put("direccion", "^(c/|av.|p.|pza.)\\s[A-Z][a-z]{3,15},\\s\\d{1,3}$");
        columnasExpresiones.put("correo", "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
        columnasExpresiones.put("telefono", "^\\d{3}-\\d{2}-\\d{2}-\\d{2}$");
        try {
            Conexion conexion = new Conexion();
            Connection connection = conexion.hacerConexion(this.usuario.getNombre_usuario(), this.usuario.getClave_usuario());
            DatabaseMetaData infoDatabase = connection.getMetaData();
            ResultSet columnasContacto = infoDatabase.getColumns(null, null, "contacto", null);
            String insertSQL = "INSERT INTO contacto (nombre, apellidos, direccion, correo, telefono, id_agenda) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            int i = 1;
            while (columnasContacto.next()) {
                String nombre_columna = columnasContacto.getString("COLUMN_NAME");
                String expresionRegular = columnasExpresiones.get(nombre_columna);
                String dato = null;
                if (nombre_columna.equalsIgnoreCase("telefono")) {
                    dato = this.devolverString("Introduce el dato para campo " + nombre_columna + " formato xxx-xx-xx-xx", expresionRegular, true);
                } else if (expresionRegular != null) {
                    dato = this.devolverString("Introduce el dato para campo " + nombre_columna + ", introduce null para dejar en blanco", expresionRegular, false);
                }
                if (dato != null && dato.equalsIgnoreCase("null")) {
                    preparedStatement.setString(i, "");
                } else {
                    preparedStatement.setString(i, dato);
                }
                i++;
            }
            preparedStatement.setInt(6, this.usuario.getAgendaSeleccionada().getId_agenda());
            preparedStatement.executeUpdate();
            System.out.println("Contacto añadido correctamente");
            preparedStatement.close();
            conexion.cerrarConexion();
        } catch (SQLException err) {
            System.out.println(err.getMessage());
        }

    }

    public void listarUsuarios() {
        if (this.usuario.getAgendaSeleccionada() == null) {
            System.out.println("Selecciona una agenda");
            return;
        }
        try {
            Conexion conexion = new Conexion();
            Connection connection = conexion.hacerConexion(this.usuario.getNombre_usuario(), this.usuario.getClave_usuario());
            String insertSQL = "select * from contacto inner join agenda on contacto.id_agenda = agenda.id_agenda where agenda.id = ? order by contacto.nombre asc, contacto.apellidos asc";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setInt(1, this.usuario.getAgendaSeleccionada().getId_agenda());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {


            }
            preparedStatement.setInt(6, this.usuario.getAgendaSeleccionada().getId_agenda());
            preparedStatement.executeUpdate(insertSQL);
            System.out.println("Contacto añadido correctamente");
            preparedStatement.close();
            conexion.cerrarConexion();
        } catch (SQLException err) {
            System.out.println(err.getMessage());
        }
    }


    public boolean validarDatos(String patronCumplir, String textoBuscar) {
        Pattern patron = Pattern.compile(patronCumplir);
        Matcher matcher = patron.matcher(textoBuscar);
        return matcher.matches();
    }

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
                    System.out.println("hola");
                    throw new Exception("Contenido invalido");
                }
            } catch (Exception err) {
                System.out.println("Contenido inválido");
                stringDevolver = null;
            }
        }
        return stringDevolver;
    }

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
