public class ProfesorInterino extends Profesor{
    private String aulaDesignadaInterino;

    public ProfesorInterino(String nombre, int edad, int matricula, String materia, String aulaDesignadaInterino) {
        super(nombre, edad, matricula, materia);
        this.aulaDesignadaInterino = aulaDesignadaInterino;
    }

    @Override
    public void mostrarDatos() {
        System.out.println("Profesor Interino");
        super.mostrarDatos();
        System.out.println("Aula designada para el profesor Interino: " + aulaDesignadaInterino);
    }
}
