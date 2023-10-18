import java.util.Scanner;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        Controller controller=new Controller();
        int opcion;
        while (true){
            System.out.println("\n1. Validar entero");
            System.out.println("2. Numero negativo");
            System.out.println("3. Numero entero negativo");
            System.out.println("4. Validar DNI");
            System.out.println("5. Validar IPv4");
            System.out.println("6. Validar Matricula");
            System.out.println("7. Validar si un numero es binario");
            System.out.println("8. Validar si el numero es un número real positivo");
            System.out.println("9. Validar una fecha");
            System.out.println("10. Validar un usuario de X");
            System.out.println("11. Validar ISBN");
            System.out.println("12. Salir\n");
            Scanner opcionIN=new Scanner(System.in);
            try {
                opcion=opcionIN.nextInt();
            }catch (Exception err){
                System.out.println(err.getMessage());
                continue;
            }
            switch (opcion){
                case 1:
                    controller.validarEntero();
                    break;
                case 2:
                    controller.validarNumeroNegativo();
                    break;
                case 3:
                    controller.validarNumeroNegativoEntero();
                    break;
                case 4:
                    controller.validarDni();
                    break;
                case 5:
                    controller.validarIp();
                    break;
                case 6:
                    controller.validaMatricula();
                    break;
                case 7:
                    controller.esBinario();
                    break;
                case 8:
                    controller.esRealPositivo();
                    break;
                case 9:
                    controller.validaFecha();
                    break;
                case 10:
                    controller.validarUsuario();
                    break;
                case 11:
                    controller.validarIsbn();
                    break;
                case 12:
                    System.exit(0);
                default:
                    System.out.println("opcion invalida");
                    break;
            }

        }
    }
}