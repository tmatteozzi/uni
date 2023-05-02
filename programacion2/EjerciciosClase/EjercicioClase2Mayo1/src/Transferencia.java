public class Transferencia extends Transaccion implements InterfazUsuario{
    // Atributos
    private String aliasCuentaATransferir;

    // Constructor
    public Transferencia(int numeroTransaccion, double monto, String nombre, String dni, String aliasCuentaATransferir) {
        super(numeroTransaccion, monto, nombre, dni);
        this.aliasCuentaATransferir = aliasCuentaATransferir;
    }

    // Métodos interfaz
    @Override
    public double numeroTransaccion() {
        return numeroTransaccion;
    }
    @Override
    public double informarMonto() {
       return monto;
    }
    @Override
    public String solicitarNombreOAlias() {
        return aliasCuentaATransferir;
    }
}
