public abstract class Reloj extends Ruidosos {
    // Atributos
    private String nombre;
    // Constructor
    public Reloj(String nombre) {
        super();
        this.nombre = nombre;
    }
    // Getters & Setters
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    // ToString
    @Override
    public String toString() {
        return "Reloj [nombre=" + nombre + "]";
    }
    // Método hablar
    public void hablar() {
        System.out.println("Riiing");
    }
}