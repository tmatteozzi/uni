import java.util.Scanner;
import java.time.LocalDate;

public class Alumno extends Persona {
    // Atributos
    private String materia, curso;

    // Constructor
    public Alumno(int id, String nombre, String apellido, LocalDate fechaNacimiento, String materia, String curso){
        super(id, nombre, apellido, fechaNacimiento);
        this.materia = materia;
        this.curso = curso;
    }

    // Getters & Setters
    public String getMateria() { return materia; }

    // ToString
    @Override
    public String toString() {
        return "Alumno{" + super.toString() +
                ", materia='" + materia + '\'' +
                ", curso='" + curso + '\'' +
                '}';
    }

    // Método para ver si aprobo una materia
    Scanner teclado = new Scanner(System.in);
    public void aprobado(String materia){
        System.out.println("Ingrese la nota que se saco en " + materia + ": ");
        double notaMateria = teclado.nextDouble();
        if (notaMateria >= 4 & notaMateria <= 10){
            System.out.println("Aprobó " + materia + "!");
        }
        else if (notaMateria <= 4 & notaMateria >= 0){
            System.out.println("Desaprobó " + materia + "!");
        }
        else {
            System.out.println("La nota que cargo no es valida. ¡Recuerde que debe ser del 1 al 10!");
        }
    }
}
