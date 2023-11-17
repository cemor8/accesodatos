
public class Main {
    public static void main(String[] args) {
        ControllerLogin controllerLogin=new ControllerLogin();
        if(!controllerLogin.verificarExistenciaBase()){
            controllerLogin.crearBase();
        }
        controllerLogin.mostrarOpcionesLogin();
    }
}