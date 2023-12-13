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
        System.out.println();

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
        if (escoba != null) {
            //si se puede, empiezo buscando alguna que tenga el 7 de oros

            ArrayList<Carta> combinacion70ros = this.buscarCombinacion7OrosUnica(escoba);
            if (combinacion70ros != null) {
                System.out.println("La ia hace escoba con una combinacion con el 7 de oros: ");
                System.out.println(combinacion70ros);
                this.meterGanadas(combinacion70ros, cartasMesa);
                setPuntosEscobas(getPuntosEscobas() + 1);
                return;
            }

            // si no hay ninguna con el siete de oros, busco una con sietes

            ArrayList<Carta> escobaCon7 = this.escobaCon7(escoba);

            if (!escobaCon7.isEmpty()) {
                System.out.println("La ia hace escoba con una combinacion con sietes: ");
                System.out.println(escobaCon7);
                this.meterGanadas(escobaCon7, cartasMesa);
                return;
            }

            //si no hay con sietes, busco una con oros.

            ArrayList<Carta> escobaConOros = this.devolverEscobaConOros(escoba);

            //como si no hay ninguna escoba con oros, la funcion devuelve la combinacion mas larga, siempre escobaConOros es una
            //combinacion
            System.out.println("La ia hace escoba con la combinacion mas larga: ");
            System.out.println(escobaConOros);
            //this.meterGanadas(escoba, cartasMesa);
            setPuntosEscobas(getPuntosEscobas() + 1);
            return;
        }

        //Si no hay escobas, busco una combinacion con el siete de oros

        ArrayList<ArrayList<Carta>> combinacion7Oros = this.buscarCombinacion7Oros(todasEscobas);

        if (combinacion7Oros != null) {
            System.out.println("La ia se lleva el 7 de oros con: ");
            System.out.println(combinacion7Oros);
            //this.meterGanadas(combinacion7Oros, cartasMesa);
            return;
        }


        //buscar combinaciones con sietes si no hay ninguna con el siete de oros
        ArrayList<ArrayList<Carta>> combinacionesConSietes = new ArrayList<>();
        this.buscarSietes(todasEscobas, combinacionesConSietes);

        if (!combinacionesConSietes.isEmpty()) {
            //buscar combinacion con mas sietes





            ArrayList<Carta> combinacionOros = this.preferenciasSietes(combinacionesConSietes);
            if (combinacionOros == null) {
                System.out.println("La Ia hace la siguiente combinacion : ");
                System.out.println(combinacionesConSietes.get(combinacionesConSietes.size() - 1));
                this.meterGanadas(combinacionesConSietes.get(combinacionesConSietes.size() - 1), cartasMesa);
                return;
            }
            System.out.println("La Ia hace la siguiente combinacion : ");
            System.out.println(combinacionOros);
            this.meterGanadas(combinacionOros, cartasMesa);
            return;
        }




        ArrayList<ArrayList<Carta>> combinacionesOros = this.buscarOros(todasEscobas,null);;


        if (!combinacionesOros.isEmpty()) {

            System.out.println("La Ia hace la siguiente combinacion : ");
            System.out.println(mayor);
            this.meterGanadas(mayor, cartasMesa);
            return;
        }

        ArrayList<Carta> combinacionFinal = this.devolverMasLarga(todasEscobas);
        System.out.println("La Ia hace la siguiente combinacion : ");
        System.out.println(combinacionFinal);
        this.meterGanadas(combinacionFinal, cartasMesa);
    }

    /**
     * Método que busca si se puede hacer una combinacion con el siete de oros
     */
    public ArrayList<ArrayList<Carta>> buscarCombinacion7Oros(ArrayList<ArrayList<ArrayList<Carta>>> todasEscobas) {
        ArrayList<ArrayList<Carta>> combinacionesCon7Oros = new ArrayList<>();
        for (ArrayList<ArrayList<Carta>> combinacionPorCarta : todasEscobas) {
            for (ArrayList<Carta> cadaCombinacion : combinacionPorCarta) {
                Optional<Carta> carta = cadaCombinacion.stream().filter(carta1 -> carta1.getPalo().equalsIgnoreCase("oros") && carta1.getValorNumerico() == 7).findAny();
                if (carta.isEmpty()) {
                    continue;
                }
                combinacionesCon7Oros.add(cadaCombinacion);
            }
        }
        if (combinacionesCon7Oros.size() > 1) {

        }

        return combinacionesCon7Oros;
    }

    /**
     * Método que busca si se puede hacer una combinacion con el siete de oros para hacer una escoba
     */
    public ArrayList<Carta> buscarCombinacion7OrosUnica(ArrayList<ArrayList<Carta>> escoba) {

        for (ArrayList<Carta> cadaCombinacion : escoba) {
            Optional<Carta> carta = cadaCombinacion.stream().filter(carta1 -> carta1.getPalo().equalsIgnoreCase("oros") && carta1.getValorNumerico() == 7).findAny();
            if (carta.isEmpty()) {
                continue;
            }
            return cadaCombinacion;
        }
        return null;
    }

    /**
     * Método que busca si se puede hacer escoba
     **/
    public ArrayList<ArrayList<Carta>> buscarEscobas(ArrayList<ArrayList<ArrayList<Carta>>> todasEscobas, ArrayList<Carta> cartasEnMesa) {
        ArrayList<ArrayList<Carta>> posiblesEscobas = new ArrayList<ArrayList<Carta>>();
        for (ArrayList<ArrayList<Carta>> combinacionPorCarta : todasEscobas) {
            for (ArrayList<Carta> cadaCombinacion : combinacionPorCarta) {
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
    public ArrayList<ArrayList<Carta>> buscarSietes(ArrayList<ArrayList<ArrayList<Carta>>> todasEscobas, ArrayList<ArrayList<Carta>> listaCombinacionesCon7) {
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
        return combsSietes;
    }

    /**
     * Método que busca la combinacion con mas sietes.
     * @param combinacionesConSietes lista de combinaciones con sietes
     * */
    public ArrayList<Carta> conMasSietes(ArrayList<ArrayList<Carta>> combinacionesConSietes){
        ArrayList<ArrayList<Carta>> combsFinales = new ArrayList<>();
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
    public ArrayList<ArrayList<Carta>> buscarOros(ArrayList<ArrayList<ArrayList<Carta>>> todasEscobas , ArrayList<ArrayList<Carta>> combinacionesDiferenciar) {
        ArrayList<ArrayList<Carta>> listaConOros = new ArrayList<>();
        if(combinacionesDiferenciar != null){
            for (ArrayList<Carta> combinacion : combinacionesDiferenciar){
                Optional<Carta> carta = combinacion.stream().filter(carta1 -> carta1.getPalo().equalsIgnoreCase("oros")).findAny();
                if (carta.isEmpty()) {
                    continue;
                }
                listaConOros.add(combinacion);
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
        return listaConOros;
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

    public ArrayList<Carta> escobaCon7(ArrayList<ArrayList<Carta>> escobas) {
        ArrayList<ArrayList<Carta>> escobasCon7 = new ArrayList<>();
        for (ArrayList<Carta> cada_escoba : escobas) {
            Optional<Carta> cartaOptional = cada_escoba.stream().filter(carta -> carta.getValorNumerico() == 7).findAny();
            if (cartaOptional.isPresent()) {
                escobasCon7.add(cada_escoba);
            }
        }
        if (!escobasCon7.isEmpty() && escobasCon7.size() > 1) {
            ArrayList<Carta> escobaFinal = this.preferenciasSietes(escobasCon7);
            if (escobaFinal != null) {
                return escobaFinal;
            }
            return escobasCon7.get(escobasCon7.size() - 1);
        }

        escobasCon7.sort(Comparator.comparing(ArrayList::size));
        return escobasCon7.get(escobasCon7.size() - 1);

    }


    public ArrayList<Carta> preferenciasSietes(ArrayList<ArrayList<Carta>> combinacionesConSietes) {
        ArrayList<ArrayList<Carta>> combsFinales = new ArrayList<>();
        for (ArrayList<Carta> combinacion : combinacionesConSietes) {
            Optional<Carta> carta = combinacion.stream().filter(carta1 -> carta1.getPalo().equalsIgnoreCase("oros")).findAny();
            if (carta.isEmpty()) {
                continue;
            }
            combsFinales.add(combinacion);
        }
        if (combsFinales.isEmpty()) {
            combinacionesConSietes.sort(Comparator.comparing(ArrayList::size));
            return combinacionesConSietes.get(combinacionesConSietes.size()-1);
        }
        ArrayList<Carta> mayor = new ArrayList<>();
        int cantidad = 0;
        for (ArrayList<Carta> combConSiete : combsFinales) {
            int cartasOros = (int) combConSiete.stream().filter(carta -> carta.getPalo().equalsIgnoreCase("oros")).count();
            if (cartasOros > cantidad) {
                mayor = combConSiete;
            }
        }
        return mayor;
    }


    public ArrayList<Carta> devolverEscobaConOros(ArrayList<ArrayList<Carta>> posiblesEscobas) {
        ArrayList<ArrayList<Carta>> escobasConOros = new ArrayList<>();
        for (ArrayList<Carta> combinaciones : posiblesEscobas) {
            Optional<Carta> cartaOptional = combinaciones.stream().filter(carta -> carta.getPalo().equalsIgnoreCase("oros")).findAny();
            if (cartaOptional.isEmpty()) {
                continue;
            }
            escobasConOros.add(combinaciones);
        }
        if (escobasConOros.isEmpty()) {
            posiblesEscobas.sort(Comparator.comparing(ArrayList::size));
            return posiblesEscobas.get(0);
        }

        ArrayList<Carta> mayor = new ArrayList<>();
        int cantidad = 0;
        for (ArrayList<Carta> combinaciones : escobasConOros) {
            int cartasOros = (int) combinaciones.stream().filter(carta -> carta.getPalo().equalsIgnoreCase("oros")).count();
            if (cartasOros > cantidad) {
                mayor = combinaciones;
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
