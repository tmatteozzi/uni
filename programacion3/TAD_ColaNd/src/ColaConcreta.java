public class ColaConcreta extends Cola{
    // ATRIBUTOS
    private int cont;
    private Nodo primero, ultimo;

    // CONSTRUCTOR
    public ColaConcreta(String nombreCola) {
        super(nombreCola);
        primero = null;
        ultimo = null;
        cont = 0;
    }

    // METODOS HEREDADOS
    @Override
    public boolean esVacio() { return primero == null; }

    @Override
    public void vaciar() {
        primero = null;
        ultimo = null;
        cont = 0;
    }

    @Override
    public long largo() { return cont; }

    @Override
    public Nodo verPrimero() {
        try{
            if (!esVacio()){
                return primero;
            } else {
                throw new Exception("LISTA VACIA NO HAY PRIMERO.");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public Nodo verUltimo() {
        try{
            if (!esVacio()){
                return ultimo;
            } else {
                throw new Exception("LISTA VACIA NO HAY ULTIMO.");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public void enfilar(Nodo nuevoNodo) {
        cont++;
        nuevoNodo.setPosicion(cont);
        // Si el primer nodo esta vacio entonces la cola esta vacia y se agrega como primero y ultimo
        if (primero == null) {
            primero = nuevoNodo;
            ultimo = nuevoNodo;
        // Sino se agrega como nodo siguiente y se setea en la clase Nodo
        } else {
            ultimo.setSiguiente(nuevoNodo);
            ultimo = nuevoNodo;
        }
        System.out.println("ELEMENTO AGREGADO.");
    }

    @Override
    public void sacar() {
        try {
            if (!esVacio()) {
                cont--;
                // Mueve el puntero 'primero' al siguiente nodo, eliminando el primer nodo actual
                primero = primero.getSiguiente();
                Nodo actual = primero;
                // Al borrar el nodo reorganizar posiciones ej: (1,1) (2,2) -> (1,2) [Al sacar al (1,1)]
                int nuevaPosicion = 1;
                while (actual != null) {
                    actual.setPosicion(nuevaPosicion); // Al prox primer Nodo se le pone la posicion 1
                    nuevaPosicion++;
                    actual = actual.getSiguiente();
                }
                // Si se elimina el ultimo que quedaba entonces hacer que el contador vuelva a 0 y el ultimo sea 0
                if (primero == null) {
                    ultimo = null;
                    cont = 0;
                }
            } else {
                throw new Exception("NO SE PUEDE SACAR UN ELEMENTO DE UNA COLA QUE ESTÁ VACÍA");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // TOSTRING
    @Override
    public String toString() {
        if (primero == null) {
            return "COLA VACIA.";
        }
        StringBuilder resultado = new StringBuilder();
        Nodo actual = primero;
        while (actual != null) {
            resultado.append(actual).append(" ");
            actual = actual.getSiguiente();
        }
        return "Cola: [ " + resultado + "]";
    }
}
