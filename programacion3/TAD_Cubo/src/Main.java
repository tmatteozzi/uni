import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

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
            mostrarMenu();
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese las coordenadas para agregar (ancho, alto, largo): ");
                    int a = scanner.nextInt();
                    int b = scanner.nextInt();
                    int c = scanner.nextInt();
                    System.out.print("Ingrese el valor: ");
                    int valor = scanner.nextInt();
                    cubo.cargar(a, b, c, valor);
                    break;
                case 2:
                    System.out.print("Ingrese las coordenadas para modificar (ancho, alto, largo): ");
                    a = scanner.nextInt();
                    b = scanner.nextInt();
                    c = scanner.nextInt();
                    System.out.print("Ingrese el nuevo valor: ");
                    valor = scanner.nextInt();
                    cubo.modificar(a, b, c, valor);
                    break;
                case 3:
                    System.out.print("Ingrese las coordenadas a anular (ancho, alto, largo): ");
                    a = scanner.nextInt();
                    b = scanner.nextInt();
                    c = scanner.nextInt();
                    cubo.anular(a, b, c);
                    break;
                case 4:
                    System.out.print("Ingrese las coordenadas (ancho, alto, largo): ");
                    a = scanner.nextInt();
                    b = scanner.nextInt();
                    c = scanner.nextInt();
                    System.out.println("Valor: " + cubo.valor(a, b, c));
                    break;
                case 5:
                    System.out.println("Lista de posiciones nulas:");
                    cubo.nulos();
                    break;
                case 6:
                    System.out.println(cubo.toString());
                    break;
                case 7:
                    System.out.println("¡Hasta luego!");
                    break;
                default:
                    System.out.println("Opción no válida");
            }
        } while (opcion != 5);

        scanner.close();
    }

    public static void mostrarMenu() {
        System.out.println("MENU:\n" +
                "1. Cargar valor" +
                "2. Modificar valor" +
                "3. Anular valor" +
                "4. Obtener valor" +
                "5. Mostrar posiciones nulas" +
                "6. Mostrar el contenido del cubo" +
                "7. Salir");
    }
}
