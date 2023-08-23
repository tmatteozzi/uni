import java.util.Arrays;

public abstract class Bicola {
    private int[] bicola;
    private int frente, fin, size, cont;

    public Bicola(int size){
        this.size = size;
        bicola = new int[size];
        frente = fin = -1;
        cont = 0;
    }

    public boolean estaVacia() {
        return cont == 0;
    }

    public boolean estaLlena() {
        return cont == size;
    }

    public void agregarFrente(int valor) {
        try{
            if (!estaLlena()) {
                if (frente == -1) {
                    frente = fin = 0;
                } else if (frente == 0) {
                    frente = size - 1;
                } else {
                    frente--;
                }
                bicola[frente] = valor;
                cont++;
            } else {
                throw new Exception("LA BICOLA ESTA LLENA NO SE PUEDE AGREGAR");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void agregarFin(int valor) {
        try{
            if (!estaLlena()) {
                if (fin == -1) {
                    frente = fin = 0;
                } else if (fin == size - 1) {
                    fin = 0;
                } else {
                    fin++;
                }
                bicola[fin] = valor;
                cont++;
            } else {
                throw new Exception("LA BICOLA ESTA LLENA NO SE PUEDE AGREGAR.");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void removerFrente() {
        try{
            if (!estaVacia()) {
                int valor = bicola[frente];
                bicola[frente] = 0; // Establecer el valor a 0
                if (frente == fin) {
                    frente = fin = -1;
                } else if (frente == size - 1) {
                    frente = 0;
                } else {
                    frente++;
                }
                cont--;
                System.out.println("ELEMENTO " + valor + " ELIMINADO.");
            } else {
                throw new Exception("LA BICOLA ESTA VACIA NO SE PUEDE REMOVER.");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void removerFin() {
        try{
            if (!estaVacia()) {
                int valor = bicola[fin];
                bicola[fin] = 0; // Establecer el valor a 0
                if (frente == fin) {
                    frente = fin = -1;
                } else if (fin == 0) {
                    fin = size - 1;
                } else {
                    fin--;
                }
                cont--;
                System.out.println("ELEMENTO " + valor + " ELIMINADO.");
            } else {
                throw new Exception("LA BICOLA ESTA VACIA NO SE PUEDE REMOVER.");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public int obtenerFrente() {
        try {
            if(!estaVacia()){
                return bicola[frente];
            } else {
                throw new Exception("NO HAY FRENTE, LISTA VACIA.");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public int obtenerFin() {
        try {
            if(!estaVacia()){
                return bicola[fin];
            } else {
                throw new Exception("NO HAY FIN, LISTA VACIA.");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public int obtenerTamaño() {
        return size;
    }

    @Override
    public String toString() {
        return "Bicola: " + Arrays.toString(bicola);
    }
}
