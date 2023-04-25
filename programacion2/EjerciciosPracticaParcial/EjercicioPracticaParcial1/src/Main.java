import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        // Persona
        System.out.println("PERSONA: ");
        Persona personaTest = new Persona(00001, "Tomás", "Matteozzi", LocalDate.of(2003, 6, 17));
        personaTest.edadActual(personaTest.getFechaNacimiento());

        // Alumno
        System.out.println("ALUMNO: ");
        Alumno alumnoTest = new Alumno(00002, "Matias", "Marzorati", LocalDate.of(2004,4,19), "AM III", "Ing. en Inf.");
        alumnoTest.edadActual(alumnoTest.getFechaNacimiento());
        alumnoTest.aprobado(alumnoTest.getMateria());

        // Profesor
        System.out.println("PROFESOR: ");
        Profesor profesorTest = new Profesor(00003, "Genaro", "Scrocca", LocalDate.of(1998,10,20), "Ingeniero");
        profesorTest.edadActual(profesorTest.getFechaNacimiento());
        profesorTest.sueldo(profesorTest.getTitulo());

        // Administrativo
        System.out.println("ADMINISTRATIVO: ");
        Administrativo adminTest = new Administrativo(00004, "Franco", "Peralta", LocalDate.of(2003,9,4), "Exactas");
        adminTest.edadActual(adminTest.getFechaNacimiento());
        adminTest.hsTrabajadas(adminTest.getSeccion());
    }
}