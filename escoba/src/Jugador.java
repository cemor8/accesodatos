import java.nio.file.attribute.UserPrincipal;
import java.util.ArrayList;
import java.util.Scanner;

public class Jugador extends Participante {
    Usuario perfilUsuario;

    public Jugador(Usuario perfilUsuario, ArrayList<Carta> mano) {
        super(mano);
        this.perfilUsuario = perfilUsuario;
    }

    /**
     * Método que se encarga de jugar un turno, primero comprueba si hay cartas en la mesa, si no hay,
     * pedira una para dejar en la mesa. Si hay cartas, pedirá una para jugar con ella, buscará todas
     * las posibles combinaciones que sumen 15 con ella, si no hay ninguna, la dejara en la mesa, si hay,
     * mostrará todas las combinaciones y pedirá que se seleccione una por terminal, luego comprobará si es una
     * escoba y acabará su función.
     * @param cartasEnMesa cartas que hay en la mesa
     */
    @Override
    public void jugar(ArrayList<Carta> cartasEnMesa) {
        if(cartasEnMesa.isEmpty()){
            Carta cartaSeleccionada = this.pedirCarta("Introduce carta para dejar");
            cartasEnMesa.add(cartaSeleccionada);
            this.getMano().remove(cartaSeleccionada);
            return;
        }

        Carta cartaSeleccionada = this.pedirCarta("Introduce carta para utilizar");
        ArrayList<ArrayList<Carta>> posiblesEscobas = buscarPosiblesEscobas(cartaSeleccionada, cartasEnMesa);

        if (posiblesEscobas == null) {
            System.out.println("No hay escobas disponibles con esa carta, se dejará en la mesa");
            cartasEnMesa.add(cartaSeleccionada);
            this.getMano().remove(cartaSeleccionada);
            return;
        }

        ArrayList<Carta> combinacion = this.pedirCombinacion(posiblesEscobas, "Introduce la combinacion a elegir");
        int l = 0;
        while (l < combinacion.size()) {
            this.getCartasGanadas().add(combinacion.get(l));
            if (this.getMano().contains(combinacion.get(l))) {
                this.getMano().remove(combinacion.get(l));
            } else {
                cartasEnMesa.remove(combinacion.get(l));
            }
        }
        if (cartasEnMesa.isEmpty()) {
            this.setPuntosEscobas(this.getPuntosEscobas() + 1);
        }


    }
    /**
     * Método que pide por terminal una carta para devolvela
     * @param texto texto a mostrar por terminal
     * */
    public Carta pedirCarta(String texto) {
        Integer opcion = null;
        while (opcion == null) {
            System.out.println(texto);
            int i = 0;
            System.out.println("\n");
            for (Carta carta : this.getMano()) {
                System.out.println(i + " " + carta + "\n");
            }
            Scanner opcionIn = new Scanner(System.in);
            try {
                opcion = opcionIn.nextInt();
                if (this.getMano().get(opcion) == null) {
                    throw new Exception("Opcion Inválida");
                }
            } catch (Exception err) {
                System.out.println(err.getMessage());
                opcion = null;
            }
        }
        return this.getMano().get(opcion);

    }
    /**
     * Método que pide una combinacion por terminal
     * @param combinaciones combinaciones disponibles
     * @param texto texto a mostrar por terminal
     * */
    public ArrayList<Carta> pedirCombinacion(ArrayList<ArrayList<Carta>> combinaciones, String texto) {
        Integer opcion = null;
        while (opcion == null) {
            System.out.println(texto);
            int j = 0;
            for (ArrayList<Carta> posiblesCombinaciones : combinaciones) {
                System.out.println("Combinacion " + j);
                for (Carta cadaCartaCombinacion : posiblesCombinaciones) {
                    System.out.println(cadaCartaCombinacion);
                }
                j++;
            }
            System.out.println("Introduce la combinacion deseada");
            Scanner combinacionIN = new Scanner(System.in);
            try {
                opcion = combinacionIN.nextInt();
                if (combinaciones.get(opcion) == null) {
                    throw new Exception("Opcion Inválida");
                }
            } catch (Exception err) {
                System.out.println(err.getMessage());
                opcion = null;
            }
        }
        return combinaciones.get(opcion);
    }
}
