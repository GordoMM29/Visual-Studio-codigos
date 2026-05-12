import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.List;

public class VentanaPrincipal extends JFrame {
    private JTextField txtMatricula, txtNombre, txtCarrera, txtSemestre;
    private JButton btnAgregar, btnModificar, btnEliminar, btnConsultar, btnBuscar;
    private JTable tabla;
    private DefaultTableModel modelo;
    private DBManager db;

    public VentanaPrincipal() {
        db = new DBManager("estudiantes.db4o");

        setTitle("Gestión de Estudiantes");
        setSize(700, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        // Labels y TextFields
        JLabel l1 = new JLabel("Matrícula:"); l1.setBounds(20, 20, 80, 25);
        txtMatricula = new JTextField(); txtMatricula.setBounds(100, 20, 150, 25);

        JLabel l2 = new JLabel("Nombre:"); l2.setBounds(20, 60, 80, 25);
        txtNombre = new JTextField(); txtNombre.setBounds(100, 60, 150, 25);

        JLabel l3 = new JLabel("Carrera:"); l3.setBounds(20, 100, 80, 25);
        txtCarrera = new JTextField(); txtCarrera.setBounds(100, 100, 150, 25);

        JLabel l4 = new JLabel("Semestre:"); l4.setBounds(20, 140, 80, 25);
        txtSemestre = new JTextField(); txtSemestre.setBounds(100, 140, 150, 25);

        add(l1); add(txtMatricula);
        add(l2); add(txtNombre);
        add(l3); add(txtCarrera);
        add(l4); add(txtSemestre);

        // Botones
        btnAgregar = new JButton("Agregar"); btnAgregar.setBounds(300, 20, 120, 25);
        btnModificar = new JButton("Modificar"); btnModificar.setBounds(300, 60, 120, 25);
        btnEliminar = new JButton("Eliminar"); btnEliminar.setBounds(300, 100, 120, 25);
        btnConsultar = new JButton("Consultar Todo"); btnConsultar.setBounds(300, 140, 150, 25);
        btnBuscar = new JButton("Buscar por Matrícula"); btnBuscar.setBounds(300, 180, 180, 25);

        add(btnAgregar); add(btnModificar); add(btnEliminar); add(btnConsultar); add(btnBuscar);

        // Tabla
        modelo = new DefaultTableModel(new String[]{"Matrícula", "Nombre", "Carrera", "Semestre"}, 0);
        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(20, 220, 640, 120);
        add(scroll);

        // Eventos
        btnAgregar.addActionListener(e -> agregar());
        btnModificar.addActionListener(e -> modificar());
        btnEliminar.addActionListener(e -> eliminar());
        btnConsultar.addActionListener(e -> consultarTodos());
        btnBuscar.addActionListener(e -> buscar());

        setVisible(true);
    }

    private void agregar() {
        try {
            Estudiante e = new Estudiante(
                    txtMatricula.getText(),
                    txtNombre.getText(),
                    txtCarrera.getText(),
                    Integer.parseInt(txtSemestre.getText())
            );
            db.agregarEstudiante(e);
            consultarTodos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar: " + ex.getMessage());
        }
    }

    private void modificar() {
        try {
            Estudiante e = new Estudiante(
                    txtMatricula.getText(),
                    txtNombre.getText(),
                    txtCarrera.getText(),
                    Integer.parseInt(txtSemestre.getText())
            );
            db.modificarEstudiante(e);
            consultarTodos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al modificar: " + ex.getMessage());
        }
    }

    private void eliminar() {
        try {
            db.eliminarEstudiante(txtMatricula.getText());
            consultarTodos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage());
        }
    }

    private void buscar() {
        try {
            Estudiante e = db.buscarPorMatricula(txtMatricula.getText());
            modelo.setRowCount(0);
            if (e != null) {
                modelo.addRow(new Object[]{e.getMatricula(), e.getNombre(), e.getCarrera(), e.getSemestre()});
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el estudiante");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al buscar: " + ex.getMessage());
        }
    }

    private void consultarTodos() {
        modelo.setRowCount(0);
        List<Estudiante> lista = db.consultarTodos();
        for (Estudiante e : lista) {
            modelo.addRow(new Object[]{e.getMatricula(), e.getNombre(), e.getCarrera(), e.getSemestre()});
        }
    }

    public static void main(String[] args) {
        new VentanaPrincipal();
    }
}


