public class Bulto extends Transporte {
    // Atributos
    private int cantidadMini, cantidadMid, cantidadMax;

    // Constructor
    public Bulto(String descripcionCarga, String numeroPatente, int localidadSalida, int localidadLlegada, int cantidadMini, int cantidadMid, int cantidadMax) {
        super(descripcionCarga, numeroPatente, localidadSalida, localidadLlegada);
        this.cantidadMini = cantidadMini;
        this.cantidadMid = cantidadMid;
        this.cantidadMax = cantidadMax;
    }

    // ToString
    @Override
    public String toString() {
        return "Bulto {" + super.toString() +
                ", cantidadMini=" + cantidadMini +
                ", cantidadMid=" + cantidadMid +
                ", cantidadMax=" + cantidadMax +
                '}';
    }

    // Método para obtener el peso
    @Override
    public double obtienePeso() {
        // Peso por cantidad
        double pesoMini = cantidadMini * 10;
        double pesoMid = cantidadMid * 60;
        double pesoMax = cantidadMax * 125;
        // Peso total
        double pesoTotal = pesoMini + pesoMid + pesoMax;
        return pesoTotal;
    }
}
