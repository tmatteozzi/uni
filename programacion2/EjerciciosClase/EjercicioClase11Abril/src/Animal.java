public abstract class Animal extends Ruidosos{
    // Atributos
    private String nombre;
    // Constructor
    public Animal(String nombre) {
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
        return "Animal [nombre=" + nombre + "]";
    }
    // Método para hablar
    public abstract void hablar();
}