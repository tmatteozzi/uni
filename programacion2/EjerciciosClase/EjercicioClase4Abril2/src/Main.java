public class Main {
    public static void main(String [] args) {
        Empleado e1 = new Empleado("Tomas M", "cedula", 19, true, 1500);
        Programador p1 = new Programador("Matias M", "cedula", 25, true, 2000, 2500, "Ingles");
        // Funciones
        System.out.println("La clasificacion de " + e1 + " es: " + e1.clasificarSegunEdad(e1.getEdad()));
        System.out.println("La clasificacion de " + p1 + " es: " + p1.clasificarSegunEdad(p1.getEdad()));

        e1.imprimirDatosEmpleado();
        p1.imprimirDatosEmpleado();

        System.out.println("El salario es de: " + e1.getSalario() + ", el salario aumentado un 4%: " + e1.aumentarSalario(0.04));
    }
}
