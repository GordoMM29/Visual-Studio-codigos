package estructura_java;

import java.util.Stack;

public class laberinto {
    private static final int[] MOV_I = {-1, -1, -1, 0, 1, 1, 1, 0};
    private static final int[] MOV_J = {-1, 0, 1, 1, 1, 0, -1, -1};
    private static final String[] DIRECCIONES = {"Arriba-Izquierda", "Arriba", "Arriba-Derecha","Derecha", "Abajo-Derecha", "Abajo", "Abajo-Izquierda", "Izquierda"};

    static class Paso {
        int i, j, movimiento;
        public Paso(int i, int j, int movimiento) {
            this.i = i;
            this.j = j;
            this.movimiento = movimiento;
        }
    }

    public static void main(String[] args) {
        int[][] laberinto = {
            {0, 1, 0, 0, 0, 1, 1, 1, 0, 0},
            {0, 1, 1, 1, 0, 0, 0, 1, 1, 0},
            {0, 0, 0, 1, 1, 1, 0, 0, 0, 0},
            {1, 1, 1, 1, 0, 0, 0, 1, 1, 0},
            {0, 0, 0, 0, 0, 1, 1, 1, 0, 0},
            {0, 1, 1, 1, 0, 0, 0, 1, 1, 0},
            {0, 0, 0, 1, 1, 1, 0, 0, 0, 0},
            {1, 1, 0, 0, 0, 0, 1, 1, 1, 0},
            {0, 0, 0, 1, 1, 1, 0, 0, 0, 0},
            {0, 1, 1, 1, 0, 0, 0, 1, 1, 0}
        };

        int inicioI = 0, inicioJ = 2; 
        int finI = 9, finJ = 7; 

        resolverLaberinto(laberinto, inicioI, inicioJ, finI, finJ);
    }

    public static void resolverLaberinto(int[][] laberinto, int inicioI, int inicioJ, int finI, int finJ) {
        int m = laberinto.length;
        int n = laberinto[0].length;

        int[][] camino = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                camino[i][j] = laberinto[i][j] == 1 ? 1 : 5;
            }
        }

        Stack<Paso> pila = new Stack<>();
        pila.push(new Paso(inicioI, inicioJ, 0));
        camino[inicioI][inicioJ] = 2;

        boolean encontrado = false;

        while (!pila.isEmpty() && !encontrado) {
            Paso actual = pila.peek();
            int i = actual.i;
            int j = actual.j;

            if (i == finI && j == finJ) {
                encontrado = true;
                break;
            }

            boolean movimientoHecho = false;
            for (int mov = actual.movimiento; mov < 8 && !movimientoHecho; mov++) {
                int nuevaI = i + MOV_I[mov];
                int nuevaJ = j + MOV_J[mov];

                if (nuevaI >= 0 && nuevaI < m && nuevaJ >= 0 && nuevaJ < n && 
                    camino[nuevaI][nuevaJ] == 5) {

                    pila.pop();
                    pila.push(new Paso(i, j, mov + 1));

                    pila.push(new Paso(nuevaI, nuevaJ, 0));
                    camino[nuevaI][nuevaJ] = 2;
                    movimientoHecho = true;

                    System.out.println("Movimiento: " + DIRECCIONES[mov] + 
                                       " a (" + nuevaI + ", " + nuevaJ + ")");
                }
            }

            if (!movimientoHecho) {
                pila.pop();
                camino[i][j] = 0;
                System.out.println("Retrocediendo desde (" + i + ", " + j + ")");
            }
        }

        if (encontrado) {
            System.out.println("\n¡Camino encontrado!");
            imprimirLaberinto(camino);
        } else {
            System.out.println("No se encontró un camino válido.");
        }
    }

    public static void imprimirLaberinto(int[][] laberinto) {
        for (int[] fila : laberinto) {
            for (int celda : fila) {
                System.out.print(celda + " ");
            }
            System.out.println();
        }
    }
}
