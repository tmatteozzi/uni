public class Nodo {
    private int posicion, contenido;

    public Nodo(int contenido){
        this.contenido = contenido;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public int getContenido() {
        return contenido;
    }

    public void setContenido(int contenido) {
        this.contenido = contenido;
    }

    @Override
    public String toString() {
        return "(" + posicion + ", "+ contenido + ")";
    }
}
