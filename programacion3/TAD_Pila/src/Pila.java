public abstract class Pila {
    private String nombrePila;

    public Pila(String nombrePila){
        this.nombrePila = nombrePila;
    }

    public abstract void apilarElemento(int elemento);
    public abstract int desapilarElemento();
    public abstract int topePila();
    public abstract boolean pilaVacia();

    public String getNombrePila() {
        return nombrePila;
    }
}
