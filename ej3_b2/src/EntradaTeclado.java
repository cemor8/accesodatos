import java.util.Scanner;

public class EntradaTeclado {

    public EntradaTeclado() {
    }
    public Integer pedir_numeros(){

        System.out.println("Introduce un numero entre el 1 y el 49");
        Integer opcion=null;

        while (opcion == null) {
            Scanner opcionIN=new Scanner(System.in);
            try {
                opcion=opcionIN.nextInt();
            }catch (Exception error){
                System.out.println("Opcion invalida");
                continue;
            }
            if(opcion>49 || opcion<1){
                System.out.println("Numero invalido, introduce otro");
                opcion=null;
            }
        }
        return opcion;

        }

}

