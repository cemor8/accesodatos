import java.sql.*;
import java.util.Scanner;

public class ControllerLogin {
    /**
     * Método que se encarga de mostrar el login y comprobar la existencia del usuario o administrador para
     * seguir usando la aplicación.
     * */
    public void mostrarOpcionesLogin(){
        System.out.println("Para usar este programa es necesario tener una base de datos creada llamada gestionAgenda.");
        System.out.println("Comprobando existencia de la base de datos...");
            // comprobar existencia de la base de datos.

        Integer opcion=null;
        while (opcion==null){
            opcion=devolverInteger("\n1. Login Administradores \n2. Login Usuarios");
            String usuario;
            String password;
            switch (opcion){
                case 1:
                     usuario=this.devolverString("\nIntroduce nombre de usuario");
                     password=this.devolverString("\nIntroduce contraseña");
                    if(!comprobarCredenciales(usuario,password,"administrador")){
                        opcion=null;
                        break;
                    }
                    ControllerAdmin controllerAdmin = new ControllerAdmin(new Administrador(usuario,password));
                    controllerAdmin.mostrarMenu();
                    break;
                case 2:
                     usuario=this.devolverString("\nIntroduce nombre de usuario");
                     password=this.devolverString("\nIntroduce contraseña");
                    if(!comprobarCredenciales(usuario,password,"usuario")){
                        opcion=null;
                        break;
                    }
                    ControllerUsuario controllerUsuario=  new ControllerUsuario(new Usuario(usuario,password));
                    controllerUsuario.mostrarMenu();
                    break;
                default:
                    opcion=null;
                    break;
            }
        }


    }
    /**
     * Método que se encarga de comprobar las credenciales del usuario introducido para validar que existe.
     * @param nombreUsuario nombre del usuario a validar
     * @param passwordUsuario contraseña a validar
     * @param nombreTablaBuscar donde buscar el usuario
     * */
    public boolean comprobarCredenciales(String nombreUsuario, String passwordUsuario, String nombreTablaBuscar){
        Conexion conexion=new Conexion();
        Connection connection= conexion.hacerConexion("usuarioValidarCredencial","validarCredencial");
        try {
            String consulta="SELECT clave_administrador from ? where nombre_administrador like ?";
            PreparedStatement statement = connection.prepareStatement(consulta);
            statement.setString(1,nombreTablaBuscar);
            statement.setString(2, nombreUsuario);
            ResultSet resultados = statement.executeQuery();
            if (resultados.next()) {
                String claveAdministrador = resultados.getString("clave_administrador");
                if(!claveAdministrador.equals(passwordUsuario)){
                    System.out.println("Credenciales incorrectas");
                    return false;
                }
                conexion.cerrarConexion();
                statement.close();
                return true;
            }
        }catch (SQLException err){
            System.out.println(err.getMessage());
        }
        return false;
    }

    /**
     * Método que se encarga de pedir un integer por terminal y devolverlo
     * @param texto texto a mostrar
     * */
    public Integer devolverInteger(String texto){
        Integer integerDevolver=null;
        while (integerDevolver==null){
            System.out.println(texto);
            Scanner integerDevolverIn=new Scanner(System.in);
            try {
                integerDevolver=integerDevolverIn.nextInt();
            }catch (Exception err){
                System.out.println("Opcion inválida");
            }
        }
        return integerDevolver;
    }
    /**
     * Método que se encarga de pedir una string por terminal y devolverla
     * @param texto texto a mostrar.
     * */
    public String devolverString(String texto){
        String stringDevolver=null;
        while (stringDevolver==null){
            Scanner stringDevolverIn=new Scanner(System.in);
            try {
                stringDevolver= stringDevolverIn.nextLine();
            }catch (Exception err){
                System.out.println("Contenido inválido");
            }
        }
        return stringDevolver;
    }

}
