import java.util.ArrayList;

public class PilaDinamica extends Pila{
    private ArrayList<Integer> pila;

    public PilaDinamica(String nombrePila) {
        super(nombrePila);
        pila = new ArrayList<>();
    }

    @Override
    public void apilarElemento(int elemento) {
        pila.add(elemento);
    }

    @Override
    public int desapilarElemento() {
        try{
            if(!pilaVacia()){
                return pila.remove(pila.size() - 1);
            } else {
                throw new Exception("NO SE PUEDE DESAPILAR UNA PILA VACIA");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    return 0;
    }

    @Override
    public int topePila() {
        try {
            if (!pilaVacia()) {
                return pila.get(pila.size() - 1);
            } else {
                throw new Exception("NO HAY TOPE. PILA VACIA");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    @Override
    public boolean pilaVacia() {
        return (pila.size() == 0);
    }

    @Override
    public String toString() {
        return "Pila Dinamica: " + pila;
    }
}
