import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Agenda {
    private int id_agenda;
    private ArrayList<Contacto> listaContactos;

    public Agenda(int id_agenda, ArrayList<Contacto> lista) {
        this.id_agenda = id_agenda;
        this.listaContactos= lista;
    }

    public int getId_agenda() {
        return id_agenda;
    }

    public void ordenarContactos(){
        Collections.sort(this.listaContactos, Comparator.comparing(Contacto::getNombre).thenComparing(Contacto::getApellidos));

    }

    public ArrayList<Contacto> getListaContactos() {
        return listaContactos;
    }
}
