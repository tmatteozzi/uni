public class Nodo {
    private int contenido;
    private Nodo hijoIzquierdo;
    private Nodo hijoDerecho;

    public Nodo(int contenido) {
        this.contenido = contenido;
        this.hijoIzquierdo = null;
        this.hijoDerecho = null;
    }

    public int getContenido() { return contenido; }

    public Nodo getHijoIzquierdo() {
        return hijoIzquierdo;
    }

    public void setHijoIzquierdo(Nodo hijoIzquierdo) {
        this.hijoIzquierdo = hijoIzquierdo;
    }

    public Nodo getHijoDerecho() {
        return hijoDerecho;
    }

    public void setHijoDerecho(Nodo hijoDerecho) {
        this.hijoDerecho = hijoDerecho;
    }

    @Override
    public String toString() {
        return "(" + contenido + ")";
    }
}
