import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * PANEL QUE REPRESENTA VISUALEMENTE A UNA PERSONA EN LA FILA CON UN COLOR ALEATORIO
 */
public class PersonaPanel extends JPanel {
    private static final Random random = new Random();

    public PersonaPanel(int id) {
        setPreferredSize(new Dimension(100, 30));
        setBackground(generarColorAleatorio());
        add(new JLabel("Persona " + id));
    }

    /**
     * COLOR ALEATORIO PARA CADA PERSONA
     */
    private Color generarColorAleatorio() {
        float r = random.nextFloat();
        float g = random.nextFloat();
        float b = random.nextFloat();
        return new Color(r, g, b);
    }
}
