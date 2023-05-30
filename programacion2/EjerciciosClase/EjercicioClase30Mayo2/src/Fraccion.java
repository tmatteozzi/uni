public class Fraccion {
    int numerador, denominador;

    public Fraccion(int numerador, int denominador){
        this.numerador = numerador;
        this.denominador = denominador;
    }

    @Override
    public String toString(){
        return numerador + "/" + denominador;
    }

    public void checkFraccion(){
        try{
            if(denominador == 0){
                throw new FraccionException("No se puede dividir por 0");
            }
            System.out.println("La fracción es valida!");
        } catch (FraccionException e){
            System.out.println(e.getMessage());
        }
    }
}
