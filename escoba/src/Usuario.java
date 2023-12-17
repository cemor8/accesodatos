public class Usuario {
    private String nombreUsuario;
    private String clave;
    private Clasificacion clasificacion;

    public Usuario(String nombreUsuario, String clave, Clasificacion clasificacion) {
        this.nombreUsuario = nombreUsuario;
        this.clave = clave;
        this.clasificacion = clasificacion;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getClave() {
        return clave;
    }

    public Clasificacion getClasificacion() {
        return clasificacion;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public void setClasificacion(Clasificacion clasificacion) {
        this.clasificacion = clasificacion;
    }

    @Override
    public String toString() {
        return "Nombre de usuario: '" + nombreUsuario + '\'' +
                " Clave: " + clave + '\'';
    }
}
