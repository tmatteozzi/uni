public class Deposito extends Transaccion implements InterfazUsuario{
    // Atributos
    private String aliasCuentaADepositar;

    // Constructor
    public Deposito(int numeroTransaccion, double monto, String nombre, String dni, String aliasCuentaADepositar) {
        super(numeroTransaccion, monto, nombre, dni);
        this.aliasCuentaADepositar = aliasCuentaADepositar;
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
        return aliasCuentaADepositar;
    }
}
