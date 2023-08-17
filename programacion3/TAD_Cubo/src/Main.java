import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // Input de datos del cubo
        System.out.println("Ingrese las dimensiones del cubo:");
        System.out.print("Ancho: ");
        int ancho = scanner.nextInt();
        System.out.print("Alto: ");
        int alto = scanner.nextInt();
        System.out.print("Largo: ");
        int largo = scanner.nextInt();
        Cubo cubo = new CuboConcreto(ancho, alto, largo);

        int opcion;
        do {
            imprimirMenu();
            int a,b,c;
            opcion = scanner.nextInt();
            switch (opcion) {
                case 1:
                    System.out.println("Ingrese las coordenadas para agregar (ancho, alto, largo): ");
                    a = scanner.nextInt();
                    b = scanner.nextInt();
                    c = scanner.nextInt();
                    System.out.println("Ingrese el valor: ");
                    int valor = scanner.nextInt();
                    cubo.cargar(a, b, c, valor);
                    break;
                case 2:
                    System.out.println("Ingrese las coordenadas para modificar (ancho, alto, largo): ");
                    a = scanner.nextInt();
                    b = scanner.nextInt();
                    c = scanner.nextInt();
                    System.out.print("Ingrese el nuevo valor: ");
                    valor = scanner.nextInt();
                    cubo.modificar(a, b, c, valor);
                    break;
                case 3:
                    System.out.println("Ingrese las coordenadas a anular (ancho, alto, largo): ");
                    a = scanner.nextInt();
                    b = scanner.nextInt();
                    c = scanner.nextInt();
                    cubo.anular(a, b, c);
                    break;
                case 4:
                    System.out.println("Ingrese las coordenadas (ancho, alto, largo): ");
                    a = scanner.nextInt();
                    b = scanner.nextInt();
                    c = scanner.nextInt();
                    System.out.println("Valor: " + cubo.valor(a, b, c));
                    break;
                case 5:
                    cubo.nulos();
                    break;
                case 6:
                    System.out.println(cubo);
                    break;
                case 7:
                    cubo.sumatoria();
                    break;
                case 8:
                    System.out.println("FIN DEL PROGRAMA");
                    break;
                default:
                    System.out.println("OPCION NO VALIDA");
            }
        } while (opcion != 8);
        scanner.close(); // Cerrar scanner
    }

    public static void imprimirMenu() {
        System.out.println("MENU:\n" +
                "1. Cargar valor\n" +
                "2. Modificar valor\n" +
                "3. Anular valor\n" +
                "4. Obtener valor\n" +
                "5. Mostrar posiciones nulas\n" +
                "6. Mostrar el contenido del cubo\n" +
                "7. Obtener sumatoria de los valores del cubo\n" +
                "8. Salir");
    }
}
