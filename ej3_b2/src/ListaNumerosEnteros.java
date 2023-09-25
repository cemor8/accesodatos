import java.util.ArrayList;

public class ListaNumerosEnteros {
    private ArrayList<Integer> numeros;

    public ListaNumerosEnteros() {
        this.numeros = new ArrayList<>();
    }

    public ArrayList<Integer> getNumeros() {
        return numeros;
    }

    public void lista(Integer numero){
        if(!numeros.contains(numero)){
            numeros.add(numero);
        }else {
            System.out.println("El numero ya esta en la lista");
        }

    }
}

