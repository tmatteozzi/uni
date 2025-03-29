class CounterWithInterface implements Runnable {
    private String name;

    public CounterWithInterface(String name) {
        this.name = name;
    }

    public void run() {
        for (int i = 1; i <= 5; i++) {
            System.out.println(name + ": " + i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(name + " interrumpido.");
            }
        }
        System.out.println(name + " finalizado.");
    }
}