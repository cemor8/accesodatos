import java.util.ArrayList;
import java.util.List;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
    Agenda agenda=new Agenda(new ArrayList<Contacto>(List.of(new Contacto("pepe","615-23-23-34"))));
    agenda.añadir(new Contacto("pep2e","3213131"));
        System.out.println();
    }
}