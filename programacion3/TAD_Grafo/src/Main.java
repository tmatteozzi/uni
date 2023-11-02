import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // INICIALIZACION DE SCANNER
        Scanner scanner = new Scanner(System.in);
        // INPUTS
        System.out.print("Ingrese la cantidad de nodos: ");
        int cantidadNodos = scanner.nextInt();
        System.out.print("Ingrese la cantidad de flechas: ");
        int cantidadFlechas = scanner.nextInt();
        // CREACION DEL GRAFO
        Grafo grafo = new Grafo(cantidadNodos, cantidadFlechas);
        // INPUT IDENTIFICADORES DE NODO
        for (int i = 0; i < cantidadNodos; i++) {
            System.out.print("Ingrese el identificador del nodo " + (i+1) + ": ");
            String identificador = scanner.next();
            grafo.agregarNodo(i, identificador);
        }
        // INPUT IDENTIFICADORES DE FLECHAS
        for (int i = 0; i < cantidadFlechas; i++) {
            System.out.print("Ingrese el identificador de la flecha " + (i+1) + ": ");
            String identificador = scanner.next();
            grafo.agregarFlecha(i, identificador);
        }
        // INPUT DE ARISTAS (RELACIONES
        for (int i = 0; i < cantidadFlechas; i++) {
            System.out.println("Relación para la flecha " + grafo.getFlechas()[i] + ":");
            System.out.print("Ingrese el nodo de inicio: ");
            String inicioIdentificador = scanner.next();
            int inicio = grafo.obtenerPosicionNodo(inicioIdentificador);
            if (inicio == -1) {
                System.out.println("Error: Identificador de nodo no encontrado.");
                return;
            }
            System.out.print("Ingrese el nodo de fin: ");
            String finIdentificador = scanner.next();
            int fin = grafo.obtenerPosicionNodo(finIdentificador);
            if (fin == -1) {
                System.out.println("Error: Identificador de nodo no encontrado.");
                return;
            }
            grafo.agregarArista(inicio, fin, grafo.getFlechas()[i]);
        }
        // IMPRIMIR MATRICES
        grafo.imprimirMatrizAdyacencia();
        grafo.imprimirMatrizIncidencia();
    }
}