import java.util.ArrayList;

public class Main {
    public static ArrayList<Persona> p = new ArrayList<> ();
    public static void main(String[] args) {
        // Crear nuevos alumnos y profesores y agregarlos al array list
        Alumno alumno1 = new Alumno("T", "M", "45015317", 19, "$30000", "Ingeniería en Informática");
        p.add(alumno1);
        Alumno alumno2 = new Alumno("M", "M", "45145879", 19, "$30000", "Ingeniería en Informática");
        p.add(alumno2);
        Profesor prof1 = new Profesor("J", "S", "23789543", 45, "$150000");
        p.add(prof1);

        // Imprimir todo
        for(Persona per : p) {
            System.out.println(per.getNombre() + ", " + per.getApellido());
        }

    }
}
