public class Transaccion{
    // Atributos
    protected int numeroTransaccion;
    protected double monto;
    protected String nombre, dni;

    // Constructor
    public Transaccion(int numeroTransaccion, double monto, String nombre, String dni){
        this.numeroTransaccion = numeroTransaccion;
        this.monto = monto;
        this.nombre = nombre;
        this.dni = dni;
    }
}
