public abstract class Conjunto {
    // CONSTRUCTOR
    public Conjunto(){}

    // METODOS
    public abstract void conjuntoVacio();
    public abstract boolean esVacio();
    public abstract void agregar(int pe);
    public abstract void retirar(int pe);
    public abstract boolean pertenece(int pe);
    public abstract int getCont();
    public abstract int[] getConj();
}
