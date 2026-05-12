package estructura_java;
import javax.swing.JOptionPane;

class NodoLista {
    int dato;
    NodoLista siguiente;

    public NodoLista(int dato) {
        this.dato = dato;
        this.siguiente = null;
    }
}

class ListaEncadenada {
    NodoLista cabeza;

    public ListaEncadenada() {
        cabeza = null;
    }

    public void insertarInicio(int dato) {
        NodoLista nuevo = new NodoLista(dato);
        nuevo.siguiente = cabeza;
        cabeza = nuevo;
    }

    public void insertarFinal(int dato) {
        NodoLista nuevo = new NodoLista(dato);
        if (cabeza == null) {
            cabeza = nuevo;
            return;
        }
        NodoLista actual = cabeza;
        while (actual.siguiente != null) {
            actual = actual.siguiente;
        }
        actual.siguiente = nuevo;
    }

    public String mostrar() {
        StringBuilder sb = new StringBuilder();
        NodoLista actual = cabeza;
        while (actual != null) {
            sb.append(actual.dato).append(" -> ");
            actual = actual.siguiente;
        }
        sb.append("null");
        return sb.toString();
    }

    public boolean buscar(int dato) {
        NodoLista actual = cabeza;
        while (actual != null) {
            if (actual.dato == dato) {
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }

    public void eliminar(int dato) {
        if (cabeza == null) return;

        if (cabeza.dato == dato) {
            cabeza = cabeza.siguiente;
            return;
        }

        NodoLista anterior = cabeza;
        NodoLista actual = cabeza.siguiente;

        while (actual != null) {
            if (actual.dato == dato) {
                anterior.siguiente = actual.siguiente;
                return;
            }
            anterior = actual;
            actual = actual.siguiente;
        }
    }

    public int profundidad() {
        int contador = 0;
        NodoLista actual = cabeza;
        while (actual != null) {
            contador++;
            actual = actual.siguiente;
        }
        return contador;
    }

    public void vaciar() {
        cabeza = null;
    }
}

class NodoArbol {
    int dato;
    NodoArbol izquierdo, derecho;

    public NodoArbol(int dato) {
        this.dato = dato;
        this.izquierdo = this.derecho = null;
    }
}

class ArbolBinario {
    NodoArbol raiz;

    public ArbolBinario() {
        raiz = null;
    }

    public void insertar(int dato) {
        raiz = insertarRec(raiz, dato);
    }

    private NodoArbol insertarRec(NodoArbol raiz, int dato) {
        if (raiz == null) {
            raiz = new NodoArbol(dato);
            return raiz;
        }

        if (dato < raiz.dato) {
            raiz.izquierdo = insertarRec(raiz.izquierdo, dato);
        } else if (dato > raiz.dato) {
            raiz.derecho = insertarRec(raiz.derecho, dato);
        }

        return raiz;
    }

    public String inorden() {
        StringBuilder sb = new StringBuilder();
        inordenRec(raiz, sb);
        return sb.toString();
    }

    private void inordenRec(NodoArbol raiz, StringBuilder sb) {
        if (raiz != null) {
            inordenRec(raiz.izquierdo, sb);
            sb.append(raiz.dato).append(" ");
            inordenRec(raiz.derecho, sb);
        }
    }

    public boolean buscar(int dato) {
        return buscarRec(raiz, dato);
    }

    private boolean buscarRec(NodoArbol raiz, int dato) {
        if (raiz == null) return false;
        if (raiz.dato == dato) return true;

        if (dato < raiz.dato) {
            return buscarRec(raiz.izquierdo, dato);
        } else {
            return buscarRec(raiz.derecho, dato);
        }
    }

    public int profundidad() {
        return profundidadRec(raiz);
    }

    private int profundidadRec(NodoArbol nodo) {
        if (nodo == null) {
            return 0;
        } else {
            int profundidadIzq = profundidadRec(nodo.izquierdo);
            int profundidadDer = profundidadRec(nodo.derecho);
            return Math.max(profundidadIzq, profundidadDer) + 1;
        }
    }

    public void transferirALista(ListaEncadenada lista) {
        transferirAListaRec(raiz, lista);
    }

    private void transferirAListaRec(NodoArbol nodo, ListaEncadenada lista) {
        if (nodo != null) {
            transferirAListaRec(nodo.izquierdo, lista);
            lista.insertarFinal(nodo.dato);
            transferirAListaRec(nodo.derecho, lista);
        }
    }
}

public class trabajo {
    public static void main(String[] args) {
        ListaEncadenada lista = new ListaEncadenada();
        ArbolBinario arbol = new ArbolBinario();

        while (true) {
            String opcion = JOptionPane.showInputDialog(
                "MENU \n\n" +
                "1. Insertar datos en la LISTA\n" +
                "2. Insertar datos en el ÁRBOL\n" +
                "3. Mostrar LISTA\n" +
                "4. Mostrar ARBOL (inorden)\n" +
                "5. Buscar dato en LISTA\n" +
                "6. Buscar dato en ARBOL\n" +
                "7. Eliminar dato de la LISTA\n" +
                "8. Transferir datos del ARBOL a la LISTA\n" +
                "9. Mostrar profundidad de la LISTA\n" +
                "10. Mostrar profundidad del ARBOL\n" +
                "11. Mostrar LISTA y ARBOL\n" +
                "12. Salir\n\n" +
                "Que quieres hacer hoy?:"
            );

            if (opcion == null || opcion.equals("12")) {
                break;
            }

            try {
                switch (opcion) {
                    case "1":
                        int datoLista = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el dato a insertar en la LISTA:"));
                        lista.insertarFinal(datoLista);
                        break;
                    case "2":
                        int datoArbol = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el dato a insertar en el ARBOL:"));
                        arbol.insertar(datoArbol);
                        break;
                    case "3":
                        JOptionPane.showMessageDialog(null, "Contenido de la LISTA:\n" + lista.mostrar());
                        break;
                    case "4":
                        JOptionPane.showMessageDialog(null, "Recorrido INORDEN del ARBOL:\n" + arbol.inorden());
                        break;
                    case "5":
                        int buscarLista = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el dato a buscar en la LISTA:"));
                        boolean encontradoLista = lista.buscar(buscarLista);
                        JOptionPane.showMessageDialog(null, encontradoLista ? "El dato ESTA en la lista" : "El dato NO ESTA en la lista");
                        break;
                    case "6":
                        int buscarArbol = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el dato a buscar en el ARBOL:"));
                        boolean encontradoArbol = arbol.buscar(buscarArbol);
                        JOptionPane.showMessageDialog(null, encontradoArbol ? "El dato ESTA en el arbol" : "El dato NO ESTA en el arbol");
                        break;
                    case "7":
                        int eliminarLista = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el dato a eliminar de la LISTA:"));
                        lista.eliminar(eliminarLista);
                        break;
                    case "8":
                        arbol.transferirALista(lista);
                        JOptionPane.showMessageDialog(null, "Datos del ARBOL transferidos a la LISTA exitosamente");
                        break;
                    case "9":
                        JOptionPane.showMessageDialog(null, "Profundidad de la LISTA: " + lista.profundidad() + " elementos");
                        break;
                    case "10":
                        JOptionPane.showMessageDialog(null, "Profundidad del ARBOL: " + arbol.profundidad() + " niveles");
                        break;
                    case "11":
                        String mensaje = "Contenido de ambas estructuras:\n\n" +
                                        "LISTA: " + lista.mostrar() + "\n\n" +
                                        "ARBOL (inorden): " + arbol.inorden();
                        JOptionPane.showMessageDialog(null, mensaje);
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Opcion invalida.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Error");
            }
        }
        JOptionPane.showMessageDialog(null, "Saliendo del programa...");
    }
}