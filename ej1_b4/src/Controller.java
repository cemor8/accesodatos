import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Collections;

public class Controller {
    private ArrayList<Carta> baraja;
    private ArrayList<Carta> cartasSacadas;
    /**
     * Crea la baraja
     * */
    public Controller() {
        this.baraja=new ArrayList<>();
        this.cartasSacadas=new ArrayList<>();
        ArrayList<String> palos=new ArrayList<>(List.of("oros","copas","espadas","bastos"));
        for (String palo : palos) {

            for (int j = 1; j <= 10; j++) {

                switch (j) {
                    case 8:
                        baraja.add(new Carta("sota", palo));
                        break;
                    case 9:
                        baraja.add(new Carta("caballo", palo));
                        break;
                    case 10:
                        baraja.add(new Carta("rey", palo));
                        break;
                    default:
                        baraja.add(new Carta(String.valueOf(j), palo));
                        break;
                }

            }
        }

    }
    /**
     * Muestra opciones a realizar en el programa
     * */
    public void menu(){
        Integer opcion=null;
        while (opcion==null){
            System.out.println("Que quieres hacer");
            System.out.println("1. Barajar");
            System.out.println("2. Devolver siguiente carta");
            System.out.println("3. Numero de Cartas disponibles");
            System.out.println("4. Mostrar una cantidad determinada de cartas");
            System.out.println("5. Mostrar las cartas que ya han salido");
            System.out.println("6. Mostrar la baraja");
            System.out.println("7. Salir");
            Scanner opcionIN=new Scanner(System.in);
            try {
                opcion=opcionIN.nextInt();
            }catch (Exception err){
                System.out.println("Error al introducir la opcion");
                continue;
            }
            switch (opcion){
                case 1:
                    barajar();
                    break;
                case 2:
                    Carta carta=siguienteCarta();
                    if(carta!=null){
                        sacarCarta(carta);
                    }
                    break;
                case 3:
                    if(this.baraja.isEmpty()){
                        break;
                    }
                    System.out.println("Se pueden repartir "+this.baraja.size()+" cartas");
                    break;
                case 4:
                    darCartas();
                    break;
                case 5:
                    for(Carta cadaCartaSacada : this.cartasSacadas){
                        System.out.println(cadaCartaSacada);
                    }
                    break;
                case 6:
                    for(Carta cadaCartaBaraja : this.baraja){
                        System.out.println(cadaCartaBaraja);
                    }
                    break;
                case 7:
                    System.exit(0);
                default:
                    menu();
                    break;
            }
            opcion=null;
        }
    }
    /**
     * Método que recibe una carta y la mete en la lista de las cartas que ya han salido, tambien la saca de la
     * baraja y la muestra por pantalla
     * */
    public void sacarCarta(Carta carta){
        this.cartasSacadas.add(carta);
        this.baraja.remove(carta);
        System.out.println(carta);
    }
    /**
     *Método que baraja de forma aleatoria la carta
     * */
    public void barajar(){
        Collections.shuffle(this.baraja);
    }
    /**
     * Método que devuelve la primera carta de la baraja, si hay una lo indica y si está vacia no
     * deja sacar cartas.
     * @return Carta
     * */
    public Carta siguienteCarta(){
        Carta carta = null;
        if(!this.baraja.isEmpty()){
            if(this.baraja.size()==1){
                System.out.println("Es la ultima carta");
            }
            carta=this.baraja.get(0);
        }else {
            System.out.println("No hay cartas");
        }
        return carta;

    }
    /**
     * Método que pide por terminal un numero de cartas a sacar, si puede sacarlas, las muestra por pantalla, luego llama
     * al método que se encarga de administrar las listas de las cartas, sacarCarta()
     * */
    public void darCartas(){

        Integer opcion=null;
        while (opcion==null){
            System.out.println("Introduce el numero de cartas que quieres mostrar");
            Scanner opcionIN=new Scanner(System.in);
            try {
                opcion=opcionIN.nextInt();
            }catch (Exception err){
                System.out.println("Error al introducir la opcion");
                continue;
            }
            if((opcion>0&&opcion<=this.baraja.size())&&!this.baraja.isEmpty()){
                for(int i=0;i<opcion;i++){
                    sacarCarta(this.baraja.get(0));
                }
            }else {
                System.out.println("No se puede repartir ese numero de cartas");
            }
        }
    }
}
