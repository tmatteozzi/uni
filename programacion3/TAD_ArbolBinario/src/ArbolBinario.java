public abstract class ArbolBinario {
    private String nombreArbol;

    public ArbolBinario(String nombreArbol){
        this.nombreArbol = nombreArbol;
    }

    public abstract void destruir();
    public abstract Nodo padre(Nodo nodo);
    public abstract Nodo hijoIzquierdo(Nodo padre);
    public abstract Nodo hijoDerecho(Nodo padre);
    public abstract Nodo raiz();
    public abstract void insertarHijoIzquierda(Nodo padre, Nodo nodoAInsertar);
    public abstract void insertarHijoDerecha(Nodo padre, Nodo nodoAInsertar);
    public abstract void podarHijoIzquierda(Nodo padre);
    public abstract void podarHijoDerecha(Nodo padre);
}
