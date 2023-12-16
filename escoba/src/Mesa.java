import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class Mesa {
    private Participante participante1;
    private Participante participante2;
    private Baraja baraja;
    private ArrayList<Carta> cartasEnMesa = new ArrayList<>();
    private int puntosAJugar;
    private int numeroTurno = 0;
    Clasificacion clasificacionAlmacenar = null;

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
    public void iniciarPartida(Clasificacion clasificacion){
        this.participante1.setMesa(this);
        this.participante2.setMesa(this);

        this.clasificacionAlmacenar = clasificacion;
        while (this.participante1.getPuntosTotales()<this.puntosAJugar && this.participante2.getPuntosTotales()<this.puntosAJugar){
            this.crearRonda();
        }
        // Una vez acabada la partida se actualiza la clasificacion y se muestra el ganador
        this.actualizarClasificacion();
        if (this.participante1.getPuntosTotales()>this.participante2.getPuntosTotales()){
            if(this.participante1 instanceof Jugador){
                Jugador jugador = (Jugador) this.participante1;
                System.out.println("Ganador de la partida "+jugador.getPerfilUsuario().getNombreUsuario());
                this.sumarPartidaGanada();
            }else {
                System.out.println("Gano la IA la partida");
            }
        }else {
            if(this.participante2 instanceof Jugador){
                Jugador jugador = (Jugador) this.participante2;
                System.out.println("Ganador de la partida "+jugador.getPerfilUsuario().getNombreUsuario());
                this.sumarPartidaGanada();
            }else {
                System.out.println("Gano la IA la partida");
            }
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
            System.out.println("\t\n-----Cartas En Mesa-----\n ");

            this.mostrarCartasMesa();

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
        this.numeroTurno+=1;

        this.participante1.jugar(this.cartasEnMesa);
        Participante temp = this.participante1;
        this.participante1 = this.participante2;
        this.participante2 = temp;

        // comprobar si hay que repartir cartas
        if(this.participante1.getMano().isEmpty() && this.participante2.getMano().isEmpty() && !this.baraja.getCartas().isEmpty()) {
            this.repartirCartas(this.participante1.getMano());
            this.repartirCartas((this.participante2.getMano()));
            return false;
            // comprobar si se ha acabado la ronda
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
        System.out.println("\t\n-----Se decide el orden-----\n");
        this.baraja.barajar();
        Carta cartaJugador1 = baraja.sacarCarta();
        Carta cartaJugador2 = baraja.sacarCarta();
        System.out.println(" Carta jugador: "+cartaJugador1+"\n");
        System.out.println(" Carta IA: "+cartaJugador2+"\n");
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
        System.out.println("\nRepartiendo cartas");
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

        // puntos por cantidad de cartas
        if(this.participante1.getCartasGanadas().size() > this.participante2.getCartasGanadas().size()){
            this.participante1.setPuntosCartas(this.participante1.getPuntosCartas()+1);
        }else if(this.participante2.getCartasGanadas().size() > this.participante1.getCartasGanadas().size()){
            this.participante2.setPuntosCartas(this.participante1.getPuntosCartas()+1);
        }

        //compruebo oros
        if(this.participante1.cantidadOros() == 10){
            this.participante1.setPuntosOros(this.participante1.getPuntosOros()+2);
        }else if(this.participante2.cantidadOros() == 10){
            this.participante2.setPuntosOros(this.participante2.getPuntosOros()+2);
        }else if(this.participante1.cantidadOros() > this.participante2.cantidadOros()){
            this.participante1.setPuntosOros(this.participante1.getPuntosOros()+1);
        }else if(this.participante1.cantidadOros() < this.participante2.cantidadOros()){
            this.participante2.setPuntosOros(this.participante2.getPuntosOros()+1);
        }else {
            this.participante1.setPuntosOros(this.participante1.getPuntosOros()+1);
            this.participante2.setPuntosOros(this.participante2.getPuntosOros()+1);
        }
        // puntos de velo
        if(this.participante1.tiene7oros()){
            this.participante1.setPuntosVelo(this.participante1.getPuntosVelo() + 1);
        }else {
            this.participante2.setPuntosVelo(this.participante2.getPuntosVelo() + 1);
        }
        //puntos de cantidad de sietes
        if(this.participante1.cantidadSietes() > this.participante2.cantidadSietes()){
            this.participante1.setPuntosSietes(this.participante1.getPuntosSietes() + 1);
        }else {
            this.participante2.setPuntosSietes(this.participante2.getPuntosSietes() + 1);
        }

        this.mostrar();
        this.participante1.setCartasGanadas(new ArrayList<>());
        this.participante2.setCartasGanadas(new ArrayList<>());


    }
    public void mostrar(){

        if(participante1 instanceof Jugador){
            System.out.println("\nPUNTOS JUGADOR ");
            System.out.println("Puntos de Escobas: " + participante1.getPuntosEscobas());
            System.out.println("Puntos de Sietes: " + participante1.getPuntosSietes());
            System.out.println("Puntos de Velo: " + participante1.getPuntosVelo());
            System.out.println("Puntos de Oros: " + participante1.getPuntosOros());
            System.out.println("Puntos de Cartas: " + participante1.getPuntosCartas());
            System.out.println("-----------");
            System.out.println("Puntos totales: "+participante1.getPuntosTotales()+"\n");

            this.clasificacionAlmacenar.setPuntos_escobas(this.clasificacionAlmacenar.getPuntos_escobas() + this.participante1.getPuntosEscobas());
            this.clasificacionAlmacenar.setPuntos_oros(this.clasificacionAlmacenar.getPuntos_oros() + this.participante1.getPuntosOros());
            this.clasificacionAlmacenar.setPuntos_velo(this.clasificacionAlmacenar.getPuntos_velo() + this.participante1.getPuntosVelo());
            this.clasificacionAlmacenar.setPuntos_cantidad_cartas(this.clasificacionAlmacenar.getPuntos_cantidad_cartas() + this.participante1.getPuntosCartas());
            this.clasificacionAlmacenar.setPuntos_sietes(this.clasificacionAlmacenar.getPuntos_sietes() + this.participante1.getPuntosSietes());

            System.out.println("\nPUNTOS IA ");
            System.out.println("Puntos de Escobas: " + participante2.getPuntosEscobas());
            System.out.println("Puntos de Sietes: " + participante2.getPuntosSietes());
            System.out.println("Puntos de Velo: " + participante2.getPuntosVelo());
            System.out.println("Puntos de Oros: " + participante2.getPuntosOros());
            System.out.println("Puntos de Cartas: " + participante2.getPuntosCartas());
            System.out.println("-----------");
            System.out.println("Puntos totales: "+participante2.getPuntosTotales()+"\n");

        }else {
            System.out.println("\nPUNTOS IA ");
            System.out.println("Puntos de Escobas: " + participante1.getPuntosEscobas());
            System.out.println("Puntos de Sietes: " + participante1.getPuntosSietes());
            System.out.println("Puntos de Velo: " + participante1.getPuntosVelo());
            System.out.println("Puntos de Oros: " + participante1.getPuntosOros());
            System.out.println("Puntos de Cartas: " + participante1.getPuntosCartas());
            System.out.println("-----------");
            System.out.println("Puntos totales: "+participante1.getPuntosTotales()+"\n");

            System.out.println("\nPUNTOS Jugador ");
            System.out.println("Puntos de Escobas: " + participante2.getPuntosEscobas());
            System.out.println("Puntos de Sietes: " + participante2.getPuntosSietes());
            System.out.println("Puntos de Velo: " + participante2.getPuntosVelo());
            System.out.println("Puntos de Oros: " + participante2.getPuntosOros());
            System.out.println("Puntos de Cartas: " + participante2.getPuntosCartas());
            System.out.println("-----------");
            System.out.println("Puntos totales: "+participante2.getPuntosTotales()+"\n");

            this.clasificacionAlmacenar.setPuntos_escobas(this.clasificacionAlmacenar.getPuntos_escobas() + this.participante2.getPuntosEscobas());
            this.clasificacionAlmacenar.setPuntos_oros(this.clasificacionAlmacenar.getPuntos_oros() + this.participante2.getPuntosOros());
            this.clasificacionAlmacenar.setPuntos_velo(this.clasificacionAlmacenar.getPuntos_velo() + this.participante2.getPuntosVelo());
            this.clasificacionAlmacenar.setPuntos_cantidad_cartas(this.clasificacionAlmacenar.getPuntos_cantidad_cartas() + this.participante2.getPuntosCartas());
            this.clasificacionAlmacenar.setPuntos_sietes(this.clasificacionAlmacenar.getPuntos_sietes() + this.participante2.getPuntosSietes());
        }
        this.participante1.resetearPuntos();
        this.participante2.resetearPuntos();
    }


    /**
     * Método que crea otra ronda si se ha empatado
     * */
    public void comprobarDiferencia(){
        while (this.participante1.getPuntosTotales() == this.participante2.getPuntosTotales()){
            this.crearRonda();
        }

    }
    /**
     * Método que se encarga de sumar una partida ganada al jugador
     * */
    public void sumarPartidaGanada(){
        Conexion conexion = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            conexion = new Conexion();
            connection = conexion.hacerConexion(this.clasificacionAlmacenar.getUsuario().getNombreUsuario(),this.clasificacionAlmacenar.getUsuario().getClave(),false);
            String updateSQL = "UPDATE escoba.clasificacion SET escoba.clasificacion.partidas_ganadas = partidas_ganadas + 1 WHERE nombre_usuario = ?";
            preparedStatement = connection.prepareStatement(updateSQL);
            preparedStatement.setString(1,this.clasificacionAlmacenar.getUsuario().getNombreUsuario());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
            conexion.cerrarConexion();
        }catch (SQLException err){
            System.out.println(err.getMessage());
        }finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException err) {
                    System.out.println(err.getMessage());
                }
            }
            if (conexion != null) {
                conexion.cerrarConexion();
            }
        }
    }
    /**
     * Método que se encarga de sumar las puntuaciones que ha obtenido el jugador
     * durante la partida a la base de datos
     * */
    public void actualizarClasificacion(){
        Conexion conexion = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            conexion = new Conexion();
            connection = conexion.hacerConexion(this.clasificacionAlmacenar.getUsuario().getNombreUsuario(),this.clasificacionAlmacenar.getUsuario().getClave(),false);
            String updateSQL = "UPDATE clasificacion SET " +
                    "puntos_oros = puntos_oros + ?, " +
                    "puntos_escobas = puntos_escobas + ?, " +
                    "puntos_velo = puntos_velo + ?, " +
                    "puntos_cantidad_cartas = puntos_cantidad_cartas + ?, " +
                    "puntos_sietes = puntos_sietes + ? " +
                    "WHERE nombre_usuario = ?";
            preparedStatement = connection.prepareStatement(updateSQL);
            preparedStatement.setInt(1, this.clasificacionAlmacenar.getPuntos_oros());
            preparedStatement.setInt(2, this.clasificacionAlmacenar.getPuntos_escobas());
            preparedStatement.setInt(3, this.clasificacionAlmacenar.getPuntos_velo());
            preparedStatement.setInt(4, this.clasificacionAlmacenar.getPuntos_cantidad_cartas());
            preparedStatement.setInt(5, this.clasificacionAlmacenar.getPuntos_sietes());
            preparedStatement.setString(6,this.clasificacionAlmacenar.getUsuario().getNombreUsuario());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
            conexion.cerrarConexion();
        }catch (SQLException err){
            System.out.println(err.getMessage());
        }finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException err) {
                    System.out.println(err.getMessage());
                }
            }
            if (conexion != null) {
                conexion.cerrarConexion();
            }
        }
    }

    public int getNumeroTurno() {
        return numeroTurno;
    }
    /**
     * Método que muestra las cartas de la mesa
     * */
    public void mostrarCartasMesa(){

        if(this.cartasEnMesa.isEmpty()){
            System.out.println("\n\tNo hay cartas en la mesa");
            return;
        }
        for (Carta carta : this.cartasEnMesa){
            System.out.println("\t"+carta);
        }
        System.out.println("\n");

    }
}
