//package Algoritmos;  
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Arbolbinario {
    private Nodoab raiz;
    
    public Arbolbinario() {
        this.raiz = null;
    }
    
    // insertar un nodo ordenado por nump
    public void insertar(int nump, String texto) {
        Nodoab nuevo = new Nodoab(nump, texto);
        
        if (raiz == null) {
            raiz = nuevo;
            JOptionPane.showMessageDialog(null, 
                "Nodo insertado como raíz.\n" +
                "Nump: " + nump + "\n" +
                "Texto: " + texto + "\n" +
                "Texto encriptado: " + nuevo.textoencrip,
                "Inserción Exitosa",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        Nodoab actual = raiz;
        Nodoab padre = null;
        
        while (actual != null) {
            padre = actual;
            if (nump < actual.nump) {
                actual = actual.lchild;
            } else if (nump > actual.nump) {
                actual = actual.rchild;
            } else {
                JOptionPane.showMessageDialog(null,
                    "Ya existe un nodo con el número de párrafo: " + nump,
                    "Error de Inserción",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        if (nump < padre.nump) {
            padre.lchild = nuevo;
        } else {
            padre.rchild = nuevo;
        }
        
        JOptionPane.showMessageDialog(null,
            "Nodo insertado correctamente.\n" +
            "Nump: " + nump + "\n" +
            "Texto: " + texto + "\n" +
            "Texto encriptado: " + nuevo.textoencrip,
            "Inserción Exitosa",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Recorrido INORDER 
    public String inorderNormal(Nodoab nodo, StringBuilder resultado) {
        if (nodo != null) {
            inorderNormal(nodo.lchild, resultado);
            resultado.append("Nump: ").append(nodo.nump)
                    .append(" | Texto: ").append(nodo.texto)
                    .append(" | Estatus: ").append(nodo.estatus)
                    .append("\n");
            inorderNormal(nodo.rchild, resultado);
        }
        return resultado.toString();
    }
    
    // Recorrido INORDER 
    public String inorderEncriptado(Nodoab nodo, StringBuilder resultado) {
        if (nodo != null) {
            inorderEncriptado(nodo.lchild, resultado);
            resultado.append("Nump: ").append(nodo.nump)
                    .append(" | Texto Encriptado: ").append(nodo.textoencrip)
                    .append(" | Estatus: ").append(nodo.estatus)
                    .append("\n");
            inorderEncriptado(nodo.rchild, resultado);
        }
        return resultado.toString();
    }
    
    // extraer mensaje oculto usando clave especial
    public String extraerMensajeOculto(String clave) {
        StringBuilder mensajeOculto = new StringBuilder();
        extraerMensajeRecursivo(raiz, clave, mensajeOculto);
        return mensajeOculto.toString();
    }
    
    // extraer mensaje oculto
    private void extraerMensajeRecursivo(Nodoab nodo, String clave, StringBuilder mensaje) {
        if (nodo == null) return;
        
        // Recorrido INORDER
        extraerMensajeRecursivo(nodo.lchild, clave, mensaje);
        
        // extraer caracteres
        String[] palabras = nodo.texto.split(" ");
        if (palabras.length > 0) {
            // caracteres extraer
            int indiceClave = mensaje.length() % clave.length();
            char caracterClave = clave.charAt(indiceClave);
            
            // Reglas 
            for (int i = 0; i < palabras.length; i++) {
                if (i != 3) { // Evitar la cuarta palabra
                    String palabra = palabras[i];
                    if (!palabra.isEmpty()) {
                        // Extraer caracteres 
                        int pos = (caracterClave % palabra.length());
                        mensaje.append(palabra.charAt(pos));
                        
                        // Cada 3 caracteres, agregar un separador
                        if (mensaje.length() % 4 == 0) {
                            mensaje.append(" ");
                        }
                    }
                }
            }
        }
        
        extraerMensajeRecursivo(nodo.rchild, clave, mensaje);
    }
    
    public void mostrarMensajeDescifrado(String clave) {
        if (raiz == null) {
            JOptionPane.showMessageDialog(null,
                "El árbol está vacío. No hay mensajes para descifrar.",
                "Árbol Vacío",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String mensajeOculto = extraerMensajeOculto(clave);
        
        StringBuilder resultado = new StringBuilder();
        resultado.append("=== MENSAJE DESCIFRADO CON CLAVE: ").append(clave).append(" ===\n\n");
        resultado.append("Mensaje oculto: ").append(mensajeOculto).append("\n\n");
        resultado.append("Interpretación del mensaje:\n");
        
        String[] partes = mensajeOculto.split(" ");
        for (int i = 0; i < partes.length; i++) {
            resultado.append("Palabra ").append(i+1).append(": ").append(partes[i]).append("\n");
        }
        
        JTextArea textArea = new JTextArea(resultado.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new java.awt.Dimension(400, 300));
        
        JOptionPane.showMessageDialog(null,
            scrollPane,
            "Mensaje Descifrado",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void mostrarMenu() {
        String[] opciones = {
            "Insertar nodo",
            "Mostrar INORDER (Texto normal)",
            "Mostrar INORDER (Texto encriptado)",
            "Descifrar mensaje con clave",
            "Salir"
        };
        
        while (true) {
            int seleccion = JOptionPane.showOptionDialog(
                null,
                "Seleccione una operación:",
                "Árbol Binario - Menú Principal",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                opciones,
                opciones[0]
            );
            
            switch(seleccion) {
                case 0:
                    insertarNodoDialog();
                    break;
                case 1:
                    mostrarInorderNormal();
                    break;
                case 2:
                    mostrarInorderEncriptado();
                    break;
                case 3:
                    descifrarConClaveDialog();
                    break;
                case 4:
                case JOptionPane.CLOSED_OPTION:
                    int confirmacion = JOptionPane.showConfirmDialog(
                        null,
                        "¿Está seguro que desea salir?",
                        "Confirmar Salida",
                        JOptionPane.YES_NO_OPTION
                    );
                    if (confirmacion == JOptionPane.YES_OPTION) {
                        JOptionPane.showMessageDialog(null,
                            "¡Hasta luego!",
                            "Despedida",
                            JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    break;
            }
        }
    }
    
    // Diálogo para insertar nodo
    private void insertarNodoDialog() {
        try {
            String numpStr = JOptionPane.showInputDialog(
                null,
                "Ingrese el número de párrafo:",
                "Insertar Nodo - Número de Párrafo",
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (numpStr == null) return; // Usuario canceló
            
            int nump = Integer.parseInt(numpStr);
            
            String texto = JOptionPane.showInputDialog(
                null,
                "Ingrese el texto para el párrafo " + nump + ":",
                "Insertar Nodo - Texto",
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (texto != null && !texto.trim().isEmpty()) {
                insertar(nump, texto);
            } else {
                JOptionPane.showMessageDialog(null,
                    "El texto no puede estar vacío.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                "Error: Debe ingresar un número válido.",
                "Error de Formato",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Mostrar recorrido INORDER normal
    private void mostrarInorderNormal() {
        if (raiz == null) {
            JOptionPane.showMessageDialog(null,
                "El árbol está vacío.",
                "Árbol Vacío",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        StringBuilder resultado = new StringBuilder();
        resultado.append("=== RECORRIDO INORDER - TEXTO NORMAL ===\n\n");
        inorderNormal(raiz, resultado);
        
        // Mostrar en un JTextArea con scroll
        JTextArea textArea = new JTextArea(resultado.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new java.awt.Dimension(500, 400));
        
        JOptionPane.showMessageDialog(null,
            scrollPane,
            "Recorrido INORDER - Texto Normal",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void mostrarInorderEncriptado() {
        if (raiz == null) {
            JOptionPane.showMessageDialog(null,
                "El árbol está vacío.",
                "Árbol Vacío",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        StringBuilder resultado = new StringBuilder();
        resultado.append("=== RECORRIDO INORDER - TEXTO ENCRIPTADO ===\n\n");
        inorderEncriptado(raiz, resultado);
        
        // Mostrar en un JTextArea con scroll
        JTextArea textArea = new JTextArea(resultado.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new java.awt.Dimension(500, 400));
        
        JOptionPane.showMessageDialog(null,
            scrollPane,
            "Recorrido INORDER - Texto Encriptado",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void descifrarConClaveDialog() {
        if (raiz == null) {
            JOptionPane.showMessageDialog(null,
                "El árbol está vacío. No hay mensajes para descifrar.",
                "Árbol Vacío",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String clave = JOptionPane.showInputDialog(
            null,
            "Ingrese la clave especial para descifrar el mensaje:",
            "Descifrar Mensaje - Clave",
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (clave != null && !clave.trim().isEmpty()) {
            mostrarMensajeDescifrado(clave);
        } else if (clave != null) {
            JOptionPane.showMessageDialog(null,
                "La clave no puede estar vacía.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void main(String[] args) {
        // Configurar el Look and Feel para que se vea mejor
        try {
            javax.swing.UIManager.setLookAndFeel(
                javax.swing.UIManager.getSystemLookAndFeelClassName()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        Arbolbinario programa = new Arbolbinario();
        programa.mostrarMenu();
    }
}