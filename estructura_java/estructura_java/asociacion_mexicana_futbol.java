package estructura_java;

import javax.swing.JOptionPane;
import java.util.LinkedList;

public class asociacion_mexicana_futbol {

    class nodoab {
        int numequipo;
        String nombre;
        int puntos;
        int golesafavor;
        char estatus;
        nodoab izq;
        nodoab der;
    }

    private nodoab raiz;
    private LinkedList<nodoab> listaEquipos;

    public asociacion_mexicana_futbol() {
        raiz = null;
        listaEquipos = new LinkedList<>();
    }

    public void insertarNodo(int numequipo, String nombre, int puntos) {
        nodoab nuevo = new nodoab();
        nuevo.numequipo = numequipo;
        nuevo.nombre = nombre;
        nuevo.puntos = puntos;
        nuevo.izq = null;
        nuevo.der = null;

        if (raiz == null) {
            raiz = nuevo;
        } else {
            insertarRecursivo(raiz, nuevo);
        }
    }

    private void insertarRecursivo(nodoab actual, nodoab nuevo) {
        if (nuevo.numequipo < actual.numequipo) {
            if (actual.izq == null) {
                actual.izq = nuevo;
            } else {
                insertarRecursivo(actual.izq, nuevo);
            }
        } else {
            if (actual.der == null) {
                actual.der = nuevo;
            } else {
                insertarRecursivo(actual.der, nuevo);
            }
        }
    }

    public void eliminarNodo(int numequipo) {
        raiz = eliminarRecursivo(raiz, numequipo);
    }

    private nodoab eliminarRecursivo(nodoab nodo, int numequipo) {
        if (nodo == null) {
            return null;
        }
        if (numequipo < nodo.numequipo) {
            nodo.izq = eliminarRecursivo(nodo.izq, numequipo);
        } else if (numequipo > nodo.numequipo) {
            nodo.der = eliminarRecursivo(nodo.der, numequipo);
        } else {
            if (nodo.izq == null) {
                return nodo.der;
            } else if (nodo.der == null) {
                return nodo.izq;
            }
            nodoab sucesor = encontrarMinimo(nodo.der);
            nodo.numequipo = sucesor.numequipo;
            nodo.nombre = sucesor.nombre;
            nodo.puntos = sucesor.puntos;
            nodo.der = eliminarRecursivo(nodo.der, sucesor.numequipo);
        }
        return nodo;
    }

    private nodoab encontrarMinimo(nodoab nodo) {
        while (nodo.izq != null) {
            nodo = nodo.izq;
        }
        return nodo;
    }

    public void recorridos() {
        StringBuilder preorder = new StringBuilder("Preorder: ");
        StringBuilder inorder = new StringBuilder("Inorder: ");
        StringBuilder postorder = new StringBuilder("Postorder: ");

        recorrerPreorder(raiz, preorder);
        recorrerInorder(raiz, inorder);
        recorrerPostorder(raiz, postorder);

        JOptionPane.showMessageDialog(null, preorder + "\n" + inorder + "\n" + postorder);
    }

    private void recorrerPreorder(nodoab nodo, StringBuilder sb) {
        if (nodo != null) {
            sb.append(nodo.nombre).append(" ");
            recorrerPreorder(nodo.izq, sb);
            recorrerPreorder(nodo.der, sb);
        }
    }

    private void recorrerInorder(nodoab nodo, StringBuilder sb) {
        if (nodo != null) {
            recorrerInorder(nodo.izq, sb);
            sb.append(nodo.nombre).append(" ");
            recorrerInorder(nodo.der, sb);
        }
    }

    private void recorrerPostorder(nodoab nodo, StringBuilder sb) {
        if (nodo != null) {
            recorrerPostorder(nodo.izq, sb);
            recorrerPostorder(nodo.der, sb);
            sb.append(nodo.nombre).append(" ");
        }
    }

    public void consultarBinario() {
        int numequipo = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el numero de equipo a buscar:"));
        nodoab resultado = consultarRecursivo(raiz, numequipo);
        if (resultado != null) {
            JOptionPane.showMessageDialog(null, "Equipo encontrado: " + resultado.nombre);
        } else {
            JOptionPane.showMessageDialog(null, "Equipo no encontrado.");
        }
    }

    private nodoab consultarRecursivo(nodoab nodo, int numequipo) {
        if (nodo == null || nodo.numequipo == numequipo) {
            return nodo;
        }
        if (numequipo < nodo.numequipo) {
            return consultarRecursivo(nodo.izq, numequipo);
        }
        return consultarRecursivo(nodo.der, numequipo);
    }

    public void contarBinario() {
        int contador = contarBinario(raiz);
        JOptionPane.showMessageDialog(null, "Numero total de equipos: " + contador);
    }

    private int contarBinario(nodoab nodo) {
        if (nodo == null) {
            return 0;
        }
        return 1 + contarBinario(nodo.izq) + contarBinario(nodo.der);
    }

    public void profundidadBinario() {
        int profundidad = profundidadBinario(raiz);
        JOptionPane.showMessageDialog(null, "Profundidad del arbol: " + profundidad);
    }

    private int profundidadBinario(nodoab nodo) {
        if (nodo == null) {
            return 0;
        }
        int izquierda = profundidadBinario(nodo.izq);
        int derecha = profundidadBinario(nodo.der);
        return 1 + Math.max(izquierda, derecha);
    }

    public void copiarArbolaLista() {
        listaEquipos.clear();
        copiarArbolABaLista(raiz);
        JOptionPane.showMessageDialog(null, "Se copio el arbol binario en la lista encadenada.");
    }

    private void copiarArbolABaLista(nodoab nodo) {
        if (nodo != null) {
            copiarArbolABaLista(nodo.izq);
            listaEquipos.add(nodo);
            copiarArbolABaLista(nodo.der);
        }
    }

    public void mostrarlista() {
        if (listaEquipos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "La lista esta vacia.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (nodoab nodo : listaEquipos) {
            sb.append("Equipo: ").append(nodo.numequipo)
              .append(", Nombre: ").append(nodo.nombre)
              .append(", Puntos: ").append(nodo.puntos).append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    public static void main(String[] args) {
        String nombreAlumno = "Diego Yasir Marin Medina";
        String matricula = "23096254";
        String materia = "Estructura de Datos";

        JOptionPane.showMessageDialog(
            null,
            "Nombre: " + nombreAlumno + "\n" +
            "Matrícula: " + matricula + "\n" +
            "Materia: " + materia,
            "Datos del Alumno",
            JOptionPane.INFORMATION_MESSAGE
        );

        asociacion_mexicana_futbol arbol = new asociacion_mexicana_futbol();
        int opcion;
        do {
            String menu = """
                MENU:
                1. Insertar nodo en el arbol
                2. Eliminar nodo del arbol
                3. Recorridos del arbol
                4. Consultar nodo por numequipo
                5. Contar nodos del arbol
                6. Profundidad del arbol
                7. Copiar arbol a lista encadenada
                8. Mostrar lista encadenada
                9. Salir
                Seleccione una opcion:
            """;
            opcion = Integer.parseInt(JOptionPane.showInputDialog(menu));

            switch (opcion) {
                case 1 -> {
                    int numequipo = Integer.parseInt(JOptionPane.showInputDialog("Ingrese numero de equipo:"));
                    String nombre = JOptionPane.showInputDialog("Ingrese nombre del equipo:");
                    int puntos = Integer.parseInt(JOptionPane.showInputDialog("Ingrese puntos del equipo:"));
                    arbol.insertarNodo(numequipo, nombre, puntos);
                }
                case 2 -> {
                    int numequipo = Integer.parseInt(JOptionPane.showInputDialog("Ingrese numero de equipo a eliminar:"));
                    arbol.eliminarNodo(numequipo);
                }
                case 3 -> arbol.recorridos();
                case 4 -> arbol.consultarBinario();
                case 5 -> arbol.contarBinario();
                case 6 -> arbol.profundidadBinario();
                case 7 -> arbol.copiarArbolaLista();
                case 8 -> arbol.mostrarlista();
                case 9 -> JOptionPane.showMessageDialog(null, "Saliendo del programa...");
                default -> JOptionPane.showMessageDialog(null, "Opcion invalida.");
            }
        } while (opcion != 9);
    }
}