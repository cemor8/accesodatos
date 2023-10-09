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
            System.out.println("Menu");
            System.out.println("1. Crear libro");
            System.out.println("2. Actualizar informacion de libro");
            System.out.println("3. Borrar libros");
            System.out.println("4. Ver libros");
            System.out.println("5. Borrar un libro");
            System.out.println("6. Encontrar un libro directamente en el almacen");
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
                    this.creaLibro();
                    opcion = null;
                    break;
                case 2:
                    actualizarInfo();
                    opcion = null;
                    break;
                case 3:
                    this.libros=new ArrayList<>();
                    escribeArchivo();
                    break;
                case 4:
                    for (Libro cada_libro : this.libros){
                        System.out.println(cada_libro);
                    }
                    opcion = null;
                    break;
                case 5:
                    eliminaLibro();
                    opcion=null;
                    break;
                case 7:
                    escribeArchivo();
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
        // asi el bufferedreader se cierra despues del try, sin tener que cerrarlo.


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
            err.printStackTrace();
        }
        return librosLeer;
    }
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
            err.printStackTrace();
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
        System.out.println("Libro creado y añadido a la lista de libros correctamente");
    }

    /**
     * Método que comprueba si hay libros, luego pide un libro para modificar y muestra las opciones a modificar por
     * terminal para que el usuario modifique los valores del libro seleccionado.
     */
    public void actualizarInfo() {
        if (this.libros.isEmpty()) {
            System.out.println("No hay libros almacenados");
            return;
        }
        Libro libroModificar = pideLibro();
        Integer opcion = null;
        while (opcion == null) {
            opcion = pideInteger("Que quieres cambiar\n1.Nombre\n2.Autor\n3.Páginas\n4.Año");
            switch (opcion) {
                case 1:
                    libroModificar.setNombre(pideString("Introduce un nuevo nombre"));
                    break;
                case 2:
                    libroModificar.setAutor(pideString("Introduce un nuevo Autor"));
                    break;
                case 3:
                    libroModificar.setPaginas(pideInteger("Introduce las páginas"));
                    break;
                case 4:
                    libroModificar.setAño(pideInteger("Introduce el año"));
                    break;
                default:
                    System.out.println("Opcion inválida");
                    opcion = null;
                    break;
            }
        }


    }

    /**
     *Método que busca un libro por el nombre, luego lo devuelve.
     * @return Libro
     */
    public Libro pideLibro() {
        String nombre = null;
        Optional<Libro> libroEncontradoOptional = null;
        while (nombre == null) {
            nombre = pideString("Introduce el nombre de el libro");
            try {
                String finalNombre = nombre;
                libroEncontradoOptional = this.libros.stream().filter(libro -> libro.getNombre().equals(finalNombre)).findAny();
                if (libroEncontradoOptional.isEmpty()) {
                    throw new Exception("Libro no encontrado");
                }
            } catch (Exception err) {
                System.out.println(err.getMessage());
                nombre = null;
            }

        }
        return libroEncontradoOptional.get();
    }
    public void eliminaLibro(){
        Libro liroEliminar=pideLibro();
        this.libros.remove(liroEliminar);
        System.out.println("libro eliminado correctamente");
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
    public void leerUno(){
        String nombreLibro=pideString("Introduce el nombre del libro");
        try {
            RandomAccessFile randomAccessFile=new RandomAccessFile("./almacen/almacen.txt","r");
            String linea;
            while ((linea= randomAccessFile.readLine())!=null){
                String[] datos=linea.split("\\|");
                if(datos.length>0&&datos[0].equals(nombreLibro)){
                    System.out.println("Se encontro un libro con el nombre deseado.");
                }
            }
        }catch (IOException err){
            err.printStackTrace();
        }
    }
}
