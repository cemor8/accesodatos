import java.util.ArrayList;
import java.util.Scanner;

public class EntradaTeclado {
    private ArrayList<Integer> numeros;
    public EntradaTeclado() {
    }
    public Integer pedir_numeros(){

        System.out.println("Introduce un numero entre el 1 y el 49");
        Integer opcion=null;
        Scanner opcionIN=new Scanner(System.in);
        while (opcion == null) {
            try {
                opcion=opcionIN.nextInt();
            }catch (Exception error){
                System.out.println("Opcion invalida");
                opcion=null;
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

