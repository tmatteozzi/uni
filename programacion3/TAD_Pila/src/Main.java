import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        int opcionInicial;
        do{
            System.out.println("Ingrese una opcion: \n" +
                    "1. Pila Estatica \n" +
                    "2. Pila Dinamica \n" +
                    "3. Otras Operaciones \n" +
                    "4. Salir");
            opcionInicial = scanner.nextInt();
            switch (opcionInicial){
                case 1:
                    System.out.println("Ingrese el nombre de la pila estatica: ");
                    String nombrePE = scanner.next();
                    System.out.println("Ingrese el size de la pila estatica: ");
                    int sizePE = scanner.nextInt();
                    Pila pE = new PilaEstatica(nombrePE, sizePE);
                    int opcionEstatica;

                    do{
                        System.out.println("Ingrese una opcion: \n" +
                            "1. Apilar elemento \n" +
                            "2. Desapilar elemento \n" +
                            "3. Tope de pila \n" +
                            "4. Chequear si la pila esta vacia \n" +
                            "5. Chequear si la pila esta llena \n" +
                            "6. Salir");
                        opcionEstatica = scanner.nextInt();
                        switch (opcionEstatica){
                            case 1:
                                System.out.println("INGRESE ELEMENTO A APILAR: ");
                                int elementoApilarPE = scanner.nextInt();
                                pE.apilarElemento(elementoApilarPE);
                                break;
                            case 2:
                                pE.desapilarElemento();
                                break;
                            case 3:
                                System.out.println("EL ELEMENTO TOPE ES: " + pE.topePila());
                                break;
                            case 4:
                                if(pE.pilaVacia()){
                                    System.out.println("PILA VACIA.");
                                    break;
                                } else {
                                    System.out.println("PILA NO VACIA.");
                                    break;
                                }
                            case 5:
                                if(((PilaEstatica) pE).pilaLlena()){
                                    System.out.println("PILA LLENA.");
                                    break;
                                } else {
                                    System.out.println("PILA CON ESPACIO.");
                                    break;
                                }
                            case 6:
                                System.out.println("DE VUELTA AL MENU PRINICPAL.");
                                break;
                            case 7:
                                System.out.println(pE);
                            default:
                                System.out.println("OPCION NO VALIDA.");
                                break;
                        }
                    } while (opcionEstatica != 6);
                    break;
                case 2:
                    System.out.println("Ingrese el nombre de la pila dinamica: ");
                    String nombrePD = scanner.next();
                    Pila pD = new PilaDinamica(nombrePD);
                    int opcionDinamica;

                    do{
                        System.out.println("Ingrese una opcion: \n" +
                                "1. Apilar elemento \n" +
                                "2. Desapilar elemento \n" +
                                "3. Tope de pila \n" +
                                "4. Chequear si la pila esta vacia \n" +
                                "5. Salir");
                        opcionDinamica = scanner.nextInt();
                        switch (opcionDinamica){
                            case 1:
                                System.out.println("INGRESE ELEMENTO A APILAR: ");
                                int elementoApilarPD = scanner.nextInt();
                                pD.apilarElemento(elementoApilarPD);
                                break;
                            case 2:
                                pD.desapilarElemento();
                                break;
                            case 3:
                                System.out.println("EL ELEMENTO TOPE ES: " + pD.topePila());
                                break;
                            case 4:
                                if(pD.pilaVacia()){
                                    System.out.println("PILA VACIA.");
                                    break;
                                } else {
                                    System.out.println("PILA NO VACIA.");
                                    break;
                                }
                            case 5:
                                System.out.println("DE VUELTA AL MENU PRINICPAL.");
                                break;
                            case 7:
                                System.out.println(pD);
                            default:
                                System.out.println("OPCION NO VALIDA.");
                                break;
                        }
                    } while (opcionDinamica != 5);
                    break;
            }
        } while (opcionInicial != 4);
    }
}
