import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Agenda {
    private int id_agenda;
    private ArrayList<Contacto> listaContactos;
    private String nombre_usuario;
    private String nombre;

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

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setId_agenda(int id_agenda) {
        this.id_agenda = id_agenda;
    }

    @Override
    public String toString() {
        return "Agenda{" +
                "id_agenda=" + id_agenda +
                ", nombre_usuario='" + nombre_usuario + '\'' +
                ", nombre='" + nombre + '\'' +
                '}';
    }

    public ArrayList<Contacto> getListaContactos() {
        return listaContactos;
    }
}
