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
    public void desapilarElemento() {
        try{
            if(!pilaVacia()){
                int desapilado = pila.remove(pila.size() - 1);
                System.out.println("EL ELEMENTO " + desapilado + " HA SIDO DESAPILADO");
            } else {
                throw new Exception("NO SE PUEDE DESAPILAR UNA PILA VACIA");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    @Override
    public int topePila() {
        return pila.get(pila.size());
    }

    @Override
    public boolean pilaVacia() {
        return (pila.size() == 0);
    }
}
