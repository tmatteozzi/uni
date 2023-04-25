public class Profesor extends Persona {
    private int matricula;
    private String materia;

    public Profesor(String nombre, int edad, int matricula, String materia){
        super(nombre, edad);
        this.matricula = matricula;
        this.materia = materia;
    }

    @Override
    public void mostrarDatos() {
        System.out.println("Profesor");
        System.out.println("DATOS:\n" +
                "Nombre: " + super.getNombre() +"\n" +
                "Edad: " + super.getEdad() + "\n" +
                "Matricula: "+ matricula + "\n" +
                "Materia: " + materia +".");
    }
}
