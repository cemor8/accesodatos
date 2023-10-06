public class Villano extends Combatiente{
    public Villano(double vida, double poder_escudo) {
        super(vida, poder_escudo);
    }

    @Override
    public int tirarDado() {
        return (int) Math.floor(Math.random()*91);
    }
}
