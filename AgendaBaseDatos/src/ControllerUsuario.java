import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class ControllerUsuario {
    Usuario usuario;

    public ControllerUsuario(Usuario usuarioEntrada) {
        this.usuario=usuarioEntrada;
    }

    public void mostrarMenu(){
            while (true){

            }



        Integer opcion;
        while (true){
            System.out.println("""

                    1. Introducir nuevo contacto
                    2. Listar contactos
                    3. Modificar contacto
                    4. Eliminar contacto
                    5. Backup agenda
                    6. Salir
                    
                    """);
            Scanner opcionIn=new Scanner(System.in);
            try {
                opcion=opcionIn.nextInt();
            }catch (Exception err){
                System.out.println("Error al introducir la opcion");
                continue;
            }
            switch (opcion){
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
            }
        }
    }

    public void elegirAgenda(){
        Conexion conexion = new Conexion();
        StringBuilder mostrarAgendas=new StringBuilder();
        Connection connection=null;
        try {
            connection=conexion.hacerConexion(this.usuario.getNombre_usuario(),this.usuario.getClave_usuario());
            String sql="Select agenda.* from agenda inner join usuario on agenda.nombre_usuario = usuario.nombre_usuario";
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            ResultSet resultSet= preparedStatement.executeQuery();
            while (resultSet.next()){
                int id_agenda=resultSet.getInt("id_agenda");
                String nombre_agenda=resultSet.getString("nombre");
                mostrarAgendas.append(id_agenda+" "+nombre_agenda).append("\n");
            }

        }catch (SQLException err){
            System.out.println(err.getMessage());
            return;
        }
        if(mostrarAgendas.isEmpty()){
            System.out.println("El usuario no tiene agendas asignadas, contacta con un administrador");
            return;
        }
        Integer agendaElegida= devolverInteger(String.valueOf(mostrarAgendas));
        try{
            String sql="SELECT COUNT(*) FROM agenda WHERE id_agenda = ?";
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setInt(1, agendaElegida);
            ResultSet resultSet= preparedStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            if (count > 0) {
                System.out.println("La agenda con ID " + agendaElegida + " seleccionada.");
                this.usuario.setAgendaSeleccionada(new Agenda(agendaElegida));
            } else {
                System.out.println("La agenda con ID " + agendaElegida + " no existe.");
            }

        }catch (SQLException err){
            System.out.println(err.getMessage());
        }
    }

    public String devolverString(String texto){
        String stringDevolver=null;
        while (stringDevolver==null){
            Scanner stringDevolverIn=new Scanner(System.in);
            try {
                stringDevolver= stringDevolverIn.nextLine();
            }catch (Exception err){
                System.out.println("Contenido inválido");
            }
        }
        return stringDevolver;
    }
    public Integer devolverInteger(String texto){
        Integer integerDevolver=null;
        while (integerDevolver==null){
            System.out.println(texto);
            Scanner integerDevolverIn=new Scanner(System.in);
            try {
                integerDevolver=integerDevolverIn.nextInt();
            }catch (Exception err){
                System.out.println("Opcion inválida");
            }
        }
        return integerDevolver;
    }
}
