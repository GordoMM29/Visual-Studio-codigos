import javax.swing.*;
import java.util.*;

public class tarea11 {
    
    private Set<Integer> universal;
    private Set<Integer> pares;
    private Set<Integer> impares;
    private Set<Integer> primos;
    
    // Diferentes tipos 
    private Set<Integer> paresTreeSet;
    private Set<Integer> imparesHashSet;
    private Set<Integer> primosLinkedHashSet;
    
    public tarea11() {
        universal = new TreeSet<>();
        pares = new TreeSet<>();
        impares = new TreeSet<>();
        primos = new TreeSet<>();
        
        paresTreeSet = new TreeSet<>();
        imparesHashSet = new HashSet<>();
        primosLinkedHashSet = new LinkedHashSet<>();
        
        // Crear conjunto universal 
        for (int i = 0; i <= 200; i++) {
            universal.add(i);
        }
        
        // Separar numeros
        separarNumeros();
    }
    
    private void separarNumeros() {
        for (int i = 0; i <= 200; i++) {
            if (i % 2 == 0) {
                pares.add(i);
                paresTreeSet.add(i);
            } else {
                impares.add(i);
                imparesHashSet.add(i);
            }
            
            if (esPrimo(i)) {
                primos.add(i);
                primosLinkedHashSet.add(i);
            }
        }
    }
    
    private boolean esPrimo(int numero) {
        if (numero < 2) return false;
        for (int i = 2; i <= Math.sqrt(numero); i++) {
            if (numero % i == 0) return false;
        }
        return true;
    }
    
    public void iniciar() {
        while (true) {
            String menu = "Operaciones de conjuntos numericos\n\n" +
                         "1. Mostrar todos los conjuntos\n" +
                         "2. Eliminar un valor de los conjuntos\n" +
                         "3. Union de Impares y Primos\n" +
                         "4. Union de Pares y Primos\n" +
                         "5. Interseccion de Impares con Primos\n" +
                         "6. Interseccion de Pares con Primos\n" +
                         "7. Diferencia de Impares - Primos\n" +
                         "8. Diferencia de Pares - Primos\n" +
                         "9. Complemento de Impares con Primos\n" +
                         "10. HeadSet y TailSet de Pares\n" +
                         "11. Verificar duplicados\n" +
                         "0. Salir\n\n" +
                         "Que quieres hacer hoy?";
            
            String opcion = JOptionPane.showInputDialog(null, menu, "Menu", JOptionPane.QUESTION_MESSAGE);
            
            if (opcion == null || opcion.equals("0")) {
                JOptionPane.showMessageDialog(null, "Adios");
                break;
            }
            
            procesarOpcion(opcion);
        }
    }
    
    private void procesarOpcion(String opcion) {
        switch (opcion) {
            case "1":
                mostrarTodosConjuntos();
                break;
            case "2":
                eliminarValor();
                break;
            case "3":
                unionImparesPrimos();
                break;
            case "4":
                unionParesPrimos();
                break;
            case "5":
                interseccionImparesPrimos();
                break;
            case "6":
                interseccionParesPrimos();
                break;
            case "7":
                diferenciaImparesPrimos();
                break;
            case "8":
                diferenciaParesPrimos();
                break;
            case "9":
                complementoImparesPrimos();
                break;
            case "10":
                headSetTailSetPares();
                break;
            case "11":
                verificarDuplicados();
                break;
            default:
                JOptionPane.showMessageDialog(null, "Opcion no valida", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void mostrarTodosConjuntos() {
        String reporte = "CONJUNTOS NUMERICOS \n\n";
        
        reporte += "UNIVERSAL (0-200):\n";
        reporte += "Tamaño: " + universal.size() + " elementos\n";
        reporte += "Primeros 20: " + obtenerPrimeros(universal, 20) + "\n\n";
        
        reporte += "PARES (TreeSet):\n";
        reporte += "Tamaño: " + pares.size() + " elementos\n";
        reporte += "Elementos: " + obtenerPrimeros(pares, 30) + "\n\n";
        
        reporte += "IMPARES (HashSet):\n";
        reporte += "Tamaño: " + impares.size() + " elementos\n";
        reporte += "Elementos: " + obtenerPrimeros(impares, 30) + "\n\n";
        
        reporte += "PRIMOS (LinkedHashSet):\n";
        reporte += "Tamaño: " + primos.size() + " elementos\n";
        reporte += "Elementos: " + obtenerPrimeros(primos, 30) + "\n\n";
        
        reporte += "PARES (TreeSet - especifico):\n";
        reporte += "Tamaño: " + paresTreeSet.size() + " elementos\n\n";
        
        reporte += "IMPARES (HashSet - especifico):\n";
        reporte += "Tamaño: " + imparesHashSet.size() + " elementos\n\n";
        
        reporte += "PRIMOS (LinkedHashSet - especifico):\n";
        reporte += "Tamaño: " + primosLinkedHashSet.size() + " elementos";
        
        mostrarReporte(reporte);
    }
    
    private void eliminarValor() {
        try {
            String valorStr = JOptionPane.showInputDialog("Ingrese el valor a eliminar (0-200):");
            if (valorStr == null) return;
            
            int valor = Integer.parseInt(valorStr);
            
            if (valor < 0 || valor > 200) {
                JOptionPane.showMessageDialog(null, "El valor debe estar entre 0 y 200", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            boolean eliminadoUniversal = universal.remove(valor);
            boolean eliminadoPares = pares.remove(valor);
            boolean eliminadoImpares = impares.remove(valor);
            boolean eliminadoPrimos = primos.remove(valor);
            boolean eliminadoParesTS = paresTreeSet.remove(valor);
            boolean eliminadoImparesHS = imparesHashSet.remove(valor);
            boolean eliminadoPrimosLHS = primosLinkedHashSet.remove(valor);
            
            String reporte = "Resultado de eliminacion\n\n";
            reporte += "Valor eliminado: " + valor + "\n\n";
            reporte += "Universal: " + (eliminadoUniversal ? " Eliminado" : " No encontrado") + "\n";
            reporte += "Pares: " + (eliminadoPares ? " Eliminado" : " No encontrado") + "\n";
            reporte += "Impares: " + (eliminadoImpares ? " Eliminado" : " No encontrado") + "\n";
            reporte += "Primos: " + (eliminadoPrimos ? " Eliminado" : " No encontrado") + "\n";
            reporte += "Pares (TreeSet): " + (eliminadoParesTS ? " Eliminado" : " No encontrado") + "\n";
            reporte += "Impares (HashSet): " + (eliminadoImparesHS ? "Eliminado" : " No encontrado") + "\n";
            reporte += "Primos (LinkedHashSet): " + (eliminadoPrimosLHS ? " Eliminado" : " No encontrado");
            
            mostrarReporte(reporte);
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: Ingrese un numero valido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void unionImparesPrimos() {
        Set<Integer> union = new TreeSet<>(impares);
        union.addAll(primos);
        
        String reporte = "UNION: Impares ∪ Primos \n\n";
        reporte += "Impares: " + impares.size() + " elementos\n";
        reporte += "Primos: " + primos.size() + " elementos\n";
        reporte += "Union: " + union.size() + " elementos\n\n";
        reporte += "Elementos de la union:\n";
        reporte += obtenerPrimeros(union, 50);
        
        mostrarReporte(reporte);
    }
    
    private void unionParesPrimos() {
        Set<Integer> union = new TreeSet<>(pares);
        union.addAll(primos);
        
        String reporte = "UNION: Pares ∪ Primos \n\n";
        reporte += "Pares: " + pares.size() + " elementos\n";
        reporte += "Primos: " + primos.size() + " elementos\n";
        reporte += "Union: " + union.size() + " elementos\n\n";
        reporte += "Elementos de la union:\n";
        reporte += obtenerPrimeros(union, 50);
        
        mostrarReporte(reporte);
    }
    
    private void interseccionImparesPrimos() {
        Set<Integer> interseccion = new TreeSet<>(impares);
        interseccion.retainAll(primos);
        
        String reporte = "INTERSECCION: Impares ∩ Primos \n\n";
        reporte += "Impares: " + impares.size() + " elementos\n";
        reporte += "Primos: " + primos.size() + " elementos\n";
        reporte += "Interseccion: " + interseccion.size() + " elementos\n\n";
        reporte += "Numeros primos impares:\n";
        reporte += interseccion.toString();
        
        mostrarReporte(reporte);
    }
    
    private void interseccionParesPrimos() {
        Set<Integer> interseccion = new TreeSet<>(pares);
        interseccion.retainAll(primos);
        
        String reporte = "INTERSECCION: Pares ∩ Primos \n\n";
        reporte += "Pares: " + pares.size() + " elementos\n";
        reporte += "Primos: " + primos.size() + " elementos\n";
        reporte += "Interseccion: " + interseccion.size() + " elementos\n\n";
        reporte += "Numeros primos pares:\n";
        reporte += interseccion.toString() + "\n";
        reporte += "(Nota: El unico primo par es el 2)";
        
        mostrarReporte(reporte);
    }
    
    private void diferenciaImparesPrimos() {
        Set<Integer> diferencia = new TreeSet<>(impares);
        diferencia.removeAll(primos);
        
        String reporte = "=== DIFERENCIA: Impares - Primos ===\n\n";
        reporte += "Impares: " + impares.size() + " elementos\n";
        reporte += "Primos: " + primos.size() + " elementos\n";
        reporte += "Diferencia: " + diferencia.size() + " elementos\n\n";
        reporte += "Impares que no son primos:\n";
        reporte += obtenerPrimeros(diferencia, 50);
        
        mostrarReporte(reporte);
    }
    
    private void diferenciaParesPrimos() {
        Set<Integer> diferencia = new TreeSet<>(pares);
        diferencia.removeAll(primos);
        
        String reporte = "DIFERENCIA: Pares - Primos \n\n";
        reporte += "Pares: " + pares.size() + " elementos\n";
        reporte += "Primos: " + primos.size() + " elementos\n";
        reporte += "Diferencia: " + diferencia.size() + " elementos\n\n";
        reporte += "Pares que no son primos:\n";
        reporte += obtenerPrimeros(diferencia, 50);
        
        mostrarReporte(reporte);
    }
    
    private void complementoImparesPrimos() {
        Set<Integer> complemento = new TreeSet<>(universal);
        complemento.removeAll(impares);
        complemento.removeAll(primos);
        
        String reporte = "COMPLEMENTO: Universal - (Impares ∪ Primos) \n\n";
        reporte += "Universal: " + universal.size() + " elementos\n";
        reporte += "Impares ∪ Primos: " + (impares.size() + primos.size() - interseccionSize(impares, primos)) + " elementos\n";
        reporte += "Complemento: " + complemento.size() + " elementos\n\n";
        reporte += "Elementos que no son impares ni primos:\n";
        reporte += obtenerPrimeros(complemento, 50);
        
        mostrarReporte(reporte);
    }
    
    private void headSetTailSetPares() {
        try {
            String valorStr = JOptionPane.showInputDialog("Ingrese un numero de referencia para HeadSet y TailSet (0-200):");
            if (valorStr == null) return;
            
            int valorReferencia = Integer.parseInt(valorStr);
            
            if (!paresTreeSet.contains(valorReferencia)) {
                JOptionPane.showMessageDialog(null, 
                    "El numero " + valorReferencia + " no esta en el conjunto de pares", 
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
            
            String reporte = "HeadSet y TailSet del conjunto de Pares (TreeSet) \n\n";
            reporte += "Numero de referencia: " + valorReferencia + "\n\n";
            
            // HeadSet 
            SortedSet<Integer> headSet = ((TreeSet<Integer>) paresTreeSet).headSet(valorReferencia);
            reporte += "HEAD SET (elementos < " + valorReferencia + "):\n";
            reporte += "Tamaño: " + headSet.size() + " elementos\n";
            reporte += "Elementos: " + obtenerPrimeros(headSet, 30) + "\n\n";
            
            // TailSet 
            SortedSet<Integer> tailSet = ((TreeSet<Integer>) paresTreeSet).tailSet(valorReferencia);
            reporte += "TAIL SET (elementos >= " + valorReferencia + "):\n";
            reporte += "Tamaño: " + tailSet.size() + " elementos\n";
            reporte += "Elementos: " + obtenerPrimeros(tailSet, 30);
            
            mostrarReporte(reporte);
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: Ingrese un numero valido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void verificarDuplicados() {
        String reporte = "Verificacion de duplicados \n\n";
        
        // Verificar pares
        reporte += "PARES (TreeSet):\n";
        reporte += "Tamaño esperado: 101 (0-200 pares)\n";
        reporte += "Tamaño real: " + pares.size() + "\n";
        reporte += "¿Sin duplicados?: " + (pares.size() == 101 ? "✓ Si" : "✗ No") + "\n\n";
        
        // Verificar impares
        reporte += "IMPARES (HashSet):\n";
        reporte += "Tamaño esperado: 100 (1-199 impares)\n";
        reporte += "Tamaño real: " + impares.size() + "\n";
        reporte += "¿Sin duplicados?: " + (impares.size() == 100 ? "✓ Si" : "✗ No") + "\n\n";
        
        // Verificar primos
        reporte += "PRIMOS (LinkedHashSet):\n";
        reporte += "Tamaño esperado: 46 (primos entre 0-200)\n";
        reporte += "Tamaño real: " + primos.size() + "\n";
        reporte += "¿Sin duplicados?: " + (primos.size() == 46 ? "✓ Si" : "✗ No") + "\n\n";
        
        reporte += "Los conjuntos en Java automaticamente evitan duplicados.";
        
        mostrarReporte(reporte);
    }
    
    private int interseccionSize(Set<Integer> set1, Set<Integer> set2) {
        Set<Integer> temp = new TreeSet<>(set1);
        temp.retainAll(set2);
        return temp.size();
    }
    
    private String obtenerPrimeros(Set<Integer> conjunto, int cantidad) {
        StringBuilder sb = new StringBuilder();
        int contador = 0;
        for (Integer num : conjunto) {
            if (contador >= cantidad) {
                sb.append("...");
                break;
            }
            if (contador > 0) sb.append(", ");
            sb.append(num);
            contador++;
        }
        return sb.toString();
    }
    
    private String obtenerPrimeros(SortedSet<Integer> conjunto, int cantidad) {
        StringBuilder sb = new StringBuilder();
        int contador = 0;
        for (Integer num : conjunto) {
            if (contador >= cantidad) {
                sb.append("...");
                break;
            }
            if (contador > 0) sb.append(", ");
            sb.append(num);
            contador++;
        }
        return sb.toString();
    }
    
    private void mostrarReporte(String contenido) {
        JTextArea textArea = new JTextArea(20, 60);
        textArea.setText(contenido);
        textArea.setEditable(false);
        textArea.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);
        
        JOptionPane.showMessageDialog(null, scrollPane, "Reporte de Conjuntos", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        tarea11 programa = new tarea11();
        programa.iniciar();
    }
}