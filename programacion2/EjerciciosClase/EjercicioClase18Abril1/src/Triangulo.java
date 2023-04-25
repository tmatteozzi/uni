import java.util.Scanner;
public class Triangulo extends  FigurasGeometricas{

    public Triangulo() {
        super();
    }
    @Override
    public void solicitudDatos(){
        System.out.println("Ingresar datos del Triangulo:");
        Scanner lectura= new Scanner(System.in);
        System.out.println("ingrese lado A (base): ");
        ladoA = lectura.nextInt();

        System.out.println("ingrese ladoB (altura): ");
        ladoB = lectura.nextInt();
    }
    @Override
    public void calcularArea() {
        super.area= (super.ladoA*super.ladoB)/2;
    }

}