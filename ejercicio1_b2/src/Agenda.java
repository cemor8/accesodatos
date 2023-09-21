import java.util.ArrayList;
import java.util.Collection;

public class Agenda {
    private ArrayList<Contacto> contactos;

    public Agenda(ArrayList<Contacto> contactos) {
        this.contactos = contactos;
    }
    public void añadir(Contacto contacto){
        if(contactos.stream().anyMatch(contacto1 -> contacto1.getNombre().equals(contacto.getNombre()))){
            System.out.println("ya existe un contacto con ese nombre");
            return;
        }
        this.contactos.add(contacto);
        System.out.println("contacto añadido correctamente");
    }
    public void buscar(String nombre){
        for(Contacto cada_contacto : contactos){
            if(cada_contacto.getNombre()==nombre){

            }
        }
    }


}
