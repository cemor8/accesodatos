import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {
    public Controller() {
    }
    /**
     * Método que pide por terminal un patron y una string donde buscar el patron,
     * si la string es el patron exacto, lo indica.
     * */
    public void exactoPatron(){
        String patronBuscar=pideString("Introduce el patron que quieres buscar");
        String cadenaTexto=pideString("Introduce la cadena de texto donde quieres buscar");
        Pattern patron= Pattern.compile(patronBuscar);
        Matcher matcher=patron.matcher(cadenaTexto);
        if(matcher.matches()){
            System.out.println("Patron encontrado");
        }else {
            System.out.println("Patron no encontrado");
        }

    }
    /**
     * Método que pide una subcadena de texto que se buscara en la string que se pedira
     * a continuacion, si esta string contiene la subcadena de texto, lo indicara.
     * */
    public void patronSubstring(){
        String substringBuscar=pideString("Introduce la subcadena de texto para buscar");
        String cadenaTexto=pideString("Introduce la cadena de texto donde quieres buscar");
        Pattern patron= Pattern.compile(substringBuscar);
        Matcher matcher=patron.matcher(cadenaTexto);
        if(matcher.find()){
            System.out.println("Substring encontrada");
        }else {
            System.out.println("Substring no encontrada");
        }

    }
    /**
     * Método que pide un patron y una string, si la string empieza por el patron,
     * lo indica, si no, dira que no comienza por el.
     * */
    public void empiezaPor(){
        String patrongBuscar=pideString("Introduce el patron que quieres buscar");
        String stringBuscar=pideString("Introduce la cadena de texto donde quieres buscar");

        Pattern patron= Pattern.compile(patrongBuscar);
        Matcher matcher=patron.matcher(stringBuscar);

        if(matcher.find() && matcher.start()==0){
            System.out.println("La cadena comienza con el patron");
        }else {
            System.out.println("La cadena no comienza con el patron");
        }

    }
    /**
     * Método que pide una string y un conjunto de caracteres los cuales son los que pueden formar la string,
     * si esto se cumple lo indica
     * */
    public void cadenaConSoN(){
        String patronBuscar=pideString("Introduce el conjunto de caracteres que forman la string");
        String stringBuscar=pideString("Introduce la cadena de texto donde quieres buscar");

        String patronFinal= "^["+patronBuscar+"]+$";
        Pattern patron= Pattern.compile(patronFinal);
        Matcher matcher=patron.matcher(stringBuscar);

        if(matcher.matches()){
            System.out.println("La cadena esta formada solo por el patron");
        }else {
            System.out.println("La cadena no esta formada solo por el patron");
        }
    }
    /**
     * Método que pide una string e indica si contiene el numero cero seguido del numero 1
     * */
    public void cadenaCon0noseguidode1(){
        String patronBuscar="01";
        String stringBuscar=pideString("Introduce la cadena de texto donde quieres buscar");

        Pattern patron= Pattern.compile(patronBuscar);
        Matcher matcher=patron.matcher(stringBuscar);

        if(matcher.find()){
            System.out.println("La cadena contiene el numero 1 seguido de un 0");
        }else {
            System.out.println("La cadena no contiene el numero 1 seguido de un 0");
        }

    }

    /**
     * Método que comprueba si un email es valido, si lo es, lo indica.
     * */
    public void esEmailValido(){
        String stringBuscar=pideString("Introduce un email");
        String patronBuscar="^[A-Za-z0-9+_.-]+@[A-Za-z.]$";
        Pattern patron=Pattern.compile(patronBuscar);
        Matcher matcher=patron.matcher(stringBuscar);

        if(matcher.matches()){
            System.out.println("Email valido");
        }else {
            System.out.println("Email invalido");
        }
    }
    /**
     * Método que recibe una String para mostrarla por terminal, luego pide un texto String y lo devuelve.
     * */
    public String pideString(String texto){
        String textoDevolver=null;
        while (textoDevolver==null){
            System.out.println(texto);
            Scanner textoDevolverIN=new Scanner(System.in);
            try {
                textoDevolver=textoDevolverIN.nextLine();
            }catch (Exception err){
                System.out.println("error al introducir el texto");
            }
        }
        return textoDevolver;
    }
}
