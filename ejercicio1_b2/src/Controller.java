import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Controller {
    private Agenda agenda;

    public Controller(Agenda agenda) {
        this.agenda = agenda;
    }
    public void menu(){
        Integer opcion=null;
        while (opcion==null){
            System.out.println("Que quieres hacer en la agenda");
            System.out.println("1. Añadir un nuevo contaco");
            System.out.println("2. Buscar un contacto por el nombre");
            System.out.println("3. Modificar los datos de un contacto");
            System.out.println("4. Eliminar los datos de un contacto");
            System.out.println("5. Mostrar los contactos de la agenda");
            System.out.println("6. Vaciar la agenda");
            System.out.println("7. Crear un nuevo contacto");
            System.out.println("8. Salir");
            Scanner opcionIN=new Scanner(System.in);
            try {
                opcion=opcionIN.nextInt();
            }catch (Exception err){
                System.out.println("Error al introducir la opcion");
                continue;
            }
            switch (opcion){
                case 1:
                    String nombre=pideString("Introduce el nombre del contacto");

                    String numero=null;
                    while (numero==null){
                        numero=pideString("Introduce el numero del contacto");
                        if (!agenda.esNumeroTelefonoValido(numero)){
                            System.out.println("El numero es invalido");
                            numero=null;
                        }
                    }
                    agenda.asignar(new Contacto(nombre,numero));
                    opcion=null;
                    break;
                case 2:
                    agenda.buscar(pideString("Introduce el nombre del contacto"));
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:

                case 8:
                    System.exit(0);
                default:
                    System.out.println("Opcion invalida");
                    opcion=null;

            }

        }
    }
    public String pideString(String mostrar){
        String texto=null;
        while (texto==null){
            System.out.println(mostrar);
            Scanner textoIN=new Scanner(System.in);
            try {
                texto=textoIN.nextLine();
            }catch (Exception err){
                System.out.println("Error al introducir los datos");
            }
        }
        return texto;
    }
}
