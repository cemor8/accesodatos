import java.util.ArrayList;

public class Juego {
    private ArrayList<Jugador> jugadores;
    private Revolver revolver;

    public Juego(ArrayList<Jugador> jugadores, Revolver revolver) {
        this.jugadores = jugadores;
        this.revolver = revolver;
    }
    public boolean finJuego(){
        boolean vivo=false;
        boolean encontrado=this.jugadores.stream().anyMatch(jugador -> jugador.isVivo()==vivo);
        if(encontrado){
            return true;
        }
        return false;
    }
    public void ronda(){
        for(Jugador cadaJugador : this.jugadores){
            cadaJugador.Disparar(this.revolver);
            this.revolver.SiguienteBala();
        }
    }
}
