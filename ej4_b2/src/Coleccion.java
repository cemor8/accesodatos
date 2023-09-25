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

    public void setNombreColeccion(String nombreColeccion) {
        this.nombreColeccion = nombreColeccion;
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
        return texto;

    }
    public Figura masValioso(){
        ArrayList<Figura> figuras= (ArrayList<Figura>) this.listaFiguras.clone();
        Collections.sort(figuras, new Comparator<Figura>() {
            @Override
            public int compare(Figura figura1, Figura figura2) {

                return Double.compare(figura1.getPrecio(), figura2.getPrecio());
            }
        });
        return figuras.get(0);


    }
    public double getValorColeccion(){
        double precio=0;
        for (Figura cada_figura : this.listaFiguras){
            precio+=cada_figura.getPrecio();
        }
        return precio;
    }
    public double getVolumenColeccion(){
        double figurasVolumen=0;
        for (Figura cada_figura : this.listaFiguras){
            figurasVolumen+=cada_figura.getDimension().getVolumen();
        }
        return figurasVolumen+200;
    }

}
