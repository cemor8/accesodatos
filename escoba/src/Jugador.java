import java.nio.file.attribute.UserPrincipal;

public class Jugador extends Participante{
    Usuario perfilUsuario;

    public Jugador(Usuario perfilUsuario) {
        this.perfilUsuario = perfilUsuario;
    }
    @Override
    public void jugar(){

    }
}
