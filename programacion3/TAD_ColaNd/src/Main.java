/***
Desarrollar un Producto de Software que cumpla con las siguientes características.
    -Diseñar y Construir en Java un TAD de nombre COLAND. Implementar con un TAD tipo Nodo.
    -Que cumpla con las buenas prácticas de documentación de código.
    -Que presente un menú de trabajo tipo gráfico de barras.
    -El TAD se basará en las especificaciones para colas presentes en la lámina  8 de esta presentación.
    -Probar y verificar que la implementación se encuentra trabajando correctamente.
*/

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // INICIALIZACION DE SCANNER
        Scanner scanner = new Scanner(System.in);
        // CREACION DE COLA
        Cola cola = null;
        // VALIDADOR
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
                case 1: // CREAR COLA
                    if(!creado){
                        System.out.println("INGRESE NOMBRE PARA LA COLA: ");
                        String nombreCola = scanner.next();
                        cola = new ColaConcreta(nombreCola);
                        creado = true;
                    } else {
                        System.out.println("YA ESTA CREADA LA COLA " + cola.getNombreCola().toUpperCase() + ".");
                    }
                    break;
                case 2: // CHEQUEAR SI COLA ESTA VACIA
                    if(cola.esVacio()){
                        System.out.println("LA LISTA ESTA VACIA");
                    } else {
                        System.out.println("LA LISTA NO ESTA VACIA");
                    }
                    break;
                case 3: // VACIAR COLA
                    cola.vaciar();
                    break;
                case 4: // CHEQUEAR EL LARGO DE LA COLA
                    System.out.println("EL LARGO DE LA COLA ES DE: " + cola.largo());
                    break;
                case 5: // VER EL PRIMER ELEMENTO DE LA COLA
                    if(cola.verPrimero() != null){
                        System.out.println("EL PRIMER ELEMENTO DE LA COLA ES: " + cola.verPrimero());
                    }
                    break;
                case 6: // VER EL ULTIMO ELEMENTO DE LA COLA
                    if(cola.verPrimero() != null) {
                        System.out.println("EL ULTIMO ELEMENTO DE LA COLA ES: " + cola.verUltimo());
                    }
                    break;
                case 7: // ENFILAR ELEMENTO
                    System.out.println("INGRESE EL CONTENIDO DEL NODO A ENFILAR: ");
                    int contenido = scanner.nextInt();
                    Nodo nodoAEnfilar = new Nodo(contenido);
                    cola.enfilar(nodoAEnfilar);
                    break;
                case 8: // SACAR UN ELEMENTO
                    cola.sacar();
                    break;
                case 9: // IMPRIMIR
                    System.out.println(cola);
                    break;
                case 10:
                    // FINALIZAR
                    System.out.println("FIN DEL PROGRAMA");
                    break;
                default:
                    System.out.println("INGRESE UNA OPCION CORRECTA");
                    break;
            }
        } while (opcion != 10);
    }
}
