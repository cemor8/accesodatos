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
    /**
     * Método que inicia una partida, pide por pantalla el menu del programa, si se apuesta, se llama al menu de la apuesta el
     * cual pedira el numero por el que se querrá apostar, luego los genera,
     * comprobará que sean diferentes y comprobará si el jugador gana o pierde.
     * */
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
                    while (num1.equals(num2)){
                        num2= this.generadorNumerosAleatorios.generar();
                    }
                    Double jugador=null;
                    Double ia=null;
                    while (ia==null&&jugador==null){
                        switch (this.entradaTeclado.apostar().toLowerCase()) {
                            case "a" -> {
                                jugador = num1;
                                ia = num2;
                            }
                            case "b" -> {
                                ia = num1;
                                jugador = num2;
                            }
                            default -> System.out.println("opcion invalida");
                        }

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
