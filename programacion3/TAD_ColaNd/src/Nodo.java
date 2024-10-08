public class Nodo {
    // ATRIBUTOS
    private int posicion, contenido;
    private Nodo siguiente;

    // CONSTRUCTOR
    public Nodo(int contenido){ this.contenido = contenido; }

    // GETTERS Y SETTERS
    public Nodo getSiguiente(){ return siguiente; }
    public void setPosicion(int posicion) { this.posicion = posicion; }
    public void setSiguiente(Nodo siguiente) { this.siguiente = siguiente; }

    // TOSTRING
    @Override
    public String toString() { return "(" + posicion + ", "+ contenido + ")"; }
}
