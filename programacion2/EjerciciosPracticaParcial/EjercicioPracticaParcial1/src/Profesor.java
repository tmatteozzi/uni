import java.time.LocalDate;

public class Profesor extends Persona{
    // Atributos
    private String titulo;

    // Constructor
    public Profesor(int id, String nombre, String apellido, LocalDate fechaNacimiento, String titulo){
        super(id, nombre, apellido, fechaNacimiento);
        this.titulo = titulo;
    }

    // Getters
    public String getTitulo(){
        return titulo;
    }

    // ToString
    @Override
    public String toString() {
        return "Profesor{" + super.toString() +
                ", titulo='" + titulo + '\'' +
                '}';
    }

    // Método para calcular sueldo
    public void sueldo(String titulo){
        double sueldo = 0;
        if (titulo == "Ingeniero"){
            sueldo = 200000;
        }
        else if (titulo == "Psicologo"){
            sueldo = 100000;
        }
        else if (titulo == "Economista"){
            sueldo = 150000;
        }
        else {
            sueldo = 80000;
        }
        System.out.println("El sueldo es de: " + sueldo);
    }
}
