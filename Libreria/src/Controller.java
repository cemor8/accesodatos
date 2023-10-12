import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

public class Controller {
    private ArrayList<Libro> libros;

    public Controller() {
        this.libros = this.leeArchivo();
    }

    public void menu() {

        Integer opcion = null;
        while (opcion == null) {
            System.out.println("\nMenu");
            System.out.println("1. Crear libro");
            System.out.println("2. Ver libros");
            System.out.println("3. Modificar datos libro");
            System.out.println("4. Eliminar libro por nombre");
            System.out.println("5. Eliminar todos los libros");
            System.out.println("6. Salir");
            Scanner opcionIN = new Scanner(System.in);
            try {
                opcion = opcionIN.nextInt();
            } catch (Exception err) {
                System.out.println("Error al introducir la opcion");
                continue;
            }
            switch (opcion) {
                case 1:
                    this.creaLibro();
                    opcion = null;
                    break;
                case 2:
                    for (Libro cada_libro : this.libros){
                        System.out.println(cada_libro);
                    }
                    opcion = null;
                    break;
                case 3:
                    modificarDirectamente(pideString("Introuce la opcion a modificar"));
                    opcion=null;
                    break;
                case 4:
                    eliminaLibroNombre();
                    opcion=null;
                    break;
                case 5:
                    eliminarContenido();
                    opcion=null;
                    break;
                case 6:
                    this.escribeArchivo();
                    System.exit(0);
                default:
                    System.out.println("Opcion invalida");
                    opcion = null;
            }
        }
    }
    /**
     * Método que lee un archivo y devuelve en una lista la info de este.
     * @return Arraylist de la clase Libro
     * */
    public ArrayList<Libro> leeArchivo() {
        String rutaArchivo = "./almacen/almacen.txt";
        ArrayList<Libro> librosLeer = new ArrayList<>();

        try (BufferedReader bufferedReader= new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea=bufferedReader.readLine())!=null){
                String[] partes = linea.split("\\|");
                if(partes.length==4){
                    String nombre=partes[0];
                    String autor=partes[1];
                    Integer paginas=Integer.parseInt(partes[2]);
                    Integer año=Integer.parseInt(partes[3]);
                    Libro libroCreado=new Libro(nombre,autor,paginas,año);
                    librosLeer.add(libroCreado);
                    System.out.println(libroCreado);
                }
            }

        }catch (FileNotFoundException fil){
            System.out.println("No se encontro el archivo, se creara uno vacio");
            this.crearArchivo();

        } catch (IOException err) {
            System.out.println(err.getMessage());
        }
        return librosLeer;
    }
    /**
     * Método que crea un archivo vacio.
     * */
    public void crearArchivo(){
        try(FileWriter fileWriter=new FileWriter("./almacen/almacen.txt")) {
            fileWriter.close();
            System.out.println("Archivo creado correctamente");
        }catch (IOException err){
            System.out.println("error al crear el archivo cuando no existe");
        }

    }
    /**
     * Método que escribe un archivo con la informacion guardada en la lista de libros
     * */
    public void escribeArchivo() {
        String rutaArchivo = "./almacen/almacen.txt";
        try (FileWriter fileWriter = new FileWriter(rutaArchivo)) {
            for (Libro libro : this.libros) {
                fileWriter.write(libro.getNombre() + "|" + libro.getAutor() + "|" + libro.getPaginas() + "|" + libro.getAño() + System.lineSeparator());
            }
        } catch (IOException err) {
            System.out.println(err.getMessage());
        }
        System.out.println("Libros guardados en el archivo " + rutaArchivo);
    }

    /**
     * Método que crea una nueva instancia de la clase libro para añadirlo
     * a la lista que estan guardados los demas libros.
     */
    public void creaLibro() {
        this.libros.add(new Libro(
                pideString("Introduce el nombre del libro"),
                pideString("Introduce el nombre del autor"),
                pideInteger("Introduce el numero de páginas"),
                pideInteger("Introduce el año de publicación")
        ));
        this.escribeArchivo();

        System.out.println("Libro creado y añadido a la lista de libros correctamente");
    }
    /**
     * Método que pide el nombre de un libro para borrarlo. El funcionamiento de este metodo es:
     * Se crean dos instancias de la clase file, una que contiene el archivo donde se guardan los libros y otro
     * que sera donde se van a guardar solo los libros que no tengan como nombre el indicado.
     * se crean filereader y bufferreader para leer el archivo de los libros original y filewriter y bufferedwriter para
     * almacenar en el nuevo archivo los libros, una vez acabado, se borra el primer archivo y el archivo temporal donde se guardaban
     * los libros pasa a ser el original.
     * */
    public void eliminaLibroNombre(){
        String nombreArchivo=pideString("Introduce el nombre del libro a eliminar");
        File archivoTemp = new File("./almacen/libros_temp.txt");
        File archivo=new File("./almacen/almacen.txt");
        try (FileReader fileReader = new FileReader("./almacen/almacen.txt");
             BufferedReader bufferedReader = new BufferedReader(fileReader);

             FileWriter fileWriter = new FileWriter(archivoTemp, true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

            String linea;
            while ((linea = bufferedReader.readLine()) != null) {
                if (!linea.contains(nombreArchivo)) {
                    bufferedWriter.write(linea);
                    bufferedWriter.newLine();
                }
            }

        }catch (IOException err){
            System.out.println(err.getMessage());
        }
        archivo.delete();
        archivoTemp.renameTo(archivo);
        this.leeArchivo();
    }
    /**
     * Método que recibe una string, la muestra por terminal, y devuelve
     * la informacion introducida por terminal.
     * @return String
     * */
    public String pideString(String texto) {
        String contenido = null;
        while (contenido == null) {
            System.out.println(texto);
            Scanner contenidoIN = new Scanner(System.in);
            try {
                contenido = contenidoIN.nextLine();
            } catch (Exception err) {
                System.out.println("Error al introducir los datos");
            }
        }
        return contenido;
    }
    /**
     * Método que recibe una string, la muestra por terminal, y devuelve
     *  la informacion introducida por terminal.
     * @return Integer
     * */
    public Integer pideInteger(String texto) {
        Integer contenido = null;
        while (contenido == null) {
            System.out.println(texto);
            Scanner contenidoIN = new Scanner(System.in);
            try {
                contenido = contenidoIN.nextInt();
            } catch (Exception err) {
                System.out.println("Error al introducir los datos");
            }
        }
        return contenido;
    }
    /**
     * Método que pide una propiedad para modificarla por un valor que se pide, se identificaran a los libros
     * con su nombre.
     * */
    public void modificarDirectamente(String opcion){
        String nombreLibro=pideString("Introduce el nombre del libro");
        String[] datos = new String[4];
        try (RandomAccessFile randomAccessFile=new RandomAccessFile("./almacen/almacen.txt","rw")){
            String linea;
            long posicionInicial=0;
            boolean encontrado=false;
            while ((linea= randomAccessFile.readLine())!=null){
                datos=linea.split("\\|");
                if(datos.length>0&&datos[0].equals(nombreLibro)){
                    System.out.println("Se encontro un libro con el nombre deseado.");
                    encontrado=true;
                    break;
                }
                posicionInicial=randomAccessFile.getFilePointer();
            }


            if(encontrado){
                switch (opcion.toLowerCase()){
                    case "año":
                        Integer añoLibro=pideInteger("Introduce el año del libro a modificar");
                        randomAccessFile.seek(posicionInicial);
                        String nuevoLibroAño= datos[0]+"|"+datos[1]+"|"+datos[2]+"|"+añoLibro;
                        randomAccessFile.writeBytes(nuevoLibroAño);
                        System.out.println("Año del libro: "+nombreLibro+" Modificado correctamente");
                        break;
                    case "paginas":
                        Integer paginasLibro=pideInteger("Introduce el numero de paginas del libro a modificar");
                        randomAccessFile.seek(posicionInicial);
                        String nuevoLibroPaginas= datos[0]+"|"+datos[1]+"|"+paginasLibro+"|"+datos[3];
                        randomAccessFile.writeBytes(nuevoLibroPaginas);
                        System.out.println("Paginas del libro: "+nombreLibro+" Modificado correctamente");
                        break;
                    case "autor":
                        String autorLibro=pideString("Introduce el nombre del autor del libro a modificar");
                        randomAccessFile.seek(posicionInicial);
                        String nuevoLibroAutor =datos[0]+"|"+autorLibro+"|"+datos[2]+"|"+datos[3];
                        randomAccessFile.writeBytes(nuevoLibroAutor);
                        System.out.println("Autor del libro: "+nombreLibro+" Modificado correctamente");
                        break;
                    case "nombre":
                        String nombreLibroFinal=pideString("Introduce el nombre del libro a modificar");
                        randomAccessFile.seek(posicionInicial);
                        String nuevoLibroNombre =nombreLibroFinal+"|"+datos[1]+"|"+datos[2]+"|"+datos[3];
                        randomAccessFile.writeBytes(nuevoLibroNombre);
                        System.out.println("Nombre del libro: "+nombreLibro+" Modificado correctamente a: "+nombreLibroFinal);
                        break;
                }
            }
        }catch (IOException err){
            System.out.println(err.getMessage());
        }
    }


    /**
     * Método que elimina el contenido de un archivo txt.
     * */
    public void eliminarContenido(){
        this.libros=new ArrayList<>();
        try(FileWriter fileWriter = new FileWriter("./almacen/almacen.txt")) {

        }catch (IOException err){
            System.out.println(err.getMessage());
        }
        System.out.println("Archivo vacio");

    }
}
