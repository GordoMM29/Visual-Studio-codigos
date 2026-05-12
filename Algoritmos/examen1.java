// Autor: Diego Yasir Marin Medina
// Matrícula: 23096254
import javax.swing.JOptionPane;

class Nodo {
    int numcarrera;
    String fecha; // formato dd/mm/aaaa
    String nombre;
    float distancia; // en KM
    float tiempo; // en hr y/o minutos
    String distaletra; // CONVERSION DE LA distancia A STRING
    String nombreencrip;
    int letras, vocales, consonantes, espacios, digitos, especiales, palabras;
    Nodo izq, der;

public Nodo(int numcarrera, String fecha, String nombre, float distancia, float tiempo) {
    this.numcarrera = numcarrera;
    this.fecha = fecha;
    this.nombre = nombre;
    this.distancia = distancia;
    this.tiempo = tiempo;
    this.izq = null;
    this.der = null;
    }
}
public class examen1{
    Nodo raiz, p, q;
    int letras, vocales, consonantes, espacios, digitos, especiales, palabras;
    private String distanciaString(float distancia) {
    return String.valueOf(distancia) + " KM";
    }
    private String encriptarNombre(String nombre) {
    StringBuilder encriptado = new StringBuilder();
    int desplazamiento = 3; // cifrado Cesar

    for (char c : nombre.toCharArray()) {
        if (Character.isLetter(c)) {
            char base = Character.isUpperCase(c) ? 'A' : 'a';
            char encriptadoChar = (char) ((c - base + desplazamiento) % 26 + base);
            encriptado.append(encriptadoChar);
        } else {
            encriptado.append(c); // No encriptar caracteres no alfabeticos
        }
    }
    return encriptado.toString();
}
 private void analizarNombre(String nombre){
    letras = 0;
    vocales = 0;
    consonantes = 0;
    espacios = 0;
    digitos = 0;
    especiales = 0;
    palabras = 0;

    boolean enPalabra = false;

    for (char c : nombre.toCharArray()) {
        if (Character.isLetter(c)) {
            letras++;
            if ("AEIOUaeiou".indexOf(c) != -1) {
                vocales++;
            } else {
                consonantes++;
            }
            enPalabra = true;
        } else if (Character.isDigit(c)) {
            digitos++;
            enPalabra = false;
        } else if (Character.isWhitespace(c)) {
            espacios++;
            if (enPalabra) {
                palabras++;
                enPalabra = false;
            }
        } else {
            especiales++;
            enPalabra = false;
        }
    }
    if (enPalabra) {
        palabras++;
    }
}
    public void insertar(int numcarrera, String fecha, String nombre, float distancia, float tiempo) {
    Nodo nuevo = new Nodo(numcarrera, fecha, nombre, distancia, tiempo);
    nuevo.distaletra = distanciaString(distancia);
    nuevo.nombreencrip = encriptarNombre(nombre);
    analizarNombre(nombre);
    nuevo.letras = letras;
    nuevo.vocales = vocales;
    nuevo.consonantes = consonantes;
    nuevo.espacios = espacios;
    nuevo.digitos = digitos;
    nuevo.especiales = especiales;
    nuevo.palabras = palabras;
    if (raiz == null) {
        raiz = nuevo;
    } else {
        Nodo actual = raiz;
        Nodo padre;
        while (true) {
            padre = actual;
            if (numcarrera < actual.numcarrera) {
                actual = actual.izq;
                if (actual == null) {
                    padre.izq = nuevo;
                    return;
                }
            } else {
                actual = actual.der;
                if (actual == null) {
                    padre.der = nuevo;
                    return;
                }
            }
        }
    }
 }
 public String preorder (Nodo nodo) {
    if (nodo == null) {
        return "";
    }
    String resultado = "NNumero de Carrera: " + nodo.numcarrera + "\n" +
                       "Fecha: " + nodo.fecha + "\n" +
                       "Nombre: " + nodo.nombre + "\n" +
                       "Distancia: " + nodo.distancia + " KM\n" +
                       "Tiempo: " + nodo.tiempo + " hr\n" +
                       "Distancia en Letras: " + nodo.distaletra + "\n" +
                       "Nombre Encriptado: " + nodo.nombreencrip + "\n" +
                       "Analisis del Nombre:\n" +
                       "- Letras: " + nodo.letras + "\n" +
                       "- Vocales: " + nodo.vocales + "\n" +
                       "- Consonantes: " + nodo.consonantes + "\n" +
                       "- Espacios: " + nodo.espacios + "\n" +
                       "- Palabras: " + nodo.palabras + "\n" +
                       "- Digitos: " + nodo.digitos + "\n" +
                       "- Caracteres Especiales: " + nodo.especiales + "\n\n";
    resultado += preorder(nodo.izq);
    resultado += preorder(nodo.der);
    return resultado;
 }
 public String inorder (Nodo nodo) {
    if (nodo == null) {
        return "";
    }
    String resultado = "";
    resultado += inorder(nodo.izq);
    resultado += "NNumero de Carrera: " + nodo.numcarrera + "\n" +
                 "Fecha: " + nodo.fecha + "\n" +
                 "Nombre: " + nodo.nombre + "\n" +
                 "Distancia: " + nodo.distancia + " KM\n" +
                 "Tiempo: " + nodo.tiempo + " hr\n" +
                 "Distancia en Letras: " + nodo.distaletra + "\n" +
                 "Nombre Encriptado: " + nodo.nombreencrip + "\n" +
                 "Analisis del Nombre:\n" +
                 "- Letras: " + nodo.letras + "\n" +
                 "- Vocales: " + nodo.vocales + "\n" +
                 "- Consonantes: " + nodo.consonantes + "\n" +
                 "- Espacios: " + nodo.espacios + "\n" +
                     "- Palabras: " + nodo.palabras + "\n" +
                     "- Digitos: " + nodo.digitos + "\n" +
                 "- Caracteres Especiales: " + nodo.especiales + "\n\n";
    resultado += inorder(nodo.der);
    return resultado;
 }
 public String postorder (Nodo nodo) {
    if (nodo == null) {
        return "";
    }
    String resultado = "";
    resultado += postorder(nodo.izq);
    resultado += postorder(nodo.der);
    resultado += "Numero de Carrera: " + nodo.numcarrera + "\n" +
                 "Fecha: " + nodo.fecha + "\n" +
                 "Nombre: " + nodo.nombre + "\n" +
                 "Distancia: " + nodo.distancia + " KM\n" +
                 "Tiempo: " + nodo.tiempo + " hr\n" +
                 "Distancia en Letras: " + nodo.distaletra + "\n" +
                 "Nombre Encriptado: " + nodo.nombreencrip + "\n" +
                 "Analisis del Nombre:\n" +
                 "- Letras: " + nodo.letras + "\n" +
                 "- Vocales: " + nodo.vocales + "\n" +
                 "- Consonantes: " + nodo.consonantes + "\n" +
                 "- Espacios: " + nodo.espacios + "\n" +
                 "- Digitos: " + nodo.digitos + "\n" +
                 "- Caracteres Especiales: " + nodo.especiales + "\n\n";
    return resultado;
 }
 private String obtenerInfoNodo(Nodo nodo) {
    return "Numero de Carrera: " + nodo.numcarrera + "\n" +
           "Fecha: " + nodo.fecha + "\n" +
           "Nombre: " + nodo.nombre + "\n" +
           "Distancia: " + nodo.distancia + " KM\n" +
           "Tiempo: " + nodo.tiempo + " hr\n" +
           "Distancia en Letras: " + nodo.distaletra + "\n" +
           "Nombre Encriptado: " + nodo.nombreencrip + "\n" +
           "Analisis del Nombre:\n" +
           "- Letras: " + nodo.letras + "\n" +
           "- Vocales: " + nodo.vocales + "\n" +
           "- Consonantes: " + nodo.consonantes + "\n" +
       "- Espacios: " + nodo.espacios + "\n" +
       "- Palabras: " + nodo.palabras + "\n" +
           "- Digitos: " + nodo.digitos + "\n" +
           "- Caracteres Especiales: " + nodo.especiales + "\n\n";
 }
 public String inorderTiempoMenor1Hr(Nodo nodo, String resultado){
    if (nodo == null) {
        return resultado;
    }
    resultado = inorderTiempoMenor1Hr(nodo.izq, resultado);
    if (nodo.tiempo < 1.0) {
        resultado += obtenerInfoNodo(nodo);
    }
    resultado = inorderTiempoMenor1Hr(nodo.der, resultado);
    return resultado;
 }
 public class RangeSum {
    public float total = 0f;
 }

 public String inorderPorRangoFechas(Nodo nodo, String resultado, String fechaInicio, String fechaFin, RangeSum sum) {
    if (nodo == null) {
        return resultado;
    }
    resultado = inorderPorRangoFechas(nodo.izq, resultado, fechaInicio, fechaFin, sum);
    if (compararFechas(nodo.fecha, fechaInicio) >= 0 && compararFechas(nodo.fecha, fechaFin) <= 0) {
        resultado += "numcarrera: " + nodo.numcarrera + "\n" +
                     "nombrencrip: " + nodo.nombreencrip + "\n\n";
        sum.total += nodo.distancia;
    }
    resultado = inorderPorRangoFechas(nodo.der, resultado, fechaInicio, fechaFin, sum);
    return resultado;
 }
 private int compararFechas(String fecha1, String fecha2) {
        String[] partes1 = fecha1.split("/");
        String[] partes2 = fecha2.split("/");
        
        // Comparar año
        if (!partes1[2].equals(partes2[2])) {
            return partes1[2].compareTo(partes2[2]);
        }
        // Comparar mes
        if (!partes1[1].equals(partes2[1])) {
            return partes1[1].compareTo(partes2[1]);
        }
        // Comparar dia
        return partes1[0].compareTo(partes2[0]);
    }
 public String inorderEstadisticas(Nodo nodo, Estadisticas stats, String resultado) {
        if (nodo != null) {
            resultado = inorderEstadisticas(nodo.izq, stats, resultado);
            
            resultado += "====================\n" +
                        "Numero de carrera: " + nodo.numcarrera + "\n" +
                        "Nombre encriptado: " + nodo.nombreencrip + "\n";
            
            stats.sumarPalabras += nodo.palabras;
            stats.sumarVocales += nodo.vocales;
            stats.sumarConsonantes += nodo.consonantes;
            stats.sumarLetras += nodo.letras;
            stats.sumarDigitos += nodo.digitos;
            stats.sumarEspacios += nodo.espacios;
            stats.sumarEspeciales += nodo.especiales;
            
            resultado = inorderEstadisticas(nodo.der, stats, resultado);
        }
        return resultado;
    }
    
    class Estadisticas {
        int sumarPalabras = 0;
        int sumarVocales = 0;
        int sumarConsonantes = 0;
        int sumarLetras = 0;
        int sumarDigitos = 0;
        int sumarEspacios = 0;
        int sumarEspeciales = 0;
    }
    public String postorderNumEncrip(Nodo nodo, String resultado) {
        if (nodo == null) {
            return resultado;
        }
        resultado = postorderNumEncrip(nodo.izq, resultado);
        resultado = postorderNumEncrip(nodo.der, resultado);
        resultado += "Numero de Carrera: " + nodo.numcarrera + "\n" +
                     "Nombre Encriptado: " + nodo.nombreencrip + "\n\n";
        return resultado;
    }
    public Nodo buscarPorNumCarrera (int numcarrera) {
        Nodo actual = raiz;
        while (actual != null) {
            if (numcarrera == actual.numcarrera) {
                return actual;
            } else if (numcarrera < actual.numcarrera) {
                actual = actual.izq;
            } else {
                actual = actual.der;
            }
        }
        return null;
    }
    public String postorderSimple (Nodo nodo, String resultado) {
        if (nodo == null) {
            return resultado;
        }
        resultado = postorderSimple(nodo.izq, resultado);
        resultado = postorderSimple(nodo.der, resultado);
        resultado += "NNumero de Carrera: " + nodo.numcarrera + "\n" +
                     "Fecha: " + nodo.fecha + "\n" +
                     "Nombre: " + nodo.nombre + "\n" +
                     "Distancia: " + nodo.distancia + " KM\n" +
                     "Tiempo: " + nodo.tiempo + " hr\n\n";
        return resultado;
    }
    public boolean estavacio() {
        return raiz == null;
    }
    public static void main (String [] args){
        examen1 arbol = new examen1();
            JOptionPane.showMessageDialog(null, "Diego Yasir Marin Medina\nMatricula: 23096254");
       while (true)     {
            String opcion = JOptionPane.showInputDialog("Que quieres hacer hoy?:\n" +
                                                        "1. Insertar nueva carrera\n" +
                                                        "2. Mostrar carreras (Preorder)\n" +
                                                        "3. Mostrar carreras (Inorder)\n" +
                                                        "4. Mostrar carreras (Postorder)\n" +
                                                        "5. Buscar carrera por numero\n" +
                                                        "6. Mostrar carreras con tiempo menor a 1 hr\n" +
                                                        "7. Mostrar carreras por rango de fechas\n" +
                                                        "8. Mostrar estadisticas de nombres\n" +
                                                        "9. Mostrar Postorder (Numero + Nombre Encriptado)\n" +
                                                        "10. Salir\n");
            if (opcion == null) break; 
            switch (opcion) {
                case "1":
                    try {
                        int numcarrera = Integer.parseInt(JOptionPane.showInputDialog("Número de Carrera:"));
                        String fecha = JOptionPane.showInputDialog("Fecha (dd/mm/aaaa):");
                        String nombre = JOptionPane.showInputDialog("Nombre del Corredor:");
                        float distancia = Float.parseFloat(JOptionPane.showInputDialog("Distancia (KM):"));
                        float tiempo = Float.parseFloat(JOptionPane.showInputDialog("Tiempo (hr):"));
                        arbol.insertar(numcarrera, fecha, nombre, distancia, tiempo);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Error al ingresar datos. Intente nuevamente.");
                    }
                    break;
                case "2":
                    JOptionPane.showMessageDialog(null, arbol.preorder(arbol.raiz));
                    break;
                case "3":
                    JOptionPane.showMessageDialog(null, arbol.inorder(arbol.raiz));
                    break;
                case "4":
                    JOptionPane.showMessageDialog(null, arbol.postorder(arbol.raiz));
                    break;
                case "5":
                    try {
                        int numcarrera = Integer.parseInt(JOptionPane.showInputDialog("Numero de Carrera a buscar:"));
                        Nodo encontrado = arbol.buscarPorNumCarrera(numcarrera);
                        if (encontrado != null) {
                            JOptionPane.showMessageDialog(null, arbol.obtenerInfoNodo(encontrado));
                        } else {
                            JOptionPane.showMessageDialog(null, "Carrera no encontrada.");
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Error al ingresar numero de carrera.");
                    }
                    break;
                case "6":
                    String resultado6 = arbol.inorderTiempoMenor1Hr(arbol.raiz, "");
                    if (resultado6 == null || resultado6.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No se encontraron carreras con tiempo menor a 1 hr.");
                    } else {
                        JOptionPane.showMessageDialog(null, resultado6);
                    }
                    break;
                case "7":
                    try {
                        String fechaInicio = JOptionPane.showInputDialog("Fecha inicio (dd/mm/aaaa):");
                        String fechaFin = JOptionPane.showInputDialog("Fecha fin (dd/mm/aaaa):");
                        if (fechaInicio == null || fechaFin == null) break;
                        RangeSum sum = arbol.new RangeSum();
                        String resultado7 = arbol.inorderPorRangoFechas(arbol.raiz, "", fechaInicio, fechaFin, sum);
                        if (resultado7 == null || resultado7.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "No se encontraron carreras en ese rango de fechas.");
                        } else {
                            String salida = resultado7 + "=== Suma de distancias en el periodo: " + sum.total + " KM ===";
                            JOptionPane.showMessageDialog(null, salida);
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Error con las fechas.");
                    }
                    break;

                case "8":
                    examen1.Estadisticas stats = arbol.new Estadisticas();
                    String estadisticas = arbol.inorderEstadisticas(arbol.raiz, stats, "");
                    estadisticas += "=== Totales ===\n" +
                                   "Palabras: " + stats.sumarPalabras + "\n" +
                                   "Vocales: " + stats.sumarVocales + "\n" +
                                   "Consonantes: " + stats.sumarConsonantes + "\n" +
                                   "Letras: " + stats.sumarLetras + "\n" +
                                   "Dígitos: " + stats.sumarDigitos + "\n" +
                                   "Espacios: " + stats.sumarEspacios + "\n" +
                                   "Especiales: " + stats.sumarEspeciales + "\n";
                    JOptionPane.showMessageDialog(null, estadisticas);
                    break;
                case "10":
                    System.exit(0);
                    break;
                case "9":
                    String postNumEncrip = arbol.postorderNumEncrip(arbol.raiz, "");
                    if (postNumEncrip == null || postNumEncrip.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Árbol vacío.");
                    } else {
                        JOptionPane.showMessageDialog(null, postNumEncrip);
                    }
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción inválida.");
                    break;
            }

        }
    }
}



