public class Main {
    public static void main(String[] args) {
        Profesor profe = new Profesor("Tomas",20,024121,"AM III");
        profe.mostrarDatos();

        ProfesorTitular profeTitular = new ProfesorTitular("Matias",20,012354,"AM III", "Aula 200");
        profeTitular.mostrarDatos();

        ProfesorInterino profeInterino = new ProfesorInterino("Franco",23,073472,"AM III", "Aula 205");
        profeInterino.mostrarDatos();
    }
}