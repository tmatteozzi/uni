public class Infante extends Persona{

    // Constructor por defecto
    public Infante(){}
    // Constructor con nombre, edad y sexo
    public Infante(String nombre, int edad, char sexo){
        super(nombre, edad, sexo);
    }
    // Constructor con todos los atributos
    public Infante(String nombre, int edad, int dni, int peso, double altura, char sexo) {
        super(nombre, edad, dni, peso, altura, sexo);
    }
    @Override
    public void calcularIMC() {
        double pesoIdeal = 0;
        pesoIdeal = peso/(altura*altura);
        System.out.println("El imc es de: " + pesoIdeal);
    }

}
