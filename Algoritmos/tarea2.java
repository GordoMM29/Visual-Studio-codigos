//package Algoritmos;
import javax.swing.JOptionPane;

public class tarea2 {
    
    private String cadena;
    
    public tarea2(String cadena) {
        this.cadena = cadena;
    }
    
    public void leerString() {
        this.cadena = JOptionPane.showInputDialog(null, 
            "Ingrese una cadena de texto:", 
            "Entrada de datos", 
            JOptionPane.PLAIN_MESSAGE);
        
        if (this.cadena == null) {
            this.cadena = ""; 
        }
    }
    
    public int obtenerLongitud() {
        return cadena.length();
    }
    
    public String analizarLetras() {
        int vocales = 0;
        int consonantes = 0;
        int letras = 0;
        
        for (int i = 0; i < cadena.length(); i++) {
            char c = Character.toLowerCase(cadena.charAt(i));
            if (Character.isLetter(c)) {
                letras++;
                if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u' ||
                    c == 'á' || c == 'é' || c == 'í' || c == 'ó' || c == 'ú' ||
                    c == 'ü') {
                    vocales++;
                } else {
                    consonantes++;
                }
            }
        }
        
        return "Número de letras: " + letras + 
               "\nVocales: " + vocales + 
               "\nConsonantes: " + consonantes;
    }
    
    public int obtenerDigitos() {
        int digitos = 0;
        for (int i = 0; i < cadena.length(); i++) {
            if (Character.isDigit(cadena.charAt(i))) {
                digitos++;
            }
        }
        return digitos;
    }
    
    public int obtenerEspacios() {
        int espacios = 0;
        for (int i = 0; i < cadena.length(); i++) {
            if (Character.isWhitespace(cadena.charAt(i))) {
                espacios++;
            }
        }
        return espacios;
    }
    
    public int obtenerCaracteresEspeciales() {
        int especiales = 0;
        for (int i = 0; i < cadena.length(); i++) {
            char c = cadena.charAt(i);
            if (!Character.isLetter(c) && !Character.isDigit(c) && !Character.isWhitespace(c)) {
                especiales++;
            }
        }
        return especiales;
    }

    public int obtenerNumeroPalabras() {
        if (cadena == null || cadena.trim().isEmpty()) {
            return 0;
        }
        
        String[] palabras = cadena.trim().split("\\s+");
        return palabras.length;
    }
    
    public String convertirMayusculas() {
        return cadena.toUpperCase();
    }
    
    public String convertirMinusculas() {
        return cadena.toLowerCase();
    }
    
    public int obtenerPalabrasInicianConVocal() {
        if (cadena == null || cadena.trim().isEmpty()) {
            return 0;
        }
        
        String[] palabras = cadena.trim().split("\\s+");
        int contador = 0;
        
        for (String palabra : palabras) {
            if (!palabra.isEmpty()) {
                char primeraLetra = Character.toLowerCase(palabra.charAt(0));
                if (primeraLetra == 'a' || primeraLetra == 'e' || primeraLetra == 'i' || 
                    primeraLetra == 'o' || primeraLetra == 'u' ||
                    primeraLetra == 'á' || primeraLetra == 'é' || primeraLetra == 'í' ||
                    primeraLetra == 'ó' || primeraLetra == 'ú' || primeraLetra == 'ü') {
                    contador++;
                }
            }
        }
        
        return contador;
    }
    
    public String encriptarCesar() {
        StringBuilder encriptado = new StringBuilder();
        for (int i = 0; i < cadena.length(); i++) {
            char c = cadena.charAt(i);
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                c = (char) (((c - base + 3) % 26) + base);
            }
            encriptado.append(c);
        }
        return encriptado.toString();
    }
    
    public String desencriptarCesar() {
        StringBuilder desencriptado = new StringBuilder();
        for (int i = 0; i < cadena.length(); i++) {
            char c = cadena.charAt(i);
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                c = (char) (((c - base - 3 + 26) % 26) + base);
            }
            desencriptado.append(c);
        }
        return desencriptado.toString();
    }
    
    public String encriptarRot3Ascii() {
        StringBuilder encriptado = new StringBuilder();
        for (int i = 0; i < cadena.length(); i++) {
            char c = cadena.charAt(i);
            encriptado.append((char)(c + 3));
        }
        return encriptado.toString();
    }
    
    public String desencriptarRot3Ascii() {
        StringBuilder desencriptado = new StringBuilder();
        for (int i = 0; i < cadena.length(); i++) {
            char c = cadena.charAt(i);
            desencriptado.append((char)(c - 3));
        }
        return desencriptado.toString();
    }
    
    public String encriptarXor() {
        StringBuilder encriptado = new StringBuilder();
        char clave = 'K';
        for (int i = 0; i < cadena.length(); i++) {
            char c = cadena.charAt(i);
            encriptado.append((char)(c ^ clave));
        }
        return encriptado.toString();
    }
    
    public String desencriptarXor() {
        return encriptarXor();
    }
    
    public String encriptarJulioVerne() {
        StringBuilder encriptado = new StringBuilder();
        for (int i = 0; i < cadena.length(); i++) {
            char c = cadena.charAt(i);
            if (Character.isLetter(c)) {
                if (Character.isLowerCase(c)) {
                    c = Character.toUpperCase(c);
                } else {
                    c = Character.toLowerCase(c);
                }
            }
            encriptado.append(c);
        }
        return encriptado.toString();
    }
    
    public String desencriptarJulioVerne() {
        return encriptarJulioVerne();
    }
    
    public String encriptarAtbash() {
        StringBuilder encriptado = new StringBuilder();
        for (int i = 0; i < cadena.length(); i++) {
            char c = cadena.charAt(i);
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                c = (char)(base + (25 - (c - base)));
            }
            encriptado.append(c);
        }
        return encriptado.toString();
    }
    
    public String desencriptarAtbash() {
        return encriptarAtbash();
    }
    
    public String encriptarSustitucion() {
        String alfabeto = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        String sustitucion = "ZYXWVUTSRQPONMLKJIHGFEDCBAzyxwvutsrqponmlkjihgfedcba";
        StringBuilder encriptado = new StringBuilder();
        
        for (int i = 0; i < cadena.length(); i++) {
            char c = cadena.charAt(i);
            int index = alfabeto.indexOf(c);
            if (index != -1) {
                encriptado.append(sustitucion.charAt(index));
            } else {
                encriptado.append(c);
            }
        }
        return encriptado.toString();
    }
    
    public String desencriptarSustitucion() {
        String alfabeto = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        String sustitucion = "ZYXWVUTSRQPONMLKJIHGFEDCBAzyxwvutsrqponmlkjihgfedcba";
        StringBuilder desencriptado = new StringBuilder();
        
        for (int i = 0; i < cadena.length(); i++) {
            char c = cadena.charAt(i);
            int index = sustitucion.indexOf(c);
            if (index != -1) {
                desencriptado.append(alfabeto.charAt(index));
            } else {
                desencriptado.append(c);
            }
        }
        return desencriptado.toString();
    }
    
    public String invertirStringRecursivo() {
        return invertirRecursivo(cadena);
    }
    
    private String invertirRecursivo(String str) {
        if (str.isEmpty() || str.length() == 1) {
            return str;
        }
        return str.charAt(str.length() - 1) + 
               invertirRecursivo(str.substring(0, str.length() - 1));
    }
    
    public String invertirString() {
        StringBuilder invertido = new StringBuilder();
        for (int i = cadena.length() - 1; i >= 0; i--) {
            invertido.append(cadena.charAt(i));
        }
        return invertido.toString();
    }
    
    public void mostrarAnalisisCompleto() {
        StringBuilder resultado = new StringBuilder();
        resultado.append("=== ANÁLISIS COMPLETO DE LA CADENA ===\n\n");
        resultado.append("Cadena original: ").append(cadena).append("\n");
        resultado.append("----------------------------------------\n");
        resultado.append("Longitud total: ").append(obtenerLongitud()).append(" caracteres\n");
        resultado.append("Análisis de letras:\n").append(analizarLetras()).append("\n");
        resultado.append("Dígitos numéricos: ").append(obtenerDigitos()).append("\n");
        resultado.append("Espacios en blanco: ").append(obtenerEspacios()).append("\n");
        resultado.append("Caracteres especiales: ").append(obtenerCaracteresEspeciales()).append("\n");
        resultado.append("Número total de palabras: ").append(obtenerNumeroPalabras()).append("\n");
        resultado.append("Palabras que inician con vocal: ").append(obtenerPalabrasInicianConVocal()).append("\n");
        resultado.append("----------------------------------------\n");
        resultado.append("Convertido a MAYÚSCULAS: ").append(convertirMayusculas()).append("\n");
        resultado.append("Convertido a minúsculas: ").append(convertirMinusculas()).append("\n");
        resultado.append("Cadena invertida (iterativo): ").append(invertirString()).append("\n");
        resultado.append("Cadena invertida (recursivo): ").append(invertirStringRecursivo()).append("\n");
        resultado.append("----------------------------------------\n");
        resultado.append("ENCRIPTACIONES:\n");
        resultado.append("César: ").append(encriptarCesar()).append("\n");
        resultado.append("Julio Verne: ").append(encriptarJulioVerne()).append("\n");
        resultado.append("Atbash: ").append(encriptarAtbash()).append("\n");
        resultado.append("ROT3 ASCII: ").append(encriptarRot3Ascii()).append("\n");
        resultado.append("XOR: ").append(encriptarXor()).append("\n");
        resultado.append("Sustitución: ").append(encriptarSustitucion());
        
        JOptionPane.showMessageDialog(null, 
            resultado.toString(), 
            "Resultado del Análisis", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void main(String[] args) {
        String entrada = JOptionPane.showInputDialog(null, 
            "Ingrese la cadena de texto a analizar:", 
            "Analizador de Cadenas", 
            JOptionPane.PLAIN_MESSAGE);
        
        if (entrada == null) {
            entrada = ""; 
        }
        
        tarea2 analizador = new tarea2(entrada);
        
        String[] opciones = {
            " Leer nueva cadena",
            " Obtener longitud",
            " Analizar letras (vocales y consonantes)",
            " Obtener número de dígitos",
            " Obtener número de espacios",
            " Obtener caracteres especiales",
            " Obtener número de palabras",
            " Convertir a mayúsculas",
            " Convertir a minúsculas",
            " Obtener palabras que inician con vocal",
            " Invertir cadena (iterativo)",
            " Invertir cadena (recursivo)",
            " Mostrar todos los análisis",
            " ===== ENCRIPTACIÓN =====",
            " Encriptar César",
            " Desencriptar César",
            " Encriptar Julio Verne",
            " Desencriptar Julio Verne",
            " Encriptar ROT3 ASCII",
            " Desencriptar ROT3 ASCII",
            " Encriptar Atbash",
            " Desencriptar Atbash",
            " Encriptar XOR",
            " Desencriptar XOR",
            " Encriptar Sustitución",
            " Desencriptar Sustitución",
            " Salir"
        };
        
        int opcion;
        do {
            String cadenaActual = analizador.cadena.isEmpty() ? 
                "(Cadena vacía)" : 
                "\"" + analizador.cadena + "\"";
            
            String mensajeMenu = "Cadena actual: " + cadenaActual + 
                                "\n\nSeleccione una opción:\n\n";
            
            for (int i = 0; i < opciones.length; i++) {
                mensajeMenu += (i + 1) + ". " + opciones[i] + "\n";
            }
            
            String opcionSeleccionada = (String) JOptionPane.showInputDialog(null,
                mensajeMenu,
                "Menú Principal - Analizador de Cadenas",
                JOptionPane.PLAIN_MESSAGE,
                null,
                opciones,
                opciones[0]);
            
            if (opcionSeleccionada == null) {
                break;
            }
            
            opcion = -1;
            for (int i = 0; i < opciones.length; i++) {
                if (opcionSeleccionada.equals(opciones[i])) {
                    opcion = i;
                    break;
                }
            }
            
            if (opcion == -1) {
                continue; 
            }
            
            switch (opcion) {
                case 0: 
                    analizador.leerString();
                    JOptionPane.showMessageDialog(null, 
                        "Cadena actualizada:\n\"" + analizador.cadena + "\"", 
                        "Cadena Actualizada", 
                        JOptionPane.INFORMATION_MESSAGE);
                    break;
                    
                case 1: 
                    JOptionPane.showMessageDialog(null, 
                        "Longitud de la cadena: " + analizador.obtenerLongitud() + " caracteres", 
                        "Longitud", 
                        JOptionPane.INFORMATION_MESSAGE);
                    break;
                    
                case 2: 
                    JOptionPane.showMessageDialog(null, 
                        analizador.analizarLetras(), 
                        "Análisis de Letras", 
                        JOptionPane.INFORMATION_MESSAGE);
                    break;
                    
                case 3: 
                    JOptionPane.showMessageDialog(null, 
                        "Número de dígitos: " + analizador.obtenerDigitos(), 
                        "Dígitos", 
                        JOptionPane.INFORMATION_MESSAGE);
                    break;
                    
                case 4:
                    JOptionPane.showMessageDialog(null, 
                        "Número de espacios: " + analizador.obtenerEspacios(), 
                        "Espacios", 
                        JOptionPane.INFORMATION_MESSAGE);
                    break;
                    
                case 5: 
                    JOptionPane.showMessageDialog(null, 
                        "Caracteres especiales: " + analizador.obtenerCaracteresEspeciales(), 
                        "Caracteres Especiales", 
                        JOptionPane.INFORMATION_MESSAGE);
                    break;
                    
                case 6: 
                    JOptionPane.showMessageDialog(null, 
                        "Número de palabras: " + analizador.obtenerNumeroPalabras(), 
                        "Palabras", 
                        JOptionPane.INFORMATION_MESSAGE);
                    break;
                    
                case 7: 
                    JOptionPane.showMessageDialog(null, 
                        "Cadena en mayúsculas:\n\"" + analizador.convertirMayusculas() + "\"", 
                        "Mayúsculas", 
                        JOptionPane.INFORMATION_MESSAGE);
                    break;
                    
                case 8: 
                    JOptionPane.showMessageDialog(null, 
                        "Cadena en minúsculas:\n\"" + analizador.convertirMinusculas() + "\"", 
                        "Minúsculas", 
                        JOptionPane.INFORMATION_MESSAGE);
                    break;
                    
                case 9: 
                    JOptionPane.showMessageDialog(null, 
                        "Palabras que inician con vocal: " + analizador.obtenerPalabrasInicianConVocal(), 
                        "Palabras con Vocal Inicial", 
                        JOptionPane.INFORMATION_MESSAGE);
                    break;
                    
                case 10: 
                    JOptionPane.showMessageDialog(null, 
                        "Cadena invertida (iterativo):\n\"" + analizador.invertirString() + "\"", 
                        "Cadena Invertida", 
                        JOptionPane.INFORMATION_MESSAGE);
                    break;
                    
                case 11: 
                    JOptionPane.showMessageDialog(null, 
                        "Cadena invertida (recursivo):\n\"" + analizador.invertirStringRecursivo() + "\"", 
                        "Cadena Invertida Recursiva", 
                        JOptionPane.INFORMATION_MESSAGE);
                    break;
                    
                case 12: 
                    analizador.mostrarAnalisisCompleto();
                    break;
                    
                case 14: 
                    JOptionPane.showMessageDialog(null, 
                        "Texto encriptado (César):\n\"" + analizador.encriptarCesar() + "\"", 
                        "Encriptación César", 
                        JOptionPane.INFORMATION_MESSAGE);
                    break;
                    
                case 15: 
                    JOptionPane.showMessageDialog(null, 
                        "Texto desencriptado (César):\n\"" + analizador.desencriptarCesar() + "\"", 
                        "Desencriptación César", 
                        JOptionPane.INFORMATION_MESSAGE);
                    break;
                    
                case 16: 
                    JOptionPane.showMessageDialog(null, 
                        "Texto encriptado (Julio Verne):\n\"" + analizador.encriptarJulioVerne() + "\"", 
                        "Encriptación Julio Verne", 
                        JOptionPane.INFORMATION_MESSAGE);
                    break;
                    
                case 17: 
                    JOptionPane.showMessageDialog(null, 
                        "Texto desencriptado (Julio Verne):\n\"" + analizador.desencriptarJulioVerne() + "\"", 
                        "Desencriptación Julio Verne", 
                        JOptionPane.INFORMATION_MESSAGE);
                    break;
                    
                case 18: 
                    JOptionPane.showMessageDialog(null, 
                        "Texto encriptado (ROT3 ASCII):\n\"" + analizador.encriptarRot3Ascii() + "\"", 
                        "Encriptación ROT3 ASCII", 
                        JOptionPane.INFORMATION_MESSAGE);
                    break;
                    
                case 19: 
                    JOptionPane.showMessageDialog(null, 
                        "Texto desencriptado (ROT3 ASCII):\n\"" + analizador.desencriptarRot3Ascii() + "\"", 
                        "Desencriptación ROT3 ASCII", 
                        JOptionPane.INFORMATION_MESSAGE);
                    break;
                    
                case 20: 
                    JOptionPane.showMessageDialog(null, 
                        "Texto encriptado (Atbash):\n\"" + analizador.encriptarAtbash() + "\"", 
                        "Encriptación Atbash", 
                        JOptionPane.INFORMATION_MESSAGE);
                    break;
                    
                case 21: 
                    JOptionPane.showMessageDialog(null, 
                        "Texto desencriptado (Atbash):\n\"" + analizador.desencriptarAtbash() + "\"", 
                        "Desencriptación Atbash", 
                        JOptionPane.INFORMATION_MESSAGE);
                    break;
                    
                case 22: 
                    JOptionPane.showMessageDialog(null, 
                        "Texto encriptado (XOR):\n\"" + analizador.encriptarXor() + "\"", 
                        "Encriptación XOR", 
                        JOptionPane.INFORMATION_MESSAGE);
                    break;
                    
                case 23: 
                    JOptionPane.showMessageDialog(null, 
                        "Texto desencriptado (XOR):\n\"" + analizador.desencriptarXor() + "\"", 
                        "Desencriptación XOR", 
                        JOptionPane.INFORMATION_MESSAGE);
                    break;
                    
                case 24: 
                    JOptionPane.showMessageDialog(null, 
                        "Texto encriptado (Sustitución):\n\"" + analizador.encriptarSustitucion() + "\"", 
                        "Encriptación Sustitución", 
                        JOptionPane.INFORMATION_MESSAGE);
                    break;
                    
                case 25: 
                    JOptionPane.showMessageDialog(null, 
                        "Texto desencriptado (Sustitución):\n\"" + analizador.desencriptarSustitucion() + "\"", 
                        "Desencriptación Sustitución", 
                        JOptionPane.INFORMATION_MESSAGE);
                    break;
                    
                case 26: 
                    JOptionPane.showMessageDialog(null, 
                        "Terminando de usar el código.", 
                        "Gracias", 
                        JOptionPane.INFORMATION_MESSAGE);
                    break;
            }
            
        } while (opcion != 26 && opcion != -1);
    }
}