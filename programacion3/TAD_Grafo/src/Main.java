import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // INICIALIZACION DE SCANNER
        Scanner scanner = new Scanner(System.in);
        // INPUTS
        System.out.print("INGRESE LA CANTIDAD DE NODOS: ");
        int cantidadNodos = scanner.nextInt();
        System.out.print("INGRESE LA CANTIDAD DE FLECHAS: ");
        int cantidadFlechas = scanner.nextInt();
        // CREACION DEL GRAFO
        Grafo grafo = new Grafo(cantidadNodos, cantidadFlechas);
        // INPUT IDENTIFICADORES DE NODO
        for (int i = 0; i < cantidadNodos; i++) {
            System.out.print("IDENTIFICADOR DEL NODO " + (i+1) + ": ");
            String identificador = scanner.next();
            grafo.agregarNodo(i, identificador);
        }
        // INPUT IDENTIFICADORES DE FLECHAS
        for (int i = 0; i < cantidadFlechas; i++) {
            System.out.print("IDENTIFICADOR DE LA FLECHA " + (i+1) + ": ");
            String identificador = scanner.next();
            grafo.agregarFlecha(i, identificador);
        }
        // INPUT DE ARISTAS (RELACIONES
        for (int i = 0; i < cantidadFlechas; i++) {
            System.out.println("\nRELACION PARA LA FLECHA " + grafo.getFlechas()[i] + ":");
            System.out.println("---------------------------");
            System.out.print("NODO INICIO: ");
            String inicioIdentificador = scanner.next();
            int inicio = grafo.obtenerPosicionNodo(inicioIdentificador);
            if (inicio == -1) {
                System.out.println("NODO NO ENCONTRADO");
                return;
            }
            System.out.print("NODO FIN: ");
            String finIdentificador = scanner.next();
            int fin = grafo.obtenerPosicionNodo(finIdentificador);
            if (fin == -1) {
                System.out.println("NODO NO ENCONTRADO");
                return;
            }
            grafo.agregarArista(inicio, fin, grafo.getFlechas()[i]);
        }
        // IMPRIMIR MATRICES
        grafo.imprimirMatrizAdyacencia();
        grafo.imprimirMatrizIncidencia();
    }
}