/***
    Realizar un programa que solicite al usuario ingresar la cantidad de nodos y flechas que desea crear.
    Luego, pedir identificadores únicos para cada nodo, evitando repeticiones.
    El programa debe permitir establecer relaciones entre los nodos y las flechas.
    Finalmente, imprimir las matrices de adyacencia e incidencia del grafo creado
*/

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // INICIALIZACION DE SCANNER
        Scanner scanner = new Scanner(System.in);
        // INPUT CANT NODOS
        System.out.print("INGRESE LA CANTIDAD DE NODOS: ");
        int cantidadNodos = scanner.nextInt();
        // VALIDACION CANT NODOS
        cantidadNodos = validarCantidad(cantidadNodos, scanner);
        // INPUT CANT FLECHAS
        System.out.print("INGRESE LA CANTIDAD DE FLECHAS: ");
        int cantidadFlechas = scanner.nextInt();
        // VALIDACION CANT FLECHAS
        cantidadFlechas = validarCantidad(cantidadFlechas, scanner);
        // CREACION DEL GRAFO
        Grafo grafo = new Grafo(cantidadNodos, cantidadFlechas);

        // INPUT IDENTIFICADORES DE NODO
        ArrayList<String> identificadoresNodos = new ArrayList<>(); // ARRAY PARA ALMACENAR LOS IDENTIFICADORES
        for (int i = 0; i < cantidadNodos; i++) {
            System.out.print("IDENTIFICADOR DEL NODO " + (i+1) + ": ");
            String identificador = scanner.next();
            // VALIDACION SI YA EXISTE EL IDENTIFICADOR
            if(identificadoresNodos.contains(identificador)){
                System.out.println("EL NODO CON IDENTIFICADOR " + identificador
                        + " YA EXISTE. INGRESAR UN IDENTIFICADOR DIFERENTE");
                i--;
                continue;
            }
            // SI EL IDENTIFICADOR NO EXISTE AGREGARLO A LA LISTA Y AGREGAR EL NODO
            identificadoresNodos.add(identificador);
            grafo.agregarNodo(i, identificador);
        }

        // INPUT IDENTIFICADORES DE FLECHAS
        ArrayList<String> identificadoresFlechas = new ArrayList<>(); // ARRAY PARA ALMACENAR LOS IDENTIFICADORES
        for (int i = 0; i < cantidadFlechas; i++) {
            System.out.print("IDENTIFICADOR DE LA FLECHA " + (i+1) + ": ");
            String identificador = scanner.next();
            // VALIDACION SI YA EXISTE EL IDENTIFICADOR
            if(identificadoresFlechas.contains(identificador)){
                System.out.println("LA FLECHA CON IDENTIFICADOR " + identificador
                        + " YA EXISTE. INGRESAR UN IDENTIFICADOR DIFERENTE");
                i--;
                continue;
            }
            // SI EL IDENTIFICADOR NO EXISTE AGREGARLO A LA LISTA Y AGREGAR LA FLECHA
            identificadoresFlechas.add(identificador);
            grafo.agregarFlecha(i, identificador);
        }

        // INPUT DE ARISTAS (RELACIONES
        for (int i = 0; i < cantidadFlechas; i++) {
            System.out.println("\nRELACION PARA LA FLECHA " + grafo.getFlechas()[i] + ":");
            System.out.println("---------------------------");
            // INPUT Y VALIDACION DEL NODO DE INICIO
            System.out.print("NODO INICIO: ");
            int inicio = validarNodo(grafo, scanner);
            // INPUT Y VALIDACION DEL NODO DEL FINAL
            System.out.print("NODO FIN: ");
            int fin = validarNodo(grafo, scanner);
            // AGREGAR RELACION
            grafo.agregarArista(inicio, fin, grafo.getFlechas()[i]);
            System.out.println("RELACIÓN AGREGADA: " + grafo.getNodos()[inicio] + " -" + grafo.getFlechas()[i] + "-> " + grafo.getNodos()[fin]);
        }

        // IMPRIMIR MATRICES
        grafo.imprimirMatrizAdyacencia();
        grafo.imprimirMatrizIncidencia();
    }

    public static int validarCantidad(int cantidad, Scanner scanner){
        while(cantidad <= 0){
            System.out.println("LA CANTIDAD DEBE SER UN VALOR POSITIVO MAYOR A 0");
            System.out.print("INGRESE LA CANTIDAD DE NUEVO: ");
            cantidad = scanner.nextInt();
        }
        return cantidad;
    }

    public static int validarNodo(Grafo grafo, Scanner scanner) {
        String identificador = scanner.next();
        int posicion = grafo.obtenerPosicionNodo(identificador);

        while (posicion == -1) {
            System.out.println("NODO NO ENCONTRADO. POR FAVOR INGRESE UN IDENTIFICADOR VÁLIDO.");
            System.out.print("NODO: ");
            identificador = scanner.next();
            posicion = grafo.obtenerPosicionNodo(identificador);
        }
        return posicion;
    }
}

//higdss