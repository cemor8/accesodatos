import java.util.Comparator;
import java.util.Optional;

public class Jugador {
    private int id;
    private String nombre;
    private boolean vivo;

    public Jugador(int id) {
        this.id = id;
        this.nombre = "Jugador "+id;
    }
    public void Disparar(Revolver revolver){
        if(revolver.disparar()){
            this.vivo=false;
        }
    }

    public boolean isVivo() {
        return vivo;
    }
}
