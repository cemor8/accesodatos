public class Clasificacion {
    private String nombre_usuario;
    private int partidas_ganadas;
    private int puntos_oros;
    private int puntos_escobas;
    private int puntos_velo;
    private int puntos_cantidad_cartas;
    private int puntos_sietes;
    private Usuario usuario;

    public Clasificacion(String nombre_usuario, int partidas_ganadas, int puntos_oros, int puntos_escobas, int puntos_velo, int puntos_cantidad_cartas, int puntos_sietes, Usuario usuario) {
        this.nombre_usuario = nombre_usuario;
        this.partidas_ganadas = partidas_ganadas;
        this.puntos_oros = puntos_oros;
        this.puntos_escobas = puntos_escobas;
        this.puntos_velo = puntos_velo;
        this.puntos_cantidad_cartas = puntos_cantidad_cartas;
        this.puntos_sietes = puntos_sietes;
        this.usuario = usuario;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public int getPartidas_ganadas() {
        return partidas_ganadas;
    }

    public int getPuntos_oros() {
        return puntos_oros;
    }

    public int getPuntos_escobas() {
        return puntos_escobas;
    }

    public int getPuntos_velo() {
        return puntos_velo;
    }

    public int getPuntos_cantidad_cartas() {
        return puntos_cantidad_cartas;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public void setPartidas_ganadas(int partidas_ganadas) {
        this.partidas_ganadas = partidas_ganadas;
    }

    public void setPuntos_oros(int puntos_oros) {
        this.puntos_oros = puntos_oros;
    }

    public void setPuntos_escobas(int puntos_escobas) {
        this.puntos_escobas = puntos_escobas;
    }

    public void setPuntos_velo(int puntos_velo) {
        this.puntos_velo = puntos_velo;
    }

    public void setPuntos_cantidad_cartas(int puntos_cantidad_cartas) {
        this.puntos_cantidad_cartas = puntos_cantidad_cartas;
    }

    public int getPuntos_sietes() {
        return puntos_sietes;
    }

    public void setPuntos_sietes(int puntos_sietes) {
        this.puntos_sietes = puntos_sietes;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    @Override
    public String toString() {
        return "Usuario: '" + nombre_usuario + '\'' +
                ", Partidas ganadas: " + partidas_ganadas +
                ", Puntos oros: " + puntos_oros +
                ", Puntos escobas: " + puntos_escobas +
                ", Puntos Velo: " + puntos_velo +
                ", Puntos Cantidad de Cartas: " + puntos_cantidad_cartas +
                ", Puntos Sietes: " + puntos_sietes;
    }
}
