
public class Libro {

    private String nombre;
    private String autor;
    private Integer paginas;
    private Integer año;

    public Libro(String nombre, String autor, Integer paginas, Integer año) {
        if(nombre==null||autor==null||paginas==null||año==null){
            throw new IllegalArgumentException("El campoObligatorio es obligatorio y no puede ser nulo.");
        }
        this.nombre = nombre;
        this.autor = autor;
        this.paginas = paginas;
        this.año = año;
    }

    public String getAutor() {
        return autor;
    }

    public Integer getPaginas() {
        return paginas;
    }

    public Integer getAño() {
        return año;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void setPaginas(Integer paginas) {
        this.paginas = paginas;
    }

    public void setAño(Integer año) {
        this.año = año;
    }

    @Override
    public String toString() {
        return "Libro{" +
                "nombre='" + nombre + '\'' +
                ", autor='" + autor + '\'' +
                '}';
    }
}
