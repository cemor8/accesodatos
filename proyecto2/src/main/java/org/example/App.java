package org.example;

import java.sql.SQLException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws ClassNotFoundException, SQLException {
        Controller controller=new Controller();
        controller.mostrarMenu();
    }
}
