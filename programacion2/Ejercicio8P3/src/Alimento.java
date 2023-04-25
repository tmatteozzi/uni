
public class Alimento {
    // Atributos
    private int cantidadAlimentos;
    private double peso;

    // Constructor
    public Alimento(int cantidadAlimentos, double peso) {
        this.cantidadAlimentos = cantidadAlimentos;
        this.peso = peso;
    }

    // Método para calcular el peso de cada alimento
    public double calcularPesoAlimento(){
        double pesoAlimento = cantidadAlimentos * peso;
        return pesoAlimento;
    }
}
