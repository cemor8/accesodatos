import java.util.ArrayList;
import java.util.List;

public class Participante {
    private ArrayList<Carta> mano;
    private ArrayList<Carta> cartasGanadas = new ArrayList<>();
    private Mesa mesa;
    private int puntosEscobas;
    private int puntosVelo;
    private int puntosSietes;
    private int puntosCartas;
    private int puntosOros;
    private int puntosTotales = 0;
    private boolean haConseguidoCartasEnRonda = false;
    private int ultimaRondaObtieneCartas = 0;

    public Participante(ArrayList<Carta> mano) {
        this.mano = mano;
    }

    public ArrayList<Carta> getMano() {
        return mano;
    }

    public ArrayList<Carta> getCartasGanadas() {
        return cartasGanadas;
    }

    public int getPuntosEscobas() {
        return puntosEscobas;
    }

    public int getPuntosVelo() {
        return puntosVelo;
    }

    public int getPuntosSietes() {
        return puntosSietes;
    }

    public int getPuntosCartas() {
        return puntosCartas;
    }

    public int getPuntosOros() {
        return puntosOros;
    }

    public void setMano(ArrayList<Carta> mano) {
        this.mano = mano;
    }

    public void setCartasGanadas(ArrayList<Carta> cartasGanadas) {
        this.cartasGanadas = cartasGanadas;
    }

    public void setPuntosEscobas(int puntosEscobas) {
        this.puntosEscobas = puntosEscobas;
    }

    public void setPuntosVelo(int puntosVelo) {
        this.puntosVelo = puntosVelo;
    }

    public void setPuntosSietes(int puntosSietes) {
        this.puntosSietes = puntosSietes;
    }

    public void setPuntosCartas(int puntosCartas) {
        this.puntosCartas = puntosCartas;
    }

    public void setPuntosOros(int puntosOros) {
        this.puntosOros = puntosOros;
    }

    public int getPuntosTotales() {
        this.puntosTotales += this.puntosEscobas + this.puntosVelo + this.puntosCartas + this.puntosOros + this.puntosSietes;
        return puntosTotales;
    }
    /**
     * Método que busca todas las posibles combinaciones de 15 con la carta seleccionada
     * @param carta carta seleccionada
     * @param cartasEnMesa lista de cartas en mesa
     * */
    public ArrayList<ArrayList<Carta>> buscarPosiblesEscobas(Carta carta,ArrayList<Carta> cartasEnMesa){
        //meto la carta en la lista de cartas en mesa clonada
        ArrayList<Carta> cartasEnviar = (ArrayList<Carta>) cartasEnMesa.clone();
        cartasEnviar.add(carta);
        return comprobarCombinacion(cartasEnviar, 15, 0, new ArrayList<>(), carta);
    }
    /**
     * Método que busca las posibles combinaciones que sumen 15 en una lista de cartas, para que la combinacion
     * sea válida, debe de contener la carta que se ha seleccionado
     * @param carta carta seleccionada
     * @param suma combinacion a buscar
     * @param cartas lista de cartas en las que buscar la combinacion
     * @param indice desde donde se empieza a recorrer la lista
     * @param combinacionActual combinacion de cartas que se esta probando
     * */
    public ArrayList<ArrayList<Carta>> comprobarCombinacion(ArrayList<Carta> cartas, int suma, int indice, ArrayList<Carta> combinacionActual,Carta carta) {
        ArrayList<ArrayList<Carta>> combinacionesValidas = new ArrayList<>();
        // si se ha alcanzado 15 y la combinacion tiene la carta original
        if (suma == 0 && combinacionActual.contains(carta)) {

            // Se ha encontrado una combinación que suma 15, hay que comprobar si
            //la combinacion ya existe dentro de la lista

            if (!combinacionYaExiste(combinacionesValidas, combinacionActual)) {
                System.out.println("Combinación que suma 15: " + combinacionActual);
                System.out.println("\n");
                combinacionesValidas.add(new ArrayList<>(combinacionActual));
            }

        }
        // se recorren las cartas desde la posicion que recibe la funcion
        for (int i = indice; i < cartas.size(); i++) {
            // si la suma sigue siendo mayor o igual a cero
            if (suma - cartas.get(i).getValorNumerico() >= 0) {
                // se mete la carta en la combinacion y se vuelve a llamar al metodo incrementando la posicion desde donde buscar,
                // como al inicio de la funcion se comprueba si la combinacion es valida, se añadiria la la lista de combinaciones validas
                //en caso de que lo cumpliese, si no, se sigue intentando buscar combinaciones con las cartas
                combinacionActual.add(cartas.get(i));
                ArrayList<ArrayList<Carta>> combinaciones = comprobarCombinacion(cartas, suma - cartas.get(i).getValorNumerico(), i + 1, combinacionActual,carta);
                // si se encuentran combinaciones se meten en la lista de combinaciones validas
                if (combinaciones != null) {
                    combinacionesValidas.addAll(combinaciones);
                }
                // se elimina la ultima carta para seguir buscando diferentes combinaciones
                combinacionActual.remove(combinacionActual.size() - 1);
            }
        }

        if(combinacionesValidas.isEmpty()){
            return null;
        }else {
            return combinacionesValidas;
        }
    }
    /**
     * Método que se encarga de verificar si una combinacion es repetida
     * @param combinacionActual combinacion a comprobar
     * @param combinacionesValidas combinaciones encontradas
     * */
    private boolean combinacionYaExiste(ArrayList<ArrayList<Carta>> combinacionesValidas, ArrayList<Carta> combinacionActual) {
        for (ArrayList<Carta> combinacion : combinacionesValidas) {
            if (combinacion.size() == combinacionActual.size() && combinacion.containsAll(combinacionActual)) {
                return true;
            }
        }
        return false;
    }


    public void jugar(ArrayList<Carta> cartasMesa){
    }
    /**
     * Método que busca la cantidad de oros que tiene el participante
     * */
    public Integer cantidadOros(){
        int i = 0;
        for (Carta cada_carta : this.cartasGanadas){
            if(cada_carta.getPalo().equalsIgnoreCase("oros")){
                i+=1;
            }
        }
        return i;
    }
    /**
     * Método que se encarga de verificar si el participante tiene el 7 de oros
     * @return si lo tiene o no
     * */
    public boolean tiene7oros(){
        boolean tiene = false;
        for (Carta cada_carta : this.cartasGanadas){
            if (cada_carta.getPalo().equalsIgnoreCase("oros") && cada_carta.getValorNumerico() == 7) {
                tiene = true;
                break;
            }

        }
        return tiene;
    }
    /**
     * Método que se encarga de contar la cantidad de sietes que tiene
     * el participante
     * */
    public Integer cantidadSietes(){
        int i = 0;
        for (Carta cada_carta : this.cartasGanadas){
            if(cada_carta.getValorNumerico() == 7){
                i++;
            }
        }
        return i;
    }
    /**
     * Método que resetea los puntos del participante a cero
     * */
    public void resetearPuntos(){
        setPuntosCartas(0);
        setPuntosOros(0);
        setPuntosVelo(0);
        setPuntosSietes(0);
        setPuntosEscobas(0);
    }

    public int getUltimaRondaObtieneCartas() {
        return ultimaRondaObtieneCartas;
    }

    public void setUltimaRondaObtieneCartas(int ultimaRondaObtieneCartas) {
        this.ultimaRondaObtieneCartas = ultimaRondaObtieneCartas;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }
}
