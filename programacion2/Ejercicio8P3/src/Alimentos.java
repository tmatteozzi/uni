import java.util.ArrayList;

public class Alimentos extends Transporte {
    // ArrayList
    private ArrayList<Alimento> listaAlimentos;

    // Constructor
    public Alimentos(String descripcionCarga, String numeroPatente, int localidadSalida, int localidadLlegada) {
        super(descripcionCarga, numeroPatente, localidadSalida, localidadLlegada);
        listaAlimentos = new ArrayList<>();
        inicializarTiposAlimentos();
    }

    // Método para agregarle a la lista los tipos de alimentos
    public void inicializarTiposAlimentos() {
        listaAlimentos.add(new Alimento(100, 0.5));
        listaAlimentos.add(new Alimento(200, 1));
        listaAlimentos.add(new Alimento(300, 1.5));
    }

    // Método para el costo del viaje
    @Override
    public double obtienePeso() {
        double pesoTotal = 0;
        for (int i = 0; i < listaAlimentos.size(); i++){
            pesoTotal = pesoTotal + listaAlimentos.get(i).calcularPesoAlimento();
        }
        return pesoTotal;
    }
}
