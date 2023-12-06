import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;

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
        ArrayList<Carta> escoba = this.buscarEscoba(todasEscobas,cartasMesa);
        if(escoba !=null){
            // eliminar cartas de la baraja y mano y irse
            return;
        }

        ArrayList<Carta> combinacion7Oros = this.buscarCombinacion7Oros(todasEscobas);
        if(combinacion7Oros !=null){
            // eliminar cartas de la baraja y mano y irse
            return;
        }

        ArrayList<ArrayList<Carta>> combinacionesConSietes = new ArrayList<>();
        this.buscarSietes(todasEscobas,combinacionesConSietes);
        if(!combinacionesConSietes.isEmpty()){
            //buscar prioridades de combinaciones y luego eliminar cartas
            return;
        }
        ArrayList<ArrayList<Carta>> combinacionesOros = new ArrayList<>();
        this.buscarOros(todasEscobas,combinacionesOros);
        if(!combinacionesOros.isEmpty()){
            //buscar prioridades de combinaciones y luego eliminar cartas
            return;
        }
        ArrayList<Carta> combinacionFinal = this.devolverMasLarga(todasEscobas);

        //eliminar las cartas y meterlas luego
    }

    /**
     * Método que busca si se puede hacer una combinacion con el siete de oros
     * */
    public ArrayList<Carta> buscarCombinacion7Oros(ArrayList<ArrayList<ArrayList<Carta>>> todasEscobas){
        for(ArrayList<ArrayList<Carta>> combinacionPorCarta : todasEscobas){
            for(ArrayList<Carta> cadaCombinacion : combinacionPorCarta){
                Optional<Carta> carta = cadaCombinacion.stream().filter(carta1 -> carta1.getPalo().equalsIgnoreCase("oros") && carta1.getValorNumerico() == 7).findAny();
                if(carta.isEmpty()){
                    continue;
                }
                return cadaCombinacion;
            }
        }
        return null;
    }
    /**
     * Método que busca si se puede hacer escoba
     * **/
    public ArrayList<Carta> buscarEscoba(ArrayList<ArrayList<ArrayList<Carta>>> todasEscobas, ArrayList<Carta> cartasEnMesa){
        for(ArrayList<ArrayList<Carta>> combinacionPorCarta : todasEscobas){
            for(ArrayList<Carta> cadaCombinacion : combinacionPorCarta){
               if(cadaCombinacion.size() - 1 == cartasEnMesa.size()){
                   return cadaCombinacion;
               }
            }
        }
        return null;
    }

    /**
     * Método que busca las combinaciones con sietes
     * */
    public void buscarSietes(ArrayList<ArrayList<ArrayList<Carta>>> todasEscobas, ArrayList<ArrayList<Carta>> listaCombinacionesCon7){
        for(ArrayList<ArrayList<Carta>> combinacionPorCarta : todasEscobas){
            for(ArrayList<Carta> cadaCombinacion : combinacionPorCarta){
                Optional<Carta> carta = cadaCombinacion.stream().filter(carta1 -> carta1.getValorNumerico() == 7).findAny();
                if(carta.isEmpty()){
                    continue;
                }
                listaCombinacionesCon7.add(cadaCombinacion);
            }
        }
    }
    /**
     * Método que busca combinaciones con oros
     * */
    public void buscarOros(ArrayList<ArrayList<ArrayList<Carta>>> todasEscobas, ArrayList<ArrayList<Carta>> listaCombinacionesCon7){
        for(ArrayList<ArrayList<Carta>> combinacionPorCarta : todasEscobas){
            for(ArrayList<Carta> cadaCombinacion : combinacionPorCarta){
                Optional<Carta> carta = cadaCombinacion.stream().filter(carta1 -> carta1.getPalo().equalsIgnoreCase("oros")).findAny();
                if(carta.isEmpty()){
                    continue;
                }
                listaCombinacionesCon7.add(cadaCombinacion);
            }
        }
    }
    /**
     * Método que busca combinaciones con oros
     * */
    public ArrayList<Carta> devolverMasLarga(ArrayList<ArrayList<ArrayList<Carta>>> todasEscobas){
        ArrayList<Carta> mayor = new ArrayList<>();
        for(ArrayList<ArrayList<Carta>> combinacionPorCarta : todasEscobas){
            for(ArrayList<Carta> cadaCombinacion : combinacionPorCarta){
                if(cadaCombinacion.size()>mayor.size()){
                    mayor=cadaCombinacion;
                }
            }
        }
        return mayor;
    }

    /**
     * Método que deja una carta en la mesa
     * */
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
