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
                    System.out.println("INGRESE EL ELEMENTO A LOCALIZAR: ");
                    int elementoALocalizar = scanner.nextInt();
                    System.out.println("EL ELEMENTO SE ENCUENTRA EN LA POSICION: " + lista.localizar(elementoALocalizar));
                    break;
                case 2:
                    System.out.println("INGRESE EL ELEMENTO A INSERTAR: ");
                    int elementoAInsertarSinPosicion = scanner.nextInt();
                    lista.insertar(elementoAInsertarSinPosicion);
                    break;
                case 3:
                    System.out.println("INGRESE EL ELEMENTO A INSERTAR: ");
                    int elementoAInsertar = scanner.nextInt();
                    System.out.println("INGRESE LA POSICION DEL ELEMENTO A INSERTAR: ");
                    int posicionAInsertar = scanner.nextInt();
                    lista.insertarEnPosicion(posicionAInsertar, elementoAInsertar);
                    break;
                case 4:
                    System.out.println("INGRESE LA POSICION DEL ELEMENTO A ELIMINAR: ");
                    int elementoAEliminar = scanner.nextInt();
                    lista.eliminar(elementoAEliminar);
                    break;
                case 5:
                    System.out.println("LISTA DE ELEMENTOS ORDENADA: " + lista.ordenarElementos());
                    break;
                case 6:
                    System.out.println("LISTA DE ELEMENTOS COPIADA: " + lista.copiarLista());
                    break;
                case 7:
                    // Crear la primera lista con algunos elementos
                    Lista nuevaLista = new ListaConcreta("Lista1");
                    nuevaLista.insertar(10);
                    nuevaLista.insertar(20);
                    nuevaLista.insertar(30);

                    // Crear la segunda lista con otros elementos
                    Lista segundaLista = new ListaConcreta("Lista2");
                    segundaLista.insertar(40);
                    segundaLista.insertar(50);
                    segundaLista.insertar(60);
                    break;
                case 8:
                    System.out.println("Ingrese la posición para dividir la lista: ");
                    int posicionADividir = scanner.nextInt();
                    lista.dividirLista(posicionADividir);
                    break;
                case 9:
                    System.out.println(lista);
                    break;
                case 10:
                    System.out.println("DE VUELTA AL MENU PRINCIPAL");
                    break;
                default:
                    System.out.println("INGRESE UNA OPCION CORRECTA");
                    break;
            }
        } while (opcion != 10);
    }
}
