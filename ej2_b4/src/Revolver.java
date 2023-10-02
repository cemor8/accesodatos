public class Revolver {
    private int posicionActual;
    private int posicionBala;

    public Revolver(int posicionActual, int posicionBala) {
        this.posicionActual = posicionActual;
        this.posicionBala = posicionBala;
    }
    public boolean disparar(){
        return this.posicionActual == this.posicionBala;
    }
    public void SiguienteBala(){
        if(this.posicionActual==6){
            this.posicionActual=0;
        }else {
            this.posicionActual+=1;
        }
    }
    public void miRevolver(){
        System.out.println("Posicion actual: "+this.posicionActual);
        System.out.println("Poscion de la bala: "+this.posicionBala);
    }
}
