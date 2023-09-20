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
                        System.out.println("ARBOL " + arbol.getNombreArbol().toUpperCase() + " YA CREADO.");
                    }
                    break;
                case 2:
                    arbol.destruir();
                    break;
                case 3:
                    // INPUT
                    System.out.println("INGRESE EL NODO PARA BUSCAR AL PADRE:");
                    int contenidoNodoABuscarPadre = scanner.nextInt();
                    // BUSCAR AL PADRE
                    Nodo padre = arbol.padre(arbol.obtenerNodoPorContenido(contenidoNodoABuscarPadre));
                    // RESULTADO
                    if (padre != null){
                        System.out.println(padre);
                    } else {
                        System.out.println("EL NODO NO TIENE PADRE.");
                    }
                    break;
                case 4:
                    // INPUT
                    System.out.println("INGRESE EL NODO PARA BUSCAR AL HIJO DE LA IZQUIERDA:");
                    int contenidoNodoABuscarIzquierda = scanner.nextInt();
                    // BUSCAR HIJO DE LA IZQUIERDA
                    Nodo padreDelIzquierdo = arbol.obtenerNodoPorContenido(contenidoNodoABuscarIzquierda);
                    Nodo izquierdo = arbol.hijoIzquierdo(padreDelIzquierdo);
                    // RESULTADO
                    if (izquierdo != null){
                        System.out.println("EL HIJO IZQUIERDO DEL NODO " + padreDelIzquierdo + " ES " + izquierdo);
                    } else {
                        System.out.println("EL NODO NO TIENE HIJO IZQUIERDO.");
                    }
                    break;
                case 5:
                    // INPUT
                    System.out.println("INGRESE EL NODO PARA BUSCAR AL HIJO DE LA IZQUIERDA:");
                    int contenidoNodoABuscarDerecha = scanner.nextInt();
                    // BUSCAR HIJO DE LA DERECHA
                    Nodo padreDelDerecho = arbol.obtenerNodoPorContenido(contenidoNodoABuscarDerecha);
                    Nodo derecho = arbol.hijoDerecho(padreDelDerecho);
                    // RESULTADO
                    if (derecho != null){
                        System.out.println("EL HIJO DERECHO DEL NODO " + padreDelDerecho + " ES " + derecho);
                    } else {
                        System.out.println("EL NODO NO TIENE HIJO DERECHO.");
                    }
                    break;
                case 6:
                    if (arbol != null) {
                        System.out.println("LA RAIZ DEL ARBOL ES: " + arbol.raiz());
                    } else {
                        System.out.println("NO HAY UN ARBOL CREADO.");
                    }
                    break;
                case 7:
                    // SI LA RAIZ NO EXISTE SE AGREGA EL NODO COMO RAIZ
                    if(arbol.raiz() == null){
                        System.out.println("INGRESAR EL VALOR DEL PRIMER NODO:");
                        int valorNodoAIzquierda = scanner.nextInt();
                        Nodo nodoAIzquierda = new Nodo(valorNodoAIzquierda);
                        arbol.insertarHijoIzquierda(null, nodoAIzquierda);
                        System.out.println("PRIMER NODO INSERTADO. EL NODO " + nodoAIzquierda + " ES LA RAIZ.");
                    }
                    // PERO SI EXISTE SE AGREGA A LA IZQUIERDA
                    else {
                        System.out.println("INGRESE EL CONTENIDO DEL NODO PADRE:");
                        int contenidoPadreParaInsertarIzquierda = scanner.nextInt();
                        Nodo padreParaInsertarIzquierda = arbol.obtenerNodoPorContenido(contenidoPadreParaInsertarIzquierda);
                        if (padreParaInsertarIzquierda != null) {
                            System.out.println("INGRESAR EL VALOR DEL NODO PARA AGREGAR A LA IZQUIERDA:");
                            int valorNodoAIzquierda = scanner.nextInt();
                            Nodo nodoAIzquierda = new Nodo(valorNodoAIzquierda);
                            arbol.insertarHijoIzquierda(padreParaInsertarIzquierda, nodoAIzquierda);
                            System.out.println("NODO " + nodoAIzquierda + " INSERTADO COMO HIJO IZQUIERDO DE " + padreParaInsertarIzquierda);
                        } else {
                            System.out.println("EL PADRE NO EXISTE, NO SE PUEDE INSERTAR.");
                        }
                    }
                    break;
                case 8:
                    System.out.println("INGRESE EL CONTENIDO DEL NODO PADRE:");
                    int contenidoPadreParaInsertarDerecha = scanner.nextInt();
                    Nodo padreParaInsertarDerecha = arbol.obtenerNodoPorContenido(contenidoPadreParaInsertarDerecha);
                    if(padreParaInsertarDerecha != null){
                        System.out.println("INGRESAR EL VALOR DEL NODO PARA AGREGAR A LA DERECHA:");
                        int valorNodoADerecha = scanner.nextInt();
                        Nodo nodoADerecha = new Nodo(valorNodoADerecha);
                        arbol.insertarHijoDerecha(padreParaInsertarDerecha, nodoADerecha);
                        System.out.println("NODO " + nodoADerecha + " INSERTADO COMO HIJO IZQUIERDO DE " + padreParaInsertarDerecha);
                    } else {
                        System.out.println("EL PADRE NO EXISTE, NO SE PUEDE INSERTAR.");
                    }
                    break;
                case 9:
                    System.out.println("INGRESE EL CONTENIDO DEL NODO PADRE:");
                    int contenidoPadreParaPodarIzquierda = scanner.nextInt();
                    Nodo padreParaPodarIzquierda = arbol.obtenerNodoPorContenido(contenidoPadreParaPodarIzquierda);
                    arbol.podarHijoIzquierda(padreParaPodarIzquierda);
                    break;
                case 10:
                    System.out.println("INGRESE EL CONTENIDO DEL NODO PADRE:");
                    int contenidoPadreParaPodarDerecha = scanner.nextInt();
                    Nodo padreParaPodarDerecha = arbol.obtenerNodoPorContenido(contenidoPadreParaPodarDerecha);
                    arbol.podarHijoDerecha(padreParaPodarDerecha);
                    break;
                case 11:
                    // IMPRIMIR EL ARBOL
                    if (arbol != null) {
                        System.out.println(arbol);
                    } else {
                        System.out.println("NO HAY UN ARBOL CREADO.");
                    }
                    break;
                case 12:
                    System.out.println("FIN DEL PROGRAMA.");
                    break;
                default:
                    System.out.println("OPCION INVALIDA.");
                    break;
            }
        } while (opcion != 12);
    }

    public static void imprimirMenu(){
        System.out.println("MENU: \n" +
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
