//package Algoritmos;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class tarea4 extends JFrame {

    private static final String[] ANIMALES = {"PERRO", "GATO", "ELEFANTE", "LEON", "TIGRE", 
                                              "MONO", "JIRAFA", "CABALLO", "VACA", "OSO"};  
    private static final String[] VEGETALES = {"MANZANA", "PERA", "NARANJA", "LIMON", "FRESA", 
                                               "UVA", "PIÑA", "SANDIA", "MELON", "PLATANO"};    
    private static final String[] COLORES = {"ROJO", "AZUL", "VERDE", "AMARILLO", "NARANJA", 
                                             "MORADO", "ROSA", "BLANCO", "NEGRO", "GRIS"};

    private JLabel lblCategoria;
    private JLabel lblPalabra;
    private JLabel lblIntentos;
    private JLabel lblLetrasUsadas;
    private JTextField txtLetra;
    private JButton btnAdivinar;
    private JButton btnPista;
    private JButton btnReiniciar;
    private JButton btnSalir;
    private JPanel panelPalabra;

    private String[] arregloActual;
    private String palabraSecreta;
    private char[] palabraOculta;
    private int intentosRestantes;
    private StringBuilder letrasUsadas;
    private boolean juegoActivo;

    public tarea4() {
        setTitle("Juego de Adivinanza de Palabras");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setSize(600, 400);
        setLocationRelativeTo(null);

        inicializarComponentes();
        inicializarJuego();

        setVisible(true);
    }

    private void inicializarComponentes() {
        JPanel panelSuperior = new JPanel(new GridLayout(2, 2, 10, 10));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        lblCategoria = new JLabel("Categoría: ");
        lblIntentos = new JLabel("Intentos: 5");
        lblLetrasUsadas = new JLabel("Letras usadas: ");
        
        panelSuperior.add(lblCategoria);
        panelSuperior.add(lblIntentos);
        panelSuperior.add(new JLabel()); // Espacio vacío
        panelSuperior.add(lblLetrasUsadas);

        panelPalabra = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelPalabra.setBorder(BorderFactory.createTitledBorder("Palabra a adivinar"));
        lblPalabra = new JLabel("");
        lblPalabra.setFont(new Font("Monospaced", Font.BOLD, 24));
        panelPalabra.add(lblPalabra);

        JPanel panelInferior = new JPanel(new BorderLayout(10, 10));
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelEntrada = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelEntrada.add(new JLabel("Introduce una letra:"));
        txtLetra = new JTextField(5);
        txtLetra.setFont(new Font("Arial", Font.BOLD, 16));
        txtLetra.setHorizontalAlignment(JTextField.CENTER);
        panelEntrada.add(txtLetra);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnAdivinar = new JButton("Adivinar");
        btnPista = new JButton("Pista");
        btnReiniciar = new JButton("Reiniciar");
        btnSalir = new JButton("Salir");

        btnAdivinar.addActionListener(e -> adivinarLetra());
        btnPista.addActionListener(e -> darPista());
        btnReiniciar.addActionListener(e -> reiniciarJuego());
        btnSalir.addActionListener(e -> System.exit(0));

        txtLetra.addActionListener(e -> adivinarLetra());

        panelBotones.add(btnAdivinar);
        panelBotones.add(btnPista);
        panelBotones.add(btnReiniciar);
        panelBotones.add(btnSalir);

        panelInferior.add(panelEntrada, BorderLayout.NORTH);
        panelInferior.add(panelBotones, BorderLayout.SOUTH);

        add(panelSuperior, BorderLayout.NORTH);
        add(panelPalabra, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private void inicializarJuego() {
        String[] opciones = {"Animales", "Vegetales", "Colores"};
        int seleccion = JOptionPane.showOptionDialog(this,
            "Selecciona la categoría de palabras:",
            "Selección de Categoría",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            opciones,
            opciones[0]);

        if (seleccion == -1) {
            System.exit(0);
        }

        switch (seleccion) {
            case 0:
                arregloActual = ANIMALES;
                lblCategoria.setText("Categoría: Animales");
                break;
            case 1:
                arregloActual = VEGETALES;
                lblCategoria.setText("Categoría: Vegetales");
                break;
            case 2:
                arregloActual = COLORES;
                lblCategoria.setText("Categoría: Colores");
                break;
        }

        intentosRestantes = 5;
        letrasUsadas = new StringBuilder();
        juegoActivo = true;

        Random random = new Random();
        palabraSecreta = arregloActual[random.nextInt(arregloActual.length)];

        palabraOculta = new char[palabraSecreta.length()];
        for (int i = 0; i < palabraOculta.length; i++) {
            palabraOculta[i] = '_';
        }

        actualizarInterfaz();
        
        JOptionPane.showMessageDialog(this,
            "¡Comienza el juego!\n" +
            "Palabra de " + palabraSecreta.length() + " letras\n" +
            "Tienes " + intentosRestantes + " intentos",
            "¡Buena suerte!",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void adivinarLetra() {
        if (!juegoActivo) return;

        String input = txtLetra.getText().trim().toUpperCase();
        txtLetra.setText("");

        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor, introduce una letra.",
                "Entrada vacía",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (input.length() != 1 || !Character.isLetter(input.charAt(0))) {
            JOptionPane.showMessageDialog(this,
                "Por favor, introduce una sola letra válida.",
                "Entrada inválida",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        char letra = input.charAt(0);

        if (letrasUsadas.toString().contains(String.valueOf(letra))) {
            JOptionPane.showMessageDialog(this,
                "Ya has usado la letra '" + letra + "'. Intenta con otra.",
                "Letra repetida",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        letrasUsadas.append(letra).append(" ");
        lblLetrasUsadas.setText("Letras usadas: " + letrasUsadas.toString());

        boolean letraEncontrada = false;
        for (int i = 0; i < palabraSecreta.length(); i++) {
            if (palabraSecreta.charAt(i) == letra) {
                palabraOculta[i] = letra;
                letraEncontrada = true;
            }
        }

        if (letraEncontrada) {
            JOptionPane.showMessageDialog(this,
                "¡Correcto! La letra '" + letra + "' está en la palabra.",
                "¡Bien hecho!",
                JOptionPane.INFORMATION_MESSAGE);

            // Verificar si ganó
            if (verificarVictoria()) {
                mostrarVictoria();
                juegoActivo = false;
                return;
            }
        } else {
            intentosRestantes--;
            lblIntentos.setText("Intentos: " + intentosRestantes);
            
            JOptionPane.showMessageDialog(this,
                "Incorrecto. La letra '" + letra + "' no está en la palabra.\n" +
                "Intentos restantes: " + intentosRestantes,
                "Letra incorrecta",
                JOptionPane.WARNING_MESSAGE);

            if (intentosRestantes == 0) {
                mostrarDerrota();
                juegoActivo = false;
                return;
            }
        }

        actualizarInterfaz();
    }

    private void darPista() {
        if (!juegoActivo || intentosRestantes <= 1) {
            JOptionPane.showMessageDialog(this,
                "No puedes pedir pistas en este momento.",
                "Pista no disponible",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        for (int i = 0; i < palabraSecreta.length(); i++) {
            if (palabraOculta[i] == '_') {
                char letraPista = palabraSecreta.charAt(i);
                palabraOculta[i] = letraPista;
                intentosRestantes--; 
                
                JOptionPane.showMessageDialog(this,
                    "Pista: La letra '" + letraPista + "' está en la posición " + (i + 1) + 
                    "\nPierdes un intento por la pista.",
                    "Pista",
                    JOptionPane.INFORMATION_MESSAGE);
                
                lblIntentos.setText("Intentos: " + intentosRestantes);
                actualizarInterfaz();
                
                if (verificarVictoria()) {
                    mostrarVictoria();
                    juegoActivo = false;
                }
                break;
            }
        }
    }

    private void reiniciarJuego() {
        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Estás seguro de que quieres reiniciar el juego?\n" +
            "Se perderá el progreso actual.",
            "Confirmar reinicio",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            inicializarJuego();
        }
    }

    private boolean verificarVictoria() {
        for (char c : palabraOculta) {
            if (c == '_') {
                return false;
            }
        }
        return true;
    }

    private void mostrarVictoria() {
        actualizarInterfaz();
        
        int opcion = JOptionPane.showOptionDialog(this,
            "¡FELICIDADES! ¡HAS GANADO!\n\n" +
            "Palabra: " + palabraSecreta + "\n" +
            "Intentos restantes: " + intentosRestantes + "\n\n" +
            "¿Qué quieres hacer?",
            "¡Victoria!",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            new String[]{"Jugar otra vez", "Salir"},
            "Jugar otra vez");

        if (opcion == 0) {
            reiniciarJuego();
        } else {
            System.exit(0);
        }
    }

    private void mostrarDerrota() {
        for (int i = 0; i < palabraSecreta.length(); i++) {
            palabraOculta[i] = palabraSecreta.charAt(i);
        }
        actualizarInterfaz();
        
        int opcion = JOptionPane.showOptionDialog(this,
            "¡GAME OVER! Has perdido.\n\n" +
            "La palabra era: " + palabraSecreta + "\n\n" +
            "¿Qué quieres hacer?",
            "Derrota",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            new String[]{"Intentar otra vez", "Salir"},
            "Intentar otra vez");

        if (opcion == 0) {
            reiniciarJuego();
        } else {
            System.exit(0);
        }
    }

    private void actualizarInterfaz() {
        StringBuilder palabraMostrada = new StringBuilder();
        for (int i = 0; i < palabraOculta.length; i++) {
            palabraMostrada.append(palabraOculta[i]).append(" ");
        }
        lblPalabra.setText(palabraMostrada.toString());
        
        lblIntentos.setText("Intentos: " + intentosRestantes);
        lblLetrasUsadas.setText("Letras usadas: " + letrasUsadas.toString());
        
        if (!juegoActivo) {
            lblPalabra.setForeground(intentosRestantes > 0 ? Color.GREEN : Color.RED);
        } else {
            lblPalabra.setForeground(Color.BLACK);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new tarea4();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}