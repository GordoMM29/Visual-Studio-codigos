//package Algoritmos;
import javax.swing.JOptionPane;

public class tarea6 {
    public static int sumaDigitosAnteriores(int n) {
        if (n <= 0) return 0;
        return n + sumaDigitosAnteriores(n - 1);
    }
    
    public static int sumaDigitos(int n) {
        if (n == 0) return 0;
        return (n % 10) + sumaDigitos(n / 10);
    }
    
    public static String invertirString(String s) {
        if (s.isEmpty()) return "";
        return invertirString(s.substring(1)) + s.charAt(0);
    }
    
    public static int potencia(int base, int exponente) {
        if (exponente == 0) return 1;
        return base * potencia(base, exponente - 1);
    }
    
    public static int factorial(int n) {
        if (n <= 1) return 1;
        return n * factorial(n - 1);
    }
    
    public static int fibonacci(int n) {
        if (n <= 1) return n;
        return fibonacci(n - 1) + fibonacci(n - 2);
    }
    
    public static int sumaEnteros(int a, int b) {
        if (b == 0) return a;
        return sumaEnteros(a + 1, b - 1);
    }
    
    public static int contarCaracter(String s, char c) {
        if (s.isEmpty()) return 0;
        return (s.charAt(0) == c ? 1 : 0) + contarCaracter(s.substring(1), c);
    }
    
    public static void main(String[] args) {
        String menu = "1. Suma digitos anteriores\n2. Suma digitos\n3. Invertir string\n4. Potencia\n5. Factorial\n6. Fibonacci\n7. Sumar enteros\n8. Contar caracter\n9. Salir";
        
        while (true) {
            String opcion = JOptionPane.showInputDialog(menu);
            if (opcion == null || opcion.equals("9")) break;
            
            try {
                switch (opcion) {
                    case "1":
                        int n1 = Integer.parseInt(JOptionPane.showInputDialog("Ingrese número:"));
                        JOptionPane.showMessageDialog(null, "Resultado: " + sumaDigitosAnteriores(n1));
                        break;
                    case "2":
                        int n2 = Integer.parseInt(JOptionPane.showInputDialog("Ingrese número:"));
                        JOptionPane.showMessageDialog(null, "Resultado: " + sumaDigitos(n2));
                        break;
                    case "3":
                        String s = JOptionPane.showInputDialog("Ingrese texto:");
                        JOptionPane.showMessageDialog(null, "Resultado: " + invertirString(s));
                        break;
                    case "4":
                        int base = Integer.parseInt(JOptionPane.showInputDialog("Base:"));
                        int exp = Integer.parseInt(JOptionPane.showInputDialog("Exponente:"));
                        JOptionPane.showMessageDialog(null, "Resultado: " + potencia(base, exp));
                        break;
                    case "5":
                        int n5 = Integer.parseInt(JOptionPane.showInputDialog("Ingrese número:"));
                        JOptionPane.showMessageDialog(null, "Resultado: " + factorial(n5));
                        break;
                    case "6":
                        int n6 = Integer.parseInt(JOptionPane.showInputDialog("Ingrese posición:"));
                        JOptionPane.showMessageDialog(null, "Resultado: " + fibonacci(n6));
                        break;
                    case "7":
                        int a = Integer.parseInt(JOptionPane.showInputDialog("Primer número:"));
                        int b = Integer.parseInt(JOptionPane.showInputDialog("Segundo número:"));
                        JOptionPane.showMessageDialog(null, "Resultado: " + sumaEnteros(a, b));
                        break;
                    case "8":
                        String texto = JOptionPane.showInputDialog("Ingrese texto:");
                        char caracter = JOptionPane.showInputDialog("Ingrese caracter:").charAt(0);
                        JOptionPane.showMessageDialog(null, "Resultado: " + contarCaracter(texto, caracter));
                        break;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error en los datos");
            }
        }
    }
}