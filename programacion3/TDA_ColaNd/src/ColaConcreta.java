public class ColaConcreta extends Cola{
    public ColaConcreta(String nombreCola) {
        super(nombreCola);
    }

    @Override
    public boolean esVacio() {
        return false;
    }

    @Override
    public void vaciar() {
        
    }

    @Override
    public long largo() {
        return cont;
    }

    @Override
    public Nodo verPrimero() {
        return null;
    }

    @Override
    public Nodo verUltimo() {
        return null;
    }

    @Override
    public void enfilar(Nodo nuevoNodo) {
        cont++;
        nuevoNodo.setPosicion(cont);
    }

    @Override
    public void sacar() {

    }
}
