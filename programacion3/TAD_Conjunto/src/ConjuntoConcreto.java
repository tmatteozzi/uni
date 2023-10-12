import java.util.Arrays;

public class ConjuntoConcreto extends Conjunto{
    // ATRIBUTOS
    private int n, cont;
    private int[] conj;

    // CONSTRUCTOR
    public ConjuntoConcreto(int n){
        super();
        this.n = n;
        try {
            if(n > 0){
                conj = new int[n];
                cont = 0;
                System.out.println("CONJUNTO CREADO");
            } else{
                throw new Exception("CONJUNTO NO CREADO, TAMANO INVALIDO");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    // METODOS HEREDADOS
    @Override
    public void conjuntoVacio() {
        conj = new int[n];
        cont = 0;
    }

    @Override
    public boolean esVacio() {
        try {
            if (cont == 0) {
                return true;
            } else {
                throw new Exception("CONJUNTO NO VACÍO");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public void agregar(int pe) {
        try{
            if (cont < n) {
                if (!pertenece(pe)) {
                    conj[cont] = pe;
                    cont++;
                    System.out.println("ELEMENTO AÑADIDO");
                } else {
                    throw new Exception("EL ELEMENTO YA EXISTE");
                }
            } else {
                throw new Exception("CONJUNTO LLENO, NO SE PUEDE AÑADIR ELEMENTO");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void retirar(int pe) {
        int posicion = -1;
        for (int i = 0; i < cont; i++) { // BUSCAR EN QUE POSICION SE ENCUENTRA EL ELEMENTO
            if (conj[i] == pe) {
                posicion = i; // SI SE ENCUENTRA SALIR
                break;
            }
        }
        if (posicion != -1) {
            for (int i = posicion; i < cont - 1; i++) { // MOVER ELEMENTOS DESPUES DE RETIRAR
                conj[i] = conj[i + 1];
            }
            conj[cont - 1] = 0; // PONER UN 0 DONDE SE RETIRO EL ELEMENTO
            cont--;
            System.out.println("ELEMENTO ELIMINADO");
        } else {
            System.out.println("EL ELEMENTO NO EXISTE");
        }
    }

    @Override
    public boolean pertenece(int pe) {
        try {
            for (int i = 0; i < cont; i++) {
                if (conj[i] == pe) {
                    return true;
                }
            }
            throw new Exception("EL ELEMENTO NO EXISTE");
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public int getCont() { return cont; }

    @Override
    public int[] getConj() { return conj; }

    // TOSTRING
    @Override
    public String toString() {
        return "Conjunto: " + Arrays.toString(conj);
    }
}
