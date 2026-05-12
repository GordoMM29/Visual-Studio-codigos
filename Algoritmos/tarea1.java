//package Algoritmos;
import javax.swing.JOptionPane;

public class tarea1
 {

    private static String str1 = "";
    private static String str2 = "";
    private static char caracter = ' ';
    
    public static void main(String[] args) {
        boolean continuar = true;
        
        while (continuar) {
            String menu = "Menu\n\n" +
                "Datos actuales\n" +
                "1. String 1: \"" + str1 + "\"\n" +
                "2. String 2: \"" + str2 + "\"\n" +
                "3. Carácter: '" + caracter + "'\n\n" +
                "Que quieres hacer hoy?:\n" +
                "---------------------\n" +
                "4. Longitud\n" +
                "5. Caracter en posición\n" +
                "6. Comparación exacta\n" +
                "7. Sin may/min\n" +
                "8. Comparación lexicografica\n" +
                "9. Lexicografica sin case\n" +
                "10. Primera ocurrencia\n" +
                "11. Ultima ocurrencia\n" +
                "12. Comienza con\n" +
                "13. Termina con\n" +
                "14. Minusculas\n" +
                "15. Mayusculas\n" +
                "16. Concatenar\n" +
                "17. Eliminar espacios\n" +
                "18. Reemplazar caracteres\n" +
                "19. Extraer parte\n\n" +
                "0. Salir del programa.";
            
            String opcionStr = JOptionPane.showInputDialog(null, menu, 
                "15 Metodos de String", JOptionPane.PLAIN_MESSAGE);
            
            if (opcionStr == null) {
                continuar = false;
                break;
            }
            
            try {
                int opcion = Integer.parseInt(opcionStr);
                
                switch (opcion) {
                    case 0:
                        continuar = false;
                        JOptionPane.showMessageDialog(null, "¡Gracias por usar este programa!", 
                            "Fin", JOptionPane.INFORMATION_MESSAGE);
                        break;
                        
                    case 1:
                        str1 = JOptionPane.showInputDialog("Ingrese String 1:");
                        break;
                        
                    case 2:
                        str2 = JOptionPane.showInputDialog("Ingrese String 2:");
                        break;
                        
                    case 3:
                        String charInput = JOptionPane.showInputDialog("Ingrese un caracter:");
                        if (charInput != null && charInput.length() > 0) {
                            caracter = charInput.charAt(0);
                        }
                        break;
                        
                    case 4:
                        metodoLength();
                        break;
                        
                    case 5:
                        metodoCharAt();
                        break;
                        
                    case 6:
                        metodoEquals();
                        break;
                        
                    case 7:
                        metodoEqualsIgnoreCase();
                        break;
                        
                    case 8:
                        metodoCompareTo();
                        break;
                        
                    case 9:
                        metodoCompareToIgnoreCase();
                        break;
                        
                    case 10:
                        metodoIndexOf();
                        break;
                        
                    case 11:
                        metodoLastIndexOf();
                        break;
                        
                    case 12:
                        metodoStartsWith();
                        break;
                        
                    case 13:
                        metodoEndsWith();
                        break;
                        
                    case 14:
                        metodoToLowerCase();
                        break;
                        
                    case 15:
                        metodoToUpperCase();
                        break;
                        
                    case 16:
                        metodoConcat();
                        break;
                        
                    case 17:
                        metodoTrim();
                        break;
                        
                    case 18:
                        metodoReplace();
                        break;
                        
                    case 19:
                        metodoSubstring();
                        break;
                        
                    default:
                        JOptionPane.showMessageDialog(null, "Opcion no valida");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Ingrese un numero valido");
            }
        }
    }
    
    private static void metodoLength() {
        String seleccion = JOptionPane.showInputDialog("¿Sobre que string?\n1. String 1\n2. String 2\n3. Ambos");
        if (seleccion == null) return;
        
        String resultado = "=== length() ===\n\n";
        switch (seleccion) {
            case "1":
                resultado += "String 1: \"" + str1 + "\"\n";
                resultado += "length() = " + str1.length();
                break;
            case "2":
                resultado += "String 2: \"" + str2 + "\"\n";
                resultado += "length() = " + str2.length();
                break;
            case "3":
                resultado += "String 1: \"" + str1 + "\"\n";
                resultado += "length() = " + str1.length() + "\n\n";
                resultado += "String 2: \"" + str2 + "\"\n";
                resultado += "length() = " + str2.length();
                break;
        }
        JOptionPane.showMessageDialog(null, resultado);
    }
    
    private static void metodoCharAt() {
        String seleccion = JOptionPane.showInputDialog("¿Sobre que string?\n1. String 1\n2. String 2");
        if (seleccion == null) return;
        
        String str = seleccion.equals("1") ? str1 : str2;
        if (str.isEmpty()) {
            JOptionPane.showMessageDialog(null, "El string esta vacio");
            return;
        }
        
        String posStr = JOptionPane.showInputDialog("Posicion (0 a " + (str.length()-1) + "):");
        try {
            int pos = Integer.parseInt(posStr);
            if (pos >= 0 && pos < str.length()) {
                char c = str.charAt(pos);
                String resultado = "=== charAt() ===\n\n";
                resultado += "String: \"" + str + "\"\n";
                resultado += "charAt(" + pos + ") = '" + c + "'";
                JOptionPane.showMessageDialog(null, resultado);
            } else {
                JOptionPane.showMessageDialog(null, "Posicion fuera de rango");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Posicion invalida");
        }
    }
    
    private static void metodoEquals() {
        if (str1.isEmpty() || str2.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese ambos strings primero");
            return;
        }
        boolean resultado = str1.equals(str2);
        String mensaje = "=== equals() ===\n\n";
        mensaje += "String 1: \"" + str1 + "\"\n";
        mensaje += "String 2: \"" + str2 + "\"\n\n";
        mensaje += "equals() = " + resultado;
        JOptionPane.showMessageDialog(null, mensaje);
    }
    
    private static void metodoEqualsIgnoreCase() {
        if (str1.isEmpty() || str2.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese ambos strings primero");
            return;
        }
        boolean resultado = str1.equalsIgnoreCase(str2);
        String mensaje = "=== equalsIgnoreCase() ===\n\n";
        mensaje += "String 1: \"" + str1 + "\"\n";
        mensaje += "String 2: \"" + str2 + "\"\n\n";
        mensaje += "equalsIgnoreCase() = " + resultado;
        JOptionPane.showMessageDialog(null, mensaje);
    }
    
    private static void metodoCompareTo() {
        if (str1.isEmpty() || str2.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese ambos strings primero");
            return;
        }
        int resultado = str1.compareTo(str2);
        String comparacion;
        if (resultado == 0) comparacion = "iguales";
        else if (resultado < 0) comparacion = "String 1 es menor";
        else comparacion = "String 1 es mayor";
        
        String mensaje = "=== compareTo() ===\n\n";
        mensaje += "String 1: \"" + str1 + "\"\n";
        mensaje += "String 2: \"" + str2 + "\"\n\n";
        mensaje += "compareTo() = " + resultado + "\n";
        mensaje += comparacion + " (alfabéticamente)";
        JOptionPane.showMessageDialog(null, mensaje);
    }
    
    private static void metodoCompareToIgnoreCase() {
        if (str1.isEmpty() || str2.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese ambos strings primero");
            return;
        }
        int resultado = str1.compareToIgnoreCase(str2);
        String comparacion;
        if (resultado == 0) comparacion = "iguales";
        else if (resultado < 0) comparacion = "String 1 es menor";
        else comparacion = "String 1 es mayor";
        
        String mensaje = "=== compareToIgnoreCase() ===\n\n";
        mensaje += "String 1: \"" + str1 + "\"\n";
        mensaje += "String 2: \"" + str2 + "\"\n\n";
        mensaje += "compareToIgnoreCase() = " + resultado + "\n";
        mensaje += comparacion + " (ignorando mayusculas o minusculas)";
        JOptionPane.showMessageDialog(null, mensaje);
    }
    
    private static void metodoIndexOf() {
        if (str1.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese String 1 primero");
            return;
        }
        
        String buscar = JOptionPane.showInputDialog("¿Qué desea buscar en String 1?");
        if (buscar == null || buscar.isEmpty()) return;
        
        int posicion;
        if (buscar.length() == 1) {
            posicion = str1.indexOf(buscar.charAt(0));
        } else {
            posicion = str1.indexOf(buscar);
        }
        
        String mensaje = posicion >= 0 ? 
            "Encontrado en posición: " + posicion : 
            "No encontrado (-1)";
        
        String resultado = "=== indexOf() ===\n\n";
        resultado += "String: \"" + str1 + "\"\n";
        resultado += "Buscando: \"" + buscar + "\"\n\n";
        resultado += "indexOf() = " + posicion + "\n";
        resultado += mensaje;
        JOptionPane.showMessageDialog(null, resultado);
    }
    
    private static void metodoLastIndexOf() {
        if (str1.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese String 1 primero");
            return;
        }
        
        String buscar = JOptionPane.showInputDialog("¿Que desea buscar en String 1?");
        if (buscar == null || buscar.isEmpty()) return;
        
        int posicion;
        if (buscar.length() == 1) {
            posicion = str1.lastIndexOf(buscar.charAt(0));
        } else {
            posicion = str1.lastIndexOf(buscar);
        }
        
        String mensaje = posicion >= 0 ? 
            "Ultima aparicion en posicion: " + posicion : 
            "No encontrado (-1)";
        
        String resultado = "=== lastIndexOf() ===\n\n";
        resultado += "String: \"" + str1 + "\"\n";
        resultado += "Buscando: \"" + buscar + "\"\n\n";
        resultado += "lastIndexOf() = " + posicion + "\n";
        resultado += mensaje;
        JOptionPane.showMessageDialog(null, resultado);
    }
    
    private static void metodoStartsWith() {
        if (str1.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese String 1 primero");
            return;
        }
        
        String prefijo = JOptionPane.showInputDialog("¿Con que prefijo desea verificar?");
        if (prefijo == null) return;
        
        boolean resultado = str1.startsWith(prefijo);
        String mensaje = resultado ? 
            "SI comienza con \"" + prefijo + "\"" : 
            "NO comienza con \"" + prefijo + "\"";
        
        String output = "=== startsWith() ===\n\n";
        output += "String: \"" + str1 + "\"\n";
        output += "Prefijo: \"" + prefijo + "\"\n\n";
        output += "startsWith() = " + resultado + "\n";
        output += mensaje;
        JOptionPane.showMessageDialog(null, output);
    }
    
    private static void metodoEndsWith() {
        if (str1.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese String 1 primero");
            return;
        }
        
        String sufijo = JOptionPane.showInputDialog("¿Con que sufijo desea verificar?");
        if (sufijo == null) return;
        
        boolean resultado = str1.endsWith(sufijo);
        String mensaje = resultado ? 
            "SI termina con \"" + sufijo + "\"" : 
            "NO termina con \"" + sufijo + "\"";
        
        String output = "=== endsWith() ===\n\n";
        output += "String: \"" + str1 + "\"\n";
        output += "Sufijo: \"" + sufijo + "\"\n\n";
        output += "endsWith() = " + resultado + "\n";
        output += mensaje;
        JOptionPane.showMessageDialog(null, output);
    }
    
    private static void metodoToLowerCase() {
        if (str1.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese String 1 primero");
            return;
        }
        
        String resultado = str1.toLowerCase();
        String output = "=== toLowerCase() ===\n\n";
        output += "Original: \"" + str1 + "\"\n";
        output += "Resultado: \"" + resultado + "\"";
        JOptionPane.showMessageDialog(null, output);
    }
    
    private static void metodoToUpperCase() {
        if (str1.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese String 1 primero");
            return;
        }
        
        String resultado = str1.toUpperCase();
        String output = "=== toUpperCase() ===\n\n";
        output += "Original: \"" + str1 + "\"\n";
        output += "Resultado: \"" + resultado + "\"";
        JOptionPane.showMessageDialog(null, output);
    }
    
    private static void metodoConcat() {
        if (str1.isEmpty() || str2.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese ambos strings primero");
            return;
        }
        
        String resultado = str1.concat(str2);
        String output = "=== concat() ===\n\n";
        output += "String 1: \"" + str1 + "\"\n";
        output += "String 2: \"" + str2 + "\"\n\n";
        output += "concat() = \"" + resultado + "\"\n\n";
        output += "Con operador + = \"" + (str1 + str2) + "\"";
        JOptionPane.showMessageDialog(null, output);
    }
    
    private static void metodoTrim() {
        if (str1.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese String 1 primero");
            return;
        }
        
        String resultado = str1.trim();
        String output = "=== trim() ===\n\n";
        output += "Original: \"" + str1 + "\"\n";
        output += "Longitud: " + str1.length() + "\n\n";
        output += "Resultado: \"" + resultado + "\"\n";
        output += "Longitud: " + resultado.length() + "\n\n";
        output += "Espacios eliminados: " + (str1.length() - resultado.length());
        JOptionPane.showMessageDialog(null, output);
    }
    
    private static void metodoReplace() {
        if (str1.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese String 1 primero");
            return;
        }
        
        if (caracter == ' ') {
            JOptionPane.showMessageDialog(null, "Primero ingrese un caracter (opción 3)");
            return;
        }
        
        String nuevoCharStr = JOptionPane.showInputDialog("Ingrese el nuevo caracter:");
        if (nuevoCharStr == null || nuevoCharStr.length() != 1) return;
        
        char nuevoChar = nuevoCharStr.charAt(0);
        String resultado = str1.replace(caracter, nuevoChar);
        
        String output = "=== replace() ===\n\n";
        output += "Original: \"" + str1 + "\"\n";
        output += "Reemplazar: '" + caracter + "' por '" + nuevoChar + "'\n\n";
        output += "Resultado: \"" + resultado + "\"";
        JOptionPane.showMessageDialog(null, output);
    }
    
    private static void metodoSubstring() {
        if (str1.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese String 1 primero");
            return;
        }
        
        if (str1.length() < 2) {
            JOptionPane.showMessageDialog(null, "El string debe tener al menos 2 caracteres");
            return;
        }
        
        String iniStr = JOptionPane.showInputDialog("Posición inicial (0 a " + (str1.length()-2) + "):");
        if (iniStr == null) return;
        
        try {
            int inicio = Integer.parseInt(iniStr);
            if (inicio < 0 || inicio >= str1.length()-1) {
                JOptionPane.showMessageDialog(null, "Posicion fuera de rango");
                return;
            }
            
            String finStr = JOptionPane.showInputDialog("Posicion final (" + (inicio+1) + " a " + str1.length() + "):");
            if (finStr == null) return;
            
            int fin = Integer.parseInt(finStr);
            if (fin <= inicio || fin > str1.length()) {
                JOptionPane.showMessageDialog(null, "Posicion final invalida");
                return;
            }
            
            String resultado = str1.substring(inicio, fin);
            String output = "=== substring() ===\n\n";
            output += "Original: \"" + str1 + "\"\n";
            output += "substring(" + inicio + ", " + fin + ")\n\n";
            output += "Resultado: \"" + resultado + "\"\n";
            output += "Longitud: " + resultado.length();
            JOptionPane.showMessageDialog(null, output);
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Entrada invalida");
        }
    }
}

