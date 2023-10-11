import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Cola cola = null;
        boolean creado = false;
        int opcion;
        do{
            System.out.println("MENU \n" +
                    "1. Crear cola\n" +
                    "2. Chequear si esta vacia\n" +
                    "3. Vaciar cola\n" +
                    "4. Largo de la cola\n" +
                    "5. Ver primer elemento de la cola\n" +
                    "6. Ver ultimo elemento de la cola\n" +
                    "7. Enfilar elemento\n" +
                    "8. Sacar elemento\n" +
                    "9. Imprimir\n" +
                    "10. Salir\n");
            opcion = scanner.nextInt();
            switch (opcion){
                case 1:
                    if(!creado){
                        System.out.println("INGRESE NOMBRE PARA LA COLA: ");
                        String nombreCola = scanner.next();
                        cola = new ColaConcreta(nombreCola);
                        creado = true;
                    } else {
                        System.out.println("YA ESTA CREADA LA COLA " + cola.getNombreCola().toUpperCase() + ".");
                    }
                    break;
                case 2:
                    if(cola.esVacio()){
                        System.out.println("LA LISTA ESTA VACIA");
                    } else {
                        System.out.println("LA LISTA NO ESTA VACIA");
                    }
                    break;
                case 3:
                    cola.vaciar();
                    break;
                case 4:
                    System.out.println("EL LARGO DE LA COLA ES DE: " + cola.largo());
                    break;
                case 5:
                    if(cola.verPrimero() != null){
                        System.out.println("EL PRIMER ELEMENTO DE LA COLA ES: " + cola.verPrimero());
                    }
                    break;
                case 6:
                    if(cola.verPrimero() != null) {
                        System.out.println("EL ULTIMO ELEMENTO DE LA COLA ES: " + cola.verUltimo());
                    }
                    break;
                case 7:
                    System.out.println("INGRESE EL CONTENIDO DEL NODO A ENFILAR: ");
                    int contenido = scanner.nextInt();
                    Nodo nodoAEnfilar = new Nodo(contenido);
                    cola.enfilar(nodoAEnfilar);
                    break;
                case 8:
                    cola.sacar();
                    break;
                case 9:
                    System.out.println(cola);
                    break;
                case 10:
                    System.out.println("FIN DEL PROGRAMA");
                    break;
                default:
                    System.out.println("INGRESE UNA OPCION CORRECTA");
                    break;
            }
        } while (opcion != 10);
    }
}
