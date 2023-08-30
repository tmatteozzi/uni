public abstract class Lista {
    private String nombreLista;

    public Lista(String nombreLista){
        this.nombreLista = nombreLista;
    }

    public abstract Nodo getPrimero();
    public String getNombreLista() { return nombreLista; }

    public abstract void localizar(int contenido);
    public abstract void insertar(Nodo nuevoNodo);
    public abstract void insertarEnPosicion(int posicion, Nodo nuevoNodo);
    public abstract void eliminar(int posicion);
    public abstract ListaConcreta ordenarElementos();
    public abstract ListaConcreta copiarLista();
    public abstract void dividirLista(int posicion);
}
