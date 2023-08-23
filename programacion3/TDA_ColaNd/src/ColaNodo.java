import java.util.LinkedList;

public class ColaNodo  {
    private String nombreCola;
    private LinkedList<Nodo> cola;

    public ColaNodo(String nombreCola) {
        this.nombreCola = nombreCola;
        cola = new LinkedList<>();
    }

    public boolean esVacio() {
        return cola.isEmpty();
    }

    public void vaciar() {
        try {
            if (!esVacio()) {
                for (Nodo elemento : cola) {
                    cola.remove(elemento);
                }
                System.out.println("COLA VACIADA.");
            } else {
                throw new Exception("NO SE PUEDE VACIAR UNA COLA QUE YA ESTÁ VACIA.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public long largo() {
        return cola.size();
    }

    public Nodo verPrimero() {
        return cola.getFirst();
    }

    public Nodo verUltimo() {
        return cola.getLast();
    }

    public void enfilar(Nodo nodo) {
        try {
            if (!cola.contains(nodo)) {
                cola.add(nodo);
            } else {
                throw new Exception("NO SE PUEDE AGREGAR UN NODO QUE YA PERTENECE");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void sacar() {
        try {
            if (!esVacio()) {
                cola.remove();
            } else {
                throw new Exception("NO SE PUEDEN SACAR ELEMENTOS DE UNA COLA VACIA.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String toString() {
        return "ColaNodo:" + cola;
    }
}
