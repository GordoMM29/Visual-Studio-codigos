package estructura_java;
import javax.swing.*;
import java.util.*;

class Grafo {
    private Map<String, List<Arista>> listaAdyacencia;
    private boolean esDirigido;

    public Grafo(boolean esDirigido) {
        this.listaAdyacencia = new HashMap<>();
        this.esDirigido = esDirigido;
    }

    public void agregarVertice(String vertice) {
        listaAdyacencia.putIfAbsent(vertice, new ArrayList<>());
    }

    public void agregarArista(String origen, String destino, int peso) {
        agregarVertice(origen);
        agregarVertice(destino);
        
        listaAdyacencia.get(origen).add(new Arista(destino, peso));
        if (!esDirigido) {
            listaAdyacencia.get(destino).add(new Arista(origen, peso));
        }
    }

    public void eliminarVertice(String vertice) {
        for (List<Arista> aristas : listaAdyacencia.values()) {
            aristas.removeIf(arista -> arista.destino.equals(vertice));
        }
        listaAdyacencia.remove(vertice);
    }

    public void eliminarArista(String origen, String destino) {
        if (listaAdyacencia.containsKey(origen)) {
            listaAdyacencia.get(origen).removeIf(arista -> arista.destino.equals(destino));
        }
        if (!esDirigido && listaAdyacencia.containsKey(destino)) {
            listaAdyacencia.get(destino).removeIf(arista -> arista.destino.equals(origen));
        }
    }

    public Map<String, Integer> dijkstra(String origen) {
        Map<String, Integer> distancias = new HashMap<>();
        PriorityQueue<NodoDistancia> colaPrioridad = new PriorityQueue<>();
        Set<String> visitados = new HashSet<>();

        for (String vertice : listaAdyacencia.keySet()) {
            distancias.put(vertice, Integer.MAX_VALUE);
        }
        distancias.put(origen, 0);
        colaPrioridad.add(new NodoDistancia(origen, 0));

        while (!colaPrioridad.isEmpty()) {
            NodoDistancia actual = colaPrioridad.poll();
            String verticeActual = actual.vertice;

            if (visitados.contains(verticeActual)) continue;
            visitados.add(verticeActual);

            for (Arista arista : listaAdyacencia.getOrDefault(verticeActual, new ArrayList<>())) {
                String vecino = arista.destino;
                int nuevaDistancia = distancias.get(verticeActual) + arista.peso;

                if (nuevaDistancia < distancias.get(vecino)) {
                    distancias.put(vecino, nuevaDistancia);
                    colaPrioridad.add(new NodoDistancia(vecino, nuevaDistancia));
                }
            }
        }

        return distancias;
    }

    public List<Arista> kruskal() {
        List<Arista> aristas = new ArrayList<>();
        UnionFind uf = new UnionFind(new ArrayList<>(listaAdyacencia.keySet()));

        for (String vertice : listaAdyacencia.keySet()) {
            for (Arista arista : listaAdyacencia.get(vertice)) {
                if (esDirigido || vertice.compareTo(arista.destino) < 0) {
                    aristas.add(new Arista(vertice, arista.destino, arista.peso));
                }
            }
        }

        Collections.sort(aristas);

        List<Arista> arbolExpansionMinima = new ArrayList<>();

        for (Arista arista : aristas) {
            if (uf.find(arista.origen) != uf.find(arista.destino)) {
                uf.union(arista.origen, arista.destino);
                arbolExpansionMinima.add(arista);
            }
        }

        return arbolExpansionMinima;
    }

    public List<String> bfs(String inicio) {
        List<String> resultado = new ArrayList<>();
        Set<String> visitados = new HashSet<>();
        Queue<String> cola = new LinkedList<>();

        visitados.add(inicio);
        cola.add(inicio);

        while (!cola.isEmpty()) {
            String actual = cola.poll();
            resultado.add(actual);

            for (Arista arista : listaAdyacencia.getOrDefault(actual, new ArrayList<>())) {
                if (!visitados.contains(arista.destino)) {
                    visitados.add(arista.destino);
                    cola.add(arista.destino);
                }
            }
        }

        return resultado;
    }

    public List<String> dfs(String inicio) {
        List<String> resultado = new ArrayList<>();
        Set<String> visitados = new HashSet<>();
        Stack<String> pila = new Stack<>();

        pila.push(inicio);

        while (!pila.isEmpty()) {
            String actual = pila.pop();

            if (!visitados.contains(actual)) {
                visitados.add(actual);
                resultado.add(actual);

                List<Arista> vecinos = listaAdyacencia.getOrDefault(actual, new ArrayList<>());
                Collections.reverse(vecinos);

                for (Arista arista : vecinos) {
                    if (!visitados.contains(arista.destino)) {
                        pila.push(arista.destino);
                    }
                }
            }
        }

        return resultado;
    }

    public String imprimirGrafo() {
        StringBuilder sb = new StringBuilder();
        for (String vertice : listaAdyacencia.keySet()) {
            sb.append(vertice).append(" -> ");
            for (Arista arista : listaAdyacencia.get(vertice)) {
                sb.append(arista.destino).append("(").append(arista.peso).append(") ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    static class Arista implements Comparable<Arista> {
        String origen;
        String destino;
        int peso;

        public Arista(String destino, int peso) {
            this.destino = destino;
            this.peso = peso;
        }

        public Arista(String origen, String destino, int peso) {
            this.origen = origen;
            this.destino = destino;
            this.peso = peso;
        }

        @Override
        public int compareTo(Arista otra) {
            return Integer.compare(this.peso, otra.peso);
        }
    }

    static class NodoDistancia implements Comparable<NodoDistancia> {
        String vertice;
        int distancia;

        public NodoDistancia(String vertice, int distancia) {
            this.vertice = vertice;
            this.distancia = distancia;
        }

        @Override
        public int compareTo(NodoDistancia otro) {
            return Integer.compare(this.distancia, otro.distancia);
        }
    }

    static class UnionFind {
        private Map<String, String> padre;
        private Map<String, Integer> rango;

        public UnionFind(List<String> vertices) {
            padre = new HashMap<>();
            rango = new HashMap<>();

            for (String vertice : vertices) {
                padre.put(vertice, vertice);
                rango.put(vertice, 0);
            }
        }

        public String find(String vertice) {
            if (!padre.get(vertice).equals(vertice)) {
                padre.put(vertice, find(padre.get(vertice)));
            }
            return padre.get(vertice);
        }

        public void union(String a, String b) {
            String raizA = find(a);
            String raizB = find(b);

            if (raizA.equals(raizB)) return;

            if (rango.get(raizA) < rango.get(raizB)) {
                padre.put(raizA, raizB);
            } else if (rango.get(raizA) > rango.get(raizB)) {
                padre.put(raizB, raizA);
            } else {
                padre.put(raizB, raizA);
                rango.put(raizA, rango.get(raizA) + 1);
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Grafo grafo = new Grafo(false);
        boolean continuar = true;

        while (continuar) {
            String opcion = JOptionPane.showInputDialog(
                "Menú Principal\n\n" +
                "1. Agregar vértice\n" +
                "2. Agregar arista\n" +
                "3. Eliminar vértice\n" +
                "4. Eliminar arista\n" +
                "5. Mostrar grafo\n" +
                "6. Dijkstra (Camino más corto)\n" +
                "7. Kruskal (Árbol de expansión mínima)\n" +
                "8. BFS (Recorrido en amplitud)\n" +
                "9. DFS (Recorrido en profundidad)\n" +
                "10. Salir\n\n" +
                "Seleccione una opción:");

            if (opcion == null) {
                continuar = false;
                continue;
            }

            try {
                switch (opcion) {
                    case "1":
                        String vertice = JOptionPane.showInputDialog("Ingrese el nombre del vértice:");
                        if (vertice != null && !vertice.trim().isEmpty()) {
                            grafo.agregarVertice(vertice);
                            JOptionPane.showMessageDialog(null, "Vértice agregado correctamente.");
                        }
                        break;

                    case "2":
                        String origen = JOptionPane.showInputDialog("Ingrese el vértice origen:");
                        String destino = JOptionPane.showInputDialog("Ingrese el vértice destino:");
                        String pesoStr = JOptionPane.showInputDialog("Ingrese el peso de la arista:");
                        if (origen != null && destino != null && pesoStr != null) {
                            try {
                                int peso = Integer.parseInt(pesoStr);
                                grafo.agregarArista(origen, destino, peso);
                                JOptionPane.showMessageDialog(null, "Arista agregada correctamente.");
                            } catch (NumberFormatException e) {
                                JOptionPane.showMessageDialog(null, "El peso debe ser un número entero.");
                            }
                        }
                        break;

                    case "3":
                        String verticeEliminar = JOptionPane.showInputDialog("Ingrese el vértice a eliminar:");
                        if (verticeEliminar != null) {
                            grafo.eliminarVertice(verticeEliminar);
                            JOptionPane.showMessageDialog(null, "Vértice eliminado correctamente.");
                        }
                        break;

                    case "4":
                        String origenEliminar = JOptionPane.showInputDialog("Ingrese el vértice origen de la arista:");
                        String destinoEliminar = JOptionPane.showInputDialog("Ingrese el vértice destino de la arista:");
                        if (origenEliminar != null && destinoEliminar != null) {
                            grafo.eliminarArista(origenEliminar, destinoEliminar);
                            JOptionPane.showMessageDialog(null, "Arista eliminada correctamente.");
                        }
                        break;

                    case "5":
                        JOptionPane.showMessageDialog(null, "Grafo actual:\n\n" + grafo.imprimirGrafo());
                        break;

                    case "6":
                        String inicioDijkstra = JOptionPane.showInputDialog("Ingrese el vértice de inicio para Dijkstra:");
                        if (inicioDijkstra != null) {
                            Map<String, Integer> distancias = grafo.dijkstra(inicioDijkstra);
                            StringBuilder sb = new StringBuilder("Distancias más cortas desde " + inicioDijkstra + ":\n\n");
                            for (Map.Entry<String, Integer> entry : distancias.entrySet()) {
                                sb.append(inicioDijkstra).append(" -> ").append(entry.getKey())
                                  .append(": ").append(entry.getValue()).append("\n");
                            }
                            JOptionPane.showMessageDialog(null, sb.toString());
                        }
                        break;

                    case "7":
                        List<Grafo.Arista> arbol = grafo.kruskal();
                        StringBuilder sbKruskal = new StringBuilder("Árbol de expansión mínima (Kruskal):\n\n");
                        for (Grafo.Arista arista : arbol) {
                            sbKruskal.append(arista.origen).append(" - ").append(arista.destino)
                                     .append(" (").append(arista.peso).append(")\n");
                        }
                        JOptionPane.showMessageDialog(null, sbKruskal.toString());
                        break;

                    case "8":
                        String inicioBFS = JOptionPane.showInputDialog("Ingrese el vértice de inicio para BFS:");
                        if (inicioBFS != null) {
                            List<String> bfsResult = grafo.bfs(inicioBFS);
                            JOptionPane.showMessageDialog(null, "Recorrido BFS:\n\n" + String.join(" -> ", bfsResult));
                        }
                        break;

                    case "9":
                        String inicioDFS = JOptionPane.showInputDialog("Ingrese el vértice de inicio para DFS:");
                        if (inicioDFS != null) {
                            List<String> dfsResult = grafo.dfs(inicioDFS);
                            JOptionPane.showMessageDialog(null, "Recorrido DFS:\n\n" + String.join(" -> ", dfsResult));
                        }
                        break;

                    case "10":
                        continuar = false;
                        break;

                    default:
                        JOptionPane.showMessageDialog(null, "Opción no válida. Intente nuevamente.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }
        }
    }
}