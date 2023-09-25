public class Marcador {
    private int aciertos;
    private int fallos;
    private int partidas;

    public Marcador(int aciertos, int fallos, int partidas) {
        this.aciertos = aciertos;
        this.fallos = fallos;
        this.partidas = partidas;
    }
    public void añadirAciertos(){
        this.aciertos+=1;
    }
    public void añadirFallos(){
        this.fallos+=1;
    }
    public void añadirPartidas(){
        this.partidas+=1;
    }
    public int getAciertos() {
        return aciertos;
    }

    public int getFallos() {
        return fallos;
    }

    public int getPartidas() {
        return partidas;
    }
}
