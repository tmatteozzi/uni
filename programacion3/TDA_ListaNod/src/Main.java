import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("INGRESE NOMBRE PARA LA LISTA: ");
        String nombreLista = scanner.next();
        Lista lista = new ListaConcreta(nombreLista);
        int opcion;
        do{
            System.out.println("MENU " + lista.getNombreLista().toUpperCase() + "\n" +
                    "1. Localizar un elemento\n" +
                    "2. Insertar un elemento\n" +
                    "3. Insertar un elemento en una posicion determinada\n" +
                    "4. Eliminar un elemento\n" +
                    "5. Ordenar los elementos\n" +
                    "6. Copiar la lista\n" +
                    "7. Unir varias listas\n" +
                    "8. Dividir una lista\n" +
                    "9. Imprimir\n" +
                    "10. Salir\n");
            opcion = scanner.nextInt();
            switch (opcion){
                case 1:
                    System.out.println("INGRESE EL CONTENIDO DEL NODO A LOCALIZAR: ");
                    int contenidoALocalizar = scanner.nextInt();
                    lista.localizar(contenidoALocalizar);
                    break;
                case 2:
                    System.out.println("INGRESE EL CONTENIDO DEL NODO A INSERTAR: ");
                    int contenidoNodo = scanner.nextInt();
                    Nodo nodoAInsertar = new Nodo(contenidoNodo);
                    lista.insertar(nodoAInsertar);
                    break;
                case 3:
                    System.out.println("INGRESE EL CONTENIDO DEL NODO A INSERTAR: ");
                    int contenidoNodoAInsertar = scanner.nextInt();
                    Nodo nodoAInsertarEnPosicion = new Nodo(contenidoNodoAInsertar);
                    System.out.println("INGRESE LA POSICION PARA INSERTAR EL NODO: ");
                    int posicionAInsertar = scanner.nextInt();
                    lista.insertarEnPosicion(posicionAInsertar, nodoAInsertarEnPosicion);
                    break;
                case 4:
                    System.out.println("INGRESE LA POSICION DEL NODO A ELIMINAR: ");
                    int posicionNodoAEliminar = scanner.nextInt();
                    lista.eliminar(posicionNodoAEliminar);
                    break;
                case 5:
                    System.out.println("LISTA DE ELEMENTOS ORDENADA: " + lista.ordenarElementos());
                    break;
                case 6:
                    System.out.println("LISTA DE ELEMENTOS COPIADA: " + lista.copiarLista());
                    break;
                case 7:
                    // Crear la primera lista con algunos elementos
                    Lista primeraLista = new ListaConcreta("Lista 1");
                    Nodo nodo1 = new Nodo(10);
                    primeraLista.insertar(nodo1);
                    Nodo nodo2 = new Nodo(20);
                    primeraLista.insertar(nodo2);
                    Nodo nodo3 = new Nodo(30);
                    primeraLista.insertar(nodo3);
                    System.out.println(primeraLista);
                    // Crear la segunda lista con otros elementos
                    Lista segundaLista = new ListaConcreta("Lista 2");
                    Nodo nodo4 = new Nodo(40);
                    segundaLista.insertar(nodo4);
                    Nodo nodo5 = new Nodo(50);
                    segundaLista.insertar(nodo5);
                    Nodo nodo6 = new Nodo(60);
                    segundaLista.insertar(nodo6);
                    System.out.println(segundaLista);
                    // Implementar metodo
                    unirListas(primeraLista, segundaLista);
                    break;
                case 8:
                    System.out.println("INGRESE LA POSICION PARA DIVIDIR LA LISTA: ");
                    int posicionADividir = scanner.nextInt();
                    lista.dividirLista(posicionADividir);
                    break;
                case 9:
                    System.out.println(lista);
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

    public static void unirListas(Lista lista1, Lista lista2) {
        try {
            if (lista1 != null && lista2 != null) {
                ListaConcreta listaUnida = new ListaConcreta("Union");
                // Iterar a través de la primera lista y copiar los nodos a la lista unida
                Nodo current1 = lista1.getPrimero();
                while (current1 != null) {
                    listaUnida.insertar(new Nodo(current1.getContenido()));
                    current1 = current1.getSiguiente();
                }
                // Iterar a través de la segunda lista y copiar los nodos a la lista unida
                Nodo current2 = lista2.getPrimero();
                while (current2 != null) {
                    listaUnida.insertar(new Nodo(current2.getContenido()));
                    current2 = current2.getSiguiente();
                }
                System.out.println("Listas unidas: " + listaUnida);
            } else {
                throw new Exception("UNA DE LAS LISTAS ESTÁ VACÍA. NO SE REALIZA LA UNION.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
