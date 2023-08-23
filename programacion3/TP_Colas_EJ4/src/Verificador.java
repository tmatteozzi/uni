import java.util.ArrayList;
import java.util.LinkedList;

public class Verificador {
    private String palabra;
    private ArrayList<Character> pila;
    private LinkedList<Character> cola;

    public Verificador(){
        pila = new ArrayList<>();
        cola = new LinkedList<>();
    }

    public boolean esPalindromo(String palabra) {
        palabra = palabra.toLowerCase(); // Transformar la palabra a minuscula
        for (char c : palabra.toCharArray()) { // Transformar la palabra a un array de char y agregar a pila y cola
            pila.add(c);
            cola.addLast(c);
        }
        while (!pila.isEmpty() && !cola.isEmpty()) {
            // Si el ultimo elemento de la pila es el primero de la cola entonces seguir, hasta que se vacie
            if (!pila.remove(pila.size() - 1).equals(cola.removeFirst())) {
                return false;
            }
        }
        // Si la pila se vacio y todos los ultimos de la pila fueron los primeros de la cola entonces es palindromo
        return true;
    }
}
