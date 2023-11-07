public class Grafo {
    // ATRIBUTOS
    private int cantidadNodos;
    private int cantidadFlechas;
    private String[] nodos;
    private String[] flechas;
    private String[][] adyacencia;
    private String[][] incidencia;

    // CONSTRUCTOR
    public Grafo(int cantidadNodos, int cantidadFlechas) {
        this.cantidadNodos = cantidadNodos;
        this.cantidadFlechas = cantidadFlechas;
        adyacencia = new String[cantidadNodos][cantidadNodos];
        incidencia = new String[cantidadNodos][cantidadFlechas];
        nodos = new String[cantidadNodos];
        flechas = new String[cantidadFlechas];
    }

    // GETTERS
    public String[] getFlechas() { return flechas; }
    public String[] getNodos() { return nodos; }

    // METODOS
    public void agregarNodo(int indice, String identificador) { nodos[indice] = identificador; }

    public void agregarFlecha(int indice, String identificador) { flechas[indice] = identificador; }

    public void agregarArista(int nodo1, int nodo2, String flecha) {
        adyacencia[nodo1][nodo2] = "+" + flecha;
        adyacencia[nodo2][nodo1] = "-" + flecha;
        incidencia[nodo1][Integer.parseInt(flecha.substring(1))-1] = "+" + nodos[nodo2];
        incidencia[nodo2][Integer.parseInt(flecha.substring(1))-1] = "-" + nodos[nodo1];
    }

    public int obtenerPosicionNodo(String identificador) {
        for (int i = 0; i < cantidadNodos; i++) {
            if (nodos[i].equals(identificador)) {
                return i;
            }
        }
        return -1; // -1 SI NO ENCUENTRA AL NODO
    }

    public void imprimirMatrizAdyacencia() {
        System.out.println("ADYACENCIA:");
        System.out.printf("%4s", ""); // ESPACIO BLANCO PARA LA ESQUINA DE LA IZQUIERDA (%4s ES EL FORMATO DE ANCHO)
        for (int i = 0; i < cantidadNodos; i++) {
            System.out.printf("%4s", nodos[i]); // CABEZERAS DE COLUMNAS (IDENTIFICADORES DE NODOS)
        }
        System.out.println();
        for (int i = 0; i < cantidadNodos; i++) {
            System.out.printf("%4s", nodos[i]); // CABEZERAS DE FILAS (IDENTIFICADORES DE NODOS)
            for (int j = 0; j < cantidadNodos; j++) {
                System.out.printf("%4s", (adyacencia[i][j] != null ? adyacencia[i][j] : "0"));
            }
            System.out.println();
        }
    }

    public void imprimirMatrizIncidencia() {
        System.out.println("\nINCIDENCIA:");
        System.out.printf("%4s", ""); // ESPACIO BLANCO PARA LA ESQUINA DE LA IZQUIERDA (%4s ES EL FORMATO DE ANCHO)
        for (int i = 0; i < cantidadFlechas; i++) {
            System.out.printf("%4s", flechas[i]); // CABEZERAS DE COLUMNAS (IDENTIFICADORES DE FLECHAS)
        }
        System.out.println();
        for (int i = 0; i < cantidadNodos; i++) {
            System.out.printf("%4s", nodos[i]); // CABEZERAS DE FILAS (IDENTIFICADORES DE NODOS)
            for (int j = 0; j < cantidadFlechas; j++) {
                String valor = incidencia[i][j];
                System.out.printf("%4s", (valor != null ? valor : "0"));
            }
            System.out.println();
        }
    }
}
