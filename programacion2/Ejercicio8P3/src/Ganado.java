public class Ganado extends Transporte {
    // Atributos
    private int cantidadDeCabezas;
    private double pesoPromedioPorCabeza;

    // Constructor
    public Ganado(String descripcionCarga, String numeroPatente, int localidadSalida, int localidadLlegada, int cantidadDeCabezas, double pesoPromedioPorCabeza) {
        super(descripcionCarga,numeroPatente, localidadSalida, localidadLlegada);
        this.cantidadDeCabezas = cantidadDeCabezas;
        this.pesoPromedioPorCabeza = pesoPromedioPorCabeza;
    }

    // ToString
    @Override
    public String toString() {
        return "Ganado {" + super.toString() +
                ", cantidadDeCabezas=" + cantidadDeCabezas +
                ", pesoPromedioPorCabeza=" + pesoPromedioPorCabeza +
                '}';
    }

    // Método para el costo del viaje (Abstracto Carga)
    @Override
    public double obtienePeso() {
        double pesoTotal = cantidadDeCabezas * pesoPromedioPorCabeza;
        return pesoTotal;
    }
}
