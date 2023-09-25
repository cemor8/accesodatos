import java.util.ArrayList;
import java.util.List;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {

        Contacto contacto1= new Contacto("pepe","615232334");
    Agenda agenda=new Agenda(new ArrayList<Contacto>(List.of(contacto1)));
    //agenda.añadir(new Contacto("pep2e","3213131"));
        agenda.vaciar();

    }
}