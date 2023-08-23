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
        try{
            if (!esVacio()){
                for(int i= 0; i < size; i++){
                    cola[i] = 0;
                }
                System.out.println("PILA VACIA");
            }
            else{
                throw new Exception("NO SE PUEDE VACIAR UNA LISTA VACIA");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

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
        try{
            if(cont != size){
                cola[cont] = nuevoObjeto;
                cont++;
            } else {
                throw new Exception("NO SE PUEDE ENFILAR ELEMENTO. COLA LLENA");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void sacar() {
        try{
            if(!esVacio()){
                cola[cont] = 0;
                cont--;
            } else {
                throw new Exception("NO SE PUEDE SACAR UN ELEMENTO DE UNA COLA VACIA");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
