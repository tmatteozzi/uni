public class Elemento {
    // ATRIBUTOS
    private int dni;
    private String nombre, apellido, email, celular, direccion;
    private Elemento siguiente; // ATRIBUTO SIGUIENTE PARA QUE SE COMPORTE COMO UN NODO

    // CONSTRUCTOR
    public Elemento(String nombre, String apellido, int dni, String email, String celular, String direccion){
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
        this.celular = celular;
        this.direccion = direccion;
    }

    // GETTERS & SETTERS
    public int getDni() { return dni; }
    public Elemento getSiguiente() { return siguiente; }
    public void setSiguiente(Elemento siguiente) { this.siguiente = siguiente; }

    // TOSTRING
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(nombre).append(" ").append(apellido).append(", DNI: ").append(dni).append(")");
        if (siguiente != null) {
            sb.append(" -> ").append(siguiente);
        }
        return sb.toString();
    }
}
