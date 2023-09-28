import java.awt.image.AreaAveragingScaleFilter;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Controller {
    private Agenda agenda;

    public Controller(Agenda agenda) throws IOException, ClassNotFoundException {
        this.agenda = agenda;
        this.leerArchivo();

    }

    public void menu() throws IOException {
        Integer opcion = null;
        while (opcion == null) {
            System.out.println("Que quieres hacer en la agenda");
            System.out.println("1. Añadir un nuevo contaco");
            System.out.println("2. Buscar un contacto por el nombre");
            System.out.println("3. Modificar los datos de un contacto");
            System.out.println("4. Eliminar los datos de un contacto");
            System.out.println("5. Mostrar los contactos de la agenda");
            System.out.println("6. Vaciar la agenda");
            System.out.println("7. Salir");
            Scanner opcionIN = new Scanner(System.in);
            try {
                opcion = opcionIN.nextInt();
            } catch (Exception err) {
                System.out.println("Error al introducir la opcion");
                continue;
            }
            switch (opcion) {
                case 1:
                    String nombre = pideString("Introduce el nombre del contacto");

                    String numero = null;
                    while (numero == null) {
                        numero = pideString("Introduce el numero del contacto");
                        if (!this.agenda.esNumeroTelefonoValido(numero)) {
                            System.out.println("El numero es invalido");
                            numero = null;
                        }
                    }
                    this.agenda.asignar(new Contacto(nombre, numero));
                    opcion = null;
                    break;
                case 2:
                    this.agenda.buscar(pideString("Introduce el nombre del contacto"));
                    opcion = null;
                    break;
                case 3:
                    Contacto contacto_modificar = buscaContacto("Introduce un contacto para modificar");
                    this.agenda.modificar(contacto_modificar);
                    opcion = null;
                    break;
                case 4:
                    Contacto contacto_eliminar = buscaContacto("Introduce un contacto para eliminar los datos");
                    this.agenda.eliminar(contacto_eliminar);
                    opcion = null;
                    break;
                case 5:
                    this.agenda.mostrar();
                    opcion = null;
                    break;
                case 6:
                    this.agenda.vaciar();
                    opcion = null;
                    break;
                case 7:
                    this.escribe_agenda();
                    System.exit(0);
                default:
                    System.out.println("Opcion invalida");
                    opcion = null;

            }

        }
    }

    public void leerArchivo() throws IOException, ClassNotFoundException {
        String rutaArchivo = "./directorioAgenda/agenda.txt";
        FileInputStream fin=new FileInputStream(rutaArchivo);
        ObjectInputStream ois= new ObjectInputStream(fin);
        this.agenda.setContactos((ArrayList<Contacto>)ois.readObject());

    }

    public void escribe_agenda() throws IOException {
        String rutaArchivo = "./directorioAgenda/agenda.txt";
        ArrayList<Contacto> lista1=this.agenda.getContactos();
        FileOutputStream fout=new FileOutputStream(rutaArchivo);
        ObjectOutputStream out= new ObjectOutputStream(fout);
        out.writeObject(lista1);
        out.close();


    }

    public String pideString(String mostrar) {
        String texto = null;
        while (texto == null) {
            System.out.println(mostrar);
            Scanner textoIN = new Scanner(System.in);
            try {
                texto = textoIN.nextLine();
            } catch (Exception err) {
                System.out.println("Error al introducir los datos");
            }
        }
        return texto;
    }

    public Contacto buscaContacto(String mostrar) {
        Integer opcion = null;
        Contacto contacto = null;
        while (opcion == null) {
            System.out.println(mostrar);
            for (int i = 0; i < this.agenda.getContactos().size(); i++) {
                System.out.println(i + " " + this.agenda.getContactos().get(i).getNombre() + "---" + this.agenda.getContactos().get(i).getNumero());
            }
            Scanner opcionIN = new Scanner(System.in);
            try {
                opcion = opcionIN.nextInt();
            } catch (Exception err) {
                System.out.println("Error al introducir el contacto");
                continue;
            }
            try {
                contacto = this.agenda.getContactos().get(opcion);
            } catch (Exception err) {
                System.out.println("El contacto que has introducido no existe, intentalo de nuevo");
                opcion = null;
            }
        }
        return contacto;

    }

}
