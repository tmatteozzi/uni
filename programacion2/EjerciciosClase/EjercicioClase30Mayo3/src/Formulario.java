import javax.swing.*;
import java.awt.event.*;

public class Formulario extends JFrame implements ActionListener, ItemListener {
    private JTextField textField1;
    private JLabel label1, label2;
    private JComboBox<String> combo1;
    private JButton button1;
    private String paisSeleccionado;

    public Formulario(){
        setLayout(null);
        // LABEL NOMBRE PERSONA
        label1 = new JLabel("Nombre: ");
        label1.setBounds(10,10,100,30);
        add(label1);
        // TEXTFIELD NOMBRE PERSONA
        textField1 = new JTextField();
        textField1.setBounds(120,10,150,20);
        add(textField1);
        // COMBOBOX
        combo1 = new JComboBox<>();
        combo1.setBounds(10,50,150,20);
        add(combo1);
        cargarCombo();
        combo1.addItemListener(this);

        // BOTON
        button1 = new JButton("Aceptar");
        button1.setBounds(150,80,100,30);
        add(button1);
        button1.addActionListener(this);

        // IMPRIMIR NOMBRE Y PAIS
        label2 = new JLabel();
        label2.setBounds(10, 120, 250, 30);
        add(label2);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource()==combo1) {
            paisSeleccionado = (String) combo1.getSelectedItem();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button1) {
            String nombre = textField1.getText();
            label2.setText("El nombre es: " + nombre + ", y el pais: " + paisSeleccionado + ".");
            setTitle("El nombre es: " + nombre + ", y el pais: " + paisSeleccionado + ".");
        }
    }

    public void cargarCombo(){
        combo1.addItem("Argentina");
        combo1.addItem("Brasil");
        combo1.addItem("Canadá");
        combo1.addItem("Dinamarca");
        combo1.addItem("Egipto");
        combo1.addItem("Francia");
        combo1.addItem("Grecia");
        combo1.addItem("Honduras");
        combo1.addItem("India");
        combo1.addItem("Japón");
        combo1.addItem("Kenia");
        combo1.addItem("Líbano");
        combo1.addItem("México");
        combo1.addItem("Noruega");
        combo1.addItem("Omán");
    }
}
