public class Tabla {
    // ATRIBUTOS
    private Elemento tabla[];
    private int size;

    // CONSTRUCTOR
    public Tabla(int size){
        this.size = size;
        this.tabla = new Elemento[size];
        for(int i = 0; i < size; i++){
            this.tabla[i] = null;
        }
    }

    // HASH GENERADOR DE POSICION
    public int hash(Elemento e){
        int clave = e.getDni();
        return clave % size;
    }

    // METODOS
    public void agregar(Elemento e, int posicion){
        if (tabla[posicion] == null) {
            tabla[posicion] = e;
        } else {
            int nuevaPosicion = encontrarNuevaPosicion(posicion);
            if (nuevaPosicion != -1) {
                tabla[nuevaPosicion] = e;
            } else {
                Elemento actual = tabla[posicion];
                while (actual.getSiguiente() != null) {
                    actual = actual.getSiguiente();
                }
                actual.setSiguiente(e);
            }
        }
    }

    public int encontrarNuevaPosicion(int posicion) {
        for (int i = 0; i < size; i++) {
            int nuevaPosicion = (posicion + i) % size;
            if (tabla[nuevaPosicion] == null) {
                return nuevaPosicion;
            }
        }
        return -1; // No se encontró una posición vacía
    }

    // TOSTRING
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(i).append(": ");
            if (tabla[i] != null) {
                sb.append(tabla[i].toString());
            } else {
                sb.append("Vacio");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

}
