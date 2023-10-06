public class Combatiente {
    private double vida;

    private double poder_escudo;

    public Combatiente(double vida, double poder_escudo) {
        this.vida = vida;
        this.poder_escudo = poder_escudo;
    }
    public int tirarDado(){
        return (int) Math.floor(Math.random()*101);
    }
    public double getVida() {
        return vida;
    }

    public double getPoder_escudo() {
        return poder_escudo;
    }
    public void restarVida(Double cantidad){
        this.vida-=cantidad;
    }
}
