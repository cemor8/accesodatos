import java.util.ArrayList;


public class Main {
    public static void main(String[] args) {
        /*
        Jugador jugador1 = new Jugador(new Usuario("carlos","12q12q12Q",null),new ArrayList<>());
        IA ia = new IA(new ArrayList<>());
        Mesa mesa = new Mesa(jugador1,ia,new Baraja(),1);
        mesa.iniciarPartida();

         */
        ControllerLogin controllerLogin = new ControllerLogin();
        if(!controllerLogin.verificarExistenciaBase()){
            controllerLogin.crearBase();
        }
        controllerLogin.mostrarOpcionesLogin();
    }
}