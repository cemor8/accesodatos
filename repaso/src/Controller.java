import java.awt.image.AreaAveragingScaleFilter;
import java.util.*;

public class Controller {
    public int ej1() {
        String texto = null;
        Scanner textoIN = new Scanner(System.in);
        System.out.println("Introduce un texto");
        try {
            texto = textoIN.nextLine();
        } catch (Exception error) {
            System.out.println("error al pedir el texto");
            return ej1();

        }
        System.out.println("Introduce un texto");
        String letra=null;
        Scanner letraIN = new Scanner(System.in);
        try {
            letra = letraIN.nextLine();

        } catch (Exception error) {
            System.out.println("error al pedir la letra");
            return ej1();

        }

        int contador=0;
        int posicion=0;
        posicion=texto.indexOf(letra);
        while (posicion!=-1){
            contador+=1;
            posicion=texto.indexOf(letra,posicion+1);

        }
        return contador;
    }
    public String ej2(){
       String producto1=pideProducto();
       Float precio1=pidePrecio();
       Integer unidades1=pideUnidades();

        String producto2=pideProducto();
        Float precio2=pidePrecio();
        Integer unidades2=pideUnidades();

        String producto3=pideProducto();
        Float precio3=pidePrecio();
        Integer unidades3=pideUnidades();

        Float total=(precio1*unidades1)+(precio2*unidades2)+(precio3*unidades3);
        System.out.println("-------------------");
        System.out.println("producto--Unidades--Precio/unidad--Total");
        System.out.println("--------------------");
        System.out.println(producto1+" -- "+unidades1+"---"+precio1);
        System.out.println(producto2+" -- "+unidades2+"---"+precio2);
        System.out.println(producto3+" -- "+unidades3+"---"+precio3);
        System.out.println("--------------------");
        System.out.println("total "+total);
        return "hola";


    }
    public String pideProducto(){
        System.out.println("Introduce el nombre del producto");
        String producto = null;
        Scanner productoIN = new Scanner(System.in);
        try {
            producto=productoIN.nextLine();
        }catch (Exception error){
            return ej2();
        }
        return producto;
    }
    public Integer pideUnidades(){
        System.out.println("Introduce las unidades");
        Integer producto = null;
        Scanner productoIN = new Scanner(System.in);
        while (producto==null){
            try {
                producto=productoIN.nextInt();
            }catch (Exception error){
                System.out.println("invalido introducelo de nuevo");
            }
        }
        return producto;
    }
    public Float pidePrecio(){
        System.out.println("Introduce el precio");
        Float precio = null;
        Scanner precioIN = new Scanner(System.in);
        while (precio==null){
            try {
                precio=precioIN.nextFloat();
            }catch (Exception error){
                System.out.println("invalido introducelo de nuevo");
            }
        }
        return precio;
    }
    public ArrayList<Integer> ej3(){
        ArrayList<Integer> lista=new ArrayList<>(List.of(1,2,3,4,5,5,6,7,5));
        Integer numero=5;
        for(Integer i=lista.size()-1;i>=0;i--){

            if(lista.get(i)==numero){
                lista.remove(lista.get(i));
            }
        }

       return lista;
    }
    public String ej4(){
        HashMap<String,Double> notas=new HashMap<String,Double>();
        notas.put("Pepe",10.0);
        notas.put("Pepe G",7.0);
        notas.put("Pepe M",6.0);
        notas.put("Paco",5.0);
        String nombre="Pepe M";
        for(Map.Entry<String,Double>cada : notas.entrySet()){
            if(cada.getKey()==nombre){
                System.out.println(cada.getValue());
            }
        }
        return "hola";
    }
    public ArrayList<Integer> ej5(){
        ArrayList<Integer> numeros=new ArrayList<>(List.of(1,2,3,4,5,6));
        ArrayList<Integer> numeros2=new ArrayList<>();
        for(int i=numeros.size()-1;i>=0;i--){
            numeros2.add(numeros.get(i));
        }
        return numeros2;
    }
    public String ej6(){
        System.out.println("introduce el numero de comensales");
        Integer comensales=null;
        Scanner comensalesIN=new Scanner(System.in);
        try {
            comensales=comensalesIN.nextInt();
        }catch (Exception error){
            return ej6();
        }
        if(comensales>5 || comensales<1){
            System.out.println("comensales incorrecto");
            return ej6();
        }
        ArrayList<Integer> menus=new ArrayList<>();
        for(int i=1;i<=comensales;i++){
            Integer menu=pideMenu();
            menus.add(menu);
        }

        for(int i=0;i<comensales;i++){
            System.out.println("Comensal "+(i+1)+" menu "+menus.get(i));
        }
        return "hola";
    }
    public Integer pideMenu(){

        System.out.println("introduce el numero del menu");
        Integer menu=null;
        Scanner menuIN=new Scanner(System.in);
        while (menu==null){
            try {
                menu=menuIN.nextInt();
            }catch (Exception error){

            }
            if(menu<1 ||menu>3){
                menu=null;
            }
        }
        return menu;

    }

}

