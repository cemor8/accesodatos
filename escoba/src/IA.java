import com.sun.source.tree.ArrayAccessTree;

import java.util.*;

public class IA extends Participante {
    public IA(ArrayList<Carta> mano) {
        super(mano);
    }

    /***
     * Método que se encarga de ejecutar el turno de la ia.
     * @param cartasMesa cartas en la mesa
     * */
    @Override
    public void jugar(ArrayList<Carta> cartasMesa) {
        System.out.println("cartas ia ");
        System.out.println(getMano());

        //si no hay cartas en la mesa dejo una directamente

        if (cartasMesa.isEmpty()) {
            Carta cartaSeleccionada = this.dejarCarta();
            cartasMesa.add(cartaSeleccionada);
            this.getMano().remove(cartaSeleccionada);
            return;
        }

        ArrayList<ArrayList<ArrayList<Carta>>> todasEscobas = new ArrayList<>();

        //busco todas las posibles combinaciones con todas las cartas de la IA

        for (Carta cadaCartaMano : getMano()) {
            ArrayList<ArrayList<Carta>> posibles_escobas = this.buscarPosiblesEscobas(cadaCartaMano, cartasMesa);
            if (posibles_escobas != null) {
                todasEscobas.add(posibles_escobas);
            }
        }

        // si no hay ninguna dejo una carta
        if (todasEscobas.isEmpty()) {
            Carta cartaSeleccionada = this.dejarCarta();
            cartasMesa.add(cartaSeleccionada);
            this.getMano().remove(cartaSeleccionada);
            System.out.println("La IA deja la carta: \n");
            System.out.println(cartaSeleccionada);
            return;
        }

        //busco si se puede hacer escoba
        ArrayList<ArrayList<Carta>> escoba = this.buscarEscobas(todasEscobas, cartasMesa);
        if (!escoba.isEmpty()) {
            //si se puede, empiezo buscando alguna que tenga el 7 de oros
            System.out.println();
            ArrayList<Carta> combinacion70ros = this.buscarCombinacion7Oros(null,escoba);
            if (!combinacion70ros.isEmpty()) {
                System.out.println("La ia hace escoba con una combinacion con el 7 de oros: ");
                System.out.println(combinacion70ros);
                this.meterGanadas(combinacion70ros, cartasMesa);
                setUltimaRondaObtieneCartas(getMesa().getNumeroTurno());
                setPuntosEscobas(getPuntosEscobas() + 1);
                return;
            }

            // si no hay ninguna con el siete de oros, busco una con sietes

            ArrayList<Carta> escobaCon7 = this.buscarSietes(null,escoba);

            if (!escobaCon7.isEmpty()) {
                System.out.println("La ia hace escoba con una combinacion con sietes: ");
                System.out.println(escobaCon7);
                this.meterGanadas(escobaCon7, cartasMesa);
                setUltimaRondaObtieneCartas(getMesa().getNumeroTurno());
                setPuntosEscobas(getPuntosEscobas()+1);
                return;
            }

            //si no hay con sietes, busco una con oros.

            ArrayList<Carta> escobaConOros = this.buscarOros(null,escoba);

            //como si no hay ninguna escoba con oros, la funcion devuelve la combinacion mas larga, siempre escobaConOros es una
            //combinacion
            System.out.println("La ia hace escoba con la combinacion mas larga: ");
            System.out.println(escobaConOros);
            this.meterGanadas(escobaConOros, cartasMesa);
            setUltimaRondaObtieneCartas(getMesa().getNumeroTurno());
            setPuntosEscobas(getPuntosEscobas() + 1);
            return;
        }

        //Si no hay escobas, busco una combinacion con el siete de oros

        ArrayList<Carta> combinacion7Oros = this.buscarCombinacion7Oros(todasEscobas,null);

        if (!combinacion7Oros.isEmpty()) {
            System.out.println("La ia se lleva el 7 de oros con: ");
            System.out.println(combinacion7Oros);
            this.meterGanadas(combinacion7Oros, cartasMesa);
            setUltimaRondaObtieneCartas(getMesa().getNumeroTurno());
            return;
        }


        //buscar combinaciones con sietes si no hay ninguna con el siete de oros
        ArrayList<Carta> combinacionConSietes = this.buscarSietes(todasEscobas,null);

        if (!combinacionConSietes.isEmpty()) {

            System.out.println("La Ia hace la siguiente combinacion : ");
            System.out.println(combinacionConSietes);
            this.meterGanadas(combinacionConSietes, cartasMesa);
            setUltimaRondaObtieneCartas(getMesa().getNumeroTurno());
            return;
        }



        // busco la combinacion de oros

        ArrayList<Carta> combinacionOros = this.buscarOros(todasEscobas,null);;

        // si se encuentra alguna la hago, el método ya se encarga de las preferencias
        if (!combinacionOros.isEmpty()) {

            System.out.println("La Ia hace la siguiente combinacion con oros: ");
            System.out.println(combinacionOros);
            this.meterGanadas(combinacionOros, cartasMesa);
            setUltimaRondaObtieneCartas(getMesa().getNumeroTurno());
            return;
        }

        //combinacion mas larga posible, ya que no hay ninguna que haga escoba, tenga el velo o sietes, o tenga oros

        ArrayList<Carta> combinacionFinal = this.devolverMasLarga(todasEscobas);
        System.out.println("La Ia hace la siguiente combinacion porque es la unica que puede : ");
        System.out.println(combinacionFinal);
        setUltimaRondaObtieneCartas(getMesa().getNumeroTurno());
        this.meterGanadas(combinacionFinal, cartasMesa);
    }

    /**
     * Método que busca si se puede hacer una combinacion con el siete de oros
     */
    public ArrayList<Carta> buscarCombinacion7Oros(ArrayList<ArrayList<ArrayList<Carta>>> todasEscobas, ArrayList<ArrayList<Carta>> escobas) {
        ArrayList<ArrayList<Carta>> combinacionesCon7Oros = new ArrayList<>();
        if(escobas!=null){
            for (ArrayList<Carta> cadaCombinacion : escobas) {
                Optional<Carta> carta = cadaCombinacion.stream().filter(carta1 -> carta1.getPalo().equalsIgnoreCase("oros") && carta1.getValorNumerico() == 7).findAny();
                if (carta.isEmpty()) {
                    continue;
                }
                combinacionesCon7Oros.add(cadaCombinacion);
            }
        }else {
            for (ArrayList<ArrayList<Carta>> combinacionPorCarta : todasEscobas) {
                for (ArrayList<Carta> cadaCombinacion : combinacionPorCarta) {
                    Optional<Carta> carta = cadaCombinacion.stream().filter(carta1 -> carta1.getPalo().equalsIgnoreCase("oros") && carta1.getValorNumerico() == 7).findAny();
                    if (carta.isEmpty()) {
                        continue;
                    }
                    combinacionesCon7Oros.add(cadaCombinacion);
                }
            }
        }


        if (combinacionesCon7Oros.size() > 1) {
           return this.buscarSietes(null,combinacionesCon7Oros);
        }else if(!combinacionesCon7Oros.isEmpty()){
            return combinacionesCon7Oros.get(0);
        }
        return new ArrayList<>();
    }

    /**
     * Método que busca si se puede hacer escoba
     **/
    public ArrayList<ArrayList<Carta>> buscarEscobas(ArrayList<ArrayList<ArrayList<Carta>>> todasEscobas, ArrayList<Carta> cartasEnMesa) {
        ArrayList<ArrayList<Carta>> posiblesEscobas = new ArrayList<ArrayList<Carta>>();
        for (ArrayList<ArrayList<Carta>> combinacionPorCarta : todasEscobas) {
            for (ArrayList<Carta> cadaCombinacion : combinacionPorCarta) {
                //como la combinacion lleva la carta de la mano le resto uno para buscar el mismo size que la lista de la mesa
                if (cadaCombinacion.size() - 1 == cartasEnMesa.size()) {
                    posiblesEscobas.add(cadaCombinacion);
                }
            }
        }
        return posiblesEscobas;
    }

    /**
     * Método que busca las combinaciones con sietes
     */
    public ArrayList<Carta> buscarSietes(ArrayList<ArrayList<ArrayList<Carta>>> todasEscobas, ArrayList<ArrayList<Carta>> listaCombinacionesCon7) {
        ArrayList<ArrayList<Carta>> combsSietes = new ArrayList<>();
        if (listaCombinacionesCon7 != null) {
            for (ArrayList<Carta> comb : listaCombinacionesCon7) {
                Optional<Carta> carta = comb.stream().filter(carta1 -> carta1.getValorNumerico() == 7).findAny();
                if (carta.isEmpty()) {
                    continue;
                }
                combsSietes.add(comb);
            }

        }else {
            for(ArrayList<ArrayList<Carta>> cadaCombCarta : todasEscobas){
                for (ArrayList<Carta> cadaComb : cadaCombCarta){
                    Optional<Carta> carta = cadaComb.stream().filter(carta1 -> carta1.getValorNumerico() == 7).findAny();
                    if (carta.isEmpty()) {
                        continue;
                    }
                    combsSietes.add(cadaComb);
                }
            }

        }

        // busco prioridad en las combinaciones con sietes
        if (combsSietes.size()>1){
            return this.conMasSietes(combsSietes);
        }else if(combsSietes.isEmpty()){
            //si no hay sietes devuelvo una lista vacia
            return new ArrayList<>();
        }
        // si solo hay una combinacion con sietes, la devuelvo
        return combsSietes.get(0);
    }

    /**
     * Método que busca la combinacion con mas sietes.
     * @param combinacionesConSietes lista de combinaciones con sietes
     * */
    public ArrayList<Carta> conMasSietes(ArrayList<ArrayList<Carta>> combinacionesConSietes){
        HashMap<Integer,ArrayList<ArrayList<Carta>>> cartasYSuCantidad = new HashMap<>();

        //clasifico las cartas por su size

        for(ArrayList<Carta> comb : combinacionesConSietes){
            int cartasSietes = (int) comb.stream().filter(carta -> carta.getValorNumerico() == 7).count();
            if(cartasSietes == 0){
                continue;
            }
            cartasYSuCantidad.computeIfAbsent(cartasSietes, llave -> new ArrayList<>());
            cartasYSuCantidad.get(cartasSietes).add(comb);
        }
        if(!cartasYSuCantidad.isEmpty()){
            Integer llave = Collections.max(cartasYSuCantidad.keySet());
            ArrayList<ArrayList<Carta>> combinacionesConMasSietes = cartasYSuCantidad.get(llave);
            if(combinacionesConMasSietes.size()==1){
                return combinacionesConMasSietes.get(0);
            }
            return this.conMasOros(combinacionesConMasSietes);
        }

        return this.conMasOros(combinacionesConSietes);


        //si hay mas de una, busco una con mas oros, el mismo metodo tambien busca la mas larga

    }

    public ArrayList<Carta> conMasOros(ArrayList<ArrayList<Carta>> combsOros){
        HashMap<Integer,ArrayList<ArrayList<Carta>>> cartasYSuCantidad = new HashMap<>();

        //clasifico las cartas por su size

        for(ArrayList<Carta> comb : combsOros){
            int cartasOros = (int) comb.stream().filter(carta -> carta.getPalo().equalsIgnoreCase("oros")).count();
            if(cartasOros == 0){
                continue;
            }
            cartasYSuCantidad.computeIfAbsent(cartasOros, llave -> new ArrayList<>());

            cartasYSuCantidad.get(cartasOros).add(comb);
        }

        if(!cartasYSuCantidad.isEmpty()){
            Integer llave = Collections.max(cartasYSuCantidad.keySet());
            ArrayList<ArrayList<Carta>> combinacionesConMasOros = cartasYSuCantidad.get(llave);
            if(combinacionesConMasOros.size()==1){
                return combinacionesConMasOros.get(0);
            }
            combinacionesConMasOros.sort(Comparator.comparing(ArrayList::size));
            return combinacionesConMasOros.get(combinacionesConMasOros.size()-1);

        }
        // si no hay oros devuelvo la combinacion mas larga

        combsOros.sort(Comparator.comparing(ArrayList::size));
        return combsOros.get(combsOros.size()-1);

    }
    /**
     * Método que busca combinaciones con oros
     */
    public ArrayList<Carta> buscarOros(ArrayList<ArrayList<ArrayList<Carta>>> todasEscobas , ArrayList<ArrayList<Carta>> combinacionesDiferenciar) {
        ArrayList<ArrayList<Carta>> listaConOros = new ArrayList<>();
        if(combinacionesDiferenciar != null){
            for (ArrayList<Carta> combinacion : combinacionesDiferenciar){
                Optional<Carta> carta = combinacion.stream().filter(carta1 -> carta1.getPalo().equalsIgnoreCase("oros")).findAny();
                if (carta.isEmpty()) {
                    continue;
                }
                listaConOros.add(combinacion);
            }
            //ordeno por si no hay ninguna con oros, ordenar por size

            if (!combinacionesDiferenciar.isEmpty()) {
                combinacionesDiferenciar.sort(Comparator.comparing(ArrayList::size));
            }

        }else {
            for (ArrayList<ArrayList<Carta>> combinacionPorCarta : todasEscobas) {
                for (ArrayList<Carta> cadaCombinacion : combinacionPorCarta) {
                    Optional<Carta> carta = cadaCombinacion.stream().filter(carta1 -> carta1.getPalo().equalsIgnoreCase("oros")).findAny();
                    if (carta.isEmpty()) {
                        continue;
                    }
                    listaConOros.add(cadaCombinacion);
                }
            }
        }

        if(listaConOros.size()>1){
            return this.conMasOros(listaConOros);
        }else if(listaConOros.isEmpty()){
            return new ArrayList<>();
        }
        return listaConOros.get(0);
    }

    /**
     * Método que busca la combinacion de carta mas largas
     */
    public ArrayList<Carta> devolverMasLarga(ArrayList<ArrayList<ArrayList<Carta>>> todasEscobas) {
        ArrayList<Carta> mayor = new ArrayList<>();
        for (ArrayList<ArrayList<Carta>> combinacionPorCarta : todasEscobas) {
            for (ArrayList<Carta> cadaCombinacion : combinacionPorCarta) {
                if (cadaCombinacion.size() > mayor.size()) {
                    mayor = cadaCombinacion;
                }
            }
        }
        return mayor;
    }



    /**
     * Método que deja una carta en la mesa
     */
    public Carta dejarCarta() {
        if (this.getMano().size() == 1) {
            return this.getMano().remove(0);
        }
        this.getMano().sort(Comparator.comparingInt(Carta::getValorNumerico));
        if (this.getMano().get(0).getValorNumerico() == 7 && this.getMano().get(0).getPalo().equalsIgnoreCase("oros")) {
            return this.getMano().remove(1);
        }
        return this.getMano().remove(0);
    }

    public void meterGanadas(ArrayList<Carta> cartas, ArrayList<Carta> cartasEnMesa) {
        int i = 0;
        while (i < cartas.size()) {
            getCartasGanadas().add(cartas.get(i));
            if (getMano().contains(cartas.get(i))) {
                getMano().remove(cartas.get(i));
            } else {
                cartasEnMesa.remove(cartas.get(i));
            }
            cartas.remove(i);
        }
    }
}
