//package Algoritmos;
import javax.swing.JOptionPane;

public class Nodoab {
    int nump;
    String texto;
    String textoencrip;
    char estatus;
    Nodoab lchild;
    Nodoab rchild;
    
    public Nodoab(int np, String t) {
        this.nump = np;
        this.estatus = 0;
        this.texto = t;
        this.textoencrip = encriptarTexto(t);
        this.lchild = null;
        this.rchild = null;
    }
    
    // Metodo para encriptar el texto 
    private String encriptarTexto(String t) {
        StringBuilder encriptado = new StringBuilder();
        for (char c : t.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isLowerCase(c) ? 'a' : 'A';
                encriptado.append((char) ((c - base + 3) % 26 + base));
            } else {
                encriptado.append(c);
            }
        }
        return encriptado.toString();
    }
    
    // Metodo para desencriptar el texto
    public String desencriptarTexto() {
        StringBuilder desencriptado = new StringBuilder();
        for (char c : textoencrip.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isLowerCase(c) ? 'a' : 'A';
                desencriptado.append((char) ((c - base - 3 + 26) % 26 + base));
            } else {
                desencriptado.append(c);
            }
        }
        return desencriptado.toString();
    }
}