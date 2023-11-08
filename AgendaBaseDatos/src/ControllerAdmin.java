import java.util.Scanner;

public class ControllerAdmin {
    public void mostrarMenu(){
        Integer opcion;
        while (true){
            System.out.println("""

                    1. Alta Usuarios
                    2. Alta BBDD
                    3. Baja usuarios
                    4. Modificar usuario
                    5. Trabajar Agenda
                    6. Listar Usuarios
                    7.Salir
                    
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
                case 7:
                    System.exit(0);
            }
        }
    }

    public void mostrarMenuAgenda(){
        Integer opcion;
        while (true){
            System.out.println("""

                    1. Listar Agendas
                    2. Crear Agenda
                    3. Eliminar Agenda
                    4. Volver Atras
                    
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
                    this.mostrarMenu();
                    return;

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
