import java.util.LinkedList;

public class ColaNodo extends Cola {
    private String nombreCola;
    private LinkedList<Nodo> cola;

    public ColaNodo(String nombreCola) {
        super(nombreCola);
        this.nombreCola = nombreCola;
        cola = new LinkedList<>();
    }

    @Override
    public boolean esVacio() {
        return cola.isEmpty();
    }

    @Override
    public void vaciar() {
        try{
            if(!esVacio()){
                while (!esVacio()) {
                    cola.remove(0); // Sacar el primer elemento en c/ iteracion
                }
                System.out.println("COLA VACIADA.");
            } else {
                throw new Exception("NO SE PUEDE VACIAR UNA COLA QUE YA ESTA VACIA.");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public long largo() {
        return cola.size();
    }

    @Override
    public Nodo verPrimero() {
        return cola.getFirst();
    }

    @Override
    public Nodo verUltimo() {
        return cola.getLast();
    }

    @Override
    public void enfilar(Nodo nuevoNodo) {
        try {
            if (nuevoNodo.getPosicion() > 0) {
                boolean posicionOcupada = false;
                // Validacion para no agregar 2 nodos con la misma posicion
                for (Nodo nodoExistente : cola) {
                    if (nodoExistente.getPosicion() == nuevoNodo.getPosicion()) {
                        posicionOcupada = true;
                        break;
                    }
                }
                if (!posicionOcupada) {
                    cola.add(nuevoNodo);
                    System.out.println("ELEMENTO " + nuevoNodo + " ENFILADO.");
                } else {
                    throw new Exception("LA POSICIÓN " + nuevoNodo.getPosicion() + " YA ESTÁ OCUPADA.");
                }
            } else {
                throw new Exception("NO SE PUEDE AGREGAR UN NODO CON POSICIÓN NEGATIVA O CERO.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void sacar() {
        try{
            if(!esVacio()){
                System.out.println("NODO " + cola.getFirst() + " ELIMINADO.");
                cola.remove();
            } else {
                throw new Exception("NO SE PUEDEN SACAR ELEMENTOS DE UNA COLA VACIA.");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "Cola Linked:" + cola;
    }
}
