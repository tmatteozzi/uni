import java.util.Arrays;

public abstract class Conjunto {
    private int n, cont;
    private int[] conj;

    public Conjunto(int n){
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

    public void conjuntoVacio() {
        conj = new int[n];
        cont = 0;
    }

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

    public void retirar(int pe) {
        int posicion = -1;
        for (int i = 0; i < cont; i++) { // Buscar la posicion donde se encuentra el elemento a retirar
            if (conj[i] == pe) {
                posicion = i; // Al encontrar la posicion asignarla y salir
                break;
            }
        }
        if (posicion != -1) {
            for (int i = posicion; i < cont - 1; i++) { // Mover elementos atras dspues de retiro
                conj[i] = conj[i + 1];
            }
            conj[cont - 1] = 0; // Poner un 0 (predet) en donde se retiro el elemento
            cont--;
            System.out.println("ELEMENTO ELIMINADO");
        } else {
            System.out.println("EL ELEMENTO NO EXISTE");
        }
    }

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

    public int getCont() { return cont; }
    public int[] getConj() { return conj; }

    @Override
    public String toString() {
        return "Conjunto: " + Arrays.toString(conj);
    }
}
