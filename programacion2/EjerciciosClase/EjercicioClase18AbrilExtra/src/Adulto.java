public class Adulto extends Persona{

    // Constructor por defecto
    public Adulto(){}
    // Constructor con nombre, edad y sexo
    public Adulto(String nombre, int edad, char sexo){
        super(nombre, edad, sexo);
    }
    // Constructor con todos los atributos
    public Adulto(String nombre, int edad, int dni, int peso, double altura, char sexo) {
        super(nombre, edad, dni, peso, altura, sexo);
    }
    @Override
    public void calcularIMC() {
        double pesoIdeal = 0;
        pesoIdeal = peso/(altura*altura);
        if (pesoIdeal < 16){
            System.out.println("Bajo peso muy grave: " + pesoIdeal);
        } else if (16 < pesoIdeal & pesoIdeal < 16.99){
            System.out.println("Bajo peso grave: " + pesoIdeal);
        } else if (17 < pesoIdeal & pesoIdeal < 18.49){
            System.out.println("Bajo peso: " + pesoIdeal);
        } else if (18.50 < pesoIdeal & pesoIdeal < 24.99){
            System.out.println("Peso normal: " + pesoIdeal);
        } else if (25 < pesoIdeal & pesoIdeal < 29.99){
            System.out.println("Sobrepeso: " + pesoIdeal);
        } else if (30 < pesoIdeal & pesoIdeal < 34.99){
            System.out.println("Obesidad Grado I: " + pesoIdeal);
        } else if (35 < pesoIdeal & pesoIdeal < 39.99){
            System.out.println("Obesidad Grado II: " + pesoIdeal);
        } else {
            System.out.println("Obesidad Grado III (Morbida): " + pesoIdeal);
        }
    }

}
