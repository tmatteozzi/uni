import java.util.Arrays;

public class PilaEstatica extends Pila{
    private int[] pila;
    private int tope, size;

    public PilaEstatica(String nombrePila, int size) {
        super(nombrePila);
        this.size = size;
        pila = new int[size];
        tope = 0;
    }

    @Override
    public void apilarElemento(int elemento) {
        try{
            if(!pilaLlena()){
                pila[tope] = elemento;
                tope++;
                System.out.println("ELEMENTO " + elemento + " APILADO");
            } else {
                throw new Exception("NO SE PUEDE APILAR ELEMENTO. PILA LLENA");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void desapilarElemento() {
        try{
            if(!pilaVacia()){
                tope--;
                int desapilado = pila[tope];
                pila[tope] = 0;
                System.out.println("EL ELEMENTO " + desapilado + " HA SIDO DESAPILADO");
            } else{
                throw new Exception("NO SE PUEDE DESAPILAR UNA LISTA VACIA");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public int topePila() {
        try{
            if (!pilaVacia()) {
                return pila[tope - 1];
            } else {
                throw new Exception("NO HAY TOPE, LISTA VACIA");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return 0;
    }
    @Override
    public boolean pilaVacia () {
        return (tope == 0);
    }

    public boolean pilaLlena () {
        return (tope == size);
    }

    @Override
    public String toString() {
        return "Pila Estatica: " + Arrays.toString(pila);
    }
}

