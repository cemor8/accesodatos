import java.util.ArrayList;
import java.util.Scanner;

public class LoteriaPrimitiva {
    private final EntradaTeclado entradaTeclado;
    private final GeneradorNumerosAleatorios generadorNumerosAleatorios;

    public LoteriaPrimitiva(EntradaTeclado entradaTeclado, GeneradorNumerosAleatorios generadorNumerosAleatorios) {
        this.entradaTeclado = entradaTeclado;
        this.generadorNumerosAleatorios = generadorNumerosAleatorios;
    }
    public String partida(){
        ListaNumerosEnteros numeros_elegidos=new ListaNumerosEnteros();
        ListaNumerosEnteros numeros_premiados=new ListaNumerosEnteros();
        while (numeros_premiados.getNumeros().size()<6){
            numeros_premiados.lista(this.generadorNumerosAleatorios.generar());
        }
        while (numeros_elegidos.getNumeros().size()<6){
            numeros_elegidos.lista(this.entradaTeclado.pedir_numeros());
        }
        System.out.println("Numeros elegidos: ");
        for (int numero:numeros_elegidos.getNumeros()){
            System.out.println(numero);
        }
        int aciertos=0;
        System.out.println("Numeros premiados: ");
        for (int numero:numeros_premiados.getNumeros()){
            System.out.println(numero);
            if(numeros_elegidos.getNumeros().contains(numero)){
                aciertos+=1;
            }
        }
        System.out.println("Aciertos: "+aciertos);
        System.out.println("Quieres volver a jugar\n1.Si\n2.No");
        Integer opcion=null;

        while (opcion==null){
            Scanner opcionIN=new Scanner(System.in);
            try {
                opcion=opcionIN.nextInt();
            }catch (Exception error){
                System.out.println("Opcion invalida");
                continue;
            }
            switch (opcion){
                case 1:
                    break;
                case 2:
                    System.exit(0);
                default:
                    System.out.println("Opcion Invalida");
                    opcion=null;
            }
        }
        return partida();
    }
}
