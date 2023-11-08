import java.sql.*;
import java.util.Scanner;

public class ControllerLogin {
    public void mostrarOpcionesLogin(){
        System.out.println("Para usar este programa es necesario tener una base de datos creada llamada gestionAgenda.");
        System.out.println("Comprobando existencia de la base de datos...");


        Integer opcion=devolverInteger("\n1. Login Administradores \n2. Login Usuarios");
        switch (opcion){
            case 1:

                break;
            case 2:
                break;
        }

    }
    public boolean comprobarCredenciales(String nombreUsuario, String passwordUsuario, String nombreTablaBuscar){
        Conexion conexion=new Conexion();
        Connection connection= conexion.hacerConexion("usuarioValidarCredencial","validarCredencial");
        try {
            String consulta="SELECT clave_administrador from administrador where nombre_administrador like ?";
            PreparedStatement statement = connection.prepareStatement(consulta);
            statement.setString(1, nombreUsuario);
            ResultSet resultados = statement.executeQuery();
            if (resultados.next()) {
                String claveAdministrador = resultados.getString("clave_administrador");
                if(!claveAdministrador.equals(passwordUsuario)){
                    System.out.println("Credenciales incorrectas");
                    return false;
                }
                return true;
            }
        }catch (SQLException err){
            System.out.println(err.getMessage());
        }
        return false;
    }


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
