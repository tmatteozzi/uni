import javax.swing.*;
import java.util.Scanner;

public class Menu {
    public Menu(){
        iniciarMenu();
    }

    public void iniciarMenu(){
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;
        while(!salir){
            System.out.println("Ingrese el size del conjunto: ");
            int n = scanner.nextInt();
            Conjunto conjunto = new Conjunto(n);
            System.out.println("MENU:\n" +
                    "1. Agregar elemento\n" +
                    "2. Eliminar elemento\n" +
                    "3. Verificar conjunto vacío\n" +
                    "4. Ver número de elementos\n" +
                    "5. Verificar existencia de elemento\n" +
                    "6. Salir");
            int opcion = scanner.nextInt();
            switch (opcion){
                case 1:
                    int agregar = Integer.parseInt(JOptionPane.showInputDialog("Ingresar el nro a agregar: "));
                    conjunto.agregar(agregar);
                case 2:
                    int eliminar = Integer.parseInt(JOptionPane.showInputDialog("Ingresar el nro a eliminar: "));
                    conjunto.agregar(eliminar);
            }
        }
    }
}
