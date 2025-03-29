public class MainThread {
    public static void main(String[] args) {
        // CLASE THREAD
        Counter t1 = new Counter("Hilo 1 (Clase Thread)");
        Counter t2 = new Counter("Hilo 2 (Clase Thread)");

        t1.start();
        t2.start();
    }
}