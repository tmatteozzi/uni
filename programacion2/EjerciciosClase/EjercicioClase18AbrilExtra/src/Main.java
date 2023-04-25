public class Main {
    public static void main(String[] args) {
        Infante infante1 = new Infante("Tomas", 16, 45015317, 40, 1.60, 'f');
        Adulto adulto1 = new Adulto("Gerardo", 50,25087654 , 59, 1.73, 'h');

        System.out.println("Infante:");
        infante1.calcularIMC();
        infante1.comprobarSexo(infante1.getSexo());
        infante1.esMayorDeEdad();

        System.out.println("Adulto:");
        adulto1.calcularIMC();
        adulto1.comprobarSexo(adulto1.getSexo());
        adulto1.esMayorDeEdad();
    }
}