import java.time.LocalDate;
import java.time.Period;

public class Persona {
    // Atributos
    protected int id;
    protected String nombre, apellido;
    protected LocalDate fechaNacimiento;

    // Constructor
    public Persona(int id, String nombre, String apellido, LocalDate fechaNacimiento) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
    }

    // Getters & Setters
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }

    // ToString
    @Override
    public String toString() {
        return "Persona{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", fechaNacimiento=" + fechaNacimiento +
                '}';
    }

    // Método para calcular la edad
    public void edadActual(LocalDate fechaNacimiento){
        LocalDate fechaActual = LocalDate.now();
        int edad = Period.between(fechaNacimiento, fechaActual).getYears();
        System.out.println("La edad actual es " + edad + " años.");
    }
}
