import java.util.Arrays;

public class Conjunto {
    private int n; // Size del conjunto
    private int cont; // Contador de elementos
    private int pe; // Elemento a agregar, evaluar o eliminar
    private int[] conj; // Arreglo del conjunto

    public Conjunto(int n){
        this.n = n;
        // Validacion de que n sea un numero positivo
        if (n < 0){
            System.out.println(n + " NO VALIDO");
            return;
        }
        conj = new int[n]; // Crea conjunto de size n
        cont = 0; // Iniciar contador en 0
        System.out.println("CONJUNTO CREADO.");

    }

    public void agregar(int pe) {
        if (!existe(pe)) {
            if (cont == n) {
                System.out.println("CONJUNTO LLENO.");
            } else {
                // Agregar si hay un elemento vacio
                for (int i = 0; i < cont; i++) {
                    if (conj[i] == 0) {
                        conj[i] = pe;
                        System.out.println("ELEMENTO AGREGADO (reemplazando un 0).");
                        return;
                    }
                }
                // Agregar al final
                conj[cont] = pe;
                cont++;
                System.out.println("ELEMENTO AGREGADO.");
            }
        }
    }

    public void eliminar(int pe){
        if(existe(pe)){
            for(int i=0; i < cont; i++){
                if (conj[i] == pe){
                    conj[i] = 0;
                    cont--;
                }
            }
        }
    }

    public void cVacio(){
        if(cont == 0){
            System.out.println("CONJUNTO VACIO.");
        }
        else{
            System.out.println("CONJUNTO NO VACIO.");
        }
    }

    public void nElementos(){
        System.out.println("NUMERO DE ELEMENTOS: " + cont);
    }

    public boolean existe(int pe){
        if (cont == 0){
            System.out.println("CONJUNTO VACIO.");
        }
        for(int i=0; i<n; i++){
            if(conj[i] == pe){
                System.out.println("EL ELEMENTO " + pe + " EXISTE");
                return true;
            }
        }
        System.out.println("EL ELEMENTO " + pe + " NO EXISTE");
        return false;
    }

    @Override
    public String toString() {
        return "Conjunto{" + Arrays.toString(conj) + '}';
    }
}
