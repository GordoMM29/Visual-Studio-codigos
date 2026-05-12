package estructura_java;

import javax.swing.JOptionPane;
import java.util.LinkedList;

public class ordinaario {
    class nodoab{
        int numvehiculo;
        String nombre;
        int existencia;
        float precio;
        char tipo;
        nodoab izq;
        nodoab der;
    }

    class nodolesc{
        int numvehiculo;
        String nombre;
        int existencia;
        float precio;
        char tipo;
        nodolesc next;
    }

    class ListaVehiculos {
        LinkedList<nodolesc> lista;

        public ListaVehiculos() {
            lista = new LinkedList<>();
        }

        public void agregarVehiculo(nodolesc nodo) {
            lista.add(nodo);
        }
        public void imprimirLista() {
            StringBuilder sb = new StringBuilder();
            for (nodolesc nodo : lista) {
                sb.append("NumVehiculo: ").append(nodo.numvehiculo)
                  .append(", Nombre: ").append(nodo.nombre)
                  .append(", Existencia: ").append(nodo.existencia)
                  .append(", Precio: ").append(nodo.precio)
                  .append(", Tipo: ").append(nodo.tipo)
                  .append("\n");
            }
            JOptionPane.showMessageDialog(null, sb.length() > 0 ? sb.toString() : "Lista vacia");
        }
    }

    public class arbolbinario {
        private nodoab raiz;

        public arbolbinario() {
            raiz = null;
        }

        public nodoab getRaiz() {
            return raiz;
        }

        public void insertar(int numvehiculo, String nombre, int existencia, float precio, char tipo) {
            nodoab nuevo = new nodoab();
            nuevo.numvehiculo = numvehiculo;
            nuevo.nombre = nombre;
            nuevo.existencia = existencia;
            nuevo.precio = precio;
            nuevo.tipo = tipo;
            nuevo.izq = null;
            nuevo.der = null;

            if (raiz == null) {
                raiz = nuevo;
            } else {
                insertarRecursivo(raiz, nuevo);
            }
        }

        public void eliminarNodo(int numvehiculo) {
            raiz = eliminarRecursivo(raiz, numvehiculo);
        }

        private nodoab eliminarRecursivo(nodoab actual, int numvehiculo) {
            if (actual == null) {
                return null;
            }
            if (numvehiculo < actual.numvehiculo) {
                actual.izq = eliminarRecursivo(actual.izq, numvehiculo);
            } else if (numvehiculo > actual.numvehiculo) {
                actual.der = eliminarRecursivo(actual.der, numvehiculo);
            } else {
                if (actual.izq == null && actual.der == null) {
                    return null;
                } else if (actual.izq == null) {
                    return actual.der;
                } else if (actual.der == null) {
                    return actual.izq;
                } else {
                    nodoab sucesor = encontrarMinimo(actual.der);
                    actual.numvehiculo = sucesor.numvehiculo;
                    actual.nombre = sucesor.nombre;
                    actual.existencia = sucesor.existencia;
                    actual.precio = sucesor.precio;
                    actual.tipo = sucesor.tipo;
                    actual.der = eliminarRecursivo(actual.der, sucesor.numvehiculo);
                }
            }
            return actual;
        }

        private nodoab encontrarMinimo(nodoab nodo) {
            while (nodo.izq != null) {
                nodo = nodo.izq;
            }
            return nodo;
        }

        private void insertarRecursivo(nodoab actual, nodoab nuevo) {
            if (nuevo.numvehiculo < actual.numvehiculo) {
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
    }

    public class recorridosbinarios{
        private arbolbinario arbol;

        public recorridosbinarios(arbolbinario arbol) {
            this.arbol = arbol;
        }

        public void recorridoInOrden() {
            StringBuilder sb = new StringBuilder();
            recorridoInOrdenRec(arbol.getRaiz(), sb);
            JOptionPane.showMessageDialog(null, sb.length() > 0 ? sb.toString() : "Arbol vacio", "Recorrido InOrder", JOptionPane.INFORMATION_MESSAGE);
        }

        private void recorridoInOrdenRec(nodoab nodo, StringBuilder sb) {
            if (nodo != null) {
                recorridoInOrdenRec(nodo.izq, sb);
                sb.append("NumVehiculo: ").append(nodo.numvehiculo)
                  .append(", Nombre: ").append(nodo.nombre)
                  .append(", Existencia: ").append(nodo.existencia)
                  .append(", Precio: ").append(nodo.precio)
                  .append(", Tipo: ").append(nodo.tipo)
                  .append("\n");
                recorridoInOrdenRec(nodo.der, sb);
            }
        }

        public void recorridoPreOrden() {
            StringBuilder sb = new StringBuilder();
            recorridoPreOrdenRec(arbol.getRaiz(), sb);
            JOptionPane.showMessageDialog(null, sb.length() > 0 ? sb.toString() : "Arbol vacio", "Recorrido PreOrder", JOptionPane.INFORMATION_MESSAGE);
        }

        private void recorridoPreOrdenRec(nodoab nodo, StringBuilder sb) {
            if (nodo != null) {
                sb.append("NumVehiculo: ").append(nodo.numvehiculo)
                  .append(", Nombre: ").append(nodo.nombre)
                  .append(", Existencia: ").append(nodo.existencia)
                  .append(", Precio: ").append(nodo.precio)
                  .append(", Tipo: ").append(nodo.tipo)
                  .append("\n");
                recorridoPreOrdenRec(nodo.izq, sb);
                recorridoPreOrdenRec(nodo.der, sb);
            }
        }

        public void recorridoPostOrden() {
            StringBuilder sb = new StringBuilder();
            recorridoPostOrdenRec(arbol.getRaiz(), sb);
            JOptionPane.showMessageDialog(null, sb.length() > 0 ? sb.toString() : "Arbol vacio", "Recorrido PostOrder", JOptionPane.INFORMATION_MESSAGE);
        }

        private void recorridoPostOrdenRec(nodoab nodo, StringBuilder sb) {
            if (nodo != null) {
                recorridoPostOrdenRec(nodo.izq, sb);
                recorridoPostOrdenRec(nodo.der, sb);
                sb.append("NumVehiculo: ").append(nodo.numvehiculo)
                  .append(", Nombre: ").append(nodo.nombre)
                  .append(", Existencia: ").append(nodo.existencia)
                  .append(", Precio: ").append(nodo.precio)
                  .append(", Tipo: ").append(nodo.tipo)
                  .append("\n");
            }
        }
    }

    public class contarnodos{
        private arbolbinario arbol;

        public contarnodos(arbolbinario arbol) {
            this.arbol = arbol;
        }

        public int contarNodos(nodoab nodo) {
            if (nodo == null) {
                return 0;
            }
            return 1 + contarNodos(nodo.izq) + contarNodos(nodo.der);
        }
    }

    public class profundidadBinario{
        private arbolbinario arbol;

        public profundidadBinario(arbolbinario arbol) {
            this.arbol = arbol;
        }

        public int calcularProfundidad(nodoab nodo) {
            if (nodo == null) {
                return 0;
            }
            int izquierda = calcularProfundidad(nodo.izq);
            int derecha = calcularProfundidad(nodo.der);
            return Math.max(izquierda, derecha) + 1;
        }
    }

    public class copiarArbolABaLista{
        private arbolbinario arbol;
        private ListaVehiculos listaVehiculos;

        public copiarArbolABaLista(arbolbinario arbol) {
            this.arbol = arbol;
            this.listaVehiculos = new ListaVehiculos();
        }

        public void copiarArbolANodo() {
            listaVehiculos.lista.clear();
            copiarRecursivo(arbol.getRaiz());
            JOptionPane.showMessageDialog(null, "Arbol copiado a lista encadenada.");
        }

        private void copiarRecursivo(nodoab nodo) {
            if (nodo != null) {
                nodolesc nuevo = new nodolesc();
                nuevo.numvehiculo = nodo.numvehiculo;
                nuevo.nombre = nodo.nombre;
                nuevo.existencia = nodo.existencia;
                nuevo.precio = nodo.precio;
                nuevo.tipo = nodo.tipo;
                listaVehiculos.agregarVehiculo(nuevo);

                copiarRecursivo(nodo.izq);
                copiarRecursivo(nodo.der);
            }
        }

        public void imprimirLista() {
            listaVehiculos.imprimirLista();
        }
    }

    public class busquedaPorPrecio {
        private arbolbinario arbol;

        public busquedaPorPrecio(arbolbinario arbol) {
            this.arbol = arbol;
        }

        public void buscarPorRango(float min, float max) {
            StringBuilder sb = new StringBuilder();
            buscarRecursivo(arbol.getRaiz(), min, max, sb);
            JOptionPane.showMessageDialog(null, sb.length() > 0 ? sb.toString() : "No hay vehiculos en ese rango de precio.", "Busqueda por rango de precio", JOptionPane.INFORMATION_MESSAGE);
        }

        private void buscarRecursivo(nodoab nodo, float min, float max, StringBuilder sb) {
            if (nodo != null) {
                if (nodo.precio >= min && nodo.precio <= max) {
                    sb.append("NumVehiculo: ").append(nodo.numvehiculo)
                      .append(", Nombre: ").append(nodo.nombre)
                      .append(", Existencia: ").append(nodo.existencia)
                      .append(", Tipo: ").append(nodo.tipo)
                      .append(", Precio: ").append(nodo.precio)
                      .append("\n");
                }
                buscarRecursivo(nodo.izq, min, max, sb);
                buscarRecursivo(nodo.der, min, max, sb);
            }
        }
    }

    public class busquedaPorTipo {
        private arbolbinario arbol;

        public busquedaPorTipo(arbolbinario arbol) {
            this.arbol = arbol;
        }

        public void buscarPorTipo(char tipoBuscado) {
            StringBuilder sb = new StringBuilder();
            buscarRecursivo(arbol.getRaiz(), tipoBuscado, sb);
            JOptionPane.showMessageDialog(null, sb.length() > 0 ? sb.toString() : "No hay vehiculos de ese tipo.", "Busqueda por tipo", JOptionPane.INFORMATION_MESSAGE);
        }

        private void buscarRecursivo(nodoab nodo, char tipoBuscado, StringBuilder sb) {
            if (nodo != null) {
                if (Character.toUpperCase(nodo.tipo) == Character.toUpperCase(tipoBuscado)) {
                    sb.append("NumVehiculo: ").append(nodo.numvehiculo)
                      .append(", Nombre: ").append(nodo.nombre)
                      .append(", Existencia: ").append(nodo.existencia)
                      .append(", Tipo: ").append(nodo.tipo)
                      .append(", Precio: ").append(nodo.precio)
                      .append("\n");
                }
                buscarRecursivo(nodo.izq, tipoBuscado, sb);
                buscarRecursivo(nodo.der, tipoBuscado, sb);
            }
        }
    }

    public static void main(String[] args) {
        ordinaario programa = new ordinaario();
        arbolbinario arbol = programa.new arbolbinario();
        recorridosbinarios recorridos = programa.new recorridosbinarios(arbol);
        contarnodos contador = programa.new contarnodos(arbol);
        profundidadBinario profundidad = programa.new profundidadBinario(arbol);
        copiarArbolABaLista copiaLista = programa.new copiarArbolABaLista(arbol);
        busquedaPorPrecio busqueda = programa.new busquedaPorPrecio(arbol);
        busquedaPorTipo busquedaTipo = programa.new busquedaPorTipo(arbol);

        String nombreAlumno = "Diego Yasir Marin Medina";
        String matricula = "23096254";
        String materia = "Estructura de Datos";
        String tipoexamen = "Examen 1";

        JOptionPane.showMessageDialog(
            null,
            "Nombre: " + nombreAlumno + "\n" +
            "Matrícula: " + matricula + "\n" +
            "Materia: " + materia,
            "Tipo de Examen: " + tipoexamen + "\n" +
            "Datos del Alumno",
            JOptionPane.INFORMATION_MESSAGE
        );
        int opcion;
        do {
            String menu = """
                MENU DE VEHICULOS
                1. Insertar vehiculo en arbol
                2. Eliminar vehiculo del arbol
                3. Recorrido InOrder
                4. Recorrido PreOrder
                5. Recorrido PostOrder
                6. Contar nodos del arbol
                7. Profundidad del arbol
                8. Copiar arbol a lista encadenada
                9. Mostrar lista encadenada
                10. Buscar vehiculos por rango de precio
                11. Buscar vehiculos por tipo
                12. Salir
                Que quieres hacer hoy?:
                """;
            opcion = Integer.parseInt(JOptionPane.showInputDialog(menu));

            switch (opcion) {
                case 1 -> {
                    int numvehiculo = Integer.parseInt(JOptionPane.showInputDialog("Numero de vehiculo:"));
                    String nombre = JOptionPane.showInputDialog("Nombre:");
                    int existencia = Integer.parseInt(JOptionPane.showInputDialog("Existencia:"));
                    float precio = Float.parseFloat(JOptionPane.showInputDialog("Precio:"));
                    char tipo = JOptionPane.showInputDialog("Tipo (G=gasolina, H=hibrido, E=electrico):").toUpperCase().charAt(0);
                    arbol.insertar(numvehiculo, nombre, existencia, precio, tipo);
                    JOptionPane.showMessageDialog(null, "Vehiculo guardado correctamente.");
                }
                case 2 -> {
                    int numvehiculo = Integer.parseInt(JOptionPane.showInputDialog("Numero de vehiculo a eliminar:"));
                    arbol.eliminarNodo(numvehiculo);
                    JOptionPane.showMessageDialog(null, "Vehiculo eliminado correctamente.");
                }
                case 3 -> recorridos.recorridoInOrden();
                case 4 -> recorridos.recorridoPreOrden();
                case 5 -> recorridos.recorridoPostOrden();
                case 6 -> {
                    int total = contador.contarNodos(arbol.getRaiz());
                    JOptionPane.showMessageDialog(null, "Total de nodos: " + total);
                }
                case 7 -> {
                    int prof = profundidad.calcularProfundidad(arbol.getRaiz());
                    JOptionPane.showMessageDialog(null, "Profundidad del arbol: " + prof);
                }
                case 8 -> {
                    copiaLista.copiarArbolANodo();
                    JOptionPane.showMessageDialog(null, "Arbol copiado a la lista encadenada correctamente.");
                }
                case 9 -> copiaLista.imprimirLista();
                case 10 -> {
                    float min = Float.parseFloat(JOptionPane.showInputDialog("Precio minimo:"));
                    float max = Float.parseFloat(JOptionPane.showInputDialog("Precio maximo:"));
                    busqueda.buscarPorRango(min, max);
                }
                case 11 -> {
                    char tipo = JOptionPane.showInputDialog("Tipo a buscar (G=gasolina, H=hibrido, E=electrico):").toUpperCase().charAt(0);
                    busquedaTipo.buscarPorTipo(tipo);
                }
                case 12 -> JOptionPane.showMessageDialog(null, "Saliendo...");
                default -> {
                    if (opcion < 1 || opcion > 12)
                        JOptionPane.showMessageDialog(null, "Opcion invalida.");
                }
            }
        } while (opcion != 12);
    }
}