public class Libro {
    private String titulo;
    private String autor;
    private int paginas;
    private String fechaLanzamiento;

    public Libro(String titulo, String autor, int paginas, String fechaLanzamiento) {
        this.titulo = titulo;
        this.autor = autor;
        this.paginas = paginas;
        this.fechaLanzamiento = fechaLanzamiento;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public int getPaginas() {
        return paginas;
    }

    public String getFechaLanzamiento() {
        return fechaLanzamiento;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void setPaginas(int paginas) {
        this.paginas = paginas;
    }

    public void setFechaLanzamiento(String fechaLanzamiento) {
        this.fechaLanzamiento = fechaLanzamiento;
    }

    @Override
    public String toString() {
        return "Libro{" +
                "titulo='" + titulo + '\'' +
                ", autor='" + autor + '\'' +
                ", paginas=" + paginas +
                ", fechaLanzamiento='" + fechaLanzamiento + '\'' +
                '}';
    }
}
