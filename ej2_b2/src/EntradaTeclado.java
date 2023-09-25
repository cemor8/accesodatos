import javax.sound.midi.Soundbank;
import java.util.Scanner;

public class EntradaTeclado {
    public EntradaTeclado() {
    }
    public String apostar(){
        System.out.println("Haz tu apuesta\n1.A\n2.B");
        Integer opcion=null;
        Scanner opcionIN=new Scanner(System.in);
        try {
            opcion=opcionIN.nextInt();
        }catch (Exception error){
            System.out.println("Opcion invalida");
            return apostar();
        }
        return switch (opcion) {
            case 1 -> "a";
            case 2 -> "b";
            default -> apostar();
        };

    }
}
