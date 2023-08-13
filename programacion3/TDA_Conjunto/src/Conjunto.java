public abstract class Conjunto {
    private int n, cont;
    private int[] conj;

    public Conjunto(int tamano){
        try {
            if(tamano > 0){
                conj = new int[tamano];
                cont = 0;
                n = tamano;
                System.out.println("CONJUNTO CREADO");
            } else{
                throw new Exception("CONJUNTO NO CREADO, TAMANO INVALIDO");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void ConjuntoVacio() {
        conj = new int[n];
        cont = 0;
    }

    public boolean Esvacio() {
        try {
            if (cont == 0) {
                System.out.println("CONJUNTO VACÍO");
                return true;
            } else {
                throw new Exception("CONJUNTO NO VACÍO");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    public void añadir(int PE) {
        try{
            if (cont < n) {
                if (!pertenece(PE)) {
                    conj[cont] = PE;
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

    public boolean pertenece(int pe) {
        try {
            for (int i = 0; i < cont; i++) {
                if (conj[i] == pe) {
                    System.out.println("EL ELEMENTO YA EXISTE");
                    return true;
                }
            }
            throw new Exception("EL ELEMENTO NO EXISTE");

        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    // SEGUIR VIENDO DE ACA PARA ABAJO
    public void retirar(int pe) {
        int indice = -1;
        for (int i = 0; i < cont; i++) {
            if (conj[i] == pe) {
                indice = i;
                break;
            }
        }
        if (indice != -1) {
            for (int i = indice; i < cont - 1; i++) {
                conj[i] = conj[i + 1];
            }
            cont--;
            System.out.println("ELEMENTO ELIMINADO");
        } else {
            System.out.println("EL ELEMENTO NO EXISTE");
        }
    }

    public abstract Conjunto Union(Conjunto otroConjunto);

    public abstract Conjunto Interseccion(Conjunto otroConjunto);

    public boolean Inclusion(Conjunto otroConjunto) {
        for (int i = 0; i < cont; i++) {
            if (!otroConjunto.pertenece(conj[i])) {
                System.out.println("NO TODOS LOS ELEMENTOS ESTÁN INCLUIDOS");
                return false;
            }
        }
        System.out.println("INCLUSIÓN VERIFICADA");
        return true;
    }
}
