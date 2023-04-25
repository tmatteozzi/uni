import java.time.LocalDate;
import java.util.Scanner;

public class Administrativo extends Persona {
    // Atributos
    private String seccion;

    // Constructor
    public Administrativo(int id, String nombre, String apellido, LocalDate fechaNacimiento, String seccion){
        super(id, nombre, apellido, fechaNacimiento);
        this.seccion = seccion;
    }

    // Getters
    public String getSeccion(){
        return seccion;
    }

    // To String
    @Override
    public String toString() {
        return "Administrativo{" + super.toString() +
                ", seccion='" + seccion + '\'' +
                '}';
    }

    // Método para calcular horas trabajadas por mes
    Scanner teclado = new Scanner(System.in);
    public void hsTrabajadas(String seccion){
        int hsPorDia = 0;
        int hsTrabajadas = 0;
        System.out.println("Ingrese la cantidad de días que trabaja por mes: ");
        int diasTrabajados = teclado.nextInt();
        if (seccion == "Exactas"){
            hsPorDia = 8;
            hsTrabajadas = hsPorDia * diasTrabajados;
            System.out.println("Las horas trabajadas por mes son: " + hsTrabajadas + " horas.");
        }
        else if (seccion == "Contables"){
            hsPorDia = 6;
            hsTrabajadas = hsPorDia * diasTrabajados;
            System.out.println("Las horas trabajadas por mes son: " + hsTrabajadas + " horas.");
        }
        else {
            System.out.println("La administracions se divide solamente en Exactas y Contables, no contamos con un sector de " + seccion +"!");
        }
    }
}
