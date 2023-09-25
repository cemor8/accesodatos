import java.util.ArrayList;
import java.util.List;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {

        Contacto contacto1= new Contacto("pepe W","615232334");
    Agenda agenda=new Agenda(new ArrayList<Contacto>(List.of(contacto1,new Contacto("paco","615-79-67-54"))));
    agenda.añadir(new Contacto("pepe A","673-23-45-21"));
    agenda.mostrar();


    }
}