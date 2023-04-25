public class Empleado {
    // Atributos
    String nombreYApellido, cedula;
    int edad;
    boolean casado;
    double salario;

    // Constructor
    public Empleado(String nombreYApellido, String cedula, int edad, boolean casado, double salario) {
        this.nombreYApellido = nombreYApellido;
        this.cedula = cedula;
        this.edad = edad;
        this.casado = casado;
        this.salario = salario;
    }

    // Getters & Setters
    public String getNombreYApellido() {
        return nombreYApellido;
    }
    public void setNombreYApellido(String nombreYApellido) {
        this.nombreYApellido = nombreYApellido;
    }
    public String getCedula() {
        return cedula;
    }
    public void setCedula(String cedula) {
        this.cedula = cedula;
    }
    public int getEdad() {
        return edad;
    }
    public void setEdad(int edad) {
        this.edad = edad;
    }
    public boolean isCasado() {
        return casado;
    }
    public void setCasado(boolean casado) {
        this.casado = casado;
    }
    public double getSalario() {
        return salario;
    }
    public void setSalario(double salario) {
        this.salario = salario;
    }


    // ToString
    @Override
    public String toString() {
        return "Empleado [nombreYApellido=" + nombreYApellido + ", cedula=" + cedula + ", edad=" + edad + ", casado="
                + casado + ", salario=" + salario + "]";
    }

    // Método de clasificacion según edad
    public String clasificarSegunEdad(int edad) {
        if (edad <= 21) {
            return "Principiante";
        }
        else if (edad >= 22 & edad <= 35) {
            return "Intermedio";
        }
        return "Senior";
    }

    // Imprimir los datos del empleado por pantalla (utilizar salto de línea \n para separar los atributos.
    public void imprimirDatosEmpleado() {
        System.out.println(nombreYApellido + "\n" + cedula + "\n" + casado + "\n" + salario );
    }

    //	Método que permita aumentar el salario en un porcentaje que sería pasado como parámetro al método.
    public double aumentarSalario(double porcentaje) {
        double aumento = salario * porcentaje;
        double salarioAumentado = salario + aumento;
        return salarioAumentado;
    }

}