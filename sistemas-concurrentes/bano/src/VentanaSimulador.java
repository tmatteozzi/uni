import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

/**
 * VENTANA PRINCIPAL, CONCURRENCIA E INTERFAZ
 **/
public class VentanaSimulador extends JFrame {
    private static final int CUBICULOS = 3; // CUBICULOS DISPONIBLES
    private static final Semaphore semaforo = new Semaphore(CUBICULOS); // SEMAFORO PARA CONTROLAR ACCESO CONCURRENTE
    private final Queue<PersonaPanel> fila = new LinkedList<>(); // COLA ESPERA

    // ELEMENTOS INTERFAZ
    private final JPanel filaPanel = new JPanel();
    private final JPanel cubiculosPanel = new JPanel();
    private final JLabel[] cubiculosLabels = new JLabel[CUBICULOS];

    private int contadorPersonas = 0; // CONTADOR PERSONAS INGRESADAS

    public VentanaSimulador() {
        // CONFIGURACION DE LA VENTANA
        setTitle("Simulador de Baño Público");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 400);
        setLayout(new BorderLayout());
        JLabel titulo = new JLabel("Simulador de Baño Público", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(titulo, BorderLayout.NORTH);

        // PANEL FILA DE ESPERA
        filaPanel.setLayout(new BoxLayout(filaPanel, BoxLayout.Y_AXIS));
        filaPanel.setBorder(BorderFactory.createTitledBorder("Fila de Espera"));
        add(filaPanel, BorderLayout.WEST);

        // PANEL CUBICULOS
        cubiculosPanel.setLayout(new GridLayout(1, CUBICULOS, 10, 10));
        cubiculosPanel.setBorder(BorderFactory.createTitledBorder("Cubículos"));
        for (int i = 0; i < CUBICULOS; i++) {
            cubiculosLabels[i] = new JLabel("Libre", JLabel.CENTER);
            cubiculosLabels[i].setOpaque(true);
            cubiculosLabels[i].setBackground(Color.LIGHT_GRAY);
            cubiculosLabels[i].setPreferredSize(new Dimension(100, 100));
            cubiculosPanel.add(cubiculosLabels[i]);
        }
        add(cubiculosPanel, BorderLayout.CENTER);

        // HILO QUE SIMULA LLEGADAS DE PERSONAS
        new Thread(this::simularLlegadas).start();
    }

    /**
     * SIMULAR LLEGADAS A LA COLA DEL BAÑO
     **/
    private void simularLlegadas() {
        while (contadorPersonas < 15) {
            contadorPersonas++;
            int id = contadorPersonas;

            // AGREGAR PERSONA A LA FILA VISUAL
            SwingUtilities.invokeLater(() -> agregarPersonaAFila(id));

            // INICIAR HILO QUE REPRESENTE A LA PERSONA
            new Thread(() -> persona(id)).start();

            // TIEMPO ESPERA ENTRE LLEGADAS
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * AGREGAR VISUALMENTE UNA PERSONA A LA FILA
     */
    private void agregarPersonaAFila(int id) {
        PersonaPanel personaPanel = new PersonaPanel(id);
        fila.add(personaPanel);
        filaPanel.add(personaPanel);
        filaPanel.revalidate();
        filaPanel.repaint();
    }

    /**
     * SIMULAR COMPORTAMIENTO DE UNA PERSONA QUE ENTRA, USA EL BAÑO Y SE VA
     */
    private void persona(int id) {
        try {
            semaforo.acquire(); // ESPERAR TURNO PARA ENTRAR AL BAÑO

            // ENTRA AL CUBICULO Y ACTUALIZA INTERFAZ
            SwingUtilities.invokeLater(() -> {
                PersonaPanel panel = fila.poll();
                if (panel != null) filaPanel.remove(panel);
                ocuparCubiculo(id);
                filaPanel.revalidate();
                filaPanel.repaint();
            });

            // TIEMPO RANDOM DE USO DEL BAÑO
            Thread.sleep((long) (Math.random() * 4000 + 2000));

            // LIBERA CUBICULO Y SEMAFORO
            SwingUtilities.invokeLater(() -> liberarCubiculo(id));
            semaforo.release();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * MARCAR VISUALMENTE EL CUBICULO COMO OCUPADO
     */
    private void ocuparCubiculo(int idPersona) {
        for (int i = 0; i < CUBICULOS; i++) {
            if (cubiculosLabels[i].getText().equals("Libre")) {
                cubiculosLabels[i].setText("Ocupado por " + idPersona);
                cubiculosLabels[i].setBackground(Color.CYAN);
                break;
            }
        }
    }

    /**
     * LIBERAR EL CUBICULO OCUPADO POR LA PERSONA
     */
    private void liberarCubiculo(int idPersona) {
        for (int i = 0; i < CUBICULOS; i++) {
            if (cubiculosLabels[i].getText().contains(String.valueOf(idPersona))) {
                cubiculosLabels[i].setText("Libre");
                cubiculosLabels[i].setBackground(Color.LIGHT_GRAY);
                break;
            }
        }
    }
}