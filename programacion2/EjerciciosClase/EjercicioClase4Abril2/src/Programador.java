public class Programador extends Empleado {
    // Atributos
    int lineasDeCodigoPorHora;
    String lenguajeDominante;

    // Constructor
    public Programador(String nombreYApellido, String cedula, int edad, boolean casado, double salario, int lineasDeCodigoPorHora, String lenguajeDominante) {
        super(nombreYApellido, cedula, edad, casado, salario);
        this.lineasDeCodigoPorHora = lineasDeCodigoPorHora;
        this.lenguajeDominante = lenguajeDominante;
    }
}
