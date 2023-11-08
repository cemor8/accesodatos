import java.util.ArrayList;

public class Agenda {
    private int id_agenda;
    private ArrayList<Contacto> listaContactos = new ArrayList<>();

    public Agenda(int id_agenda) {
        this.id_agenda = id_agenda;
    }

    public int getId_agenda() {
        return id_agenda;
    }

    private void addContacto(Contacto contacto){
        this.listaContactos.add(contacto);
    }

    public ArrayList<Contacto> getListaContactos() {
        return listaContactos;
    }
}
