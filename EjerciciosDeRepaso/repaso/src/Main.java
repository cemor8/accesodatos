import java.util.Scanner;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    /**
     * Menu del contenido del bloque 1.
     * */
    public static void main(String[] args) {
        Controller controller=new Controller();

        Integer opcion=null;
        while (opcion==null){
            System.out.println("\nQue ejercicio quieres ejecutar");
            System.out.println("1. Letra/texto");
            System.out.println("2. Factura");
            System.out.println("3. Borrar en array");
            System.out.println("4. Notas estudiantes");
            System.out.println("5. Invertir array");
            System.out.println("6. Restaurante");
            System.out.println("7. Salir\n");
            Scanner opcionIN=new Scanner(System.in);
            try {
                opcion=opcionIN.nextInt();
            }catch (Exception err){
                System.out.println("Error al introducir la opcion");
                continue;
            }
            switch (opcion){
                case 1:
                    System.out.println(controller.ej1());
                    break;
                case 2:
                    controller.ej2();
                    break;
                case 3:
                    System.out.println(controller.ej3());
                    break;
                case 4:
                    controller.ej4();
                    break;
                case 5:
                    System.out.println(controller.ej5());
                    break;
                case 6:
                    controller.ej6();
                case 7:
                    System.exit(0);
                default:
                    System.out.println("Opcion invalida");
                    opcion=null;
            }
        }



    }
}