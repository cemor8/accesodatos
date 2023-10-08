import java.io.StringReader;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

public class Controller {
    private ArrayList<Coleccion> colecciones;
    private ArrayList<Figura> figuras;

    public Controller(ArrayList<Coleccion> colecciones, ArrayList<Figura> figuras) {
        this.colecciones = colecciones;
        this.figuras = figuras;
    }
    /**
     * Método que muestra el menu del programa, lleva a los submenus relaccionados con las figuras y colecciones.
     * */
    public String menu(){
        System.out.println("-----MENU-----");
        System.out.println("1. Editar/Mostrar Figuras");
        System.out.println("2. Editar/Mostrar Colecciones");
        System.out.println("3. Salir");
        Integer opcion=null;
        while (opcion==null){
            Scanner opcionIN=new Scanner(System.in);
            try {
                opcion=opcionIN.nextInt();
            }catch (Exception error){
                System.out.println("Opción inválida");
                continue;
            }
            switch (opcion){
                case 1:
                    figuras();
                    break;
                case 2:
                    colecciones();
                    break;
                case 3:
                    System.exit(0);
                default:
                    System.out.println("Opcion inválida");
                    opcion=null;
            }
        }
    return menu();

    }
    /**
     * Método que pide el nombre de una coleccion, para buscarla
     * y devolverla si existe, si no devuelve null
     * @return Coleccion
     * */
    public Coleccion pideColeccion(){
        Coleccion coleccionFinal;
        String nombre_coleccion=pideString("Introduce el nombre de la coleccion");
        Optional<Coleccion> coleccionOptional=this.colecciones.stream().filter(coleccion -> coleccion.getNombreColeccion().equals(nombre_coleccion)).findAny();
        if (coleccionOptional.isEmpty()){
            return null;
        }
        coleccionFinal=coleccionOptional.get();
        return coleccionFinal;
    }
    /**
     * Método que pide el codigo de una Figura, para buscarla
     * en la lista que se le pasa al metodo
     * y devolverla si existe, si no devuelve null
     * @return Figura
     * */
    public Figura pideFigura(ArrayList<Figura> figuras){
        Figura figuraFinal;
        String codFigura=pideString("Introduce el codigo de la figura");
        Optional<Figura> figuraOptional=figuras.stream().filter(figura -> figura.getCodigo().equals(codFigura)).findFirst();
        if(figuraOptional.isEmpty()){
            return null;
        }
        figuraFinal=figuraOptional.get();
        return figuraFinal;
    }
    /**
     * Método que muestra las opciones a realizar con las figuras.
     * @return String menu figuras.
     * */
    public String figuras(){
        System.out.println("-----MENU-----");
        System.out.println("1. Crear Figura");
        System.out.println("2. Mostrar figuras sin coleccion");
        System.out.println("3. Subir precio a una figura de coleccion");
        System.out.println("4. Poner capa a superheroe");
        System.out.println("5. Volver atras");
        Integer opcion=null;
        while (opcion==null){
            Scanner opcionIN=new Scanner(System.in);
            try {
                opcion=opcionIN.nextInt();
            }catch (Exception error){
                System.out.println("Opción inválida");
                continue;
            }
            switch (opcion) {
                case 1 -> crearFigura();
                case 2 -> {
                    for (Figura cada_figura : this.figuras) {

                        System.out.println(cada_figura.toString());
                    }
                }
                case 3 -> subir();
                case 4-> asignarCapa();
                case 5 -> {
                    return menu();
                }
                default -> {
                    System.out.println("Opcion inválida");
                    opcion = null;
                }
            }
        }
        return figuras();
    }
    /**
     * Método que pide una coleccion y una figura para subirle el precio a esta.
     * */
    public void subir(){
        Coleccion coleccionFinal=pideColeccion();
        if(coleccionFinal==null){
            System.out.println("no se encontro la coleccion");
            return;
        }
        Figura figuraFinal=pideFigura(coleccionFinal.getListaFiguras());
        if(figuraFinal==null){
            System.out.println("no se encontro la figura");
            return;
        }
        Double precio=pideDouble("Introduce un nuevo precio para la figura");
        if(precio<=0){
            System.out.println("precio incorrecto, intentalo de nuevo");
        }else {
            figuraFinal.subirPrecio(precio);
            System.out.println("Precio añadido correctamente");
        }

    }
    /**
     * Método que se encarga de pedir los datos para crar un figura.
     * */
    public void crearFigura(){
        String nombre_coleccion=pideString("Introduce el nombre de la coleccion a la que pertenece");
        Figura figura= new Figura(pideString("Introduce un código para la figura"),
                pideDouble("Introduce un precio para la fiugra"),
                new Superheroe(pideString("Introduce el nombre del superheroe")),
                new Dimension(pideDouble("Introduce un alto para la figura"),pideDouble("Introduce un ancho para la figura"),pideDouble("Introduce la profundidad de la figura"))
        );
        Optional<Coleccion> coleccion_final=this.colecciones.stream().filter(coleccion -> coleccion.getNombreColeccion().equals(nombre_coleccion)).findFirst();
        Coleccion coleccion;
        if(coleccion_final.isPresent()){
           coleccion=coleccion_final.get();
           coleccion.asignarFiguras(figura);
        }else {
            System.out.println("No se encontro ninguna coleccion, se guardará en un array de figuras sin coleccion");
            this.figuras.add(figura);
        }

    }
    /**
     * Método que recibe una string, la muestra por pantalla y pide una string por terminal, luego la devuelve.
     * */
    public String pideString(String string){
        String texto=null;
        while (texto==null){
            System.out.println(string);
            Scanner textoIN=new Scanner(System.in);
            try {
                texto=textoIN.nextLine();
            }catch (Exception error){
                System.out.println("Error al introducir los datos");
            }
        }
        return texto;
    }
    /**
     * Método igual a pideString solo que devulve un Double.
     * */
    public Double pideDouble(String texto){
        Double cantidad=null;
        while (cantidad==null){
            System.out.println(texto);
            Scanner cantidadIN=new Scanner(System.in);
            try {
                cantidad=cantidadIN.nextDouble();
            }catch (Exception error){
                System.out.println("Error al introducir los datos");
            }
        }
        return cantidad;
    }
    /**
     * Método que muestra el menu referente a las colecciones.
     * */
    public  String colecciones(){
        System.out.println("-----MENU-----");
        System.out.println("1. Crear Coleccion");
        System.out.println("2. Añadir figura a coleccion");
        System.out.println("3. Mostrar Contenido de la colección");
        System.out.println("4. Mostrar figura mas cara de coleccion");
        System.out.println("5. Mostrar Volumen de una coleccion");
        System.out.println("6. Mostrar precio total de coleccion");
        System.out.println("7. Mostrar figuras con capa");
        System.out.println("8. Volver atras");
        Integer opcion=null;
        while (opcion==null){
            Scanner opcionIN=new Scanner(System.in);
            try {
                opcion=opcionIN.nextInt();
            }catch (Exception error){
                System.out.println("Opción inválida");
                continue;
            }
            switch (opcion) {
                case 1 -> {
                    String nombre_coleccion = pideString("Introduce un nombre para la coleccion");
                    this.colecciones.add(new Coleccion(nombre_coleccion, new ArrayList<>()));
                }
                case 2 -> meterFigura();
                case 3 -> {
                        Coleccion coleccionTemporal=mostrarContenidoColeccion();
                        if(coleccionTemporal!=null){
                            System.out.println(coleccionTemporal);
                        }
                    }
                case 4 -> devuelvePropiedad("cara");
                case 5 -> devuelvePropiedad("volumen");
                case 6 -> devuelvePropiedad("precio");
                case 7 -> devuelvePropiedad("capa");
                case 8 -> {
                    return menu();
                }
                default -> {
                    System.out.println("Opcion inválida");
                    opcion = null;
                }
            }
        }
        return colecciones();
    }
    /**
     * Método que pide el nombre de una coleccion para mostrar sus figuras.
     * @return Menu colecciones
     * */
    public Coleccion mostrarContenidoColeccion(){

        Coleccion coleccionFinal=pideColeccion();
        if(coleccionFinal==null){
            System.out.println("no se encontro la coleccion");
            return null;
        }
        if(coleccionFinal.getListaFiguras().size()<=0){
            System.out.println("La coleccion esta vacia");
            return null;
        }else {
            System.out.println("hola");
            return coleccionFinal;
        }




    }
    /**
     * Método que pide un código de una figura para luego introducirla en una coleccion la cual se pide su nombre tambien por
     * terminal.
     * @return menu
     * */
    public String meterFigura(){
        Coleccion coleccionFinal=pideColeccion();
        if(coleccionFinal==null){
            return menu();
        }
        Figura figuraFinal=pideFigura(this.figuras);
        if(figuraFinal==null){
            return menu();
        }
        coleccionFinal.asignarFiguras(figuraFinal);
        System.out.println("Figura introducida correctamente");
        return colecciones();

    }
    /**
     * Método que recibe una string y dependiendo de el valor de esta string, muestra
     * la figura mas cara, el precio total de la coleccion, su volumen total o los datos
     * de las figuras con capa.
     * */
    public void devuelvePropiedad(String propiedad){
        Coleccion coleccionFinal=pideColeccion();
        if(coleccionFinal!=null){
            switch (propiedad) {
                case "cara" -> System.out.println(coleccionFinal.masValioso());
                case "precio" -> System.out.println(coleccionFinal.getValorColeccion());
                case "volumen" -> System.out.println(coleccionFinal.getVolumenColeccion());
                case "capa"-> System.out.println(coleccionFinal.conCapa());
                default -> System.out.println("error");
            }
        }else {
            System.out.println("coleccion no encontrada");
        }
    }
    public void asignarCapa(){
        Coleccion coleccionFinal=pideColeccion();
        if (coleccionFinal==null){
            System.out.println("coleccion no encontrada");
            return;
        }
        Figura figuraFinal=pideFigura(coleccionFinal.getListaFiguras());
        if (figuraFinal==null){
            System.out.println("figura no encontrada");
            return;
        }
        figuraFinal.getSuperheroe().setCapa(true);
        System.out.println("capa añadida correctamente");
    }

}
