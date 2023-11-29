import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Baraja {
    private ArrayList<Carta> cartas = new ArrayList<>();
    public Baraja() {
        ArrayList<String> palos=new ArrayList<>(List.of("oros","copas","espadas","bastos"));
        for (String palo : palos) {

            for (int j = 1; j <= 10; j++) {

                switch (j) {
                    case 8:
                        cartas.add(new Carta("sota", palo,j));
                        break;
                    case 9:
                        cartas.add(new Carta("caballo", palo,j));
                        break;
                    case 10:
                        cartas.add(new Carta("rey", palo,j));
                        break;
                    default:
                        cartas.add(new Carta(String.valueOf(j), palo,j));
                        break;
                }

            }
        }
    }

    public ArrayList<Carta> getCartas() {
        return cartas;
    }
    public void barajar(){
        Collections.shuffle(this.cartas);
    }
    public void ponerEnMesa(ArrayList<Carta> cartasEnMesa){
        while (cartasEnMesa.size()<4){
            cartasEnMesa.add(this.cartas.get(0));
            this.cartas.remove(0);
        }
    }
    public Carta sacarCarta(){
        return this.cartas.remove(0);
    }
    public void meterCarta(Carta carta){
        this.cartas.add(carta);
    }
}
