package org.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Controller {
    private static Connection connection = null;

    public static Connection getConnection() throws ClassNotFoundException {

        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                // Cambia la URL, el usuario y la contraseña según tu configuración
                String url = "jdbc:mysql://localhost:3306/libreria";
                String user = "usuariotest";
                String password = "12q12q12Q";

                connection = DriverManager.getConnection(url, user, password);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
}
