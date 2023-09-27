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
    public void generarEjercitos(){
        System.out.println("Se esta generando una cantidad aleatoria de villanos y de heroes (ambos ejercitos tienen las mismas tropas y sus stats son aleatorias)");
        int cantidad= (int) Math.floor(Math.random()*11)+1;
        for(int i=0;i<cantidad;i++){
            this.ejercito_heroes.add(new Heroe((Math.floor(Math.random()*201)+1000),(Math.floor(Math.random()*40)+20)));
            this.ejercito_villanos.add(new Villano((Math.floor(Math.random()*201)+2000),(Math.floor(Math.random()*40)+20)));
        }
        System.out.println("Se han generado "+cantidad+" tropas de cada ejercito");
    }
    public void empezar(){
        int ganadas=0;
        double totales= (double) this.ejercito_villanos.size() /2;
        for(int i=0;i<this.ejercito_villanos.size();i++){
            ganadas+=pelear(this.ejercito_villanos.get(i),this.ejercito_heroes.get(i));
            resetearTurno();
            System.out.println(ganadas+" "+totales+" "+ejercito_villanos.size());
        }
        if(ganadas<totales){
            System.out.println("Gano el ejercito de los villanos");
        } else if (ganadas>totales) {
            System.out.println("Gano el ejercito de los Heroes");
        }else {
            System.out.println("Empate");
        }
    }
    public int pelear(Villano villano,Heroe heroe){
        int cantidad=0;
        while (villano.getVida()>0||heroe.getVida()>0){
            System.out.println("vida villano: "+villano.getVida()+" "+"vida heroe: "+heroe.getVida());
            if(this.turno%2==0){
                int tirada1= heroe.tirarDado();
                int tirada2=heroe.tirarDado();

                int ataque_heroe = Math.max(tirada1, tirada2);
                System.out.println("Ataque heroe "+ataque_heroe);
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
