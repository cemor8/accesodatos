import java.util.ArrayList;


public class Main {
    public static void main(String[] args) {
        /*
        Jugador jugador1 = new Jugador(new Usuario("cmorbla","12q12q12Q",null),new ArrayList<>());
        IA ia = new IA(new ArrayList<>());
        Mesa mesa = new Mesa(jugador1,ia,new Baraja(),1);
        mesa.iniciarPartida(new Clasificacion(jugador1.getPerfilUsuario().getNombreUsuario(),0,0,0,0,0,0,jugador1.getPerfilUsuario()));
        */


        ControllerLogin controllerLogin = new ControllerLogin();
        if(!controllerLogin.verificarExistenciaBase()){
            controllerLogin.crearBase();
        }
        controllerLogin.mostrarOpcionesLogin();


    }
}