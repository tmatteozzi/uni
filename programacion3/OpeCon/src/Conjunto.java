import java.util.HashSet;
import java.util.Random;

public class Conjunto {
    private int n; // Size del conjunto
    private int cont; // Contador de elementos
    private int pe; // Elemento a agregar, evaluar o eliminar
    private int[] conj; // Arreglo del conjunto

    public Conjunto(int[] conj, int n, int cont){
        // Validacion de que n sea un numero positivo
        if (n < 0){
            System.out.println(n + " no es valido.");
        }
        // Crear arreglo de size n
        conj = generarArregloAleatorio(n);
    }

    public void agregar(int[] conj, int pe, int n, int cont){
        existe(conj, pe, cont);
    }

    public void eliminar(int[] conj, int pe, int cont){
    }

    public void cVacio(int cont){
        if(cont == 0){
            System.out.println("CONJUNTO VACIO.");
        }
        else{
            System.out.println("CONJUNTO NO VACIO.");
        }
    }

    public void nElementos(int cont){
        System.out.println(cont + "ELEMENTOS");
    }

    public void existe(int[] conj, int pe, int count){
        // TODO: 09/08/2023
    }

    private int[] generarArregloAleatorio(int n) {
        int[] arregloAleatorio = new int[n];
        HashSet<Integer> numerosUtilizados = new HashSet<>();
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            int numAleatorio;
            do {
                numAleatorio = random.nextInt(); // Generar un número aleatorio
            } while (numerosUtilizados.contains(numAleatorio)); // Repetir si ya existe en el conjunto
            arregloAleatorio[i] = numAleatorio;
            numerosUtilizados.add(numAleatorio);
        }

        return arregloAleatorio;
    }
}
