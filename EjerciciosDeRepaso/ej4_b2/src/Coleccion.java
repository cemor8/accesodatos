import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Coleccion {
    private String nombreColeccion;
    private ArrayList<Figura> listaFiguras;



    public Coleccion(String nombreColeccion, ArrayList<Figura> listaFiguras) {
        this.nombreColeccion = nombreColeccion;
        this.listaFiguras = new ArrayList<Figura>();
    }

    public String getNombreColeccion() {
        return nombreColeccion;
    }
    public ArrayList<Figura> getListaFiguras() {
        return listaFiguras;
    }
    public void setNombreColeccion(String nombreColeccion) {
        this.nombreColeccion = nombreColeccion;
    }
    public void asignarFiguras(Figura figura){
        this.listaFiguras.add(figura);
    }

    public String toString() {
        String texto="";
       for (Figura cada_figura : this.listaFiguras){
           texto+="Figura{"+
                   "codigo='"+cada_figura.getCodigo()+'\''+
                   ", superheroe='"+cada_figura.getSuperheroe()+'\''+
                   ", precio='"+cada_figura.getPrecio()+'\''+
                   ", dimension='"+cada_figura.getDimension()+'\''+
                   ", codigo='"+cada_figura.getCodigo()+'\''+
                   ", nombre='"+cada_figura.getSuperheroe().getNombre()+'\''+
                   ", descripcion='"+cada_figura.getSuperheroe().getDescripcion()+
                   ", capa='"+cada_figura.getSuperheroe().isCapa()+
                   '}';

       }
       return texto;
    }


    /**
     *Método que muestra los datos de las figuras de la coleccion que tengan capa.
     * */
    public String conCapa(){
        String texto="";
        for (Figura cada_figura : this.listaFiguras){
            if(cada_figura.getSuperheroe().isCapa()){
                texto+="Figura{"+
                        "codigo='"+cada_figura.getCodigo()+'\''+
                        ", superheroe='"+cada_figura.getSuperheroe()+'\''+
                        ", precio='"+cada_figura.getPrecio()+'\''+
                        ", dimension='"+cada_figura.getDimension()+'\''+
                        ", codigo='"+cada_figura.getCodigo()+'\''+
                        ", nombre='"+cada_figura.getSuperheroe().getNombre()+'\''+
                        ", descripcion='"+cada_figura.getSuperheroe().getDescripcion()+
                        ", capa='"+cada_figura.getSuperheroe().isCapa()+
                        '}';

            }
            }
        if(texto.length()==0){
            texto="No hay ninguna con capa";
        }
        return texto;

    }
    /**
     *Método que ordena la lista de figuras por precio y devuelve la mas cara.
     * */
    public Figura masValioso(){
        ArrayList<Figura> figuras= (ArrayList<Figura>) this.listaFiguras.clone();
        figuras.sort((figura1, figura2) -> Double.compare(figura1.getPrecio(), figura2.getPrecio()));
        return figuras.get(0);


    }
    /**
     *Método que recorre las figuras de la coleccion para devolver el precio total de la coleccion.
     * @return double
     * */
    public double getValorColeccion(){
        double precio=0;
        for (Figura cada_figura : this.listaFiguras){
            precio+=cada_figura.getPrecio();
        }
        return precio;
    }
    /**
     * Método que devuelve una cantidad de volumen aproximada de la coleccion de figuras.
     * @return double.
     * */
    public double getVolumenColeccion(){
        double figurasVolumen=0;
        for (Figura cada_figura : this.listaFiguras){
            figurasVolumen+=cada_figura.getDimension().getVolumen();
        }
        if(figurasVolumen==0){
            return 0;
        }
        return figurasVolumen+200;
    }


}
