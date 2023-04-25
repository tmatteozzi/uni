public class Main {
    public static void main(String[] args) {
        FigurasGeometricas cuadrado = new Cuadrado();
        FigurasGeometricas triangulo = new Triangulo();
        FigurasGeometricas circulo = new Circulo();

        cuadrado.solicitudDatos();
        triangulo.solicitudDatos();
        circulo.solicitudDatos();

        cuadrado.calcularArea();
        triangulo.calcularArea();
        circulo.calcularArea();

        FigurasGeometricas vector[] = new FigurasGeometricas[3];
        vector[0] = cuadrado;
        vector[1] = triangulo;
        vector[2] = circulo;

        for (int i = 0; i < 3; i++) {
            System.out.println("El area de " + vector[i] + " es: " + vector[i].getArea());
        }
    }

}