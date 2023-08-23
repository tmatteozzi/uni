public class Nodo {
    private int posicion, contenido;

    public Nodo(int posicion, int contenido){
        this.posicion = posicion;
        this.contenido = contenido;
    }

    public int getPosicion() { return posicion; }

    @Override
    public String toString() {
        return "(" + posicion + ", "+ contenido + ")";
    }
}
