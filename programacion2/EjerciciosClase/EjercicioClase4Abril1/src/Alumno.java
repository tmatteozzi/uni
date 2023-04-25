public class Alumno extends Persona {
    // Atributos
    private String matricula, carrera;

    // Constructor
    public Alumno(String nombre, String apellido, String dni, int edad, String matricula, String carrera) {
        super(nombre, apellido, dni, edad);
        this.matricula = matricula;
        this.carrera = carrera;
    }

    // Getters & Setters
    public String getMatricula() {
        return matricula;
    }
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
    public String getCarrera() {
        return carrera;
    }
    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }


    // ToString
    @Override
    public String toString() {
        return "Alumno [matricula=" + matricula + ", carrera=" + carrera + ", nombre=" + nombre + ", apellido="
                + apellido + ", dni=" + dni + ", edad=" + edad + "]";
    }
}
