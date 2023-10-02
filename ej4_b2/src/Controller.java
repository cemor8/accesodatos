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
    public String figuras(){
        System.out.println("-----MENU-----");
        System.out.println("1. Crear Figura");
        System.out.println("2. Mostrar figuras sin coleccion");
        System.out.println("3. Volver atras");
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
                    crearFigura();
                    break;
                case 2:
                    for(Figura cada_figura : this.figuras){

                        System.out.println(cada_figura.toString());
                    }
                    break;
                case 3:
                    return menu();
                default:
                    System.out.println("Opcion inválida");
                    opcion=null;
            }
        }
        return figuras();
    }
    public void crearFigura(){
        String codigo=pideString("Introduce un código para la figura");
        String nombre_superheroe=pideString("Introduce el nombre del superheroe");
        Double precio=pideDouble("Introduce un precio para la fiugra");
        Double ancho=pideDouble("Introduce un ancho para la figura");
        Double alto=pideDouble("Introduce un alto para la figura");
        Double profundidad=pideDouble("Introduce la profundidad de la figura");
        String nombre_coleccion=pideString("Introduce el nombre de la coleccion a la que pertenece");
        Figura figura= new Figura(codigo,precio,new Superheroe(nombre_superheroe),new Dimension(alto,ancho,profundidad));
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
    public  String colecciones(){
        System.out.println("-----MENU-----");
        System.out.println("1. Crear Coleccion");
        System.out.println("2. Añadir figura a coleccion");
        System.out.println("3. Mostrar Contenido de la colección");
        System.out.println("4. Volver atras");
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
                    String nombre_coleccion=pideString("Introduce un nombre para la coleccion");
                    this.colecciones.add(new Coleccion(nombre_coleccion,new ArrayList<>()));
                    break;
                case 2:
                    meterFigura();
                    break;
                case 3:
                    mostrarContenidoColeccion();
                    break;
                case 4:
                return menu();
                default:
                    System.out.println("Opcion inválida");
                    opcion=null;
            }
        }
        return colecciones();
    }
    /**
     * Método que pide el nombre de una coleccion para mostrar sus figuras.
     * @return Menu colecciones
     * */
    public String mostrarContenidoColeccion(){
        String nombre_colección=pideString("Introduce el nombre de la coleccion");
        Optional<Coleccion>coleccion_encontrada=this.colecciones.stream().filter(coleccion -> coleccion.getNombreColeccion().equals(nombre_colección)).findFirst();
        if(coleccion_encontrada.isPresent()){
            Coleccion coleccion_final=coleccion_encontrada.get();
            if(coleccion_final.getListaFiguras().size()<=0){
                System.out.println("La coleccion esta vacia");
                return menu();
            }
            System.out.println(coleccion_final);
        }
        System.out.println("No se encontro la coleccion, asegurese de que esa coleccion existe");
        return colecciones();

    }
    /**
     * Método que pide un código de una figura para luego introducirla en una coleccion la cual se pide su nombre tambien por
     * terminal.
     * @return menu
     * */
    public String meterFigura(){
        String figura_codigo=pideString("Introduce el codigo de la figura");

        Optional<Figura>figura_encontrada=this.figuras.stream().filter(figura -> figura.getCodigo().equals(figura_codigo)).findFirst();

        if(figura_encontrada.isPresent()){
            Figura figura;
            figura=figura_encontrada.get();
            String nombre_coleccion=pideString("Introduce el nombre de la coleccion");
            Optional<Coleccion>coleccion_encontrada=this.colecciones.stream().filter(coleccion -> coleccion.getNombreColeccion().equals(nombre_coleccion)).findFirst();
            if(coleccion_encontrada.isPresent()){
                System.out.println("Figura introducida correctamente");
                Coleccion coleccion;
                coleccion=coleccion_encontrada.get();
                coleccion.asignarFiguras(figura);
            }else {
                System.out.println("Coleccion erronea, vuelve a empezar");
                return meterFigura();
            }

        }else {
            System.out.println("Figura erronea, vuelve a empezar");
            return meterFigura();

        }
        return colecciones();
    }
}
