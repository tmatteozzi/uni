public class ArbolBinarioConcreto extends ArbolBinario {
    private Nodo raiz;

    public ArbolBinarioConcreto(String nombreArbol) {
        super(nombreArbol);
    }

    @Override
    public void destruir() {
        raiz = null;
        System.out.println("ARBOL DESTRUIDO.");
    }

    @Override
    public Nodo padre(Nodo nodo) {
        return null; //TODO
    }

    @Override
    public Nodo hijoIzquierdo(Nodo padre) {
        return padre.getHijoIzquierdo();
    }

    @Override
    public Nodo hijoDerecho(Nodo padre) {
        return padre.getHijoDerecho();
    }

    @Override
    public Nodo raiz() {
        return raiz;
    }

    @Override
    public void insertarHijoIzquierda(Nodo padre, Nodo nodoAInsertar) {
        try{
            if(padre.getHijoIzquierdo() == null){
                padre.setHijoIzquierdo(nodoAInsertar);
            } else {
                throw new Exception("NO SE PUEDE CARGAR UN NODO A LA IZQUIERDA, POSICION OCUPADA POR: " + padre.getHijoIzquierdo());
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void insertarHijoDerecha(Nodo padre, Nodo nodoAInsertar) {
        try{
            if(padre.getHijoIzquierdo() != null){
                padre.setHijoDerecho(nodoAInsertar);
            } else {
                throw new Exception("NO SE PUEDE CARGAR UN NODO A LA DERECHA, NO HAY NINGUN NODO A LA IZQUIERDA");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void podarHijoIzquierda(Nodo padre) {
        try{
            if(padre.getHijoIzquierdo() != null){
                padre.setHijoIzquierdo(null);
            } else {
                throw new Exception("NO SE PUEDE PODAR UN HIJO QUE NO EXISTE.");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void podarHijoDerecha(Nodo padre) {
        try{
            if(padre.getHijoDerecho() != null){
                padre.setHijoDerecho(null);
            } else {
                throw new Exception("NO SE PUEDE PODAR UN HIJO QUE NO EXISTE.");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        imprimirArbol(raiz, "", sb);
        return sb.toString();
    }

    private void imprimirArbol(Nodo nodo, String prefijo, StringBuilder sb) {
        if (nodo != null) {
            sb.append(prefijo);
            sb.append("|-- ");
            sb.append(nodo.toString());
            sb.append("\n");

            if (nodo.getHijoIzquierdo() != null || nodo.getHijoDerecho() != null) {
                imprimirArbol(nodo.getHijoIzquierdo(), prefijo + "|   ", sb);
                imprimirArbol(nodo.getHijoDerecho(), prefijo + "|   ", sb);
            }
        }
    }
}
