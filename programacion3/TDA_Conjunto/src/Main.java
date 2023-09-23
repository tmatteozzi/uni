import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        Conjunto c1 = null, c2 = null;
        int opcionInicial;
        // MENU
        do {
            System.out.println("Ingrese una opcion: \n" +
                "1. Metodos Conjunto 1 \n" +
                "2. Metodos Conjunto 2 \n" +
                "3. Metodos compartidos \n" +
                "4. Salir");
            opcionInicial = scanner.nextInt();
            switch (opcionInicial){
                case 1:
                    int opcionC1;
                    boolean creado1 = false;
                    do {
                        imprimirMenu();
                        opcionC1 = scanner.nextInt();
                        switch (opcionC1){
                            case 1:
                                if(!creado1){
                                    System.out.println("INGRESAR EL SIZE DEL CONJUNTO: ");
                                    int nConjunto1 = scanner.nextInt();
                                    c1 = new ConjuntoConcreto(nConjunto1);
                                    creado1 = true;
                                } else {
                                    System.out.println("CONJUNTO YA CREADO!");
                                }
                                break;
                            case 2:
                                System.out.println("INGRESAR ELEMENTO A AÑADIR: ");
                                int eA = scanner.nextInt();
                                c1.agregar(eA);
                                break;
                            case 3:
                                System.out.println("INGRESAR ELEMENTO A RETIRAR: ");
                                int eR = scanner.nextInt();
                                c1.retirar(eR);
                                break;
                            case 4:
                                System.out.println("INGRESAR ELEMENTO PARA CHEQUEAR PERTENENCIA: ");
                                int eP = scanner.nextInt();
                                if(c1.pertenece(eP)){
                                    System.out.println("EL ELEMENTO PERTENECE AL CONJUNTO");
                                }
                                break;
                            case 5:
                                if(c1.esVacio()){
                                System.out.println("CONJUNTO VACÍO");
                                }
                                break;
                            case 6:
                                c1.conjuntoVacio();
                                System.out.println("CONJUNTO VACIADO");
                                break;
                            case 7:
                                System.out.println(c1);
                                break;
                            case 8:
                                System.out.println("DE VUELTA AL MENU PRINCIPAL");
                                break;
                            default:
                                System.out.println("OPCION INVALIDA");
                                break;
                        }
                    } while (opcionC1 != 8);
                    break;
                case 2:
                    int opcionC2;
                    boolean creado2 = false;
                    do {
                        imprimirMenu();
                        opcionC2 = scanner.nextInt();
                        switch (opcionC2){
                            case 1:
                                if(!creado2){
                                    System.out.println("INGRESAR EL SIZE DEL CONJUNTO: ");
                                    int nConjunto2 = scanner.nextInt();
                                    c2 = new ConjuntoConcreto(nConjunto2);
                                    creado2 = true;
                                } else {
                                    System.out.println("CONJUNTO YA CREADO!");
                                }
                                break;
                            case 2:
                                System.out.println("INGRESAR ELEMENTO A AÑADIR: ");
                                int eA = scanner.nextInt();
                                c2.agregar(eA);
                                break;
                            case 3:
                                System.out.println("INGRESAR ELEMENTO A RETIRAR: ");
                                int eR = scanner.nextInt();
                                c2.retirar(eR);
                                break;
                            case 4:
                                System.out.println("INGRESAR ELEMENTO A CHEQUEAR PERTENENCIA: ");
                                int eP = scanner.nextInt();
                                if(c2.pertenece(eP)){
                                    System.out.println("EL ELEMENTO PERTENECE AL CONJUNTO");
                                }
                                break;
                            case 5:
                                if(c2.esVacio()){
                                    System.out.println("CONJUNTO VACÍO");
                                }
                                break;
                            case 6:
                                c2.conjuntoVacio();
                                System.out.println("CONJUNTO VACIADO");
                                break;
                            case 7:
                                System.out.println(c2);
                                break;
                            case 8:
                                System.out.println("DE VUELTA AL MENU PRINCIPAL");
                                break;
                            default:
                                System.out.println("OPCION INVALIDA");
                                break;
                        }
                    } while (opcionC2 != 8);
                    break;
                case 3:
                    int opcionC3;
                    do {
                        System.out.println("MENU OPERACIONES DE AMBOS CONJUNTOS \n" +
                                "Ingrese una opcion: \n" +
                                "1. Union \n" +
                                "2. Interseccion \n" +
                                "3. Inclusion \n" +
                                "4. Salir");
                        opcionC3 = scanner.nextInt();
                        switch (opcionC3){
                            case 1:
                                System.out.println("UNION AMBOS CONJUNTOS: " + union(c1, c2));
                                break;
                            case 2:
                                System.out.println("UNION AMBOS CONJUNTOS: " + interseccion(c1, c2));
                                break;
                            case 3:
                                if(inclusion(c1, c2)){
                                    System.out.println("INCLUSIÓN VERIFICADA");
                                } else {
                                    System.out.println("NO TODOS LOS ELEMENTOS ESTÁN INCLUIDOS");
                                }
                                break;
                            case 4:
                                System.out.println("DE VUELTA AL MENU PRINCIPAL");
                                break;
                            default:
                                System.out.println("OPCION INVALIDA");
                                break;
                        }
                    } while (opcionC3 != 4);
                    break;
                case 4:
                    System.out.println("FIN DEL PROGRAMA");
                    break;
                default:
                    System.out.println("OPCION INVALIDA");
                    break;
            }
        } while (opcionInicial != 4);
    }

    public static Conjunto union(Conjunto c1, Conjunto c2){
        ArrayList<Integer> elementosUnion = new ArrayList<>(); // ARRAYLIST TEMPORAL PARA GUARDAR ELEM
        // RECORRER AMBOS CONJUNTOS Y AGREGAR ELEMENTOS A LA LISTA TEMP (SIN QUE SE REPITAN)
        for (int i = 0; i < c1.getCont(); i++) {
            int elemento = c1.getConj()[i];
            if (elemento != 0 && !elementosUnion.contains(elemento)) {
                elementosUnion.add(elemento);
            }
        }
        for (int i = 0; i < c2.getCont(); i++) {
            int elemento = c2.getConj()[i];
            if (elemento != 0 && !elementosUnion.contains(elemento)) {
                elementosUnion.add(elemento);
            }
        }
        // NUEVO CONJUNTO CON LA INTERSECCION
        Conjunto conjUnion = new ConjuntoConcreto(elementosUnion.size());
        for(Integer elemento: elementosUnion){
            conjUnion.agregar(elemento);
        }
        return conjUnion;
    }

    public static Conjunto interseccion(Conjunto c1, Conjunto c2){
        ArrayList<Integer> elementosInterseccion = new ArrayList<>(); // ARRAYLIST TEMPORAL PARA GUARDAR ELEM
        for(int i=0; i < c1.getCont(); i++){
            if(c2.pertenece(c1.getConj()[i])){
                elementosInterseccion.add(c1.getConj()[i]);
            }
        }
        // NUEVO CONJUNTO CON LA INTERSECCION
        Conjunto conjInterseccion = new ConjuntoConcreto(elementosInterseccion.size());
        for(Integer elemento: elementosInterseccion){
            if(!conjInterseccion.pertenece(elemento)){ // CARGAR SIN DUPLICADOS
                conjInterseccion.agregar(elemento);
            }
        }
        return conjInterseccion;
    }

    public static boolean inclusion(Conjunto c1, Conjunto c2) {
        for (int i = 0; i < c1.getCont(); i++) {
            if (!c2.pertenece(c1.getConj()[i])) { // CHEQUEAR QUE TODOS LOS ELEMENTOS DE C1 ESTEN EN C2
                return false;
            }
        }
        return true;
    }

    public static void imprimirMenu(){
        System.out.println("MENU CONJUNTO \n" +
                "Ingrese una opcion: \n" +
                "1. Crear conjunto \n" +
                "2. Agregar elemento \n" +
                "3. Retirar elemento \n" +
                "4. Chequear si elemento pertenece \n" +
                "5. Chequear si esta vacio \n" +
                "6. Vaciar conjunto \n" +
                "7. Imprimir \n" +
                "8. Salir");
    }
}



