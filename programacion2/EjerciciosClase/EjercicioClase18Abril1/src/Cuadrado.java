import java.util.Scanner;
public class Cuadrado extends FigurasGeometricas{

    public Cuadrado() {
        super();
    }

    @Override
    public void solicitudDatos(){
        System.out.println("Ingresar datos del Cuadrado:");
        Scanner lectura= new Scanner(System.in);
        System.out.println("ingrese Lado A: ");
        ladoA = lectura.nextInt();

        System.out.println("ingrese Lado B: ");
        ladoB = lectura.nextInt();
    }
    @Override
    public void calcularArea() {
        super.area= super.ladoA*super.ladoB;
    }
}