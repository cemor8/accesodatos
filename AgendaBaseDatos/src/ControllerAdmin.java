import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControllerAdmin {
    Administrador administrador;
    private ArrayList<Usuario> listaUsuarios = new ArrayList<>();
    private ArrayList<Agenda> listaAgendas;
    Map<String, String> columnasExpresiones = new HashMap<String, String>() {
        {
            put("nombre_usuario", "^[a-zA-Z0-9_]{3,15}$");
            put("clave", "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\\\d)(?=.*[@#$%^&+=!]).{8,}$");
        }

    };

    public ControllerAdmin(Administrador administradorEntrada) {
        this.administrador = administradorEntrada;
    }

    public void mostrarMenu() {
        this.recibirUsuarios();
        Integer opcion;
        while (true) {
            System.out.println("""

                    1. Alta Usuarios
                    2. Listar Usuarios
                    3. Baja usuarios
                    4. Modificar usuario
                    5. Trabajar Agenda
                    6. Alta Agendas
                    7. Salir
                                        
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
                    break;
                case 7:
                    System.exit(0);
            }
        }
    }

    public void mostrarMenuAgenda() {
        Integer opcion;
        while (true) {
            System.out.println("""

                    1. Listar Agendas
                    2. Crear Agenda
                    3. Eliminar Agenda
                    4. Asignar Agenda a Usuario
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
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    this.mostrarMenu();
                    return;

            }
        }
    }

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
    public void listarUsuarios(){
        this.listaUsuarios.sort(Comparator.comparing(Usuario::getNombre_usuario));
        for(Usuario usuario : this.listaUsuarios){
            System.out.println(usuario);
        }
    }
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

    public boolean validarDatos(String patronCumplir, String textoBuscar) {
        Pattern patron = Pattern.compile(patronCumplir);
        Matcher matcher = patron.matcher(textoBuscar);
        return matcher.matches();
    }

}
