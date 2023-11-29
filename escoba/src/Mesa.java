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
    public void iniciarPartida(){
        this.decidirOrden();
        this.baraja.barajar();

        this.participante2.repartir();
        this.baraja.ponerEnMesa(this.cartasEnMesa);
        while (participante1.getPuntosTotales()<puntosAJugar && participante2.getPuntosTotales()<puntosAJugar){
            this.jugarRonda();
        }


    }

    public void jugarRonda(){

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
}
