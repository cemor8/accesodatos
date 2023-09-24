import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class Agenda {
    private ArrayList<Contacto> contactos;

    public Agenda(ArrayList<Contacto> contactos) {

        this.contactos = contactos;
        ordenarPorNombre();
    }
    public void añadir(Contacto contacto){
        if(contactos.stream().anyMatch(contacto1 -> contacto1.getNombre().equals(contacto.getNombre()))){
            System.out.println("ya existe un contacto con ese nombre");
            return;
        }
        this.contactos.add(contacto);
        ordenarPorNombre();
        System.out.println("contacto añadido correctamente");
        System.out.println(this.contactos);
    }
    public void buscar(String nombre){
        for(Contacto cada_contacto : contactos){
            if(cada_contacto.getNombre().equals(nombre)){
                System.out.println(cada_contacto.getNombre()+" "+cada_contacto.getNumero());
            }
        }
    }

    public void setContactos(ArrayList<Contacto> contactos) {
        this.contactos = contactos;
    }

    public boolean esNumeroTelefonoValido(String numeroTelefono) {
        if (numeroTelefono.length() < 7 || numeroTelefono.length() > 15) {
            return false;
        }
        // Verificar que solo contiene dígitos y caracteres especiales permitidos
        if (!numeroTelefono.matches("[0-9()-]+")) {
            return false;
        }

        return true;
    }
    public String modificar(Contacto contacto){
        System.out.println("Que quieres modificar\n1.Nombre\n2.Numero");
        Integer opcion=null;
        Scanner opcionIN= new Scanner(System.in);
        try {
            opcion=opcionIN.nextInt();
        }catch (Exception error){
            System.out.println("error al introducir una opcion");
            return modificar(contacto);
        }
        switch (opcion){
            case 2:
                System.out.println("introduce un numero");
                String numero=null;
                Scanner numeroIN= new Scanner(System.in);
                try {
                    numero=numeroIN.nextLine();
                    if(!esNumeroTelefonoValido(numero)){
                        throw new Exception();
                    }
                }catch (Exception error){
                    System.out.println("error al modificar el numero");
                    return modificar(contacto);
                }
                contacto.setNumero(numero);
                System.out.println("Numero establecido correctamente");
                break;
            case 1:
                System.out.println("introduce un nombre");
                String nombre=null;
                Scanner nombreIN= new Scanner(System.in);
                try {
                    nombre=nombreIN.nextLine();
                }catch (Exception error){
                    System.out.println("error al modificar el nombre");
                    return modificar(contacto);
                }
                contacto.setNombre(nombre);
                System.out.println("nombre modificado correctamente");
                break;
            default:
                System.out.println("opcion ivalida");
                return modificar(contacto);
        }
        return contacto.getNombre()+" "+contacto.getNumero();
    }
    public void ordenarPorNombre() {
        Collections.sort(this.contactos, new Comparator<Contacto>() {
            @Override
            public int compare(Contacto contacto1, Contacto contacto2) {
                // Comparar los nombres de los contactos
                return contacto1.getNombre().compareTo(contacto2.getNombre());
            }
        });
    }
    public void eliminar(Contacto contacto){
        contacto.setNombre("");
        contacto.setNumero("");

    }
    public void mostrar(){
        for(Contacto cada_contacto:this.contactos){
            System.out.println("Nombre: "+cada_contacto.getNombre()+" "+"Número: "+cada_contacto.getNumero());
        }

    }
    public String vaciar(){
        System.out.println("Quieres vaciar la agenda?\n1.Si\n2.No");
        Integer opcion=null;
        Scanner opcionIN=new Scanner(System.in);
        try {
            opcion=opcionIN.nextInt();
        }catch (Exception error){
            System.out.println("Error al introducir la opcion");
            return vaciar();
        }
        switch (opcion){
            case 1:
                this.setContactos(new ArrayList<>());
                System.out.println("Agenda vaciada correctamente");
            case 2:
                break;
            default:
                System.out.println("opcion invalida");
                return vaciar();

        }
        return "hola";
    }


}
