public class Main {
    public static void main(String[] args) {

        LoteriaPrimitiva loteriaPrimitiva=new LoteriaPrimitiva(new EntradaTeclado(),new GeneradorNumerosAleatorios());
        System.out.println(loteriaPrimitiva.partida());
    }
}