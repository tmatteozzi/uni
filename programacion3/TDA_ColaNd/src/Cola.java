public abstract class Cola {
    private String nombreCola;
    int ultima, primera, cont;
    public Cola(String nombreCola){
        this.nombreCola = nombreCola;
        cont = 0;
    }

    public abstract boolean esVacio();
    public abstract void vaciar();
    public abstract long largo();
    public abstract Nodo verPrimero();
    public abstract Nodo verUltimo();
    public abstract void enfilar(Nodo nuevoNodo);
    public abstract void sacar();

    public String getNombreCola() { return nombreCola; }

    public void setNombreCola(String nombreCola) {
        this.nombreCola = nombreCola;
    }

    public int getUltima() {
        return ultima;
    }

    public void setUltima(int ultima) {
        this.ultima = ultima;
    }

    public int getPrimera() {
        return primera;
    }

    public void setPrimera(int primera) {
        this.primera = primera;
    }

    public int getCont() {
        return cont;
    }

    public void setCont(int cont) {
        this.cont = cont;
    }
}
