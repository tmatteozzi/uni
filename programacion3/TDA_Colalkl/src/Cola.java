public abstract class Cola {
    private String nombreCola;

    public Cola(String nombreCola){
        this.nombreCola = nombreCola;
    }

    public abstract boolean esVacio();
    public abstract void vaciar();
    public abstract long largo();
    public abstract int verPrimero();
    public abstract int verUltimo();
    public abstract void enfilar(int nuevoElemento);
    public abstract void sacar();

    public String getNombreCola() { return nombreCola; }
}
