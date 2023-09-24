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
        switch (opcion){
            case 1:
                return "a";
            case 2:
                return "b";
            default:
                return apostar();
        }

    }
}
