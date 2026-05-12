//package Algoritmos;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.Normalizer;
import java.util.*;

public class tarea3 extends JFrame {
    
    private ArrayList<String> diccionario;
    private ArrayList<String> fraseActual;
    private String archivoDiccionario;
    
    private JTextArea areaFrase;
    private JTextField campoPalabra;
    private JList<String> listaSugerencias;
    private DefaultListModel<String> modeloSugerencias;
    private JLabel lblInfo;
    private JLabel lblContador;
    
    private javax.swing.Timer timerBusqueda;
    private String ultimaBusqueda;
    private boolean enProcesoAutocompletado;
    
    public tarea3() {
        diccionario = new ArrayList<>();
        fraseActual = new ArrayList<>();
        archivoDiccionario = "diccionario_completo.txt";
        
        enProcesoAutocompletado = false;
        
        configurarVentana();
        configurarComponentes();
        cargarDiccionarioPorDefecto();
        cargarDiccionario(archivoDiccionario);
    }
    
    private void configurarVentana() {
        setTitle("Programa de Autocompletado de palabras.");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setSize(900, 600);
        setLocationRelativeTo(null);
        
        timerBusqueda = new javax.swing.Timer(300, e -> buscarSugerencias());
        timerBusqueda.setRepeats(false);
    }
    
    private void configurarComponentes() {
        configurarMenu();
        
        JPanel panelSuperior = new JPanel(new GridLayout(2, 1, 5, 5));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        lblInfo = new JLabel("Diccionario: " + archivoDiccionario);
        lblContador = new JLabel("Palabras cargadas: 0");
        
        panelSuperior.add(lblInfo);
        panelSuperior.add(lblContador);
        
        areaFrase = new JTextArea(8, 50);
        areaFrase.setEditable(false);
        areaFrase.setLineWrap(true);
        areaFrase.setWrapStyleWord(true);
        areaFrase.setFont(new Font("Monospaced", Font.PLAIN, 14));
        areaFrase.setBackground(new Color(255, 255, 240));
        
        JScrollPane scrollFrase = new JScrollPane(areaFrase);
        scrollFrase.setBorder(BorderFactory.createTitledBorder("Frase Completa"));
        
        JPanel panelCentral = new JPanel(new BorderLayout(10, 10));
        
        JPanel panelEntrada = new JPanel(new BorderLayout(5, 5));
        panelEntrada.setBorder(BorderFactory.createTitledBorder("Escribir Palabra"));
        panelEntrada.setPreferredSize(new Dimension(400, 60));
        
        campoPalabra = new JTextField();
        campoPalabra.setFont(new Font("Dialog", Font.PLAIN, 16));
        
        campoPalabra.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (!enProcesoAutocompletado) {
                    timerBusqueda.restart();
                }
            }
            
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    agregarPalabra();
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    campoPalabra.setText("");
                    modeloSugerencias.clear();
                } else if (e.getKeyCode() == KeyEvent.VK_TAB && !listaSugerencias.isSelectionEmpty()) {
                    completarPalabra();
                    e.consume();
                }
            }
        });
        
        modeloSugerencias = new DefaultListModel<>();
        listaSugerencias = new JList<>(modeloSugerencias);
        listaSugerencias.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaSugerencias.setFont(new Font("Dialog", Font.PLAIN, 12));
        listaSugerencias.setBackground(new Color(240, 248, 255));
        
        listaSugerencias.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    completarPalabra();
                }
            }
        });
        
        JScrollPane scrollSugerencias = new JScrollPane(listaSugerencias);
        scrollSugerencias.setBorder(BorderFactory.createTitledBorder("Sugerencias"));
        scrollSugerencias.setPreferredSize(new Dimension(250, 200));
        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton btnAgregar = new JButton("Agregar Palabra");
        btnAgregar.addActionListener(e -> agregarPalabra());
        
        JButton btnLimpiar = new JButton("Limpiar Campo");
        btnLimpiar.addActionListener(e -> campoPalabra.setText(""));
        
        JButton btnCompletar = new JButton("Completar Automático");
        btnCompletar.addActionListener(e -> completarAutomatico());
        
        JButton btnGuardar = new JButton("Guardar Frase");
        btnGuardar.addActionListener(e -> guardarFrase());
        
        JButton btnCargarDic = new JButton("Cargar Diccionario");
        btnCargarDic.addActionListener(e -> cargarNuevoDiccionario());
        
        panelBotones.add(btnAgregar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnCompletar);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCargarDic);
        
        panelEntrada.add(campoPalabra, BorderLayout.CENTER);
        
        JPanel panelDerecho = new JPanel(new BorderLayout());
        panelDerecho.add(scrollSugerencias, BorderLayout.CENTER);
        
        JPanel panelIzquierdo = new JPanel(new BorderLayout());
        panelIzquierdo.add(panelEntrada, BorderLayout.NORTH);
        
        panelCentral.add(panelIzquierdo, BorderLayout.WEST);
        panelCentral.add(panelDerecho, BorderLayout.CENTER);
        panelCentral.add(panelBotones, BorderLayout.SOUTH);
        
        add(panelSuperior, BorderLayout.NORTH);
        add(scrollFrase, BorderLayout.CENTER);
        add(panelCentral, BorderLayout.SOUTH);
        
        campoPalabra.requestFocus();
    }
    
    private void configurarMenu() {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu menuArchivo = new JMenu("Archivo");
        
        JMenuItem itemNuevo = new JMenuItem("Nueva Frase");
        itemNuevo.addActionListener(e -> nuevaFrase());
        
        JMenuItem itemGuardar = new JMenuItem("Guardar Frase...");
        itemGuardar.addActionListener(e -> guardarFrase());
        
        JMenuItem itemCargarFrase = new JMenuItem("Cargar Frase...");
        itemCargarFrase.addActionListener(e -> cargarFrase());
        
        JMenuItem itemSalir = new JMenuItem("Salir");
        itemSalir.addActionListener(e -> System.exit(0));
        
        menuArchivo.add(itemNuevo);
        menuArchivo.add(itemGuardar);
        menuArchivo.add(itemCargarFrase);
        menuArchivo.addSeparator();
        menuArchivo.add(itemSalir);
        
        JMenu menuDiccionario = new JMenu("Diccionario");
        
        JMenuItem itemCargarDic = new JMenuItem("Cargar Diccionario...");
        itemCargarDic.addActionListener(e -> cargarNuevoDiccionario());
        
        JMenuItem itemVerDic = new JMenuItem("Ver Diccionario");
        itemVerDic.addActionListener(e -> verDiccionario());
        
        menuDiccionario.add(itemCargarDic);
        menuDiccionario.add(itemVerDic);
        
        JMenu menuAyuda = new JMenu("Ayuda");
        
        JMenuItem itemInstrucciones = new JMenuItem("Instrucciones");
        itemInstrucciones.addActionListener(e -> mostrarInstrucciones());
        
        menuAyuda.add(itemInstrucciones);
        
        menuBar.add(menuArchivo);
        menuBar.add(menuDiccionario);
        menuBar.add(menuAyuda);
        
        setJMenuBar(menuBar);
    }
    
    private void cargarDiccionarioPorDefecto() {
        String[] palabrasBase = {
            "facultad", "sistemas", "cumple", "años", "universidad", "estudiar",
            "programacion", "ingenieria", "computacion", "alumno", "profesor",
            "favor", "fanatico", "falacia", "familia", "famoso", "farmacia",
            "felicidad", "festival", "figura", "final", "fisica", "flor",
            "fotografia", "fruta", "funcion", "futuro", "facil", "factura",
            "fachada", "factor", "faena", "faja", "falda", "fallar",
            "falso", "fama", "fango", "fantasia", "faro", "fascinante",
            "fastidio", "fatiga", "favorito", "febrero", "fecha", "felino",
            "feo", "feria", "feroz", "festejo", "feudal", "fibra", "ficha",
            "fiel", "fiesta", "figura", "fijar", "fila", "filo", "filtro",
            "fin", "finca", "fingir", "fino", "firma", "flaco", "flauta",
            "flecha", "flojo", "flor", "flota", "fluir", "foco", "fogata",
            "folleto", "fondo", "forma", "fortalecer", "forzar", "foto",
            "fracaso", "fractura", "fragancia", "franco", "frase", "frecuente",
            "freno", "fresa", "frio", "frontera", "frotar", "fruta", "fuego",
            "fuente", "fuerza", "fuga", "fumar", "funcion", "fundar", "furioso",
            "fusil", "futuro", "sistema", "software", "hardware", "datos",
            "informacion", "tecnologia", "desarrollo", "aplicacion", "web",
            "movil", "servidor", "cliente", "red", "internet", "seguridad",
            "base", "analisis", "diseno", "implementacion", "prueba", "mantenimiento",
            "la", "de", "en", "y", "el", "los", "las", "un", "una", "unos", "unas",
            "con", "sin", "por", "para", "sobre", "entre", "hacia", "desde", "hasta"
        };
        
        diccionario.addAll(Arrays.asList(palabrasBase));
        Collections.sort(diccionario);
    }
    
    private void cargarDiccionario(String ruta) {
        File archivo = new File(ruta);
        if (!archivo.exists()) {
            guardarDiccionarioPorDefecto(ruta);
            return;
        }
        
        diccionario.clear();
        cargarDiccionarioPorDefecto();
        
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            int contador = 0;
            
            while ((linea = br.readLine()) != null) {
                String palabra = linea.trim().toLowerCase();
                if (!palabra.isEmpty() && !diccionario.contains(palabra)) {
                    diccionario.add(palabra);
                    contador++;
                }
            }
            
            Collections.sort(diccionario);
            archivoDiccionario = ruta;
            
            lblInfo.setText("Diccionario: " + archivo.getName());
            lblContador.setText("Palabras cargadas: " + diccionario.size());
            
            JOptionPane.showMessageDialog(this, 
                "Diccionario cargado: " + contador + " palabras añadidas\n" +
                "Total palabras: " + diccionario.size(),
                "Diccionario Cargado",
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar diccionario: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void guardarDiccionarioPorDefecto(String ruta) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            for (String palabra : diccionario) {
                bw.write(palabra);
                bw.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                "Error al crear diccionario: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void buscarSugerencias() {
        String texto = campoPalabra.getText().trim();
        
        if (texto.isEmpty()) {
            modeloSugerencias.clear();
            return;
        }
        
        if (texto.equals(ultimaBusqueda)) {
            return;
        }
        
        ultimaBusqueda = texto;
        modeloSugerencias.clear();
        
        String textoNormalizado = normalizarTexto(texto);
        
        int maxSugerencias = 10;
        int encontradas = 0;
        
        for (String palabra : diccionario) {
            String palabraNormalizada = normalizarTexto(palabra);
            
            if (palabraNormalizada.startsWith(textoNormalizado)) {
                modeloSugerencias.addElement(palabra);
                encontradas++;
                
                if (encontradas >= maxSugerencias) {
                    if (diccionario.size() > maxSugerencias) {
                        modeloSugerencias.addElement("... y " + (contarCoincidencias(textoNormalizado) - maxSugerencias) + " más");
                    }
                    break;
                }
            }
        }
        
        if (!modeloSugerencias.isEmpty()) {
            listaSugerencias.setSelectedIndex(0);
        }
    }
    
    private int contarCoincidencias(String prefijo) {
        int contador = 0;
        String prefijoNormalizado = normalizarTexto(prefijo);
        
        for (String palabra : diccionario) {
            if (normalizarTexto(palabra).startsWith(prefijoNormalizado)) {
                contador++;
            }
        }
        
        return contador;
    }
    
    private String normalizarTexto(String texto) {
        if (texto == null) return "";
        
        texto = texto.toLowerCase();
        texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
        texto = texto.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        
        return texto;
    }
    
    private void agregarPalabra() {
        String palabra = campoPalabra.getText().trim();
        
        if (!palabra.isEmpty()) {
            fraseActual.add(palabra);
            actualizarAreaFrase();
            campoPalabra.setText("");
            modeloSugerencias.clear();
            ultimaBusqueda = "";
            
            campoPalabra.requestFocus();
        } else {
            JOptionPane.showMessageDialog(this,
                "Por favor, escribe una palabra primero.",
                "Campo Vacío",
                JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void completarPalabra() {
        if (!listaSugerencias.isSelectionEmpty()) {
            String seleccion = listaSugerencias.getSelectedValue();
            
            if (seleccion.startsWith("...")) {
                return;
            }
            
            enProcesoAutocompletado = true;
            campoPalabra.setText(seleccion);
            campoPalabra.setCaretPosition(seleccion.length());
            enProcesoAutocompletado = false;
            
            campoPalabra.requestFocus();
        }
    }
    
    private void completarAutomatico() {
        String texto = campoPalabra.getText().trim();
        
        if (!texto.isEmpty() && !modeloSugerencias.isEmpty()) {
            String primeraSugerencia = modeloSugerencias.getElementAt(0);
            
            if (!primeraSugerencia.startsWith("...")) {
                campoPalabra.setText(primeraSugerencia);
                campoPalabra.setCaretPosition(primeraSugerencia.length());
                campoPalabra.requestFocus();
            }
        }
    }
    
    private void nuevaFrase() {
        int confirmar = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de comenzar una nueva frase?\nSe perderá la frase actual.",
            "Nueva Frase",
            JOptionPane.YES_NO_OPTION);
        
        if (confirmar == JOptionPane.YES_OPTION) {
            fraseActual.clear();
            actualizarAreaFrase();
            campoPalabra.setText("");
            modeloSugerencias.clear();
            campoPalabra.requestFocus();
        }
    }
    
    private void guardarFrase() {
        if (fraseActual.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No hay frase para guardar.",
                "Frase Vacía",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Frase");
        fileChooser.setSelectedFile(new File("frase_guardada.txt"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos de texto", "txt"));
        
        int resultado = fileChooser.showSaveDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            
            if (!archivo.getName().toLowerCase().endsWith(".txt")) {
                archivo = new File(archivo.getAbsolutePath() + ".txt");
            }
            
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
                String fraseCompleta = String.join(" ", fraseActual);
                bw.write(fraseCompleta);
                
                JOptionPane.showMessageDialog(this,
                    "Frase guardada en:\n" + archivo.getAbsolutePath(),
                    "Frase Guardada",
                    JOptionPane.INFORMATION_MESSAGE);
                    
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                    "Error al guardar: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void cargarFrase() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Cargar Frase");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos de texto", "txt"));
        
        int resultado = fileChooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                String linea = br.readLine();
                
                if (linea != null && !linea.trim().isEmpty()) {
                    int confirmar = JOptionPane.showConfirmDialog(this,
                        "¿Reemplazar frase actual?\nSe perderá la frase en progreso.",
                        "Cargar Frase",
                        JOptionPane.YES_NO_OPTION);
                    
                    if (confirmar == JOptionPane.YES_OPTION) {
                        fraseActual.clear();
                        String[] palabras = linea.trim().split("\\s+");
                        
                        for (String palabra : palabras) {
                            if (!palabra.trim().isEmpty()) {
                                fraseActual.add(palabra.trim());
                            }
                        }
                        
                        actualizarAreaFrase();
                        
                        JOptionPane.showMessageDialog(this,
                            "Frase cargada: " + fraseActual.size() + " palabras",
                            "Frase Cargada",
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                    "Error al cargar: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void cargarNuevoDiccionario() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar Diccionario");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos de texto", "txt"));
        
        int resultado = fileChooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            cargarDiccionario(archivo.getAbsolutePath());
        }
    }
    
    private void verDiccionario() {
        StringBuilder contenido = new StringBuilder();
        contenido.append("Diccionario: ").append(archivoDiccionario).append("\n");
        contenido.append("Total palabras: ").append(diccionario.size()).append("\n\n");
        
        int limite = Math.min(100, diccionario.size());
        for (int i = 0; i < limite; i++) {
            contenido.append(diccionario.get(i)).append("\n");
        }
        
        if (diccionario.size() > 100) {
            contenido.append("\n... y ").append(diccionario.size() - 100).append(" palabras más");
        }
        
        JTextArea textArea = new JTextArea(contenido.toString(), 20, 40);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        
        JOptionPane.showMessageDialog(this,
            scrollPane,
            "Contenido del Diccionario",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void actualizarAreaFrase() {
        StringBuilder fraseCompleta = new StringBuilder();
        
        for (int i = 0; i < fraseActual.size(); i++) {
            fraseCompleta.append(fraseActual.get(i));
            if (i < fraseActual.size() - 1) {
                fraseCompleta.append(" ");
            }
        }
        
        areaFrase.setText(fraseCompleta.toString());
        areaFrase.setCaretPosition(areaFrase.getDocument().getLength());
    }
    
    private void mostrarInstrucciones() {
        String instrucciones = "INSTRUCCIONES:\n\n" +
            "1. Escribe en el campo 'Escribir Palabra'\n" +
            "2. Las sugerencias aparecerán automáticamente a la derecha\n" +
            "3. Usa las flechas ↑↓ para navegar por las sugerencias\n" +
            "4. Doble clic en una sugerencia para autocompletar\n" +
            "5. Presiona ENTER para agregar la palabra a la frase\n" +
            "6. Presiona ESC para limpiar el campo\n" +
            "7. Puedes cargar tu propio diccionario .txt\n" +
            "8. La frase completa se muestra en el área superior\n\n" +
            "EJEMPLO:\n" +
            "- Escribe: 'fa'\n" +
            "- Aparecen sugerencias: facultad, favor, familia...\n";
        
        JOptionPane.showMessageDialog(this,
            instrucciones,
            "Instrucciones",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                tarea3 ventana = new tarea3();
                ventana.setVisible(true);
                
                JOptionPane.showMessageDialog(ventana,
                    "BIENVENIDO AL SISTEMA DE AUTOCOMPLETADO\n\n" +
                    "Para comenzar:\n" +
                    "1. Escribe una palabra en el campo inferior\n" +
                    "2. Usa las sugerencias que aparecen a la derecha\n" +
                    "3. Presiona ENTER para agregar palabras\n" +
                    "4. Construye tu frase completa\n\n" +
                    "¡Prueba escribiendo 'fa' para comenzar!",
                    "Bienvenida",
                    JOptionPane.INFORMATION_MESSAGE);
                    
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}