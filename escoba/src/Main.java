import java.util.ArrayList;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        Jugador jugadorTerminal = new Jugador(new Usuario("Carlos"),new ArrayList<>());
        IA ia = new IA(new ArrayList<>());
        Baraja baraja = new Baraja();
        Mesa mesa = new Mesa(jugadorTerminal,ia,baraja,1);
        mesa.iniciarPartida();
    }
}