public class Main {
    public static void main(String[] args) {
        // Inicializar un ganado
        Transporte ganadoTest = new Ganado("Carga ganado", "EXL 511", 0, 3,5,20);
        System.out.println("El costo del transporte es de: " + ganadoTest.costoTransporte());

        // Inicializar un bulto
        Transporte bultoTest = new Bulto("Carga bulto", "NGO 867", 0,3,5,5,5);
        System.out.println("El costo del transporte es de: " + bultoTest.costoTransporte());

        // Inicializar
        Transporte alimentosTest = new Alimentos("Carga alimento", "MCI 086",0,3);
        System.out.println("El costo del transporte es de: " + alimentosTest.costoTransporte());

        // Comparación entre 2 transportes
        bultoTest.mayorCostoTransporte(alimentosTest);
    }
}