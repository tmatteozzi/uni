import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion;
        boolean creado = false;
        ArbolBinario arbol = null;
        do{
            imprimirMenu();
            opcion = scanner.nextInt();
            switch (opcion){
                case 1:
                    if(!creado){
                        // INPUT DATOS
                        System.out.println("INGRESE EL NOMBRE DEL ARBOL:");
                        String nombre = scanner.next();
                        arbol = new ArbolBinarioConcreto(nombre);
                        creado = true;
                    } else {
                        System.out.println("ARBOL YA CREADO.");
                    }
                    break;
                case 2:
                    arbol.destruir();
                    break;
                case 3:
                    System.out.println("INGRESE EL NODO PARA BUSCAR AL PADRE:");
                    int contenidoNodoABuscarPadre = scanner.nextInt();
                    Nodo padre = arbol.padre(((ArbolBinarioConcreto) arbol).obtenerNodoPorContenido(contenidoNodoABuscarPadre));
                    if (padre != null){
                        System.out.println(padre);
                    } else {
                        System.out.println("EL NODO NO TIENE PADRE.");
                    }
                    break;
                case 4:
                    System.out.println("INGRESE EL NODO PARA BUSCAR AL HIJO DE LA IZQUIERDA:");
                    int contenidoNodoABuscarIzquierda = scanner.nextInt();
                    Nodo izquierdo = arbol.hijoIzquierdo(((ArbolBinarioConcreto) arbol).obtenerNodoPorContenido(contenidoNodoABuscarIzquierda));
                    if (izquierdo != null){
                        System.out.println(izquierdo);
                    } else {
                        System.out.println("EL NODO NO TIENE HIJO IZQUIERDO.");
                    }
                    break;
                case 5:
                    System.out.println("INGRESE EL NODO PARA BUSCAR AL HIJO DE LA IZQUIERDA:");
                    int contenidoNodoABuscarDerecha = scanner.nextInt();
                    Nodo derecho = arbol.hijoDerecho(((ArbolBinarioConcreto) arbol).obtenerNodoPorContenido(contenidoNodoABuscarDerecha));
                    if (derecho != null){
                        System.out.println(derecho);
                    } else {
                        System.out.println("EL NODO NO TIENE HIJO DERECHO.");
                    }
                    break;
                case 6:
                    if (arbol != null) {
                        System.out.println("La raíz del árbol es: " + arbol.raiz().getContenido());
                    } else {
                        System.out.println("No hay un árbol creado.");
                    }
                    break;
                case 7:
                    if(arbol.raiz() == null){
                        System.out.println("INGRESAR EL VALOR DEL NODO PARA AGREGAR A LA IZQUIERDA");
                        int valorNodoAIzquierda = scanner.nextInt();
                        Nodo nodoAIzquierda = new Nodo(valorNodoAIzquierda);
                        arbol.insertarHijoIzquierda(null, nodoAIzquierda);
                    } else {
                        System.out.println("INGRESE EL CONTENIDO DEL NODO PADRE:");
                        int contenidoPadreParaInsertarIzquierda = scanner.nextInt();
                        Nodo padreParaInsertarIzquierda = ((ArbolBinarioConcreto) arbol).obtenerNodoPorContenido(contenidoPadreParaInsertarIzquierda);
                        if (padreParaInsertarIzquierda != null) {
                            System.out.println("INGRESAR EL VALOR DEL NODO PARA AGREGAR A LA IZQUIERDA");
                            int valorNodoAIzquierda = scanner.nextInt();
                            Nodo nodoAIzquierda = new Nodo(valorNodoAIzquierda);
                            arbol.insertarHijoIzquierda(padreParaInsertarIzquierda, nodoAIzquierda);
                        } else {
                            System.out.println("EL PADRE NO EXISTE, NO SE PUEDE INSERTAR");
                        }
                    }
                    break;
                case 8:
                    System.out.println("INGRESE EL CONTENIDO DEL NODO PADRE:");
                    int contenidoPadreParaInsertarDerecha = scanner.nextInt();
                    Nodo padreParaInsertarDerecha = ((ArbolBinarioConcreto) arbol).obtenerNodoPorContenido(contenidoPadreParaInsertarDerecha);
                    if(padreParaInsertarDerecha != null){
                        System.out.println("INGRESAR EL VALOR DEL NODO PARA AGREGAR A LA IZQUIERDA");
                        int valorNodoADerecha = scanner.nextInt();
                        Nodo nodoADerecha = new Nodo(valorNodoADerecha);
                        arbol.insertarHijoDerecha(padreParaInsertarDerecha, nodoADerecha);
                    } else {
                        System.out.println("EL PADRE NO EXISTE, NO SE PUEDE INSERTAR");
                    }
                    break;
                case 9:
                    break;
                case 10:
                    break;
                case 11:
                    // Imprimir árbol
                    if (arbol != null) {
                        System.out.println(arbol.toString());
                    } else {
                        System.out.println("No hay un árbol creado.");
                    }
                    break;
                case 12:
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, elige una opción del menú.");
                    break;

            }
        } while (opcion != 12);
    }

    public static void imprimirMenu(){
        System.out.println("MENU:\n" +
                "1. Crear arbol\n" +
                "2. Destruir arbol\n" +
                "3. Encontrar el padre de un nodo\n" +
                "4. Encontrar el hijo de la izquierda de un nodo\n" +
                "5. Encontrar el hijo de la derecha de un nodo\n" +
                "6. Mostrar la raiz del arbol\n" +
                "7. Insertar hijo a la izquierda de un nodo\n" +
                "8. Insertar hijo a la derecha de un nodo\n" +
                "9. Podar hijo de la izquierda de un nodo\n" +
                "10. Podar hijo de la derecha de un nodo\n" +
                "11. Imprimir arbol\n" +
                "12. Salir");
    }
}
