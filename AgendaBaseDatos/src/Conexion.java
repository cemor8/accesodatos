import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private Connection conexionDevolver=null;

    public Connection hacerConexion(String usuario, String password){
        String jdbcUrl = "jdbc:mysql://localhost:3306/gestionAgenda";
        try {
            Connection crearConexion = DriverManager.getConnection(jdbcUrl,usuario,password);
            conexionDevolver=crearConexion;
        }catch (SQLException err){
            System.out.println(err.getMessage());
        }
        return conexionDevolver;
    }

    public void cerrarConexion(){
        if(conexionDevolver!=null){
            try {
                conexionDevolver.close();
            }catch (SQLException err){
                System.out.println(err.getMessage());
            }
            conexionDevolver=null;
        }
    }
}
