import javax.swing.*;
import java.util.*;

public class tarea10 {
    
    static class Alumno {
        private String matricula;
        private String nombre;
        private int promedio;
        private int edad;
        private String grupo;
        
        public Alumno(String matricula, String nombre, int promedio, int edad, String grupo) {
            this.matricula = matricula;
            this.nombre = nombre;
            this.promedio = promedio;
            this.edad = edad;
            this.grupo = grupo;
        }
        
        // Getters
        public String getMatricula() { return matricula; }
        public String getNombre() { return nombre; }
        public int getPromedio() { return promedio; }
        public int getEdad() { return edad; }
        public String getGrupo() { return grupo; }
        

        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Alumno alumno = (Alumno) obj;
            //No se pueden repetir matriculas 
            if (matricula.equals(alumno.matricula) && grupo.equals(alumno.grupo)) {
                return true;
            }
            // Regla: No puede haber el mismo nombre en dos matrículas diferentes
            if (nombre.equals(alumno.nombre)) {
                return true;
            }
            return false;
        }
        
        public int hashCode() {
            return Objects.hash(matricula, grupo, nombre);
        }
        
        public String toString() {
            return String.format("Mat: %s | Nom: %s | Prom: %d | Edad: %d | Grupo: %s", 
                               matricula, nombre, promedio, edad, grupo);
        }
    }
    
    static class AlumnoComparator implements Comparator<Alumno> {
        public int compare(Alumno a1, Alumno a2) {
            int grupoCompare = a1.getGrupo().compareTo(a2.getGrupo());
            if (grupoCompare != 0) return grupoCompare;
            
            int matriculaCompare = a1.getMatricula().compareTo(a2.getMatricula());
            if (matriculaCompare != 0) return matriculaCompare;
            
            return a1.getNombre().compareTo(a2.getNombre());
        }
    }
    
    // Conjuntos
    private Set<Alumno> conjunto1;
    private Set<Alumno> conjunto2;
    private Set<Alumno> conjunto3;
    
    public tarea10() {
        conjunto1 = new TreeSet<>(new AlumnoComparator());
        conjunto2 = new HashSet<>();
        conjunto3 = new LinkedHashSet<>();
    }
    
    public void iniciar() {
        while (true) {
            String menu = "GESTION DE ALUMNOS\n\n" +
                         "1. Agregar alumno a todos los conjuntos\n" +
                         "2. Agregar alumno a conjunto específico\n" +
                         "3. Mostrar todos los conjuntos\n" +
                         "4. Mostrar conjunto específico\n" +
                         "5. Unión\n" +
                         "6. Intersección\n" +
                         "7. Buscar alumnos por promedio mínimo\n" +
                         "8. Cargar datos de ejemplo\n" +
                         "9. Verificar reglas del sistema\n" +
                         "0. Salir\n\n" +
                         "Que quiere hacer hoy?";
            
            String opcion = JOptionPane.showInputDialog(null, menu, "Menu", JOptionPane.QUESTION_MESSAGE);
            
            if (opcion == null || opcion.equals("0")) {
                JOptionPane.showMessageDialog(null, "Adios");
                break;
            }
            
            procesarOpcion(opcion);
        }
    }
    
    private void procesarOpcion(String opcion) {
        switch (opcion) {
            case "1":
                agregarAlumnoATodos();
                break;
            case "2":
                agregarAlumnoEspecifico();
                break;
            case "3":
                mostrarTodosConjuntos();
                break;
            case "4":
                mostrarConjuntoEspecifico();
                break;
            case "5":
                realizarUnion();
                break;
            case "6":
                realizarInterseccion();
                break;
            case "7":
                buscarPorPromedio();
                break;
            case "8":
                cargarDatosEjemplo();
                break;
            case "9":
                verificarReglas();
                break;
            default:
                JOptionPane.showMessageDialog(null, "Opción no valida", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void agregarAlumnoATodos() {
        try {
            String matricula = JOptionPane.showInputDialog("Ingrese matricula del alumno:");
            if (matricula == null) return;
            
            String nombre = JOptionPane.showInputDialog("Ingrese nombre completo:");
            if (nombre == null) return;
            
            String promedioStr = JOptionPane.showInputDialog("Ingrese promedio (0-100):");
            if (promedioStr == null) return;
            int promedio = Integer.parseInt(promedioStr);
            
            String edadStr = JOptionPane.showInputDialog("Ingrese edad:");
            if (edadStr == null) return;
            int edad = Integer.parseInt(edadStr);
            
            String grupo = JOptionPane.showInputDialog("Ingrese grupo (A, B, C, etc.):");
            if (grupo == null) return;
            
            Alumno nuevoAlumno = new Alumno(matricula, nombre, promedio, edad, grupo);
            
            boolean agregado1 = conjunto1.add(nuevoAlumno);
            boolean agregado2 = conjunto2.add(nuevoAlumno);
            boolean agregado3 = conjunto3.add(nuevoAlumno);
            
            String mensaje = "Resultado de la operación:\n" +
                           "Conjunto1 (TreeSet): " + (agregado1 ? " Agregado" : " No agregado (duplicado)") + "\n" +
                           "Conjunto2 (HashSet): " + (agregado2 ? " Agregado" : " No agregado (duplicado)") + "\n" +
                           "Conjunto3 (LinkedHashSet): " + (agregado3 ? " Agregado" : " No agregado (duplicado)");
            
            JOptionPane.showMessageDialog(null, mensaje, "Resultado", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: Los valores numericos no son válidos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void agregarAlumnoEspecifico() {
        String[] opciones = {"Conjunto1 (TreeSet)", "Conjunto2 (HashSet)", "Conjunto3 (LinkedHashSet)"};
        int conjunto = JOptionPane.showOptionDialog(null, 
            "¿A que conjunto desea agregar el alumno?", 
            "Seleccionar Conjunto",
            JOptionPane.DEFAULT_OPTION, 
            JOptionPane.QUESTION_MESSAGE, 
            null, 
            opciones, 
            opciones[0]);
        
        if (conjunto == -1) return;
        
        try {
            String matricula = JOptionPane.showInputDialog("Ingrese matricula del alumno:");
            if (matricula == null) return;
            
            String nombre = JOptionPane.showInputDialog("Ingrese nombre completo:");
            if (nombre == null) return;
            
            String promedioStr = JOptionPane.showInputDialog("Ingrese promedio (0-100):");
            if (promedioStr == null) return;
            int promedio = Integer.parseInt(promedioStr);
            
            String edadStr = JOptionPane.showInputDialog("Ingrese edad:");
            if (edadStr == null) return;
            int edad = Integer.parseInt(edadStr);
            
            String grupo = JOptionPane.showInputDialog("Ingrese grupo (A, B, C, etc.):");
            if (grupo == null) return;
            
            Alumno nuevoAlumno = new Alumno(matricula, nombre, promedio, edad, grupo);
            
            boolean agregado = false;
            String nombreConjunto = "";
            
            switch (conjunto) {
                case 0:
                    agregado = conjunto1.add(nuevoAlumno);
                    nombreConjunto = "Conjunto1 (TreeSet)";
                    break;
                case 1:
                    agregado = conjunto2.add(nuevoAlumno);
                    nombreConjunto = "Conjunto2 (HashSet)";
                    break;
                case 2:
                    agregado = conjunto3.add(nuevoAlumno);
                    nombreConjunto = "Conjunto3 (LinkedHashSet)";
                    break;
            }
            
            if (agregado) {
                JOptionPane.showMessageDialog(null, "Alumno agregado exitosamente a " + nombreConjunto);
            } else {
                JOptionPane.showMessageDialog(null, 
                    "No se pudo agregar el alumno. Posible violación de reglas:\n" +
                    "- Matricula repetida en el mismo grupo\n" +
                    "- Nombre repetido en diferente matricula", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: Los valores numericos no son validos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void mostrarTodosConjuntos() {
        String reporte = "REPORTE COMPLETO\n\n";
        
        reporte += "CONJUNTO 1 (TreeSet) - " + conjunto1.size() + " alumnos:\n";
        reporte += "----------------------------------------\n";
        reporte += formatearConjunto(conjunto1);
        
        reporte += "\n\nCONJUNTO 2 (HashSet) - " + conjunto2.size() + " alumnos:\n";
        reporte += "----------------------------------------\n";
        reporte += formatearConjunto(conjunto2);
        
        reporte += "\n\nCONJUNTO 3 (LinkedHashSet) - " + conjunto3.size() + " alumnos:\n";
        reporte += "----------------------------------------\n";
        reporte += formatearConjunto(conjunto3);
        
        mostrarReporte(reporte);
    }
    
    private void mostrarConjuntoEspecifico() {
        String[] opciones = {"Conjunto1 (TreeSet)", "Conjunto2 (HashSet)", "Conjunto3 (LinkedHashSet)"};
        int conjunto = JOptionPane.showOptionDialog(null, 
            "¿Que conjunto desea visualizar?", 
            "Seleccionar Conjunto",
            JOptionPane.DEFAULT_OPTION, 
            JOptionPane.QUESTION_MESSAGE, 
            null, 
            opciones, 
            opciones[0]);
        
        if (conjunto == -1) return;
        
        Set<Alumno> conjuntoSeleccionado = null;
        String nombreConjunto = "";
        
        switch (conjunto) {
            case 0:
                conjuntoSeleccionado = conjunto1;
                nombreConjunto = "Conjunto1 (TreeSet)";
                break;
            case 1:
                conjuntoSeleccionado = conjunto2;
                nombreConjunto = "Conjunto2 (HashSet)";
                break;
            case 2:
                conjuntoSeleccionado = conjunto3;
                nombreConjunto = "Conjunto3 (LinkedHashSet)";
                break;
        }
        
        String reporte = "= " + nombreConjunto + " =\n";
        reporte += "Total de alumnos: " + conjuntoSeleccionado.size() + "\n\n";
        reporte += formatearConjunto(conjuntoSeleccionado);
        
        mostrarReporte(reporte);
    }
    
    private void realizarUnion() {
        Set<Alumno> union = new TreeSet<>(new AlumnoComparator());
        union.addAll(conjunto1);
        union.addAll(conjunto2);
        
        String reporte = "UNION: Conjunto1 ∪ Conjunto2\n";
        reporte += "Conjunto1: " + conjunto1.size() + " alumnos\n";
        reporte += "Conjunto2: " + conjunto2.size() + " alumnos\n";
        reporte += "Resultado: " + union.size() + " alumnos (sin duplicados)\n\n";
        reporte += formatearConjunto(union);
        
        mostrarReporte(reporte);
    }
    
    private void realizarInterseccion() {
        Set<Alumno> interseccion = new HashSet<>(conjunto2);
        interseccion.retainAll(conjunto3);
        
        String reporte = "INTERSECCION: Conjunto2 ∩ Conjunto3\n";
        reporte += "Conjunto2: " + conjunto2.size() + " alumnos\n";
        reporte += "Conjunto3: " + conjunto3.size() + " alumnos\n";
        reporte += "Resultado: " + interseccion.size() + " alumnos en común\n\n";
        reporte += formatearConjunto(interseccion);
        
        mostrarReporte(reporte);
    }
    
    private void buscarPorPromedio() {
        try {
            String promedioStr = JOptionPane.showInputDialog("Ingrese el promedio minimo a buscar (0-100):");
            if (promedioStr == null) return;
            int promedioMinimo = Integer.parseInt(promedioStr);
            
            String[] opciones = {"Todos los conjuntos", "Conjunto1", "Conjunto2", "Conjunto3"};
            int opcion = JOptionPane.showOptionDialog(null, 
                "¿En que conjunto desea buscar?", 
                "Seleccionar Conjunto",
                JOptionPane.DEFAULT_OPTION, 
                JOptionPane.QUESTION_MESSAGE, 
                null, 
                opciones, 
                opciones[0]);
            
            if (opcion == -1) return;
            
            String reporte = "Alumnos con promedio >= " + promedioMinimo + " ===\n\n";
            
            if (opcion == 0 || opcion == 1) {
                reporte += "Conjunto1 (TreeSet):\n";
                reporte += formatearAlumnosPorPromedio(conjunto1, promedioMinimo);
            }
            
            if (opcion == 0 || opcion == 2) {
                reporte += "\nConjunto2 (HashSet):\n";
                reporte += formatearAlumnosPorPromedio(conjunto2, promedioMinimo);
            }
            
            if (opcion == 0 || opcion == 3) {
                reporte += "\nConjunto3 (LinkedHashSet):\n";
                reporte += formatearAlumnosPorPromedio(conjunto3, promedioMinimo);
            }
            
            mostrarReporte(reporte);
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: Ingrese un numero valido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void cargarDatosEjemplo() {
        // Limpiar conjuntos
        conjunto1.clear();
        conjunto2.clear();
        conjunto3.clear();
        
        Alumno[] alumnosEjemplo = {
            new Alumno("A001", "Ana García", 85, 20, "A"),
            new Alumno("A002", "Carlos López", 90, 21, "A"),
            new Alumno("A003", "María Rodríguez", 78, 19, "A"),
            new Alumno("A004", "Juan Pérez", 92, 22, "A"),
            new Alumno("A005", "Laura Martínez", 88, 20, "A"),
            new Alumno("B001", "Pedro Sánchez", 75, 21, "B"),
            new Alumno("B002", "Sofia Torres", 95, 20, "B"),
            new Alumno("B003", "Diego Ramírez", 82, 22, "B"),
            new Alumno("B004", "Valentina Castro", 89, 19, "B"),
            new Alumno("B005", "Andrés Vargas", 91, 21, "B")
        };
        
        for (Alumno alumno : alumnosEjemplo) {
            conjunto1.add(alumno);
            conjunto2.add(alumno);
            conjunto3.add(alumno);
        }
        
        JOptionPane.showMessageDialog(null, 
            "Datos de ejemplo cargados exitosamente!\n" +
            "10 alumnos agregados a cada conjunto.", 
            "Carga Exitosa", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void verificarReglas() {
        String reglas = "VERIFICACIÓN DE REGLAS\n\n" +
                       "Regla 1: No se pueden repetir matrículas en el mismo grupo\n" +
                       "Regla 2: No puede haber el mismo nombre en dos matrículas diferentes\n\n" +
                       "Estado actual de los conjuntos:\n\n" +
                       "Conjunto1 (TreeSet): " + conjunto1.size() + " alumnos\n" +
                       "Conjunto2 (HashSet): " + conjunto2.size() + " alumnos\n" +
                       "Conjunto3 (LinkedHashSet): " + conjunto3.size() + " alumnos\n\n";
        
        reglas += "Verificando posibles violaciones...\n";
        
        // Verificar en conjunto1
        Set<String> matriculasVistas = new HashSet<>();
        Set<String> nombresVistos = new HashSet<>();
        boolean hayViolaciones = false;
        
        for (Alumno alumno : conjunto1) {
            String claveMatricula = alumno.getMatricula() + "-" + alumno.getGrupo();
            if (matriculasVistas.contains(claveMatricula)) {
                reglas += " Violacion: Matricula " + alumno.getMatricula() + 
                         " repetida en grupo " + alumno.getGrupo() + "\n";
                hayViolaciones = true;
            }
            matriculasVistas.add(claveMatricula);
            
            if (nombresVistos.contains(alumno.getNombre())) {
                reglas += " Violación: Nombre '" + alumno.getNombre() + 
                         "' repetido en diferentes matriculas\n";
                hayViolaciones = true;
            }
            nombresVistos.add(alumno.getNombre());
        }
        
        if (!hayViolaciones) {
            reglas += "No se detectaron violaciones a las reglas en el Conjunto1\n";
        }
        
        reglas += "\nEl sistema está funcionando correctamente y previene duplicados automaticamente.";
        
        JOptionPane.showMessageDialog(null, reglas, "Verificacion de Reglas", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private String formatearConjunto(Set<Alumno> conjunto) {
        if (conjunto.isEmpty()) {
            return "No hay alumnos en este conjunto.\n";
        }
        
        StringBuilder sb = new StringBuilder();
        int contador = 1;
        for (Alumno alumno : conjunto) {
            sb.append(String.format("%2d. %s\n", contador++, alumno.toString()));
        }
        return sb.toString();
    }
    
    private String formatearAlumnosPorPromedio(Set<Alumno> conjunto, int promedioMinimo) {
        StringBuilder sb = new StringBuilder();
        int contador = 0;
        
        for (Alumno alumno : conjunto) {
            if (alumno.getPromedio() >= promedioMinimo) {
                sb.append(String.format("   • %s (Prom: %d)\n", 
                         alumno.getNombre(), alumno.getPromedio()));
                contador++;
            }
        }
        
        if (contador == 0) {
            sb.append("   No hay alumnos con ese promedio mínimo.\n");
        } else {
            sb.append("   Total: " + contador + " alumnos\n");
        }
        
        return sb.toString();
    }
    
    private void mostrarReporte(String contenido) {
        JTextArea textArea = new JTextArea(20, 50);
        textArea.setText(contenido);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        
        JOptionPane.showMessageDialog(null, scrollPane, "Reporte", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        tarea10 programa = new tarea10();
        programa.iniciar();
    }
}