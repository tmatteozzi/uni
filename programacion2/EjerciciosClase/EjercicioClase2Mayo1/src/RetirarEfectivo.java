public class RetirarEfectivo extends Transaccion implements InterfazUsuario{
    // Constructor
    public RetirarEfectivo(int numeroTransaccion, double monto, String nombre, String dni) {
        super(numeroTransaccion, monto, nombre, dni);
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
        return nombre;
    }
}
