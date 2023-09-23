public abstract class Cubo {
    public Cubo(){}

    public abstract void cargar(int ancho, int alto, int largo, int valor);
    public abstract void modificar(int ancho, int alto, int largo, int valor);
    public abstract void anular(int ancho, int alto, int largo);
    public abstract void nulos();
    public abstract void alea();
    public abstract int valor(int ancho, int alto, int largo);
    public abstract boolean validas(int ancho, int alto, int largo);
    public abstract void sumatoria();
}
