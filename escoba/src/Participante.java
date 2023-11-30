import java.util.ArrayList;

public class Participante {
    private ArrayList<Carta> mano = new ArrayList<>();
    private ArrayList<Carta> cartasGanadas = new ArrayList<>();
    private int puntosEscobas;
    private int puntosVelo;
    private int puntosSietes;
    private int puntosCartas;
    private int puntosOros;
    private int puntosTotales;

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
        this.puntosTotales = this.puntosEscobas + this.puntosVelo + this.puntosCartas + this.puntosOros + this.puntosSietes;
        return puntosTotales;
    }
    public void repartir(){

    }
    public void buscarPosiblesEscobas(){


    }
    public void jugar(){

    }



}
