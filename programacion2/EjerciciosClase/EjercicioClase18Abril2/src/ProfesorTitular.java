public class ProfesorTitular extends Profesor{
    private String aulaDesignada;
    public ProfesorTitular(String nombre, int edad, int matricula, String materia,String aulaDesignada) {
        super(nombre, edad, matricula, materia);
        this.aulaDesignada = aulaDesignada;
    }

    @Override
    public void mostrarDatos() {
        System.out.println("Profesor Titular");
        super.mostrarDatos();
        System.out.println("Aula designada: " + aulaDesignada);
    }
}
