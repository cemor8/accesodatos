import java.sql.*;

public class Usuario {
    private String nombre_usuario;
    private String clave_usuario;
    private Agenda agendaSeleccionada=null;

    public Usuario(String nombre_usuario, String clave_usuario) {
        this.nombre_usuario = nombre_usuario;
        this.clave_usuario = clave_usuario;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public String getClave_usuario() {
        return clave_usuario;
    }

    public void setAgendaSeleccionada(Agenda agendaSeleccionada) {
        this.agendaSeleccionada = agendaSeleccionada;
    }

    public Agenda getAgendaSeleccionada() {
        return agendaSeleccionada;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public void setClave_usuario(String clave_usuario) {
        this.clave_usuario = clave_usuario;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nombre_usuario='" + nombre_usuario + '\'' +
                ", clave_usuario='" + clave_usuario + '\'' +
                '}';
    }
}
