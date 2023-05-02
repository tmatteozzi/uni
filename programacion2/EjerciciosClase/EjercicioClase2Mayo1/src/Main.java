public class Main {
    public static void main(String[] args) {
        // Crear 4 tipos de transacciones
        Transferencia t1 = new Transferencia(15452, 15000, "Tomás", "45015317", "avion-perro-tren");
        RetirarEfectivo t2 = new RetirarEfectivo(27575, 10000, "Joaquin", "26789514");
        Deposito t3 = new Deposito(7737, 2000000, "Fernando", "33789528", "toro-cubo-flor");
        RecargaMovil t4 = new RecargaMovil(8841, 3000, "Gabriel", "7885", "rabino-forma-agua");

        // Probar funciones en todos los tipos de transacciones
        System.out.println("TRANSFERENCIA");
        System.out.println("El nombre o alias es: " + t1.solicitarNombreOAlias() + "\n" +
                            "El monto es: " + t1.informarMonto() + "\n" +
                            "El nro de transaccion es: " + t1.numeroTransaccion());

        System.out.println("RETIRAR EFECTIVO");
        System.out.println("El nombre o alias es: " + t2.solicitarNombreOAlias() + "\n" +
                "El monto es: " + t2.informarMonto() + "\n" +
                "El nro de transaccion es: " + t2.numeroTransaccion());

        System.out.println("DEPOSITO");
        System.out.println("El nombre o alias es: " + t3.solicitarNombreOAlias() + "\n" +
                "El monto es: " + t3.informarMonto() + "\n" +
                "El nro de transaccion es: " + t3.numeroTransaccion());

        System.out.println("RECARGA MOVIL");
        System.out.println("El nombre o alias es: " + t4.solicitarNombreOAlias() + "\n" +
                "El monto es: " + t4.informarMonto() + "\n" +
                "El nro de transaccion es: " + t4.numeroTransaccion());
    }
}