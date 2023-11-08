import java.util.Scanner;

public class ControllerUsuario {
    public void mostrarMenu(){
        Integer opcion;
        while (true){
            System.out.println("""

                    1. Introducir nuevo contacto
                    2.Listar contactos
                    3. Modificar contacto
                    4. Eliminar contacto
                    5. Backup agenda
                    6. Salir
                    
                    """);
            Scanner opcionIn=new Scanner(System.in);
            try {
                opcion=opcionIn.nextInt();
            }catch (Exception err){
                System.out.println("Error al introducir la opcion");
                continue;
            }
            switch (opcion){
                case 1:
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
                    break;
            }
        }
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
}
