import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {
    public Controller() {
    }
    /**
     * Método que comprueba si el numero introducido es un entero.
     * */
    public void validarEntero(){
        Matcher matcher= creaMatcher(pideString("Introduce un numero"), "^[0-9]+$");
        if(matcher.matches()){
            System.out.println("Es un entero");
        }else {
            System.out.println("no es un entero");
        }

    }/**
     * Método que comprueba si el numero introducido es un negativo.
     * */
    public void validarNumeroNegativo(){
        Matcher matcher= creaMatcher(pideString("Introduce un numero"), "-[0-9]+([.,][0-9]+)?");
        if(matcher.matches()){
            System.out.println("Es un numero negativo");
        }else {
            System.out.println("no es un numero negativo");
        }
    }
    /**
     * Método que valida si el numero es un entero negativo
     * */
    public void validarNumeroNegativoEntero(){
        Matcher matcher= creaMatcher(pideString("Introduce un numero"), "-[0-9]+");
        if(matcher.matches()){
            System.out.println("Es un entero negativo");
        }else {
            System.out.println("no es un entero negativo");
        }
    }
    /**
     * Método que comprueba si el dni introducido es correcto
     * */
    public void validarDni(){
        ArrayList<String> letras=new ArrayList<>(List.of("T", "R", "W", "A", "G", "M", "Y", "F", "P", "D", "X", "B", "N", "J", "Z", "S", "Q", "V", "H", "L", "C", "K"));
        String dni=pideString("Introduce un DNI");
        Matcher matcher= creaMatcher(dni, "^[0-9]{8}+[A-Z]{1}$");
        if (!matcher.matches()){
            System.out.println("DNI incorrecto");
            return;
        }
        String numeroString= dni.substring(0,8);
        int numeroDni= Integer.parseInt(numeroString);
        String letra=letras.get(numeroDni%23);
        if(letra.charAt(0)==(dni.charAt(8))){
            System.out.println("Dni correcto");
        }else {
            System.out.println("Dni incorrecto");
        }


    }
    /**
     * Método que valida si una direccion ipv4 es valida
     * */
    public void validarIp(){
        Matcher matcher= creaMatcher(pideString("Introduce la ipv4"), "^(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(?:\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}$");
        if (matcher.matches()){
            System.out.println("Ip valida");
        }else {
            System.out.println("ip invalida");
        }
    }
    /**
     * Método que valida si una matricula es valida, tiene en cuenta las letras que no estan permitidas
     * en una matricula española.
     * */
    public void validaMatricula(){
        Matcher matcher=creaMatcher(pideString("Introduce una matricula"),"^\\d{4}(?!.*CH)(?!.*LL)(?![AEIÑOQU])[A-Z]{3}$");
        if(matcher.matches()){
            System.out.println("Matricula valida");
        }else {
            System.out.println("Matricula Invalida");
        }
    }
    /**
     * Método que indica si el numero introducido es binario.
     * */
    public void esBinario(){
        Matcher matcher=creaMatcher(pideString("Introduce un numero binario"),"^[0-1]+$");
        if (matcher.matches()){
            System.out.println("El numero es binario");
        }else {
            System.out.println("El numero no es binario");
        }
    }
    /**
     * Método que indica si un numero es real positivo
     * */
    public void esRealPositivo(){
        Matcher matcher=creaMatcher(pideString("Introduce un numero"),"^\\d+([.,][0-9]+)?$");
        if (matcher.matches()){
            System.out.println("El numero es real positivo");
        }else {
            System.out.println("El numero no es real positivo");
        }

    }
    /**
     * Método que indica si una fecha es valida.
     * */
    public void validaFecha(){
        // la parte final ((?:(?:0[48]00|[13579][26]00|[2468][048]00)|(?:\d\d)?(?:0[48]|[2468][048]|[13579][26]))) que es la que comprueba los años bisiestos, la busque en internet.
        Matcher matcher=creaMatcher(pideString("Introduce una fecha"),"^(?:(?:0?[1-9]|1\\d|2[0-8])(\\/|-)(?:0?[1-9]|1[0-2]))(\\/|-)(?:[1-9]\\d\\d\\d)$|^(?:(?:31(\\/|-)(?:0?[13578]|1[02]))|(?:(?:29|30)(\\/|-)(?:0?[1,3-9]|1[0-2])))(\\/|-)(?:[1-9]\\d\\d\\d)$|^(29(\\/|-)0?2)(\\/|-)(?:(?:0[48]00|[13579][26]00|[2468][048]00)|(?:\\d\\d)?(?:0[48]|[2468][048]|[13579][26]))$");
        if (matcher.matches()){
            System.out.println("Fecha válida");
        }else {
            System.out.println("Fecha invalida");
        }
    }
    /**
     * Método que valida un usuario de la red social X, debe de contener entre 1 y 15 caracteres, no puede tener ni
     * espacios en blanco ni caracteres especiales.
     * */
    public void validarUsuario(){
        Matcher matcher=creaMatcher(pideString("Introduce un usuario"),"^(?![\\s])(?![.*,#-])[A-Za-z0-9]{1,15}$");
        if(matcher.matches()){
            System.out.println("Usuario válid");
        }else {
            System.out.println("Usuario inválido");
        }
    }
    /**
     * Método que valida un ISB, identificador de un libro compuesto por 13 caracteres los cuales
     * empiezan por 978 u 979, separados por guiones de una manera concreta, utiliza la validacion
     * de isbn, comprueba si es real o no.
     * */
    public void validarIsbn(){
        String isbn=pideString("Introduce un ISBN");
        Matcher matcher=creaMatcher(isbn,"^(978|979)-[0-9]{2}-[0-9]{5}-[0-9]{2}-[0-9]{1}$");
        if(!matcher.matches()){
            System.out.println("ISBN inválido");
            return;
        }
        int resultado=0;
        int i2=0;
        for(char i = 0 ;i<isbn.length();i++){

            if(String.valueOf(isbn.charAt(i)).equals("-")){
                continue;
            }
            i2++;
            if (i2%2!=0){
                resultado+=Integer.parseInt(String.valueOf(isbn.charAt(i)));
            }else {
                resultado+=3*Integer.parseInt(String.valueOf(isbn.charAt(i)));
            }

        }
        if (resultado%10==0){
            System.out.println("ISBN válido");
        }else {
            System.out.println("ISBN inválido");
        }

    }

    /**
     * Método que crea un matcher y lo devuelve a partir de las strings recibidas, la primera la cadena de texto que se quiere
     * validar y la segunda el patron que debe de cumplir la cadena de texto.
     * @return Matcher
     * */
    public Matcher creaMatcher(String stringBuscar, String patronBuscar){
        Pattern patron=Pattern.compile(patronBuscar);
        return patron.matcher(stringBuscar);
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
