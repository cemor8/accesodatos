import java.util.ArrayList;


public class Main {
    public static void main(String[] args) {
        Jugador jugador1 = new Jugador(null,new ArrayList<>());
        IA ia = new IA(new ArrayList<>());
        Mesa mesa = new Mesa(jugador1,ia,new Baraja(),1);
        mesa.iniciarPartida();
    }
}