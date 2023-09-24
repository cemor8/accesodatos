import java.util.ArrayList;

public class GeneradorNumerosAleatorios {
    private ArrayList<Integer> numeros;
    public GeneradorNumerosAleatorios() {
    }
    public Integer generar(){
        int numeroAleatorio = (int) (Math.random()*49)+1;
        return numeroAleatorio;

    }
}
