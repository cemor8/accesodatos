import java.util.Comparator;
import java.util.Optional;

public class Jugador {
    private int id;
    private String nombre;
    private boolean vivo;

    public Jugador(int id) {
        this.id = id;
        this.nombre = "Jugador "+id;
        this.vivo=true;
    }
    /**
     * Método que hace la accion de disparar, si el jugador muere lo indica y pasa su estado a muerto, luego mueve
     * la posicion actual del tambor del revolver,
     * */
    public void Disparar(Revolver revolver){
        if(revolver.disparar()){
            System.out.println("El "+this.nombre+" se ha disparado y ha muerto");
            this.vivo=false;
        }else {
            System.out.println("El "+this.nombre+" se ha disparado y sigue vivo");
        }
        revolver.SiguienteBala();
    }


    public boolean isVivo() {
        return vivo;
    }
}
