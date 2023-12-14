import javax.print.DocFlavor;
import java.util.ArrayList;

public class Mesa {
    private Participante participante1;
    private Participante participante2;
    private Baraja baraja;
    private ArrayList<Carta> cartasEnMesa = new ArrayList<>();
    private int puntosAJugar;
    private int numeroTurno = 0;

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
        this.participante1.setMesa(this);
        this.participante2.setMesa(this);
        while (this.participante1.getPuntosTotales()<this.puntosAJugar && this.participante2.getPuntosTotales()<this.puntosAJugar){
            this.crearRonda();
        }

    }
    public void crearRonda(){
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
            this.baraja = new Baraja();
            return;
        }

        boolean rondaAcabada = false;
        while (!rondaAcabada){
            System.out.println("Cartas en la mesa ");
            System.out.println(this.cartasEnMesa);
            rondaAcabada = this.jugarTurno();


        }
        this.baraja = new Baraja();
        this.comprobarDiferencia();
    }


    /**
     * Método que se encarga de jugar una roda de la partida, el participante 1 que es al que le toca el turno,
     * ejecuta su método de jugar, luego se intercambian los participantes para que en la siguiente ronda juegue el
     * participante 2.
     * */
    public boolean jugarTurno(){
        //cambiar lo de las cartas en la ronda

        this.numeroTurno+=1;

        this.participante1.jugar(this.cartasEnMesa);
        Participante temp = this.participante1;
        this.participante1 = this.participante2;
        this.participante2 = temp;


        if(this.participante1.getMano().isEmpty() && this.participante2.getMano().isEmpty() && !this.baraja.getCartas().isEmpty()) {
            this.repartirCartas(this.participante1.getMano());
            this.repartirCartas((this.participante2.getMano()));
            return false;

        }else if (this.participante1.getMano().isEmpty() && this.participante2.getMano().isEmpty() && this.baraja.getCartas().isEmpty()){
            if(!this.cartasEnMesa.isEmpty()){
                if(this.participante1.getUltimaRondaObtieneCartas() > this.participante2.getUltimaRondaObtieneCartas()){
                    this.participante1.getCartasGanadas().addAll(this.cartasEnMesa);
                }else {
                    this.participante2.getCartasGanadas().addAll(this.cartasEnMesa);
                }
                this.cartasEnMesa = new ArrayList<>();
            }
            this.numeroTurno = 0;
            this.resumenRonda();
            return true;
        }
        return false;
    }

    /**
     * Método que se encarga de decidir el orden del comienzo de los jugadores, como el jugador 1 siempre es el ganador,
     * se comprueban las cartas y si la del jugador 2 es mayor se intercambian los jugadores. Si sale la misma carta, se
     * vuelven a meter las 2 cartas en la baraja, se baraja el mazo de cartas y se vuelve a sacar 2 cartas.
     * */
    public void decidirOrden(){
        System.out.println("Se decide el orden");
        this.baraja.barajar();
        Carta cartaJugador1 = baraja.sacarCarta();
        Carta cartaJugador2 = baraja.sacarCarta();

        this.baraja.meterCarta(cartaJugador1);
        this.baraja.meterCarta(cartaJugador2);


        if(cartaJugador1.getValorNumerico() == cartaJugador2.getValorNumerico()){
            this.decidirOrden();
        }else if (cartaJugador1.getValorNumerico()< cartaJugador2.getValorNumerico()){
            System.out.println("Empieza la IA");
            Participante temp = this.participante1;
            this.participante1 = this.participante2;
            this.participante2 = temp;
            return;
        }
        System.out.println("Empieza el Jugador");
    }
    /**
     * Método que se encarga de repartir las cartas a los jugadores
     * */
    public void repartirCartas(ArrayList<Carta> mano){
        System.out.println("repartiendo");
        if(this.baraja.getCartas().isEmpty()){
            return;
        }
        while (mano.size()<3){
            mano.add(this.baraja.getCartas().get(0));
            this.baraja.getCartas().remove(0);
        }
    }
    /**
     * Método que se encarga de comprobar si hay una
     * baraja de mano.
     * */
    public boolean barajaMano(){
        int valorTotal = 0;
        for (Carta carta : this.cartasEnMesa){
            valorTotal += carta.getValorNumerico();
        }
        return valorTotal == 15;
    }
    public void resumenRonda(){
        System.out.println("\n");
        System.out.println("\n");
        //comprobar fin de ronda, quien se lleva los puntos
        System.out.println("\t ----------RESUMEN-----------");
        //puntos por cartas
        System.out.println("\n");
        System.out.println("\n");

        if(this.participante1.getCartasGanadas().size() > this.participante2.getCartasGanadas().size()){
            this.participante1.setPuntosCartas(this.participante1.getPuntosCartas()+1);
        }else if(this.participante2.getCartasGanadas().size() > this.participante1.getCartasGanadas().size()){
            this.participante2.setPuntosCartas(this.participante1.getPuntosCartas()+1);
        }

        if(this.participante1.cantidadOros() > this.participante2.cantidadOros()){
            this.participante1.setPuntosOros(this.participante1.getPuntosOros()+2);
        }else if(this.participante1.cantidadOros() < this.participante2.cantidadOros()){
            this.participante2.setPuntosOros(this.participante2.getPuntosOros()+2);
        }

        if(this.participante1.tiene7oros()){
            this.participante1.setPuntosVelo(this.participante1.getPuntosVelo() + 1);
        }else {
            this.participante2.setPuntosVelo(this.participante2.getPuntosVelo() + 1);
        }

        if(this.participante1.cantidadSietes() > this.participante2.cantidadSietes()){
            this.participante1.setPuntosSietes(this.participante1.getPuntosSietes() + 1);
        }else {
            this.participante2.setPuntosSietes(this.participante2.getPuntosSietes() + 1);
        }

        this.mostrar();
        this.participante1.resetearPuntos();
        this.participante2.resetearPuntos();

    }
    public void mostrar(){
        if(participante1 instanceof Jugador){
            System.out.println("\nPUNTOS JUGADOR ");
            System.out.println("Puntos de Escobas: " + participante1.getPuntosEscobas());
            System.out.println("Puntos de Sietes: " + participante1.getPuntosSietes());
            System.out.println("Puntos de Velo: " + participante1.getPuntosVelo());
            System.out.println("Puntos de Oros: " + participante1.getPuntosOros());
            System.out.println("Puntos de Cartas: " + participante1.getPuntosCartas());
            System.out.println("Puntos totales: "+participante1.getPuntosTotales()+"\n");

            System.out.println("\nPUNTOS IA ");
            System.out.println("Puntos de Escobas: " + participante2.getPuntosEscobas());
            System.out.println("Puntos de Sietes: " + participante2.getPuntosSietes());
            System.out.println("Puntos de Velo: " + participante2.getPuntosVelo());
            System.out.println("Puntos de Oros: " + participante2.getPuntosOros());
            System.out.println("Puntos de Cartas: " + participante2.getPuntosCartas());
            System.out.println("Puntos totales: "+participante2.getPuntosTotales()+"\n");

        }else {
            System.out.println("\nPUNTOS IA ");
            System.out.println("Puntos de Escobas: " + participante1.getPuntosEscobas());
            System.out.println("Puntos de Sietes: " + participante1.getPuntosSietes());
            System.out.println("Puntos de Velo: " + participante1.getPuntosVelo());
            System.out.println("Puntos de Oros: " + participante1.getPuntosOros());
            System.out.println("Puntos de Cartas: " + participante1.getPuntosCartas());
            System.out.println("Puntos totales: "+participante1.getPuntosTotales()+"\n");

            System.out.println("\nPUNTOS Jugador ");
            System.out.println("Puntos de Escobas: " + participante2.getPuntosEscobas());
            System.out.println("Puntos de Sietes: " + participante2.getPuntosSietes());
            System.out.println("Puntos de Velo: " + participante2.getPuntosVelo());
            System.out.println("Puntos de Oros: " + participante2.getPuntosOros());
            System.out.println("Puntos de Cartas: " + participante2.getPuntosCartas());
            System.out.println("Puntos totales: "+participante2.getPuntosTotales()+"\n");
        }
    }



    public void comprobarDiferencia(){
        while (this.participante1.getPuntosTotales() == this.participante2.getPuntosTotales()){
            this.crearRonda();
        }
        if (this.participante1.getPuntosTotales()>this.participante2.getPuntosTotales()){
            if(this.participante1 instanceof Jugador){
                Jugador jugador = (Jugador) this.participante1;
                System.out.println("Ganador de la partida "+jugador.getPerfilUsuario().getNombreUsuario());

                //sumar partida ganada

            }else {
                System.out.println("Gano la IA la partida");
            }
        }else {
            if(this.participante2 instanceof Jugador){
                Jugador jugador = (Jugador) this.participante2;
                System.out.println("Ganador de la partida "+jugador.getPerfilUsuario().getNombreUsuario());

                //sumar partida ganada

            }else {
                System.out.println("Gano la IA la partida");
            }
        }
    }

    public int getNumeroTurno() {
        return numeroTurno;
    }
}
