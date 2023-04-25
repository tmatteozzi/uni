public class Profesor extends Persona {
    // Atributos
    private String sueldo;
    // Constructor
    public Profesor(String nombre, String apellido, String dni, int edad, String sueldo) {
        super(nombre, apellido, dni, edad);
        this.sueldo = sueldo;
    }

    // Getters & Setters
    public String getSueldo() {
        return sueldo;
    }
    public void setSueldo(String sueldo) {
        this.sueldo = sueldo;
    }

    // ToString
    @Override
    public String toString() {
        return "Profesor [sueldo=" + sueldo + ", nombre=" + nombre + ", apellido=" + apellido + ", dni=" + dni
                + ", edad=" + edad + "]";
    }
}
