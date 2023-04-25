public abstract class FigurasGeometricas {

    int ladoA;
    int ladoB;
    int radio;
    double area;

    public FigurasGeometricas() {
        this.ladoA = ladoA;
        this.ladoB = ladoB;
        this.radio = radio;
        this.area = area;
    }
    public abstract void solicitudDatos();
    public abstract void calcularArea();

    public double getArea() {
        return area;
    }
}
