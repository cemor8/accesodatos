import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Controller controller=new Controller();
        int opcion;
        while (true){
            System.out.println("\n1. Patron exacto");
            System.out.println("2. Substring");
            System.out.println("3. Empieza por");
            System.out.println("4. Cadena con Caracteres a introducir");
            System.out.println("5. Cadena con 0 seguido de 1");
            System.out.println("6. Email válido");
            System.out.println("7. Salir\n");
            Scanner opcionIN=new Scanner(System.in);
            try {
                opcion=opcionIN.nextInt();
            }catch (Exception err){
                System.out.println(err.getMessage());
                continue;
            }
            switch (opcion){
                case 1:
                    controller.exactoPatron();
                    break;
                case 2:
                    controller.patronSubstring();
                    break;
                case 3:
                    controller.empiezaPor();
                    break;
                case 4:
                    controller.cadenaConSoN();
                    break;
                case 5:
                    controller.cadenaCon0noseguidode1();
                    break;
                case 6:
                    controller.esEmailValido();
                    break;
                case 7:
                    System.exit(0);
                default:
                    System.out.println("opcion invalida");
                    break;
            }

        }
    }
}