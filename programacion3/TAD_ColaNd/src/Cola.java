public abstract class Cola {
    // ATRIBUTOS
    private String nombreCola;

    // CONSTRUCTOR
    public Cola(String nombreCola){ this.nombreCola = nombreCola; }

    // METODOS
    public abstract boolean esVacio();
    public abstract void vaciar();
    public abstract long largo();
    public abstract Nodo verPrimero();
    public abstract Nodo verUltimo();
    public abstract void enfilar(Nodo nuevoNodo);
    public abstract void sacar();

    // TOSTRING
    public String getNombreCola() { return nombreCola; }
}
