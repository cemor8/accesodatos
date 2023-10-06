public class Revolver {
    private int posicionActual;
    private int posicionBala;

    public Revolver(int posicionActual, int posicionBala) {
        this.posicionActual = posicionActual;
        this.posicionBala = posicionBala;
    }
    /**
     * Método que devuelve true si la posicon actual del tambor es donde esta la bala.
     * */
    public boolean disparar(){

        return this.posicionActual == this.posicionBala;
    }

    /**
     * Método que hace la accion de mover el tambor del cargador.
     * */
    public void SiguienteBala(){
        if(this.posicionActual==6){
            this.posicionActual=0;
        }else {
            this.posicionActual+=1;
        }
    }
    /**
     * Método que muestra la posicion de la bala y la actual del cargador.
     * */
    public void miRevolver(){
        System.out.println("Posicion actual: "+this.posicionActual);
        System.out.println("Poscion de la bala: "+this.posicionBala);
    }
}
