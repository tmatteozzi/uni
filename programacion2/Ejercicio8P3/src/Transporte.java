public abstract class Transporte {
    // Atributos
    protected String descripcionCarga, numeroPatente;
    protected int localidadSalida, localidadLlegada;

    // Vector con el nombre de las ciudades
    protected String ciudades [] = {"Buenos Aires", "Rosario", "Santa Rosa", "Bariloche", "Mendoza"};

    // Matriz costos de las ciudades
    protected double costos [][] = {
            {0, 100, 150, 200, 250}, //Buenos Aires
            {100, 0, 150, 200, 250}, // Rosario
            {150, 150, 0, 200, 250}, // Santa Rosa
            {200, 200, 200, 0, 250}, // Bariloche
            {250, 250, 250, 250, 0} // Mendoza
    };

    // Constructor
    public Transporte(String descripcionCarga, String numeroPatente, int localidadSalida, int localidadLlegada) {
        this.descripcionCarga = descripcionCarga;
        this.numeroPatente = numeroPatente;
        this.localidadSalida = localidadSalida;
        this.localidadLlegada = localidadLlegada;
    }

    // ToString
    @Override
    public String toString() {
        return "descripcionCarga='" + descripcionCarga + '\'' +
                ", localidadSalida='" + localidadSalida + '\'' +
                ", localidadLlegada='" + localidadLlegada + '\'' +
                ", numeroPatente=" + numeroPatente;
    }

    // Método para calcular el costo por kg
    public double tarifaPorPeso(double pesoTotal) {
        if (pesoTotal <= 1200.0) {
            return 6.0;
        } else if (pesoTotal <= 2400.0) {
            return 5.5;
        } else if (pesoTotal <= 4000.0) {
            return 4.3;
        } else {
            return 3.6;
        }
    }

    // Método para el costo del transporte
    public double costoTransporte(){
        return (tarifaPorPeso(obtienePeso()) * obtienePeso()) + costos[localidadSalida][localidadLlegada];
    }

    // Método para comparar
    public void mayorCostoTransporte(Transporte aComparar){
        double costoTransporte1 = this.costoTransporte();
        double costoTransporte2 = aComparar.costoTransporte();
        if(costoTransporte1 > costoTransporte2){
            System.out.println("El transporte: " + this.toString() + ", es el transporte de mayor costo. Con un costo de: $" + costoTransporte1);
        }
        else {
            System.out.println("El transporte: " + aComparar + ", es el transporte de mayor costo. Con un costo de: $" + costoTransporte2);
        }
    }

    // Método abstracto para obtener peso en los diferentes tipos de carga
    public abstract double obtienePeso();
}
