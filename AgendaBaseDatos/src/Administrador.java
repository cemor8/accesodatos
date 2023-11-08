public class Administrador {
    private String nombre_administrador;
    private String clave_administrador;

    public Administrador(String nombre_usuario, String clave_usuario) {
        this.nombre_administrador = nombre_usuario;
        this.clave_administrador = clave_usuario;
    }

    public String getNombre_usuario() {
        return nombre_administrador;
    }

    public String getClave_usuario() {
        return clave_administrador;
    }
}
