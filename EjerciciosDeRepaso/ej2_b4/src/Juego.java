import java.util.ArrayList;

public class Juego {
    private ArrayList<Jugador> jugadores;
    private Revolver revolver;
    public Juego(ArrayList<Jugador> jugadores, Revolver revolver) {
        this.jugadores = jugadores;
        this.revolver = revolver;
    }
    /**
     * Método que comprueba si el juego se ha acabado, se acaba cuando muere alguien.
     * @return boolean
     * */
    public boolean finJuego() {
        boolean vivo = false;
        boolean encontrado = this.jugadores.stream().anyMatch(jugador -> jugador.isVivo() == vivo);
        return encontrado;

    }
    /**
     * Método que inicia una ronda del juego, cada jugador se dispara y si se muere, el juego se acaba.
     * */
    public void ronda() {
        for (int i =0;i<this.jugadores.size();i++){
            this.jugadores.get(i).Disparar(this.revolver);
            if(this.finJuego()){
                return;
            }else if(i==this.jugadores.size()-1){
                i=-1;
            }
        }



    }

}
