public abstract class Lista {
    private String nombreLista;

    public Lista(String nombreLista){
        this.nombreLista = nombreLista;
    }

    public abstract int localizar(int elemento);
    public abstract void insertar(int elemento);
    public abstract void insertarEnPosicion(int posicion, int elemento);
    public abstract void eliminar(int posicion);
    public abstract ListaConcreta ordenarElementos();
    public abstract ListaConcreta copiarLista();
    public abstract void dividirLista(int posicion);

    public String getNombreLista() { return nombreLista; }
}
