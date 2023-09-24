import java.util.Scanner;

public class ApuestaPorUnNumero {
    private EntradaTeclado entradaTeclado;
    private  Marcador marcador;
    private GeneradorNumerosAleatorios generadorNumerosAleatorios;

    public ApuestaPorUnNumero(EntradaTeclado entradaTeclado, Marcador marcador, GeneradorNumerosAleatorios generadorNumerosAleatorios) {
        this.entradaTeclado = entradaTeclado;
        this.marcador = marcador;
        this.generadorNumerosAleatorios = generadorNumerosAleatorios;
    }

    public String partida(){
        System.out.println("Que quieres Hacer\n1.Apostar\n2.Salir");
        Integer opcion=null;
        Scanner opcionIN=new Scanner(System.in);
            try {
                opcion=opcionIN.nextInt();
            }catch (Exception error){
                System.out.println("error al establecer la opcion");
                return partida();
            }

            switch (opcion){
                case 1:
                    Double num1= this.generadorNumerosAleatorios.generar();
                    Double num2= this.generadorNumerosAleatorios.generar();
                    while (num1==null){
                        num2= this.generadorNumerosAleatorios.generar();
                    }
                    Double jugador=null;
                    Double ia=null;
                    switch (this.entradaTeclado.apostar()){
                        case "a":
                            jugador=num1;
                            ia=num2;
                            break;
                        case "b":
                            ia=num1;
                            jugador=num2;
                            break;
                    }
                    if(jugador>ia){
                        this.marcador.añadirAciertos();
                        System.out.println("Ganaste");
                    }else{
                        this.marcador.añadirFallos();
                        System.out.println("Perdiste");
                    }
                    System.out.println(jugador+" "+ia);
                    this.marcador.añadirPartidas();
                    System.out.println("Aciertos: "+this.marcador.getAciertos());
                    System.out.println("Fallos: "+this.marcador.getFallos());
                    System.out.println("Partidas: "+this.marcador.getPartidas());
                    break;
                case 2:
                    System.exit(0);
                    break;
                default:
                    return partida();

            }
        return partida();

    }
}
