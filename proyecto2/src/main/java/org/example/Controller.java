package org.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Controller {

    public void mostrarMenu() throws SQLException {

        while (true){
            Integer opcion=pideInteger("""

                    1. Crear base de datos
                    2. Crear tabla
                    3. Modificar tabla
                    4. Introducir datos en tabla
                    5. Ejecutar consulta
                    6. Salir
                    """);

            switch (opcion){
                case 1:
                    Conexion.crearBaseDatos();
                    break;
                case 2:
                     Conexion.crearTabla();
                    break;
                case 3:
                    Conexion.modificarTabla();
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    System.exit(0);
                default:
                    break;
            }

        }

    }
    public static String pideString(String texto_mostrar){
        String texto_introducido=null;
        while (texto_introducido==null){
            System.out.println(texto_mostrar);
            try(Scanner texto_introducidoIn=new Scanner(System.in)) {
                texto_introducido=texto_introducidoIn.nextLine();
            }catch (Exception err){
                System.out.println("Contenido invalido");
            }
        }
        return texto_introducido;
    }
    public static Integer pideInteger(String texto_mostrar){
        Integer integer_introducido=null;
        while (integer_introducido==null){
            System.out.println(texto_mostrar);
            try(Scanner integer_introducidoIn=new Scanner(System.in)) {
                integer_introducido=integer_introducidoIn.nextInt();
            }catch (Exception err){
                System.out.println("Contenido invalido");
            }
        }
        return integer_introducido;
    }

}
