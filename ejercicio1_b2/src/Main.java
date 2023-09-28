import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
    Agenda agenda=new Agenda(new ArrayList<>());
    Controller controller=new Controller(agenda);
    controller.menu();
    }
}