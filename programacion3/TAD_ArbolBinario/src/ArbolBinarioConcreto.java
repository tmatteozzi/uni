import java.util.LinkedList;
import java.util.Queue;

public class ArbolBinarioConcreto extends ArbolBinario {
    private Nodo raiz;

    public ArbolBinarioConcreto(String nombreArbol) {
        super(nombreArbol);
    }

    @Override
    public void destruir() {
        try{
            if(!esVacio()){
                raiz = null;
                System.out.println("ARBOL DESTRUIDO.");
            } else {
                throw new Exception("NO SE PUEDE DESTRUIR UN ARBOL QUE ESTA VACIO.");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Nodo padre(Nodo nodo) { return encontrarPadre(raiz, nodo); }

    // BUSCAR PADRE POR RECORRIDO DE AMPLITUD
    private Nodo encontrarPadre(Nodo raiz, Nodo hijoBuscado) {
        // SE UTILIZA LA LINKED LIST COMO COLA PARA NO TENER QUE HACER LAS FUNCIONES DE COLA
        Queue<Nodo> cola = new LinkedList<>();
        cola.offer(raiz);

        while (!cola.isEmpty()) {
            // SACAR ELEMENTO DE LA COLA PARA EVALUAR SI ES EL PADRE
            Nodo actual = cola.poll();

            if (actual.getHijoIzquierdo() != null) {
                if (actual.getHijoIzquierdo() == hijoBuscado) {
                    return actual;
                }
                // SI NO SE ENCONTRO EL PADRE AGREGAR EL HIJO A LA COLA PARA PROCESARLO EN OTRA ITERACION
                cola.offer(actual.getHijoIzquierdo());
            }
            if (actual.getHijoDerecho() != null) {
                if (actual.getHijoDerecho() == hijoBuscado) {
                    return actual;
                }
                // SI NO SE ENCONTRO EL PADRE AGREGAR EL HIJO A LA COLA PARA PROCESARLO EN OTRA ITERACION
                cola.offer(actual.getHijoDerecho());
            }
        }
        return null;
    }

    @Override
    public Nodo hijoIzquierdo(Nodo padre) { return padre.getHijoIzquierdo(); }

    @Override
    public Nodo hijoDerecho(Nodo padre) { return padre.getHijoDerecho(); }

    @Override
    public Nodo raiz() { return raiz; }

    @Override
    public void insertarHijoIzquierda(Nodo padre, Nodo nodoAInsertar) {
        try {
            if (raiz == null) {
                raiz = nodoAInsertar; // Si no hay raíz, el nodo se convierte en la raíz
            } else if (padre.getHijoIzquierdo() == null) {
                padre.setHijoIzquierdo(nodoAInsertar);
            } else {
                throw new Exception("NO SE PUEDE CARGAR UN NODO A LA IZQUIERDA, POSICION OCUPADA POR: " + padre.getHijoIzquierdo());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void insertarHijoDerecha(Nodo padre, Nodo nodoAInsertar) {
        try{
            if(padre.getHijoDerecho() == null){
                if (padre.getHijoIzquierdo() != null){
                    padre.setHijoDerecho(nodoAInsertar);
                } else {
                    throw new Exception("NO SE PUEDE CARGAR UN NODO A LA DERECHA, NO HAY NINGUN NODO A LA IZQUIERDA");
                }
            } else {
                throw new Exception("YA HAY UN VALOR EN EL HIJO DERECHO.");
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

    // METODOS AUXILIARES
    @Override
    public Nodo obtenerNodoPorContenido(int contenido) { return encontrarNodoPorContenido(raiz, contenido); }

    private Nodo encontrarNodoPorContenido(Nodo raiz, int contenido) {
        // SE UTILIZA LA LINKED LIST COMO COLA PARA NO TENER QUE HACER LAS FUNCIONES DE COLA
        Queue<Nodo> cola = new LinkedList<>();
        cola.offer(raiz);

        while (!cola.isEmpty()) {
            Nodo actual = cola.poll();
            // CHEQUEAR SI EL CONTENIDO SE ENCUENTRA EN EL ACTUAL
            if (actual.getContenido() == contenido) {
                return actual;
            }
            // SI NO SE ENCUENTRA EN EL ACTUAL SE AGREGAN LOS HIJOS A LA COLA PARA LA PROX ITERACION
            if (actual.getHijoIzquierdo() != null) {
                cola.offer(actual.getHijoIzquierdo());
            }
            if (actual.getHijoDerecho() != null) {
                cola.offer(actual.getHijoDerecho());
            }
        }
        return null;
    }

    public boolean esVacio(){ return raiz == null; }

    // METODOS PARA IMPRIMIR EL ARBOL
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
            sb.append(nodo);
            sb.append("\n");

            if (nodo.getHijoIzquierdo() != null || nodo.getHijoDerecho() != null) {
                imprimirArbol(nodo.getHijoIzquierdo(), prefijo + "|   ", sb);
                imprimirArbol(nodo.getHijoDerecho(), prefijo + "|   ", sb);
            }
        }
    }
}
