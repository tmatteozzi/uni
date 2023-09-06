import java.util.Scanner;
import java.util.Stack;

public class Main {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        Pila pE = null, pD = null; // Creacion de conjuntos en null para poder usarlo en todos los cases
        int opcionInicial; // Declaracion de la variable afuera del loop para usarla como opcion adentro
        boolean creado1 = false;
        boolean creado2 = false;
        // Menu
        do{
            System.out.println("Ingrese una opcion: \n" +
                    "1. Pila Estatica \n" +
                    "2. Pila Dinamica \n" +
                    "3. Otras Operaciones \n" +
                    "4. Salir");
            opcionInicial = scanner.nextInt();
            switch (opcionInicial){
                case 1:
                    int opcionEstatica;
                    do{
                        System.out.println("MENU PILA \n" +
                            "Ingrese una opcion: \n" +
                            "1. Crear pila \n" +
                            "2. Apilar elemento \n" +
                            "3. Desapilar elemento \n" +
                            "4. Tope de pila \n" +
                            "5. Chequear si la pila esta vacia \n" +
                            "6. Chequear si la pila esta llena \n" +
                            "7. Imprimir \n" +
                            "8. Salir");
                        opcionEstatica = scanner.nextInt();
                        switch (opcionEstatica){
                            case 1:
                                if(!creado1){
                                    // Creacion pila estatica
                                    System.out.println("Ingrese el nombre de la pila estatica: ");
                                    String nombrePE = scanner.next();
                                    System.out.println("Ingrese el size de la pila estatica: ");
                                    int sizePE = scanner.nextInt();
                                    pE = new PilaEstatica(nombrePE, sizePE);
                                    creado1 = true;
                                } else {
                                    System.out.println("PILA " + pE.getNombrePila().toUpperCase() +" YA CREADA.");
                                }
                                break;
                            case 2:
                                System.out.println("INGRESE ELEMENTO A APILAR: ");
                                int elementoApilarPE = scanner.nextInt();
                                pE.apilarElemento(elementoApilarPE);
                                break;
                            case 3:
                                int desapilado = pE.desapilarElemento();
                                if (desapilado != 0){
                                    System.out.println("EL ELEMENTO " + desapilado + " HA SIDO DESAPILADO");
                                }
                                break;
                            case 4:
                                System.out.println("EL ELEMENTO TOPE ES: " + pE.topePila());
                                break;
                            case 5:
                                if(pE.pilaVacia()){
                                    System.out.println("PILA VACIA.");
                                    break;
                                } else {
                                    System.out.println("PILA NO VACIA.");
                                    break;
                                }
                            case 6:
                                if(((PilaEstatica) pE).pilaLlena()){
                                    System.out.println("PILA LLENA.");
                                    break;
                                } else {
                                    System.out.println("PILA CON ESPACIO.");
                                    break;
                                }
                            case 7:
                                System.out.println(pE);
                                break;
                            case 8:
                                System.out.println("DE VUELTA AL MENU PRINICPAL.");
                                break;
                            default:
                                System.out.println("OPCION NO VALIDA.");
                                break;
                        }
                    } while (opcionEstatica != 8);
                    break;
                case 2:
                    int opcionDinamica;
                    do{
                        System.out.println("MENU PILA \n" +
                                "Ingrese una opcion: \n" +
                                "1. Crear pila \n" +
                                "2. Apilar elemento \n" +
                                "3. Desapilar elemento \n" +
                                "4. Tope de pila \n" +
                                "5. Chequear si la pila esta vacia \n" +
                                "6. Imprimir \n" +
                                "7. Salir");
                        opcionDinamica = scanner.nextInt();
                        switch (opcionDinamica){
                            case 1:
                                if(!creado2){
                                    // Creacion pila dinamica
                                    System.out.println("Ingrese el nombre de la pila dinamica: ");
                                    String nombrePD = scanner.next();
                                    pD = new PilaDinamica(nombrePD);
                                    creado2 = true;
                                } else {
                                    System.out.println("PILA " + pD.getNombrePila().toUpperCase() +" YA CREADA.");
                                }
                                break;
                            case 2:
                                System.out.println("INGRESE ELEMENTO A APILAR: ");
                                int elementoApilarPD = scanner.nextInt();
                                pD.apilarElemento(elementoApilarPD);
                                break;
                            case 3:
                                int desapilado = pD.desapilarElemento();
                                if (desapilado != 0){
                                    System.out.println("EL ELEMENTO " + desapilado + " HA SIDO DESAPILADO");
                                }
                                break;
                            case 4:
                                System.out.println("EL ELEMENTO TOPE ES: " + pD.topePila());
                                break;
                            case 5:
                                if(pD.pilaVacia()){
                                    System.out.println("PILA VACIA.");
                                    break;
                                } else {
                                    System.out.println("PILA NO VACIA.");
                                    break;
                                }
                            case 6:
                                System.out.println(pD);
                                break;
                            case 7:
                                System.out.println("DE VUELTA AL MENU PRINICPAL.");
                                break;
                            default:
                                System.out.println("OPCION NO VALIDA.");
                                break;
                        }
                    } while (opcionDinamica != 7);
                    break;
                case 3:
                    int opcionOtrasOperaciones;
                    do{
                        System.out.println("MENU OTRAS OPERACIONES\n" +
                                "Ingrese una opcion: \n" +
                                "1. Identificar de que pila es segun el elemento\n" +
                                "2. Imprimir todos los elementos y sus posiciones\n" +
                                "3. Salir");
                        opcionOtrasOperaciones = scanner.nextInt();
                        switch (opcionOtrasOperaciones){
                            case 1:
                                System.out.println("INGRESE ELEMENTO A BUSCAR: ");
                                int elementoABucar = scanner.nextInt();
                                identificarPila(pE, pD, elementoABucar);
                                break;
                            case 2:
                                imprimirElementos(pE, pD);
                                break;
                            case 3:
                                System.out.println("DE VUELTA AL MENU PRINICPAL.");
                                break;
                            default:
                                System.out.println("OPCION NO VALIDA.");
                                break;
                        }
                    } while (opcionOtrasOperaciones != 3);
                    break;
                case 4:
                    System.out.println("FIN DEL PROGRAMA.");
                    break;
                default:
                    System.out.println("OPCION NO VALIDA.");
                    break;
            }
        } while (opcionInicial != 4);
    }

    public static void identificarPila(Pila pE, Pila pD, int elementoBusqueda){
        Stack<Integer> pilaTempEstatica = new Stack<>();
        Stack<Integer> pilaTempDinamica = new Stack<>();

        boolean encontradoEstatico = false;
        boolean encontradoDinamico = false;

        // PARTE ESTATICA
        while (!pE.pilaVacia()) {
            int elementoEstatica = pE.desapilarElemento();
            pilaTempEstatica.push(elementoEstatica);
            // Busqueda de elemento
            if (elementoEstatica == elementoBusqueda) {
                encontradoEstatico = true;
            }
        }
        while (!pilaTempEstatica.empty()) {
            pE.apilarElemento(pilaTempEstatica.pop()); // Volver a apilar en la normal
        }

        // PARTE DINAMICA
        while (!pD.pilaVacia()) {
            int elementoDinamica = pD.desapilarElemento();
            pilaTempDinamica.push(elementoDinamica);
            // Busqueda de elemento
            if (elementoDinamica == elementoBusqueda) {
                encontradoDinamico = true;
            }
        }
        while (!pilaTempDinamica.empty()) {
            pD.apilarElemento(pilaTempDinamica.pop()); // Volver a apilar en la normal
        }

        // RESULTADOS
        if (encontradoEstatico && encontradoDinamico) {
            System.out.println("EL ELEMENTO " + elementoBusqueda + " ESTA EN AMBAS PILAS.");
        } else if (encontradoEstatico) {
            System.out.println("EL ELEMENTO " + elementoBusqueda + " ESTA EN LA PILA ESTATICA.");
        } else if (encontradoDinamico) {
            System.out.println("EL ELEMENTO " + elementoBusqueda + " ESTA EN LA PILA DINAMICA.");
        } else {
            System.out.println("EL ELEMENTO " + elementoBusqueda + " NO ESTA EN NINGUNA PILA.");
        }
    }

    public static void imprimirElementos(Pila pE, Pila pD){
        Stack<Integer> pilaTempEstatica = new Stack<>();
        Stack<Integer> pilaTempDinamica = new Stack<>();

        int contadorEstatico = 0;
        int contadorDinamico = 0;

        // PARTE ESTATICA
        System.out.println("LISTA PILA ESTATICA");
        while(!pE.pilaVacia()){ // Vaciar la pila y cargar a la temporal
            pilaTempEstatica.push(pE.desapilarElemento());
        }
        while (!pilaTempEstatica.empty()){ // Volver a cargar a la pila pero con un contador para ubicar la posicion
            int elementoEstatica = pilaTempEstatica.pop();
            pE.apilarElemento(elementoEstatica);
            contadorEstatico++;
            System.out.println("ELEMENTO: " + elementoEstatica + " , ORDEN: " + contadorEstatico);
        }

        // PARTE DINAMICA
        while(!pD.pilaVacia()){
            pilaTempDinamica.push(pD.desapilarElemento()); // Vaciar la pila y cargar a la temporal
        }
        System.out.println("LISTA PILA DINAMICA");
        while (!pilaTempDinamica.empty()){ // Volver a cargar a la pila pero con un contador para ubicar la posicion
            int elementoDinamica = pilaTempDinamica.pop();
            pD.apilarElemento(elementoDinamica);
            contadorDinamico++;
            System.out.println("ELEMENTO: " + elementoDinamica + " , ORDEN: " + contadorDinamico);
        }
    }
}

