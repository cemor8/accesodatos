import java.util.ArrayList;
import java.util.List;

public class Participante {
    private ArrayList<Carta> mano;
    private ArrayList<Carta> cartasGanadas = new ArrayList<>();
    private int puntosEscobas;
    private int puntosVelo;
    private int puntosSietes;
    private int puntosCartas;
    private int puntosOros;
    private int puntosTotales = 0;
    private boolean haConseguidoCartasEnRonda = false;

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
    public void repartir(){

    }
    public ArrayList<ArrayList<Carta>> buscarPosiblesEscobas(Carta carta,ArrayList<Carta> cartasEnMesa){
        ArrayList<Carta> cartasEnviar = (ArrayList<Carta>) cartasEnMesa.clone();
        cartasEnviar.add(carta);
        return comprobarCombinacion(cartasEnviar, 15, 0, new ArrayList<>(), carta);
    }
    public ArrayList<ArrayList<Carta>> comprobarCombinacion(ArrayList<Carta> cartas, int suma, int indice, ArrayList<Carta> combinacionActual,Carta carta) {
        ArrayList<ArrayList<Carta>> combinacionesValidas = new ArrayList<>();
        if (suma == 0 && combinacionActual.contains(carta)) {

            // Se ha encontrado una combinación que suma 15
            System.out.println("Combinación que suma 15: " + combinacionActual);
            System.out.println("\n");
            ArrayList<Carta> nuevaCombinacion = new ArrayList<>(combinacionActual);
            combinacionesValidas.add(new ArrayList<>(nuevaCombinacion));
        }

        for (int i = indice; i < cartas.size(); i++) {
            if (suma - cartas.get(i).getValorNumerico() >= 0) {
                combinacionActual.add(cartas.get(i));
                ArrayList<ArrayList<Carta>> combinaciones = comprobarCombinacion(cartas, suma - cartas.get(i).getValorNumerico(), i + 1, combinacionActual,carta);
                if (combinaciones != null) {
                    combinacionesValidas.addAll(combinaciones);
                }
                combinacionActual.remove(combinacionActual.size() - 1);
            }
        }
        if(combinacionesValidas.isEmpty()){
            return null;
        }else {
            return combinacionesValidas;
        }
    }


    public void jugar(ArrayList<Carta> cartasMesa){
    }

    public boolean isHaConseguidoCartasEnRonda() {
        return haConseguidoCartasEnRonda;
    }

    public void setHaConseguidoCartasEnRonda(boolean haConseguidoCartasEnRonda) {
        this.haConseguidoCartasEnRonda = haConseguidoCartasEnRonda;
    }


    public Integer cantidadOros(){
        int i = 0;
        for (Carta cada_carta : this.cartasGanadas){
            if(cada_carta.getPalo().equalsIgnoreCase("oros"));
            i++;
        }
        return i;
    }
    public boolean tiene7oros(){
        boolean tiene = false;
        for (Carta cada_carta : this.cartasGanadas){
            if(cada_carta.getPalo().equalsIgnoreCase("oros") && cada_carta.getValorNumerico() == 7){
                tiene = true;
            }

        }
        return tiene;
    }
    public Integer cantidadSietes(){
        int i = 0;
        for (Carta cada_carta : this.cartasGanadas){
            if(cada_carta.getValorNumerico() == 7){
                i++;
            }
        }
        return i;
    }
    public void resetearPuntos(){
        this.puntosCartas = 0;
        this.puntosEscobas = 0;
        this.puntosOros = 0;
        this.puntosVelo = 0;
        this.puntosSietes = 0;
    }
}
