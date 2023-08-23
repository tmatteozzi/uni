import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("INGRESAR EL SIZE DE LA BICOLA: ");
        int size = scanner.nextInt();
        Bicola bicola = new BicolaConcreta(size);

        int opcion;
        do {
            System.out.println("MENU: \n" +
                    "1. Agregar al frente\n" +
                    "2. Agregar al final\n" +
                    "3. Remover del frente\n" +
                    "4. Remover del final\n" +
                    "5. Mostrar frente\n" +
                    "6. Mostrar fin\n" +
                    "7. Imprimir\n" +
                    "8. Salir\n");
            opcion = scanner.nextInt();
            switch (opcion) {
                case 1:
                    System.out.print("INGRESAR EL VALOR A AGREGAR: ");
                    int valorFrente = scanner.nextInt();
                    bicola.agregarFrente(valorFrente);
                    break;
                case 2:
                    System.out.print("INGRESAR EL VALOR A AGREGAR: ");
                    int valorFin = scanner.nextInt();
                    bicola.agregarFin(valorFin);
                    break;
                case 3:
                    bicola.removerFrente();
                    break;
                case 4:
                    bicola.removerFin();
                    break;
                case 5:
                    System.out.println("FRENTE: " + bicola.obtenerFrente());
                    break;
                case 6:
                    System.out.println("FIN: " + bicola.obtenerFin());
                    break;
                case 7:
                    System.out.println(bicola);
                    break;
                case 8:
                    System.out.println("FIN DEL PROGRAMA");
                    break;
                default:
                    System.out.println("SELECCIONE UNA OPCION VALIDA");
            }
        } while (opcion != 8);
        scanner.close();
    }
}
