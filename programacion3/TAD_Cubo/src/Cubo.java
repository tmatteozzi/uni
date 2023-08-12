import java.util.Random;

public abstract class Cubo {
    int[][][] conjunto;
    Random random = new Random();
    public Cubo(int alto, int ancho, int largo){
        try{
            if (alto != 0 && ancho != 0 && largo != 0){
            conjunto = new int[alto][ancho][largo];
            alea();
            } else {
                throw new Exception("HAY DIMENSIONES VACIAS, NO SE CREA EL CUBO.");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    public void cargar(int ancho, int alto, int largo, int valor) {
        try{
            if (valor != 0){
                if (validas(ancho, alto, largo)) {
                    if(conjunto[ancho][alto][largo] == 0){
                        conjunto[ancho][alto][largo] = valor;
                    }
                    else {
                        throw new Exception("NO SE PUEDE AGREGAR VALOR, YA HAY UN VALOR EXISTENTE. VALOR EXISTENTE: " + conjunto[ancho][alto][largo]);
                    }
                } else {
                    throw new Exception("NO SE PUEDE CARGAR A ESA POSICION YA QUE NO EXISTE.");
                }
            } else {
                throw new Exception("NO SE PUEDE CARGAR EL VALOR '0', UTILICE ANULAR.");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void modificar(int ancho, int alto, int largo, int valor){
        try{
            if(validas(ancho, alto, largo)){
                if(conjunto[ancho][alto][largo] != 0){
                    conjunto[ancho][alto][largo] = valor;
                } else {
                    throw new Exception("NO SE PUEDE MODIFICAR UN VALOR QUE NO EXISTE (0)");
                }
            } else {
                throw new Exception("NO SE PUEDE CARGAR A ESA POSICION YA QUE NO EXISTE.");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    public void anular(int ancho, int alto, int largo) {
        try {
            if (validas(ancho, alto, largo)) {
                conjunto[ancho][alto][largo] = 0;
            } else {
                throw new Exception("NO SE PUEDE ANULAR A ESA POSICION YA QUE NO EXISTE.");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void nulos(){
        try {
            int nulas = 0;
            System.out.println("LISTA DE POSICIONES NULAS:");
            for (int ancho = 0; ancho < conjunto.length; ancho++) {
                for (int alto = 0; alto < conjunto[ancho].length; alto++) {
                    for (int largo = 0; largo < conjunto[ancho][alto].length; largo++) {
                        if (conjunto[ancho][alto][largo] == 0) {
                            System.out.println("ANCHO:" + ancho + ", ALTO: " + alto + ", LARGO: " + largo);
                            nulas = +1;
                        }
                    }
                }
            }
            if (nulas == 0) {
                throw new Exception("NO HAY POSICIONES NULAS");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void alea(){
        for (int ancho = 0; ancho < conjunto.length; ancho++) {
            for (int alto = 0; alto < conjunto[ancho].length; alto++) {
                for (int largo = 0; largo < conjunto[ancho][alto].length; largo++) {
                    conjunto[ancho][alto][largo] = random.nextInt(100); // Valores aleatorios del 0 al 100
                }
            }
        }
    }

    public int valor(int ancho, int alto, int largo){
        return conjunto[ancho][alto][largo];
    }

    public boolean validas(int ancho, int alto, int largo){
        if (ancho < 0 || ancho >= conjunto.length || alto < 0 || alto >= conjunto[0].length ||
                largo < 0 || largo >= conjunto[0][0].length) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < conjunto.length; i++) {
            for (int j = 0; j < conjunto[i].length; j++) {
                for (int k = 0; k < conjunto[i][j].length; k++) {
                    sb.append("(").append(i).append(", ").append(j).append(", ").append(k).append("): ");
                    sb.append(conjunto[i][j][k]).append("\n");
                }
            }
        }
        return sb.toString();
    }
}
