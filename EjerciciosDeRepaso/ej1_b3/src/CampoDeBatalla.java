import java.util.ArrayList;

public class CampoDeBatalla {
    private int turno;
    private ArrayList<Heroe> ejercito_heroes;
    private ArrayList<Villano> ejercito_villanos;

    public CampoDeBatalla() {
        this.turno = 0;
        this.ejercito_villanos=new ArrayList<>();
        this.ejercito_heroes=new ArrayList<>();
    }
    public void sumarTurno(){
        this.turno+=1;
    }
    public void resetearTurno(){
        this.turno=0;
    }
    /**
     * Método que genera un ejercito aleatoriamente, la cantidad de villanos y heroes sera la misma,
     * estará entre 1 y 11 combatientes, luego sus stats seran aleatorias, la vida de los heroes entre 1000 y 1200
     * y la de los villanos entre 2000 y 2200, ambos con el escudo entre 20 y 40.
     * */
    public void generarEjercitos(){
        System.out.println("Se esta generando una cantidad aleatoria de villanos y de heroes (ambos ejercitos tienen las mismas tropas y sus stats son aleatorias)");
        int cantidad= (int) Math.floor(Math.random()*11)+1;
        for(int i=0;i<cantidad;i++){
            this.ejercito_heroes.add(new Heroe((Math.floor(Math.random()*201)+1000),(Math.floor(Math.random()*40)+20)));
            this.ejercito_villanos.add(new Villano((Math.floor(Math.random()*201)+2000),(Math.floor(Math.random()*40)+20)));
        }
        System.out.println("Se han generado "+cantidad+" tropas de cada ejercito");
    }
    /**
     * Método que empieza una partida, lleva la cuenta de los combates, si ganan los heroes se añade una victoria, si no,
     * no se hace nada, para empezar recorre la lista de los villanos (da igual el ejercito ya que son de la misma capacidad),
     * y hace que los dos combatientes, tanto el de los villanos como el de los heroes, que se encuentren en la posicion que
     * se esta recorriendo en la lista, combatan. Al final, si los heroes han ganado mas de la mitad de los combates, es que el equipo de los
     * heroes han ganado, si han ganado menos, han perdido y si han ganado las mismas batallas ambos ejercitos, será un empate.
     * */
    public void empezar(){
        int ganadas=0;
        double totales= (double) this.ejercito_villanos.size() /2;
        for(int i=0;i<this.ejercito_villanos.size();i++){
            System.out.println("Combate: "+(i+1));
            ganadas+=pelear(this.ejercito_villanos.get(i),this.ejercito_heroes.get(i));
            resetearTurno();

        }
        System.out.println("Heroes: "+ganadas+" Villanos: "+(ejercito_villanos.size()-ganadas));
        if(ganadas<totales){
            System.out.println("Gano el ejercito de los villanos");
        } else if (ganadas>totales) {
            System.out.println("Gano el ejercito de los Heroes");
        }else {
            System.out.println("Empate");
        }
    }
    /**
     * Metodo que empieza la pelea entre dos contrincantes, lleva la cuenta del turno, ya que si el turno es par
     * ataca el héroe, si no, ataca el villano.
     * */
    public int pelear(Villano villano,Heroe heroe){
        int cantidad=0;
        while (villano.getVida()>0||heroe.getVida()>0){
            if(this.turno%2==0){
                int tirada1= heroe.tirarDado();
                int tirada2=heroe.tirarDado();

                int ataque_heroe = Math.max(tirada1, tirada2);
                if(ataque_heroe>villano.getPoder_escudo()){
                    villano.restarVida(ataque_heroe/ villano.getPoder_escudo());
                }
                if(villano.getVida()<=0){
                    System.out.println("Gana el heroe");
                    cantidad=1;
                    break;
                }
            }else {
                int ataque_villano= villano.tirarDado();
                if(ataque_villano>heroe.getPoder_escudo()){
                    heroe.restarVida(ataque_villano/heroe.getPoder_escudo());
                }
                if(heroe.getVida()<=0){
                    System.out.println("Gana el villano");
                    break;
                }
            }
            sumarTurno();
        }
        return cantidad;
    }
}
