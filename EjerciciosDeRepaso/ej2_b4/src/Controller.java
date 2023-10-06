import java.util.ArrayList;
import java.util.Scanner;

public class Controller {
    private Juego juego;
    /**
     * Pide un numero de jugadores para la ruleta rusa.
     * */
    public Controller() {
        int numero = pideInt("Introduce un numero de jugadores, entre 1 y 6", 1, 6);
        ArrayList<Jugador> listaJugadores = new ArrayList<>();
        for (int i = 0; i < numero; i++) {
            listaJugadores.add(new Jugador(i + 1));
        }
        this.juego = new Juego(listaJugadores, new Revolver((int) Math.floor(Math.random() * 7), (int) Math.floor(Math.random() * 7)));


    }
    /**
     * Método que inicia la partida.
     * */
    public void iniciarPartida() {

        this.juego.ronda();

    }
    /**
     * Método que pide un int por terminal, tiene que estar comprendido entre el minimo y maximo establecido, tambien muestra un texto por
     * pantalla y al final devuelve el int introducido si es válido.
     * */
    public int pideInt(String texto, int minimo, int maximo) {
        Integer opcion = null;
        while (opcion == null) {
            System.out.println(texto);
            Scanner opcionIN = new Scanner(System.in);
            try {
                opcion = opcionIN.nextInt();
                if (opcion < minimo || opcion > maximo) {
                    throw new Error("Error, numero invalido");
                }
            } catch (Exception err) {
                System.out.println("Error al introducir el numero");
                opcion = null;
            }
        }
        return opcion;
    }
}
