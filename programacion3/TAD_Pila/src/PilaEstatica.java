public class PilaEstatica extends Pila{
    private int[] pila;
    private int tope, size;
    public PilaEstatica(String nombrePila, int size) {
        super(nombrePila);
        this.size = size;
        pila = new int[size];
        tope = -1;
    }

    @Override
    public void apilarElemento(int elemento) {
        try{
            if(!pilaLlena()){
                pila[tope++] = elemento;
                System.out.println("ELEMENTO " + elemento + " APILADO");
            } else {
                throw new Exception("NO SE PUEDE APILAR ELEMENTO. PILA LLENA");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void desapilarElemento() {
        try{
            if(!pilaVacia()){
                int desapilado = pila[tope];
                tope--;
                System.out.println("EL ELEMENTO " + desapilado + " HA SIDO DESAPILADO");
            } else{
                throw new Exception("NO SE PUEDE DESAPILAR UNA LISTA VACIA");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public int topePila() {
        try{
            if (!pilaVacia()) {
                return topePila();
            } else {
                throw new Exception("NO HAY TOPE, LISTA VACIA");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return 0;
    }
    @Override
    public boolean pilaVacia () {
        return (tope == -1);
    }

    public boolean pilaLlena () {
        return (tope == size - 1);
    }
}

