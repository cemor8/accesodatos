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

    public ArrayList<Contacto> getContactos() {
        return contactos;
    }

    /**
     * Método que recibe un contacto y lo añade a la lista de contactos, si ya existe un contacto con ese nombre,
     * lo indica por pantalla.
     */
    public void asignar(Contacto contacto) {
        if (contactos.stream().anyMatch(contacto1 -> contacto1.getNombre().equals(contacto.getNombre()))) {
            System.out.println("ya existe un contacto con ese nombre, pero se añadira igualmente");
        }
        this.contactos.add(contacto);
        ordenarPorNombre();
        System.out.println("contacto añadido correctamente");


    }

    /**
     * Método que recibe una string, la cual es el nombre de un contacto, para luego buscar un contacto de la agenda
     * con ese nombre, si lo hay, lo muestra, si no, indica que no hay ninguno.
     */
    public void buscar(String nombre) {
        boolean hay = false;
        for (Contacto cada_contacto : contactos) {
            if (cada_contacto.getNombre().equalsIgnoreCase(nombre)) {
                System.out.println(cada_contacto.getNombre() + " " + cada_contacto.getNumero());
                hay = true;
            }
        }
        if (!hay) {
            System.out.println("No hay ningun contacto con ese nombre");
        }
    }

    public void setContactos(ArrayList<Contacto> contactos) {
        this.contactos = contactos;
    }

    /**
     * Método que comprueba que el numero de telefono sea correcto.
     *
     * @return boolean
     */
    public boolean esNumeroTelefonoValido(String numeroTelefono) {
        if (numeroTelefono.length() < 7 || numeroTelefono.length() > 15) {
            return false;
        }

        if (!numeroTelefono.matches("[0-9()-]+")) {
            return false;
        }

        return true;
    }

    /**
     * Método que recibe un contacto y pregunta si quiere modificar el nombre o el numero, una vez introducida
     * la opcion, pide el nuevo valor, lo comprueba y lo cambia si es válido, al final, devuelve una string compuesta por
     * el nombre del contacto y el numero.
     */
    public String modificar(Contacto contacto) {
        System.out.println("Que quieres modificar\n1.Nombre\n2.Numero");
        Integer opcion = null;
        Scanner opcionIN = new Scanner(System.in);
        try {
            opcion = opcionIN.nextInt();
        } catch (Exception error) {
            System.out.println("error al introducir una opcion");
            return modificar(contacto);
        }
        switch (opcion) {
            case 2:
                System.out.println("introduce un numero");
                String numero = null;
                Scanner numeroIN = new Scanner(System.in);
                try {
                    numero = numeroIN.nextLine();
                    if (!esNumeroTelefonoValido(numero)) {
                        throw new Exception();
                    }
                } catch (Exception error) {
                    System.out.println("error al modificar el numero");
                    return modificar(contacto);
                }
                contacto.setNumero(numero);
                System.out.println("Numero establecido correctamente");
                break;
            case 1:
                System.out.println("introduce un nombre");
                String nombre = null;
                Scanner nombreIN = new Scanner(System.in);
                try {
                    nombre = nombreIN.nextLine();
                } catch (Exception error) {
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
        return contacto.getNombre() + " " + contacto.getNumero();
    }

    /**
     * Método que ordena la lista de contactos por el nombre.
     */
    public void ordenarPorNombre() {
        this.contactos.sort((contacto1, contacto2) -> {
            return contacto1.getNombre().compareTo(contacto2.getNombre());
        });
        /*
        Otra forma de hacerlo

        this.contactos.sort(Comparator.comparing(Contacto::getNombre));
         */
    }

    public void eliminar(Contacto contacto) {
        this.contactos.remove(contacto);

    }
    /**
     * Método que muestra los contactos de la agenda, si no hay ninguno, lo indica.
     * */
    public void mostrar() {
        boolean hay = false;
        for (Contacto cada_contacto : this.contactos) {
            System.out.println("Nombre: " + cada_contacto.getNombre() + " " + "Número: " + cada_contacto.getNumero());
            hay = true;
        }
        if (!hay) {
            System.out.println("No hay contactos, la agenda esta vacía");
        }

    }
    /**
     * Método que pregunta si quieres vaciar la agenda, si aceptas, se vacia.
     * */
    public String vaciar() {
        System.out.println("Quieres vaciar la agenda?\n1.Si\n2.No");
        Integer opcion = null;
        Scanner opcionIN = new Scanner(System.in);
        try {
            opcion = opcionIN.nextInt();
        } catch (Exception error) {
            System.out.println("Error al introducir la opcion");
            return vaciar();
        }
        switch (opcion) {
            case 1:
                this.contactos.clear();
                //this.setContactos(new ArrayList<>());
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
