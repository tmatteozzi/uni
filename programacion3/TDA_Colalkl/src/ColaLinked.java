import java.util.LinkedList;

public class ColaLinked extends Cola{
    private LinkedList<Integer> cola;
    public ColaLinked(String nombreCola) {
        super(nombreCola);
        cola = new LinkedList<>();
    }

    @Override
    public boolean esVacio() {
        return cola.isEmpty();
    }

    @Override
    public void vaciar() {
        for(Integer elemento: cola){
            cola.remove(elemento);
        }
        System.out.println("COLA VACIADA.");
    }

    @Override
    public long largo() {
        return cola.size();
    }

    @Override
    public int verPrimero() {
        return cola.getFirst();
    }

    @Override
    public int verUltimo() {
        return cola.getLast();
    }

    @Override
    public void enfilar(int nuevoObjeto) {
        cola.add(nuevoObjeto);
    }

    @Override
    public void sacar() {
        cola.remove();
    }

    @Override
    public String toString() {
        return "ColaLinked:" + cola;
    }
}
