import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.util.data.audio.AudioPlayer;
import javax.sound.sampled.AudioInputStream;
import javax.swing.JOptionPane;
//import javax.swing.JScrollPane;
//import javax.swing.JTextArea;

public class VelocidadConVozGUI {

    public static void main(String[] args) {
        try {
            while (true) {
                String[] opciones = {"Calcular Velocidad", "Salir"};
                int seleccion = JOptionPane.showOptionDialog(
                    null,
                    "CALCULADORA DE VELOCIDAD\n" +
                    "Con sintesis de voz",
                    "Menu",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
                );

                if (seleccion == 0) {
                    calcularVelocidad();
                } else {
                    System.exit(0);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public static void calcularVelocidad() {
        try {
            String entrada = JOptionPane.showInputDialog("Ingrese distancia en KM:");
            if (entrada == null) return;
            int km = Integer.parseInt(entrada);

            entrada = JOptionPane.showInputDialog("Ingrese distancia en METROS:");
            if (entrada == null) return;
            int metros = Integer.parseInt(entrada);

            entrada = JOptionPane.showInputDialog("Ingrese tiempo en HORAS:");
            if (entrada == null) return;
            int horas = Integer.parseInt(entrada);

            entrada = JOptionPane.showInputDialog("Ingrese tiempo en MINUTOS:");
            if (entrada == null) return;
            int minutos = Integer.parseInt(entrada);

            // Validación básica
            if (minutos < 0 || minutos > 59) {
                JOptionPane.showMessageDialog(null, "Los minutos deben ser entre 0 y 59");
                return;
            }

            // Calculo
            double distanciaTotal = km + (metros / 1000.0);
            double tiempoTotal = horas + (minutos / 60.0);
            
            if (tiempoTotal == 0) {
                JOptionPane.showMessageDialog(null, "El tiempo no puede ser cero");
                return;
            }
            
            double velocidad = distanciaTotal / tiempoTotal;

            // resultado
            String resultado = String.format(
                "Distancia: %d km %d m\n" +
                "Tiempo: %d h %d min\n" +
                "Velocidad: %.2f km/h",
                km, metros, horas, minutos, velocidad
            );

            JOptionPane.showMessageDialog(null, resultado);

            // Preguntar si quiere escuchar
            int opcion = JOptionPane.showConfirmDialog(null, "¿Quieres escuchar el resultado con voz?");
            if (opcion == JOptionPane.YES_OPTION) {
                String textoVoz = String.format(
                    "%d kilometros con %d metros en %d horas %d minutos. Velocidad %.2f kilometros por hora",
                    km, metros, horas, minutos, velocidad
                );
                hablar(textoVoz);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: Ingrese numeros validos");
        }
    }

    public static void hablar(String texto) {
        try {
            System.out.println("Hablando: " + texto);
            
            MaryInterface mary = new LocalMaryInterface();
            mary.setVoice("cmu-slt-hsmm");
            
            AudioInputStream audio = mary.generateAudio(texto);
            AudioPlayer player = new AudioPlayer(audio);
            player.start();
            
            // Esperar un momento hasta que termine
            Thread.sleep(2000);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error al reproducir: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}