public class Main {
    public static void main(String[] args) {
        ApuestaPorUnNumero apuestaPorUnNumero=new ApuestaPorUnNumero(new EntradaTeclado(),new Marcador(0,0,0),new GeneradorNumerosAleatorios());
        apuestaPorUnNumero.partida();
    }
}