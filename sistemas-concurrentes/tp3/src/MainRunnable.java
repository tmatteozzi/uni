public class MainRunnable {
    public static void main(String[] args) {
        // INTERFAZ RUNNABLE
        Thread t1 = new Thread(new CounterWithInterface("Hilo 1 (Interfaz Runnable)"));
        Thread t2 = new Thread(new CounterWithInterface("Hilo 2 (Interfaz Runnable)"));

        t1.start();
        t2.start();
    }
}