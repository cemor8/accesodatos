import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class IA extends Participante{
    public IA(ArrayList<Carta> mano) {
        super(mano);
    }

    @Override
    public void jugar(ArrayList<Carta> cartasMesa){
        if(cartasMesa.isEmpty()){
            Carta cartaSeleccionada = this.dejarCarta();
            cartasMesa.add(cartaSeleccionada);
            this.getMano().remove(cartaSeleccionada);
            return;
        }

        ArrayList<ArrayList<ArrayList<Carta>>> todasEscobas = new ArrayList<>();
        for(Carta cadaCartaMano : cartasMesa){
            ArrayList<ArrayList<Carta>> posibles_escobas = this.buscarPosiblesEscobas(cadaCartaMano, cartasMesa);
            if(!posibles_escobas.isEmpty()){
                todasEscobas.add(posibles_escobas);
            }
        }
        if(todasEscobas.isEmpty()){
            Carta cartaSeleccionada = this.dejarCarta();
            cartasMesa.add(cartaSeleccionada);
            this.getMano().remove(cartaSeleccionada);
            System.out.println("La IA deja la carta: \n");
            System.out.println(cartaSeleccionada);
        }


    }
    


    public Carta dejarCarta(){
        if(this.getMano().size() == 1){
            return this.getMano().remove(0);
        }
        this.getMano().sort(Comparator.comparingInt(Carta::getValorNumerico));
        if(this.getMano().get(0).getValorNumerico() == 7 && this.getMano().get(0).getPalo().equalsIgnoreCase("oros")){
            return this.getMano().remove(1);
        }
        return this.getMano().remove(0);
    }
}
