import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese una opcion: \n" +
                "1. Pila Estatica \n" +
                "2. Pila Dinamica \n" +
                "3. Otras Operaciones \n" +
                "4. Salir");
        int opcionInicial = scanner.nextInt();
        do{
            switch (opcionInicial){
                case 1:
                    System.out.println("Ingrese el nombre de la pila estatica: ");
                    String nombrePE = scanner.next();
                    System.out.println("Ingrese el size de la pila estatica: ");
                    int sizePE = scanner.nextInt();
                    PilaEstatica pE = new PilaEstatica(nombrePE, sizePE);

                    System.out.println("Ingrese una opcion: \n" +
                            "1. Apilar elemento \n" +
                            "2. Desapilar elemento \n" +
                            "3. Tope de pila \n" +
                            "4. Chequear si la pila esta vacia \n" +
                            "5. Chequear si la pila esta llena \n" +
                            "6. Salir");
                    int opcionEstatica = scanner.nextInt();
                    do{
                        switch (opcionEstatica){
                            case 1:
                                System.out.println("INGRESE ELEMENTO A APILAR: ");
                                int elementoApilarPE = scanner.nextInt();
                                pE.apilarElemento(elementoApilarPE);
                                break;
                            case 2:
                                System.out.println("INGRESE ELEMENTO A DESAPILAR: ");
                                int elementoDesapilarPE = scanner.nextInt();
                                pE.apilarElemento(elementoDesapilarPE);
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
                                if(pE.pilaLlena()){
                                    System.out.println("PILA LLENA.");
                                    break;
                                } else {
                                    System.out.println("PILA CON ESPACIO.");
                                    break;
                                }
                            case 6:
                                System.out.print("DE VUELTA AL MENU PRINICPAL.");
                                break;
                            default:
                                System.out.println("OPCION NO VALIDA.");
                                break;
                        }
                    } while (opcionEstatica != 6);
                    break;
                case 2:
                    System.out.println("Ingrese el nombre de la pila dinamica: ");
                    String nombrePD = scanner.next();
                    System.out.println("Ingrese el size de la pila estatica: ");
                    int sizePD = scanner.nextInt();
                    PilaEstatica pD = new PilaEstatica(nombrePD, sizePD);

                    System.out.println("Ingrese una opcion: \n" +
                            "1. Apilar elemento \n" +
                            "2. Desapilar elemento \n" +
                            "3. Tope de pila \n" +
                            "4. Chequear si la pila esta vacia \n" +
                            "5. Salir");
                    int opcionDinamica = scanner.nextInt();
                    do{
                        switch (opcionDinamica){
                            case 1:
                                System.out.println("INGRESE ELEMENTO A APILAR: ");
                                int elementoApilarPD = scanner.nextInt();
                                pD.apilarElemento(elementoApilarPD);
                                break;
                            case 2:
                                System.out.println("INGRESE ELEMENTO A DESAPILAR: ");
                                int elementoDesapilarPD = scanner.nextInt();
                                pD.apilarElemento(elementoDesapilarPD);
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
                                System.out.print("DE VUELTA AL MENU PRINICPAL.");
                                break;
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
