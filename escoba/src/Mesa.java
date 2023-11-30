import javax.print.DocFlavor;
import java.util.ArrayList;

public class Mesa {
    private Participante participante1;
    private Participante participante2;
    private Baraja baraja;
    private ArrayList<Carta> cartasEnMesa = new ArrayList<>();
    private int puntosAJugar;

    public Mesa(Participante participante1, Participante participante2, Baraja baraja, int puntosAJugar) {
        this.participante1 = participante1;
        this.participante2 = participante2;
        this.baraja = baraja;
        this.puntosAJugar = puntosAJugar;
    }
    /**
     * Método que inicia una partida en el juego de la escoba, primero se comprueba quien empieza y quien reparte
     * al inicio, luego se comprueba si hay baraja de mano para volver a colocar las cartas, por ultimo se empieza la partida
     * */
    public void iniciarPartida(){
        this.decidirOrden();
        this.baraja.barajar();

        this.repartirCartas(this.participante1.getMano());
        this.repartirCartas(this.participante2.getMano());

        this.baraja.ponerEnMesa(this.cartasEnMesa);

        //compruebo si hay baraja de mano
        while (this.barajaMano()){
            this.participante2.getCartasGanadas().addAll(this.cartasEnMesa);
            this.cartasEnMesa = new ArrayList<>();
            this.baraja.ponerEnMesa(this.cartasEnMesa);
        }
        if(this.baraja.getCartas().isEmpty()){
            // aqui gana jugador 2
        }

        while (this.participante1.getPuntosTotales()<this.puntosAJugar && this.participante2.getPuntosTotales()<this.puntosAJugar){
            this.jugarRonda();
        }


    }
    /**
     * Método que se encarga de jugar una roda de la partida, el participante 1 que es al que le toca el turno,
     * ejecuta su método de jugar, luego se intercambian los participantes para que en la siguiente ronda juegue el
     * participante 2.
     * */
    public void jugarRonda(){
        this.participante1.jugar();
        Participante temp = this.participante1;
        this.participante1 = this.participante2;
        this.participante2 = temp;
    }

    /**
     * Método que se encarga de decidir el orden del comienzo de los jugadores, como el jugador 1 siempre es el ganador,
     * se comprueban las cartas y si la del jugador 2 es mayor se intercambian los jugadores. Si sale la misma carta, se
     * vuelven a meter las 2 cartas en la baraja, se baraja el mazo de cartas y se vuelve a sacar 2 cartas.
     * */
    public void decidirOrden(){
        Carta cartaJugador1 = baraja.sacarCarta();
        Carta cartaJugador2 = baraja.sacarCarta();

        this.baraja.meterCarta(cartaJugador1);
        this.baraja.meterCarta(cartaJugador2);
        this.baraja.barajar();

        if(cartaJugador1.getValorNumerico() == cartaJugador2.getValorNumerico()){
            this.decidirOrden();
        }else if (cartaJugador1.getValorNumerico()< cartaJugador2.getValorNumerico()){
            Participante temp = this.participante1;
            this.participante1 = this.participante2;
            this.participante2 = temp;
        }
    }
    /**
     * Método que se encarga de repartir las cartas a los jugadores
     * */
    public void repartirCartas(ArrayList<Carta> mano){
        if(this.baraja.getCartas().isEmpty()){
            return;
        }
        while (mano.size()<3){
            mano.add(this.baraja.getCartas().get(0));
            this.baraja.getCartas().remove(0);
        }
    }
    public boolean barajaMano(){
        int valorTotal = 0;
        for (Carta carta : this.cartasEnMesa){
            valorTotal += carta.getValorNumerico();
        }
        return valorTotal == 15;
    }
}
