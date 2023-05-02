public class RecargaMovil extends Transaccion implements InterfazUsuario{
    // Atributos
    private String aliasCuentaMovilADepositar;

    // Constructor
    public RecargaMovil(int numeroTransaccion, double monto, String nombre, String dni, String aliasCuentaMovilADepositar) {
        super(numeroTransaccion, monto, nombre, dni);
        this.aliasCuentaMovilADepositar = aliasCuentaMovilADepositar;
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
        return aliasCuentaMovilADepositar;
    }
}
