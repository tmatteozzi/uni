import java.sql.SQLOutput;

public class ColaVector extends Cola{
    private int[] cola;
    private int cont, size;
    public ColaVector(String nombreCola, int size) {
        super(nombreCola);
        try{
            if (size > 0){
                cola = new int[size];
                cont = 0;
            } else {
                throw new Exception("NO SE PUEDE CREAR LISTA.");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean esVacio() {
        return (cont == 0);
    }

    @Override
    public void vaciar() {
        for(int i= 0; i < size; i++){
            cola[i] = 0;
        }
        System.out.println("PILA VACIA");
    }

    @Override
    public long largo() {
        return cont;
    }

    @Override
    public int verPrimero() {
        return cola[0];
    }

    @Override
    public int verUltimo() {
        return cola[cont];
    }

    @Override
    public void enfilar(int nuevoObjeto) {
        for(int i=0; i < size; i++){
            if(cola[i] == 0){

            }
        }
    }

    @Override
    public void sacar() {
        cola[cont] = 0;
        cont--;
    }
}
