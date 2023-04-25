import java.util.Scanner;
import java.lang.Math;
public class Circulo extends FigurasGeometricas{
    public Circulo() {
        super();
    }

    @Override
    public void solicitudDatos() {
        Scanner lectura= new Scanner(System.in);
        System.out.println("ingrese radio: ");
        radio = lectura.nextInt();
    }
    @Override
    public void calcularArea() {
        super.area= super.radio*radio* Math.PI;
    }
}
