package lib;
import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.util.data.audio.AudioPlayer;
import javax.sound.sampled.AudioInputStream;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class VelocidadConVozGUI {

    public static void main(String[] args) {

        try {
            // Configurar classloader para las librerías de MaryTTS
            configurarLibreriasMaryTTS();
            
            while (true) {
                String[] opciones = {
                    "Calcular Velocidad", 
                    "Ver Instrucciones", 
                    "Acerca de", 
                    "Salir"
                };
                
                int seleccion = JOptionPane.showOptionDialog(
                    null,
                    "Seleccione una opción:",
                    "Calculadora de Velocidad con Voz",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
                );
                
                switch (seleccion) {
                    case 0:
                        calcularVelocidadConVoz();
                        break;
                    case 1:
                        mostrarInstrucciones();
                        break;
                    case 2:
                        mostrarAcercaDe();
                        break;
                    case 3:
                    case JOptionPane.CLOSED_OPTION:
                        JOptionPane.showMessageDialog(null, "¡Gracias por usar la aplicación!");
                        System.exit(0);
                        break;
                }
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error al iniciar la aplicación:\n" + e.getMessage() + 
                "\n\nAsegúrate de tener las librerías de MaryTTS en la carpeta 'lib'",
                "Error de Inicialización",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    public static void configurarLibreriasMaryTTS() {
        try {
            // Buscar archivos JAR en la carpeta lib
            File libDir = new File("lib");
            if (libDir.exists() && libDir.isDirectory()) {
                File[] jars = libDir.listFiles((dir, name) -> name.endsWith(".jar"));
                
                if (jars != null && jars.length > 0) {
                    List<URL> urlList = new ArrayList<>();
                    for (File jar : jars) {
                        urlList.add(jar.toURI().toURL());
                        System.out.println("Cargando librería: " + jar.getName());
                    }
                    
                    URL[] urls = urlList.toArray(new URL[0]);
                    URLClassLoader classLoader = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
                    Thread.currentThread().setContextClassLoader(classLoader);
                    
                    System.out.println("Total de librerías cargadas: " + jars.length);
                } else {
                    System.err.println("No se encontraron archivos JAR en la carpeta lib");
                }
            } else {
                System.err.println("La carpeta 'lib' no existe o no es accesible");
            }
        } catch (Exception e) {
            System.err.println("Error al configurar librerías: " + e.getMessage());
        }
    }

    public static void calcularVelocidadConVoz() {
        try {
            // Solicitar datos con validación
            String kmStr = JOptionPane.showInputDialog(
                "Ingrese distancia en KM (0-99999):\n" +
                "(Ejemplo: 10 para 10 kilómetros)"
            );
            if (kmStr == null) return;
            if (kmStr.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe ingresar un valor");
                return;
            }
            int km = Integer.parseInt(kmStr.trim());
            
            String metrosStr = JOptionPane.showInputDialog(
                "Ingrese distancia en METROS (0-999):\n" +
                "(Ejemplo: 500 para 500 metros)"
            );
            if (metrosStr == null) return;
            if (metrosStr.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe ingresar un valor");
                return;
            }
            int metros = Integer.parseInt(metrosStr.trim());
            
            String horasStr = JOptionPane.showInputDialog(
                "Ingrese tiempo en HORAS (0-99999):\n" +
                "(Ejemplo: 2 para 2 horas)"
            );
            if (horasStr == null) return;
            if (horasStr.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe ingresar un valor");
                return;
            }
            int horas = Integer.parseInt(horasStr.trim());
            
            String minutosStr = JOptionPane.showInputDialog(
                "Ingrese tiempo en MINUTOS (0-59):\n" +
                "(Ejemplo: 30 para 30 minutos)"
            );
            if (minutosStr == null) return;
            if (minutosStr.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe ingresar un valor");
                return;
            }
            int minutos = Integer.parseInt(minutosStr.trim());

            // Validar rangos
            if (km < 0 || km > 99999 || metros < 0 || metros > 999 ||
                horas < 0 || horas > 99999 || minutos < 0 || minutos > 59) {

                JOptionPane.showMessageDialog(null, 
                    "Valores fuera de rango permitido.",
                    "Error de Validación",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Calcular velocidad
            double velocidad = calcularVelocidad(km, metros, horas, minutos);
            
            // Crear texto descriptivo
            String textoDescriptivo = convertirATexto(km, metros, horas, minutos);
            String textoCompleto = textoDescriptivo + 
                "\n\nVelocidad calculada: " + String.format("%.2f", velocidad) + " km/h";
            
            // Mostrar resultado en un área de texto con scroll
            JTextArea textArea = new JTextArea(textoCompleto);
            textArea.setEditable(false);
            textArea.setRows(8);
            textArea.setColumns(40);
            JScrollPane scrollPane = new JScrollPane(textArea);
            
            String[] opcionesAudio = {"Sí, reproducir con voz", "No, solo ver resultado"};
            int opcion = JOptionPane.showOptionDialog(
                null, 
                scrollPane,
                "Resultado del Cálculo",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                opcionesAudio,
                opcionesAudio[0]
            );
            
            // Preguntar si quiere escuchar el resultado
            if (opcion == 0) {
                String textoVoz = textoDescriptivo + " Velocidad " + 
                                 String.format("%.2f", velocidad) + " kilómetros por hora";
                hablar(textoVoz);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, 
                "Error: Debe ingresar números válidos.",
                "Error de Entrada",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error inesperado: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    public static void mostrarInstrucciones() {
        String instrucciones = 
            "╔════════════════════════════════════════╗\n" +
            "║        INSTRUCCIONES DE USO            ║\n" +
            "╚════════════════════════════════════════╝\n\n" +
            "📏 DISTANCIA:\n" +
            "• Kilómetros: 0 - 99,999\n" +
            "• Metros: 0 - 999\n\n" +
            "⏰ TIEMPO:\n" +
            "• Horas: 0 - 99,999\n" +
            "• Minutos: 0 - 59\n\n" +
            "📊 RESULTADO:\n" +
            "• La velocidad se calcula en km/h\n" +
            "• Puedes escuchar el resultado con voz\n\n" +
            "📁 LIBRERÍAS DETECTADAS:\n" +
            "• marytts-client-5.2\n" +
            "• marytts-runtime-5.2\n" +
            "• voice-cmu-slt-hsmm-5.2\n" +
            "• Y otros idiomas disponibles\n\n" +
            "🎯 NOTA:\n" +
            "Asegúrate que el archivo de voz esté en la carpeta 'lib'";
        
        JTextArea textArea = new JTextArea(instrucciones);
        textArea.setEditable(false);
        textArea.setRows(20);
        textArea.setColumns(50);
        JScrollPane scrollPane = new JScrollPane(textArea);
        
        JOptionPane.showMessageDialog(null, scrollPane, "Instrucciones", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void mostrarAcercaDe() {
        String acerca = 
            "╔════════════════════════════════════════╗\n" +
            "║    CALCULADORA DE VELOCIDAD CON VOZ    ║\n" +
            "╚════════════════════════════════════════╝\n\n" +
            "📌 Versión: 2.0\n\n" +
            "🛠️ Tecnologías utilizadas:\n" +
            "• Java Swing (Interfaz gráfica)\n" +
            "• MaryTTS (Síntesis de voz)\n" +
            "• JOptionPane (Diálogos)\n\n" +
            "🌐 Idiomas disponibles:\n" +
            "• Alemán, Inglés, Francés\n" +
            "• Italiano, Luxemburgués\n" +
            "• Ruso, Sueco, Telugu, Turco\n\n" +
            "🎤 Voz por defecto:\n" +
            "• cmu-slt-hsmm (Inglés)\n\n" +
            "© 2024 - Todos los derechos reservados";
        
        JTextArea textArea = new JTextArea(acerca);
        textArea.setEditable(false);
        textArea.setRows(18);
        textArea.setColumns(45);
        JScrollPane scrollPane = new JScrollPane(textArea);
        
        JOptionPane.showMessageDialog(null, scrollPane, "Acerca de", JOptionPane.INFORMATION_MESSAGE);
    }

    public static double calcularVelocidad(int km, int metros, int horas, int minutos) {
        double distanciaTotalKm = km + (metros / 1000.0);
        double tiempoTotalHoras = horas + (minutos / 60.0);

        if (tiempoTotalHoras == 0) {
            return 0;
        }
        return distanciaTotalKm / tiempoTotalHoras;
    }

    public static String convertirATexto(int km, int metros, int horas, int minutos) {
        return numeroEnLetras(km) + " kilómetros con " +
               numeroEnLetras(metros) + " metros en un tiempo de " +
               numeroEnLetras(horas) + " horas " +
               numeroEnLetras(minutos) + " minutos.";
    }

    public static String numeroEnLetras(int numero) {
        if (numero < 0) return "menos " + numeroEnLetras(Math.abs(numero));
        
        String[] unidades = {"cero", "uno", "dos", "tres", "cuatro", "cinco",
                "seis", "siete", "ocho", "nueve"};
        String[] especiales = {"diez", "once", "doce", "trece", "catorce", "quince",
                "dieciséis", "diecisiete", "dieciocho", "diecinueve"};
        String[] decenas = {"", "", "veinte", "treinta", "cuarenta", "cincuenta",
                "sesenta", "setenta", "ochenta", "noventa"};

        if (numero <= 9) {
            return unidades[numero];
        } else if (numero <= 19) {
            return especiales[numero - 10];
        } else if (numero <= 99) {
            int decena = numero / 10;
            int unidad = numero % 10;
            if (unidad == 0) {
                return decenas[decena];
            } else if (decena == 2) {
                return "veinti" + unidades[unidad];
            } else {
                return decenas[decena] + " y " + unidades[unidad];
            }
        } else if (numero <= 999) {
            if (numero == 100) {
                return "cien";
            } else if (numero < 200) {
                return "ciento " + numeroEnLetras(numero % 100);
            } else if (numero % 100 == 0) {
                String[] centenas = {"", "doscientos", "trescientos", "cuatrocientos",
                        "quinientos", "seiscientos", "setecientos", "ochocientos", "novecientos"};
                return centenas[numero / 100 - 1];
            } else {
                String[] centenas = {"", "doscientos", "trescientos", "cuatrocientos",
                        "quinientos", "seiscientos", "setecientos", "ochocientos", "novecientos"};
                return centenas[numero / 100 - 1] + " " + numeroEnLetras(numero % 100);
            }
        } else {
            // Para números mayores, simplemente devolvemos el número como cadena
            return String.valueOf(numero);
        }
    }

    public static void hablar(String texto) {
        try {
            System.out.println("Intentando hablar: " + texto);
            
            MaryInterface mary = new LocalMaryInterface();
            
            // Intentar con diferentes voces disponibles
            String[] voces = {"cmu-slt-hsmm", "cmu-rms-hsmm"};
            
            boolean exito = false;
            String vozUsada = "";
            
            for (String voz : voces) {
                try {
                    mary.setVoice(voz);
                    exito = true;
                    vozUsada = voz;
                    break;
                } catch (Exception e) {
                    System.out.println("Voz no disponible: " + voz);
                }
            }
            
            if (!exito) {
                JOptionPane.showMessageDialog(null, 
                    "No se encontraron voces disponibles.\n" +
                    "Verifica que el archivo voice-cmu-slt-hsmm-5.2.jar esté en lib",
                    "Error de Voz",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            System.out.println("Usando voz: " + vozUsada);
            
            // Configurar idioma (opcional)
            mary.setLocale(java.util.Locale.US); // Para inglés americano
            
            AudioInputStream audio = mary.generateAudio(texto);
            
            // Usar AudioPlayer para reproducción
            AudioPlayer player = new AudioPlayer(audio);
            player.start();
            
            JOptionPane.showMessageDialog(null, 
                "Reproduciendo mensaje...\nHaz clic en Aceptar para continuar",
                "Reproduciendo",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Esperar a que termine la reproducción
            player.join();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error al reproducir el audio:\n" + e.getMessage() +
                "\n\nVerifica que MaryTTS esté correctamente instalado.",
                "Error de Audio",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}