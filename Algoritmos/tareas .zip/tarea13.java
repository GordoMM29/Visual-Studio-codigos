import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;

public class tarea13 {
    
    // Variables globales para contar
    static long comparaciones;
    static long movimientos;
    static int[] arregloOriginal;
    static int tamanio;
    static String nombreArchivoActual = null;  // Para recordar el archivo cargado
    
    public static void main(String[] args) {
        // Configurar interfaz
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}
        
        while (true) {
            String[] opciones = {
                "Generar Arreglo Aleatorio",
                "Cargar Arreglo desde Archivo",
                "Ejecutar Metodos Alta Eficiencia (Grandes Datos)",
                "Ejecutar Metodos Baja Eficiencia (Pocos Datos)",
                "Salir"
            };
            
            int opcion = JOptionPane.showOptionDialog(null,
                "= SISTEMA DE ORDENAMIENTO =\n" +
                "Clasificación por eficiencia:\n\n" +
                "ALTA EFICIENCIA (Grandes volúmenes):\n" +
                "Quick, Merge, Heap, Shell, Radix, Counting, Bin\n\n" +
                "BAJA EFICIENCIA (Pocos datos):\n" +
                "Insercion, Seleccion, Burbuja, Arbol, Cocktail\n\n" +
                (nombreArchivoActual != null ? "Archivo cargado: " + nombreArchivoActual : ""),
                "Ordenamiento de Arreglos",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                opciones,
                opciones[0]);
            
            if (opcion == 4 || opcion == JOptionPane.CLOSED_OPTION) break;
            
            if (opcion == 0) generarArregloAleatorio();
            else if (opcion == 1) cargarArregloDesdeArchivo();
            else if (opcion == 2) ejecutarMetodosAltaEficiencia();
            else if (opcion == 3) ejecutarMetodosBajaEficiencia();
        }
    }   
    //GENERACION DE ARREGLOS     
    static void generarArregloAleatorio() {
        String input = JOptionPane.showInputDialog(null,
            "Ingrese el tamaño del arreglo:",
            "Tamaño",
            JOptionPane.QUESTION_MESSAGE);
        
        if (input == null) return;
        
        try {
            tamanio = Integer.parseInt(input.trim());
            if (tamanio <= 0) {
                JOptionPane.showMessageDialog(null, "El tamaño debe ser mayor a 0");
                return;
            }
            
            // Generar arreglo aleatorio
            arregloOriginal = new int[tamanio];
            Random rand = new Random();
            for (int i = 0; i < tamanio; i++) {
                arregloOriginal[i] = rand.nextInt(tamanio * 2) + 1;
            }
            
            nombreArchivoActual = null;
            
            JOptionPane.showMessageDialog(null,
                String.format("Arreglo aleatorio generado:\n" +
                    "Tamano: %,d elementos\n" +
                    "Primeros 10 valores: %s\n" +
                    "Ultimos 10 valores: %s",
                    tamanio, 
                    mostrarPrimeros(arregloOriginal, 10),
                    mostrarUltimos(arregloOriginal, 10)),
                "Generación exitosa",
                JOptionPane.INFORMATION_MESSAGE);
                    
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: Ingrese un número válido", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (OutOfMemoryError e) {
            JOptionPane.showMessageDialog(null, "Error: Memoria insuficiente", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    static void cargarArregloDesdeArchivo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar archivo de números");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos de texto", "txt"));
        
        int resultado = fileChooser.showOpenDialog(null);
        
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            nombreArchivoActual = archivo.getName();
            
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                ArrayList<Integer> listaNumeros = new ArrayList<>();
                String linea;
                int lineNumber = 0;
                
                while ((linea = br.readLine()) != null) {
                    lineNumber++;
                    linea = linea.trim();
                    
                    // Saltar líneas vacías
                    if (linea.isEmpty()) continue;
                    
                    // Separar por espacios, comas, tabs
                    String[] partes = linea.split("[\\s,]+");
                    
                    for (String parte : partes) {
                        if (parte.trim().isEmpty()) continue;
                        try {
                            int numero = Integer.parseInt(parte.trim());
                            listaNumeros.add(numero);
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(null,
                                "Error en linea " + lineNumber + ": '" + parte + "' no es un numero válido.\n" +
                                "El archivo debe contener solo números enteros separados por espacios, comas o saltos de linea.",
                                "Formato invalido",
                                JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                }
                
                if (listaNumeros.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                        "El archivo esta vacio o no contiene numeros validos.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                tamanio = listaNumeros.size();
                arregloOriginal = new int[tamanio];
                for (int i = 0; i < tamanio; i++) {
                    arregloOriginal[i] = listaNumeros.get(i);
                }               
                // Mostrar informacion del archivo
                StringBuilder info = new StringBuilder();
                info.append("Arreglo cargado exitosamente:\n");
                info.append("Archivo: ").append(nombreArchivoActual).append("\n");
                info.append("Tamano: ").append(String.format("%,d", tamanio)).append(" elementos\n");
                info.append("Rango de valores: [").append(encontrarMinimo(arregloOriginal))
                    .append(", ").append(encontrarMaximo(arregloOriginal)).append("]\n");
                info.append("\nPrimeros 10 valores: ").append(mostrarPrimeros(arregloOriginal, 10)).append("\n");
                info.append("Ultimos 10 valores: ").append(mostrarUltimos(arregloOriginal, 10)).append("\n");                
                // Verificar si está ordenado
                if (estaOrdenado(arregloOriginal)) {
                    info.append("\nEl arreglo YA esta ordenado de forma ascendente.");
                } else if (estaOrdenadoDescendente(arregloOriginal)) {
                    info.append("\nEl arreglo esta ordenado de forma descendente.");
                }
                
                JOptionPane.showMessageDialog(null, info.toString(), "Carga exitosa", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null, "No se encontró el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error al leer el archivo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (OutOfMemoryError e) {
                JOptionPane.showMessageDialog(null, "Error: El archivo es demasiado grande para la memoria disponible.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }   
    //METODOS DE ALTA EFICIENCIA (soporta grandes volumenes de datos)  
    static void ejecutarMetodosAltaEficiencia() {
        if (arregloOriginal == null) {
            JOptionPane.showMessageDialog(null, "Primero genere un arreglo o cargue un archivo.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (tamanio > 500000) {
            int confirm = JOptionPane.showConfirmDialog(null,
                "El tamano " + tamanio + " es extremadamente grande.\n" +
                "Los metodos de alta eficiencia pueden tardar varios segundos.\n" +
                "¿Desea continuar?",
                "Advertencia",
                JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;
        }
        
        String[] metodos = {
            "Quick Sort", "Merge Sort", "Heap Sort", "Shell Sort",
            "Radix Sort", "Counting Sort", "Bin Sort"
        };
        
        String[][] resultados = new String[metodos.length][5];
        
        for (int i = 0; i < metodos.length; i++) {
            resultados[i][0] = metodos[i];
            System.out.println("Ejecutando (Alta Eficiencia): " + metodos[i] + " (" + (i+1) + "/" + metodos.length + ")");
            
            try {
                int[] copia = arregloOriginal.clone();
                comparaciones = 0;
                movimientos = 0;
                
                long inicio = System.nanoTime();
                
                switch (metodos[i]) {
                    case "Quick Sort":
                        quickSort(copia, 0, copia.length - 1);
                        break;
                    case "Merge Sort":
                        mergeSort(copia, 0, copia.length - 1);
                        break;
                    case "Heap Sort":
                        heapSort(copia);
                        break;
                    case "Shell Sort":
                        shellSort(copia);
                        break;
                    case "Radix Sort":
                        radixSort(copia);
                        break;
                    case "Counting Sort":
                        countingSort(copia);
                        break;
                    case "Bin Sort":
                        binSort(copia);
                        break;
                }
                
                long fin = System.nanoTime();
                long tiempo = (fin - inicio) / 1_000_000;
                boolean ordenado = estaOrdenado(copia);
                
                resultados[i][1] = String.valueOf(tiempo);
                resultados[i][2] = String.valueOf(comparaciones);
                resultados[i][3] = String.valueOf(movimientos);
                resultados[i][4] = ordenado ? " OK" : " ERROR";
                
            } catch (StackOverflowError e) {
                resultados[i][1] = "Error";
                resultados[i][2] = "Stack Overflow";
                resultados[i][3] = "-";
                resultados[i][4] = "✗ Falló";
            } catch (OutOfMemoryError e) {
                resultados[i][1] = "Error";
                resultados[i][2] = "Out of Memory";
                resultados[i][3] = "-";
                resultados[i][4] = "✗ Falló";
            } catch (Exception e) {
                resultados[i][1] = "Error";
                resultados[i][2] = e.getMessage();
                resultados[i][3] = "-";
                resultados[i][4] = "✗ Falló";
            }
        }
        
        mostrarResultados(resultados, "ALTA EFICIENCIA");
    }
    
    //METODOS DE BAJA EFICIENCIA (POCOS DATOS)
    
    static void ejecutarMetodosBajaEficiencia() {
        if (arregloOriginal == null) {
            JOptionPane.showMessageDialog(null, "Primero genere un arreglo o cargue un archivo.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (tamanio > 50000) {
            JOptionPane.showMessageDialog(null,
                "ADVERTENCIA: Metodos de baja eficiencia\n\n" +
                "El tamaño actual es " + String.format("%,d", tamanio) + " elementos.\n" +
                "Los metodos como Burbuja, Insercion y Selección pueden tardar\n" +
                "MINUTOS o incluso HORAS con esta cantidad de datos.\n\n" +
                "Se recomienda usar un arreglo de maximo 50,000 elementos\n" +
                "para estos metodos.\n\n" +
                "¿Desea continuar de todas formas?",
                "Tamaño excesivo",
                JOptionPane.WARNING_MESSAGE);
            
            int confirm = JOptionPane.showConfirmDialog(null,
                "¿Realmente desea ejecutar metodos lentos con " + String.format("%,d", tamanio) + " elementos?",
                "Confirmación peligrosa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            if (confirm != JOptionPane.YES_OPTION) return;
        }
        
        String[] metodos = {
            "Inserción Directa", "Selección", "Burbuja", "Árbol Binario", "Cocktail Sort"
        };
        
        String[][] resultados = new String[metodos.length][5];
        
        for (int i = 0; i < metodos.length; i++) {
            resultados[i][0] = metodos[i];
            System.out.println("Ejecutando (Baja Eficiencia): " + metodos[i] + " (" + (i+1) + "/" + metodos.length + ")");
            
            try {
                int[] copia = arregloOriginal.clone();
                comparaciones = 0;
                movimientos = 0;
                
                long inicio = System.nanoTime();
                
                switch (metodos[i]) {
                    case "Inserción Directa":
                        insercionDirecta(copia);
                        break;
                    case "Selección":
                        seleccion(copia);
                        break;
                    case "Burbuja":
                        burbuja(copia);
                        break;
                    case "Árbol Binario":
                        treeSort(copia);
                        break;
                    case "Cocktail Sort":
                        cocktailSort(copia);
                        break;
                }
                
                long fin = System.nanoTime();
                long tiempo = (fin - inicio) / 1_000_000;
                boolean ordenado = estaOrdenado(copia);
                
                resultados[i][1] = String.valueOf(tiempo);
                resultados[i][2] = String.valueOf(comparaciones);
                resultados[i][3] = String.valueOf(movimientos);
                resultados[i][4] = ordenado ? " OK" : " ERROR";
                
            } catch (StackOverflowError e) {
                resultados[i][1] = "Error";
                resultados[i][2] = "Stack Overflow";
                resultados[i][3] = "-";
                resultados[i][4] = " Falló";
            } catch (OutOfMemoryError e) {
                resultados[i][1] = "Error";
                resultados[i][2] = "Out of Memory";
                resultados[i][3] = "-";
                resultados[i][4] = " Falló";
            } catch (Exception e) {
                resultados[i][1] = "Error";
                resultados[i][2] = e.getMessage();
                resultados[i][3] = "-";
                resultados[i][4] = " Falló";
            }
        }
        
        mostrarResultados(resultados, "BAJA EFICIENCIA.");
    }   
    //METODOS DE ORDENAMIENTO   
    static void insercionDirecta(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int aux = arr[i];
            int j = i - 1;
            while (j >= 0 && aux < arr[j]) {
                comparaciones++;
                arr[j + 1] = arr[j];
                movimientos++;
                j--;
            }
            comparaciones++;
            arr[j + 1] = aux;
            movimientos++;
        }
    }
    
    static void seleccion(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            int min = i;
            for (int j = i + 1; j < arr.length; j++) {
                comparaciones++;
                if (arr[j] < arr[min]) min = j;
            }
            if (min != i) {
                int aux = arr[i];
                arr[i] = arr[min];
                arr[min] = aux;
                movimientos++;
            }
        }
    }
    
    static void burbuja(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < arr.length - 1 - i; j++) {
                comparaciones++;
                if (arr[j] > arr[j + 1]) {
                    int aux = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = aux;
                    movimientos++;
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
    }
    
    static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pi = particion(arr, low, high);
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }
    
    static int particion(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            comparaciones++;
            if (arr[j] <= pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                movimientos++;
            }
        }
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        movimientos++;
        return i + 1;
    }
    
    static void mergeSort(int[] arr, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);
            merge(arr, left, mid, right);
        }
    }
    
    static void merge(int[] arr, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;
        int[] L = new int[n1];
        int[] R = new int[n2];
        
        System.arraycopy(arr, left, L, 0, n1);
        System.arraycopy(arr, mid + 1, R, 0, n2);
        movimientos += n1 + n2;
        
        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            comparaciones++;
            if (L[i] <= R[j]) {
                arr[k++] = L[i++];
            } else {
                arr[k++] = R[j++];
            }
            movimientos++;
        }
        while (i < n1) {
            arr[k++] = L[i++];
            movimientos++;
        }
        while (j < n2) {
            arr[k++] = R[j++];
            movimientos++;
        }
    }
    
    static void heapSort(int[] arr) {
        int n = arr.length;
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i);
        }
        for (int i = n - 1; i > 0; i--) {
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;
            movimientos++;
            heapify(arr, i, 0);
        }
    }
    
    static void heapify(int[] arr, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        comparaciones++;
        if (left < n && arr[left] > arr[largest]) largest = left;
        comparaciones++;
        if (right < n && arr[right] > arr[largest]) largest = right;
        if (largest != i) {
            int temp = arr[i];
            arr[i] = arr[largest];
            arr[largest] = temp;
            movimientos++;
            heapify(arr, n, largest);
        }
    }
    
    static void shellSort(int[] arr) {
        int n = arr.length;
        for (int gap = n / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i++) {
                int temp = arr[i];
                int j;
                for (j = i; j >= gap && arr[j - gap] > temp; j -= gap) {
                    comparaciones++;
                    arr[j] = arr[j - gap];
                    movimientos++;
                }
                if (j >= gap) comparaciones++;
                arr[j] = temp;
                movimientos++;
            }
        }
    }
    
    static void radixSort(int[] arr) {
        int max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            comparaciones++;
            if (arr[i] > max) max = arr[i];
        }
        for (int exp = 1; max / exp > 0; exp *= 10) {
            radixCountSort(arr, exp);
        }
    }
    
    static void radixCountSort(int[] arr, int exp) {
        int[] output = new int[arr.length];
        int[] count = new int[10];
        for (int i = 0; i < arr.length; i++) {
            count[(arr[i] / exp) % 10]++;
            movimientos++;
        }
        for (int i = 1; i < 10; i++) {
            count[i] += count[i - 1];
        }
        for (int i = arr.length - 1; i >= 0; i--) {
            output[count[(arr[i] / exp) % 10] - 1] = arr[i];
            count[(arr[i] / exp) % 10]--;
            movimientos++;
        }
        System.arraycopy(output, 0, arr, 0, arr.length);
        movimientos += arr.length;
    }
    
    static void countingSort(int[] arr) {
        if (arr.length == 0) return;
        int max = arr[0], min = arr[0];
        for (int i = 1; i < arr.length; i++) {
            comparaciones += 2;
            if (arr[i] > max) max = arr[i];
            if (arr[i] < min) min = arr[i];
        }
        int range = max - min + 1;
        int[] count = new int[range];
        int[] output = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            count[arr[i] - min]++;
            movimientos++;
        }
        for (int i = 1; i < range; i++) {
            count[i] += count[i - 1];
        }
        for (int i = arr.length - 1; i >= 0; i--) {
            output[count[arr[i] - min] - 1] = arr[i];
            count[arr[i] - min]--;
            movimientos++;
        }
        System.arraycopy(output, 0, arr, 0, arr.length);
        movimientos += arr.length;
    }
    
    static void binSort(int[] arr) {
        if (arr.length == 0) return;
        int max = arr[0], min = arr[0];
        for (int i = 1; i < arr.length; i++) {
            comparaciones += 2;
            if (arr[i] > max) max = arr[i];
            if (arr[i] < min) min = arr[i];
        }
        if (max == min) return;
        int numBuckets = Math.max(1, (int) Math.sqrt(arr.length));
        @SuppressWarnings("unchecked")
        ArrayList<Integer>[] buckets = new ArrayList[numBuckets];
        for (int i = 0; i < numBuckets; i++) {
            buckets[i] = new ArrayList<>();
        }
        for (int i = 0; i < arr.length; i++) {
            int bucketIndex = (int) ((arr[i] - min) * (numBuckets - 1.0) / (max - min));
            bucketIndex = Math.min(bucketIndex, numBuckets - 1);
            buckets[bucketIndex].add(arr[i]);
            movimientos++;
        }
        int index = 0;
        for (int i = 0; i < numBuckets; i++) {
            Collections.sort(buckets[i]);
            for (int val : buckets[i]) {
                arr[index++] = val;
                movimientos++;
            }
        }
    }
    
    static void treeSort(int[] arr) {
        if (arr.length == 0) return;
        NodoArbol raiz = new NodoArbol(arr[0]);
        movimientos++;
        for (int i = 1; i < arr.length; i++) {
            insertarNodo(raiz, arr[i]);
        }
        ArrayList<Integer> lista = new ArrayList<>();
        recorridoInorden(raiz, lista);
        for (int i = 0; i < lista.size(); i++) {
            arr[i] = lista.get(i);
            movimientos++;
        }
    }
    
    static class NodoArbol {
        int valor;
        NodoArbol izquierdo, derecho;
        NodoArbol(int valor) { this.valor = valor; }
    }
    
    static void insertarNodo(NodoArbol nodo, int valor) {
        comparaciones++;
        if (valor < nodo.valor) {
            if (nodo.izquierdo == null) {
                nodo.izquierdo = new NodoArbol(valor);
                movimientos++;
            } else {
                insertarNodo(nodo.izquierdo, valor);
            }
        } else {
            if (nodo.derecho == null) {
                nodo.derecho = new NodoArbol(valor);
                movimientos++;
            } else {
                insertarNodo(nodo.derecho, valor);
            }
        }
    }
    
    static void recorridoInorden(NodoArbol nodo, ArrayList<Integer> lista) {
        if (nodo != null) {
            recorridoInorden(nodo.izquierdo, lista);
            lista.add(nodo.valor);
            recorridoInorden(nodo.derecho, lista);
        }
    }
    
    static void cocktailSort(int[] arr) {
        boolean swapped = true;
        int start = 0;
        int end = arr.length - 1;
        while (swapped) {
            swapped = false;
            for (int i = start; i < end; i++) {
                comparaciones++;
                if (arr[i] > arr[i + 1]) {
                    int temp = arr[i];
                    arr[i] = arr[i + 1];
                    arr[i + 1] = temp;
                    movimientos++;
                    swapped = true;
                }
            }
            if (!swapped) break;
            swapped = false;
            end--;
            for (int i = end - 1; i >= start; i--) {
                comparaciones++;
                if (arr[i] > arr[i + 1]) {
                    int temp = arr[i];
                    arr[i] = arr[i + 1];
                    arr[i + 1] = temp;
                    movimientos++;
                    swapped = true;
                }
            }
            start++;
        }
    }   
    // METODOS AUXILIARES   
    static String mostrarPrimeros(int[] arr, int n) {
        StringBuilder sb = new StringBuilder("[");
        int limite = Math.min(n, arr.length);
        for (int i = 0; i < limite; i++) {
            sb.append(arr[i]);
            if (i < limite - 1) sb.append(", ");
        }
        if (arr.length > n) sb.append(", ...");
        sb.append("]");
        return sb.toString();
    }
    
    static String mostrarUltimos(int[] arr, int n) {
        StringBuilder sb = new StringBuilder("[");
        int inicio = Math.max(0, arr.length - n);
        int limite = arr.length;
        for (int i = inicio; i < limite; i++) {
            sb.append(arr[i]);
            if (i < limite - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
    
    static int encontrarMinimo(int[] arr) {
        int min = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < min) min = arr[i];
        }
        return min;
    }
    
    static int encontrarMaximo(int[] arr) {
        int max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) max = arr[i];
        }
        return max;
    }
    
    static boolean estaOrdenado(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] > arr[i + 1]) return false;
        }
        return true;
    }
    
    static boolean estaOrdenadoDescendente(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] < arr[i + 1]) return false;
        }
        return true;
    }
    
    static void mostrarResultados(String[][] resultados, String tipo) {
        StringBuilder sb = new StringBuilder();
        sb.append("╔══════════════════════╦══════════════╦═══════════════╦═══════════════╦══════════╗\n");
        sb.append("║      METODO          ║ Tiempo (ms)  ║ Comparaciones ║  Movimientos  ║  Estado  ║\n");
        sb.append("╠══════════════════════╬══════════════╬═══════════════╬═══════════════╬══════════╣\n");
        
        for (String[] r : resultados) {
            sb.append(String.format("║ %-20s ║ %12s ║ %13s ║ %13s ║ %8s ║\n",
                r[0], r[1], r[2], r[3], r[4]));
        }
        
        sb.append("╚══════════════════════╩══════════════╩═══════════════╩═══════════════╩══════════╝\n");
        sb.append("\n=== Informacion Del Sistema ===\n");
        sb.append("Tipo de ejecución: ").append(tipo).append("\n");
        sb.append("Fuente de datos: ");
        if (nombreArchivoActual != null) {
            sb.append("Archivo: '").append(nombreArchivoActual).append("'");
        } else {
            sb.append("Generacion aleatoria.");
        }
        sb.append("\n");
        sb.append("Tamano del arreglo: ").append(String.format("%,d", tamanio)).append(" elementos\n");
        sb.append("Memoria maxima: ").append(String.format("%,d MB", 
            Runtime.getRuntime().maxMemory() / 1024 / 1024)).append("\n");
        sb.append("Procesadores: ").append(Runtime.getRuntime().availableProcessors()).append("\n");
        sb.append("Sistema Operativo: ").append(System.getProperty("os.name")).append("\n");
        sb.append("Java Version: ").append(System.getProperty("java.version")).append("\n");
        
        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(950, 550));
        
        JOptionPane.showMessageDialog(null, scrollPane, "Resultados de Ordenamiento - " + tipo, 
                JOptionPane.INFORMATION_MESSAGE);
    }
}