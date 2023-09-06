import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Cola cV = null, cL = null;
        boolean creado1 = false;
        boolean creado2 = false;
        // MENU
        int opcionPrincipal;
        do{
            System.out.println("MENU COLAS\n" +
                    "1. Cola Vector\n" +
                    "2. Cola LinkedList\n" +
                    "3. Salir\n");
            opcionPrincipal = scanner.nextInt();
            switch (opcionPrincipal){
                case 1:
                    int opcionVector;
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
                        opcionVector = scanner.nextInt();
                        switch (opcionVector){
                            case 1:
                                if(!creado1){
                                    // COLA VECTOR
                                    System.out.println("INGRESE EL NOMBRE PARA LA COLA VECTOR");
                                    String nombreCV = scanner.next();
                                    System.out.println("INGRESE EL SIZE PARA LA COLA VECTOR");
                                    int size = scanner.nextInt();
                                    cV = new ColaVector(nombreCV, size);
                                    creado1 = true;
                                } else {
                                    System.out.println("COLA " + cV.getNombreCola().toUpperCase() + " YA CREADA.");
                                }
                                break;
                            case 2:
                                if(cV.esVacio()){
                                    System.out.println("LA LISTA ESTA VACIA");
                                } else {
                                    System.out.println("LA LISTA NO ESTA VACIA");
                                }
                                break;
                            case 3:
                                cV.vaciar();
                                break;
                            case 4:
                                System.out.println("EL LARGO DE LA COLA ES DE: " + cV.largo());
                                break;
                            case 5:
                                System.out.println("EL PRIMER ELEMENTO DE LA COLA ES: " + cV.verPrimero());
                                break;
                            case 6:
                                System.out.println("EL ULTIMO ELEMENTO DE LA COLA ES: " + cV.verUltimo());
                                break;
                            case 7:
                                System.out.println("INGRESE EL ELEMENTO A ENFILAR: ");
                                int elementoAEnfilar = scanner.nextInt();
                                cV.enfilar(elementoAEnfilar);
                                break;
                            case 8:
                                cV.sacar();
                                break;
                            case 9:
                                System.out.println(cV);
                                break;
                            case 10:
                                System.out.println("DE VUELTA AL MENU PRINCIPAL");
                                break;
                            default:
                                System.out.println("INGRESE UNA OPCION CORRECTA");
                                break;
                        }
                    } while (opcionVector != 10);
                    break;
                case 2:
                    int opcionLinked;
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
                        opcionLinked = scanner.nextInt();
                        switch (opcionLinked){
                            case 1:
                                if(!creado2){
                                    // COLA LINKED LIST
                                    System.out.println("INGRESE EL NOMBRE PARA LA COLA LINKED");
                                    String nombreCL = scanner.next();
                                    cL = new ColaLinked(nombreCL);
                                    creado2 = true;
                                } else {
                                    System.out.println("COLA " + cL.getNombreCola().toUpperCase() + " YA CREADA.");
                                }
                                break;
                            case 2:
                                if(cL.esVacio()){
                                    System.out.println("LA LISTA ESTA VACIA");
                                } else {
                                    System.out.println("LA LISTA NO ESTA VACIA");
                                }
                                break;
                            case 3:
                                cL.vaciar();
                                break;
                            case 4:
                                System.out.println("EL LARGO DE LA COLA ES DE: " + cL.largo());
                                break;
                            case 5:
                                System.out.println("EL PRIMER ELEMENTO DE LA COLA ES: " + cL.verPrimero());
                                break;
                            case 6:
                                System.out.println("EL ULTIMO ELEMENTO DE LA COLA ES: " + cL.verUltimo());
                                break;
                            case 7:
                                System.out.println("INGRESE EL ELEMENTO A ENFILAR: ");
                                int elementoAEnfilar = scanner.nextInt();
                                cL.enfilar(elementoAEnfilar);
                                break;
                            case 8:
                                cL.sacar();
                                break;
                            case 9:
                                System.out.println(cL);
                                break;
                            case 10:
                                System.out.println("DE VUELTA AL MENU PRINCIPAL");
                                break;
                            default:
                                System.out.println("INGRESE UNA OPCION CORRECTA");
                                break;
                        }
                    } while (opcionLinked != 10);
                    break;
                case 3:
                    System.out.println("FIN DEL PROGRAMA");
                    break;
                default:
                    System.out.println("INGRESE UNA OPCION CORRECTA");
            }
        } while (opcionPrincipal != 3);
    }
}