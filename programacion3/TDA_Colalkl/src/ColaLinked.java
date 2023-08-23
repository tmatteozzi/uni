import java.util.LinkedList;

public class ColaLinked extends Cola{
    private LinkedList<Integer> cola;
    public ColaLinked(String nombreCola) {
        super(nombreCola);
        cola = new LinkedList<>();
    }

    @Override
    public boolean esVacio() {
        return cola.isEmpty();
    }

    @Override
    public void vaciar() {
        try{
            if(!esVacio()){
                for(Integer elemento: cola){
                    cola.remove(elemento);
                }
                System.out.println("COLA VACIADA.");
            } else {
                throw new Exception("NO SE PUEDE VACIAR UNA COLA QUE YA ESTA VACIA.");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public long largo() {
        return cola.size();
    }

    @Override
    public int verPrimero() {
        return cola.getFirst();
    }

    @Override
    public int verUltimo() {
        return cola.getLast();
    }

    @Override
    public void enfilar(int nuevoObjeto) {
        try{
            if (nuevoObjeto > 0) {
                cola.add(nuevoObjeto);
            } else {
                throw new Exception("NO SE PUEDE AGREGAR UN OBJETO MENOR DE 0.");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void sacar() {
        try{
            if(!esVacio()){
                cola.remove();
            } else {
                throw new Exception("NO SE PUEDEN SACAR ELEMENTOS DE UNA COLA VACIA.");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "ColaLinked:" + cola;
    }
}
