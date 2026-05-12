import javax.swing.*;
import java.util.*;

public class trabajo_clase {
    
    // Clase Alumno 
    static class Alumno {
        private String nombre;
        private String horarioIda;
        private String horarioVuelta;
        private String lugarOrigen;
        
        public Alumno(String nombre, String horarioIda, String horarioVuelta, String lugarOrigen) {
            this.nombre = nombre;
            this.horarioIda = horarioIda;
            this.horarioVuelta = horarioVuelta;
            this.lugarOrigen = lugarOrigen;
        }
        
        public String getNombre() { return nombre; }
        public String getHorarioIda() { return horarioIda; }
        public String getHorarioVuelta() { return horarioVuelta; }
        public String getLugarOrigen() { return lugarOrigen; }
        
        public String toString() {
            return String.format("%-20s - Ida: %s - Vuelta: %s - %s", 
                               nombre, horarioIda, horarioVuelta, lugarOrigen);
        }
        
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Alumno alumno = (Alumno) obj;
            return nombre.equals(alumno.nombre);
        }
        
        public int hashCode() {
            return Objects.hash(nombre);
        }
    }
    
    // TreeSet
    static class AlumnoHorarioComparator implements Comparator<Alumno> {
        
        public int compare(Alumno a1, Alumno a2) {
            return a1.getHorarioIda().compareTo(a2.getHorarioIda());
        }
    }
    
    private Set<Alumno> alumnosTreeSet;
    private Set<Alumno> alumnosHashSet;
    private Set<Alumno> alumnosLinkedHashSet;
    private Set<String> horariosLugar1;
    private Set<String> horariosLugar2;
    
    private final String[] HORARIOS_L1_L2 = {"06:30", "07:00", "07:30"};
    private final String[] HORARIOS_L2_L1 = {"13:15", "14:10", "16:00"};
    
    public trabajo_clase() {
        alumnosTreeSet = new TreeSet<>(new AlumnoHorarioComparator());
        alumnosHashSet = new HashSet<>();
        alumnosLinkedHashSet = new LinkedHashSet<>();
        
        horariosLugar1 = new TreeSet<>(Arrays.asList(HORARIOS_L1_L2));
        horariosLugar2 = new TreeSet<>(Arrays.asList(HORARIOS_L2_L1));
        
        cargarAlumnosEjemplo();
    }
    
    private void cargarAlumnosEjemplo() {
        String[] nombres = {
            "Ana Garcia", "Carlos Lopez", "Maria Rodriguez", "Juan Perez", 
            "Laura Martinez", "Pedro Sanchez", "Sofia Torres", "Diego Ramirez",
            "Valentina Castro", "Andres Vargas", "Camila Flores", "Luis Herrera",
            "Fernanda Ruiz", "Javier Mendoza", "Daniela Ortiz", "Roberto Silva",
            "Gabriela Rios", "Oscar Chavez", "Patricia Vega", "Fernando Castro"
        };
        
        Random rand = new Random();
        
        for (int i = 0; i < 20; i++) {
            String nombre = nombres[i];
            String lugarOrigen = rand.nextBoolean() ? "Lugar1" : "Lugar2";
            
            String horarioIda;
            String horarioVuelta;
            
            if (lugarOrigen.equals("Lugar1")) {
                horarioIda = HORARIOS_L1_L2[rand.nextInt(3)];
                horarioVuelta = HORARIOS_L2_L1[rand.nextInt(3)];
            } else {
                horarioIda = HORARIOS_L2_L1[rand.nextInt(3)];
                horarioVuelta = HORARIOS_L1_L2[rand.nextInt(3)];
            }
            
            Alumno alumno = new Alumno(nombre, horarioIda, horarioVuelta, lugarOrigen);
            
            alumnosTreeSet.add(alumno);
            alumnosHashSet.add(alumno);
            alumnosLinkedHashSet.add(alumno);
        }
    }
    
    public void iniciar() {
        while (true) {
            String menu = "SISTEMA DE ANALISIS LOBUS\n" +
                         "--------------------------\n\n" +
                         "1. Ver todos los alumnos\n" +
                         "2. Ver horarios disponibles\n" +
                         "3. Alumnos por horario de ida\n" +
                         "4. Alumnos por lugar de origen\n" +
                         "5. Union de horarios (Lugar1 - Lugar2)\n" +
                         "6. Interseccion de horarios (Lugar1 - Lugar2)\n" +
                         "7. Diferencia de horarios (Lugar1 - Lugar2)\n" +
                         "8. HeadSet y TailSet de horarios\n" +
                         "9. Frecuencia de uso por horario\n" +
                         "10. Buscar alumno por nombre\n" +
                         "11. Ver los 3 tipos de conjuntos\n" +
                         "12. Verificar duplicados\n" +
                         "0. Salir\n\n" +
                         "Seleccione una opcion:";
            
            String opcion = JOptionPane.showInputDialog(null, menu, "Menu LOBUS", JOptionPane.QUESTION_MESSAGE);
            
            if (opcion == null || opcion.equals("0")) {
                JOptionPane.showMessageDialog(null, "Hasta luego.");
                break;
            }
            
            procesarOpcion(opcion);
        }
    }
    
    private void procesarOpcion(String opcion) {
        switch (opcion) {
            case "1": verTodosAlumnos(); break;
            case "2": verHorariosDisponibles(); break;
            case "3": alumnosPorHorarioIda(); break;
            case "4": alumnosPorLugarOrigen(); break;
            case "5": unionHorarios(); break;
            case "6": interseccionHorarios(); break;
            case "7": diferenciaHorarios(); break;
            case "8": headSetTailSetHorarios(); break;
            case "9": frecuenciaHorarios(); break;
            case "10": buscarAlumno(); break;
            case "11": verTiposConjuntos(); break;
            case "12": verificarDuplicados(); break;
            default: JOptionPane.showMessageDialog(null, "Opcion no valida.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void verTodosAlumnos() {
        String reporte = "LISTA DE ALUMNOS\n";
        reporte += "----------------\n\n";
        reporte += "Total: " + alumnosTreeSet.size() + " alumnos\n\n";
        
        int contador = 1;
        for (Alumno a : alumnosTreeSet) {
            reporte += contador + ". " + a.toString() + "\n";
            contador++;
        }
        
        mostrarReporte(reporte);
    }
    
    private void verHorariosDisponibles() {
        String reporte = "HORARIOS LOBUS\n";
        reporte += "--------------\n\n";
        
        reporte += "Ruta: Lugar1 a Lugar2\n";
        reporte += "  Horarios de ida: " + Arrays.toString(HORARIOS_L1_L2) + "\n";
        reporte += "  Horarios de vuelta: " + Arrays.toString(HORARIOS_L2_L1) + "\n\n";
        
        reporte += "Ruta: Lugar2 a Lugar1\n";
        reporte += "  Horarios de ida: " + Arrays.toString(HORARIOS_L2_L1) + "\n";
        reporte += "  Horarios de vuelta: " + Arrays.toString(HORARIOS_L1_L2) + "\n";
        
        mostrarReporte(reporte);
    }
    
    private void alumnosPorHorarioIda() {
        String reporte = "ALUMNOS POR HORARIO DE IDA\n";
        reporte += "--------------------------\n\n";
        
        Map<String, List<String>> alumnosPorHorario = new TreeMap<>();
        
        for (Alumno a : alumnosTreeSet) {
            alumnosPorHorario.computeIfAbsent(a.getHorarioIda(), k -> new ArrayList<>()).add(a.getNombre());
        }
        
        for (Map.Entry<String, List<String>> entry : alumnosPorHorario.entrySet()) {
            reporte += "Horario " + entry.getKey() + ":\n";
            reporte += "  Cantidad: " + entry.getValue().size() + " alumno(s)\n";
            reporte += "  Nombres: " + String.join(", ", entry.getValue()) + "\n\n";
        }
        
        mostrarReporte(reporte);
    }
    
    private void alumnosPorLugarOrigen() {
        String reporte = "ALUMNOS POR LUGAR DE ORIGEN\n";
        reporte += "---------------------------\n\n";
        
        List<String> lugar1 = new ArrayList<>();
        List<String> lugar2 = new ArrayList<>();
        
        for (Alumno a : alumnosTreeSet) {
            if (a.getLugarOrigen().equals("Lugar1")) {
                lugar1.add(a.getNombre() + " (" + a.getHorarioIda() + ")");
            } else {
                lugar2.add(a.getNombre() + " (" + a.getHorarioIda() + ")");
            }
        }
        
        reporte += "LUGAR1 (" + lugar1.size() + " alumnos):\n";
        for (String nombre : lugar1) {
            reporte += "  - " + nombre + "\n";
        }
        reporte += "\n";
        
        reporte += "LUGAR2 (" + lugar2.size() + " alumnos):\n";
        for (String nombre : lugar2) {
            reporte += "  - " + nombre + "\n";
        }
        
        mostrarReporte(reporte);
    }
    
    private void unionHorarios() {
        Set<String> union = new TreeSet<>(horariosLugar1);
        union.addAll(horariosLugar2);
        
        String reporte = "UNION DE HORARIOS\n";
        reporte += "-----------------\n\n";
        reporte += "Horarios Lugar1: " + horariosLugar1 + "\n";
        reporte += "Horarios Lugar2: " + horariosLugar2 + "\n\n";
        reporte += "Union: " + union + "\n";
        reporte += "Total: " + union.size() + " horarios\n\n";
        
        reporte += "Horarios ordenados:\n";
        for (String h : union) {
            reporte += "  - " + h + "\n";
        }
        
        mostrarReporte(reporte);
    }
    
    private void interseccionHorarios() {
        Set<String> interseccion = new TreeSet<>(horariosLugar1);
        interseccion.retainAll(horariosLugar2);
        
        String reporte = "INTERSECCION DE HORARIOS\n";
        reporte += "------------------------\n\n";
        reporte += "Horarios Lugar1: " + horariosLugar1 + "\n";
        reporte += "Horarios Lugar2: " + horariosLugar2 + "\n\n";
        reporte += "Interseccion: " + interseccion + "\n";
        reporte += "Horarios comunes: " + interseccion.size() + "\n\n";
        
        if (interseccion.isEmpty()) {
            reporte += "No hay horarios comunes entre Lugar1 y Lugar2.";
        } else {
            reporte += "Horarios que coinciden:\n";
            for (String h : interseccion) {
                reporte += "  - " + h + "\n";
            }
        }
        
        mostrarReporte(reporte);
    }
    
    private void diferenciaHorarios() {
        Set<String> diferencia = new TreeSet<>(horariosLugar1);
        diferencia.removeAll(horariosLugar2);
        
        String reporte = "DIFERENCIA DE HORARIOS\n";
        reporte += "----------------------\n\n";
        reporte += "Horarios Lugar1: " + horariosLugar1 + "\n";
        reporte += "Horarios Lugar2: " + horariosLugar2 + "\n\n";
        reporte += "Diferencia (Lugar1 - Lugar2): " + diferencia + "\n";
        reporte += "Horarios exclusivos de Lugar1: " + diferencia.size() + "\n\n";
        
        if (diferencia.isEmpty()) {
            reporte += "Todos los horarios de Lugar1 estan en Lugar2.";
        } else {
            reporte += "Horarios exclusivos de Lugar1:\n";
            for (String h : diferencia) {
                reporte += "  - " + h + "\n";
            }
        }
        
        mostrarReporte(reporte);
    }
    
    private void headSetTailSetHorarios() {
        String[] opciones = {"06:30", "07:00", "07:30", "13:15", "14:10", "16:00"};
        String horarioRef = (String) JOptionPane.showInputDialog(null,
            "Seleccione un horario de referencia:",
            "HeadSet y TailSet",
            JOptionPane.QUESTION_MESSAGE,
            null,
            opciones,
            opciones[0]);
        
        if (horarioRef == null) return;
        
        TreeSet<String> horariosTotales = new TreeSet<>();
        horariosTotales.addAll(horariosLugar1);
        horariosTotales.addAll(horariosLugar2);
        
        String reporte = "HeadSet y TailSet de Horarios\n";
        reporte += "------------------------------\n\n";
        reporte += "Horario de referencia: " + horarioRef + "\n\n";
        
        SortedSet<String> headSet = horariosTotales.headSet(horarioRef);
        reporte += "HeadSet (horarios menores a " + horarioRef + "):\n";
        reporte += "  Cantidad: " + headSet.size() + "\n";
        if (!headSet.isEmpty()) {
            reporte += "  Horarios: " + String.join(", ", headSet) + "\n";
        } else {
            reporte += "  No hay horarios menores\n";
        }
        reporte += "\n";
        
        SortedSet<String> tailSet = horariosTotales.tailSet(horarioRef);
        reporte += "TailSet (horarios mayores o iguales a " + horarioRef + "):\n";
        reporte += "  Cantidad: " + tailSet.size() + "\n";
        if (!tailSet.isEmpty()) {
            reporte += "  Horarios: " + String.join(", ", tailSet) + "\n";
        } else {
            reporte += "  No hay horarios mayores o iguales\n";
        }
        
        mostrarReporte(reporte);
    }
    
    private void frecuenciaHorarios() {
        String reporte = "FRECUENCIA DE HORARIOS\n";
        reporte += "----------------------\n\n";
        
        Map<String, Integer> frecuencia = new TreeMap<>();
        
        for (Alumno a : alumnosTreeSet) {
            frecuencia.put(a.getHorarioIda(), frecuencia.getOrDefault(a.getHorarioIda(), 0) + 1);
        }
        
        reporte += "Frecuencia de uso (ida):\n\n";
        
        int total = 0;
        for (Map.Entry<String, Integer> entry : frecuencia.entrySet()) {
            String barra = generarBarra(entry.getValue());
            reporte += String.format("%s : %2d alumnos %s\n", 
                entry.getKey(), entry.getValue(), barra);
            total += entry.getValue();
        }
        
        reporte += "\nTotal alumnos: " + total + "\n\n";
        
        Map.Entry<String, Integer> maxEntry = Collections.max(frecuencia.entrySet(), 
            Map.Entry.comparingByValue());
        Map.Entry<String, Integer> minEntry = Collections.min(frecuencia.entrySet(), 
            Map.Entry.comparingByValue());
        
        reporte += "Horario mas usado: " + maxEntry.getKey() + 
                  " (" + maxEntry.getValue() + " alumnos)\n";
        reporte += "Horario menos usado: " + minEntry.getKey() + 
                  " (" + minEntry.getValue() + " alumnos)";
        
        mostrarReporte(reporte);
    }
    
    private void buscarAlumno() {
        String nombre = JOptionPane.showInputDialog("Ingrese el nombre del alumno a buscar:");
        if (nombre == null || nombre.trim().isEmpty()) return;
        
        List<Alumno> encontrados = new ArrayList<>();
        for (Alumno a : alumnosTreeSet) {
            if (a.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                encontrados.add(a);
            }
        }
        
        if (!encontrados.isEmpty()) {
            String reporte = "ALUMNOS ENCONTRADOS\n";
            reporte += "-------------------\n\n";
            reporte += "Se encontraron " + encontrados.size() + " alumno(s):\n\n";
            
            for (int i = 0; i < encontrados.size(); i++) {
                Alumno a = encontrados.get(i);
                reporte += (i+1) + ". " + a.getNombre() + "\n";
                reporte += "   Horario ida: " + a.getHorarioIda() + "\n";
                reporte += "   Horario vuelta: " + a.getHorarioVuelta() + "\n";
                reporte += "   Lugar origen: " + a.getLugarOrigen() + "\n\n";
            }
            
            mostrarReporte(reporte);
        } else {
            JOptionPane.showMessageDialog(null, 
                "No se encontro alumno con nombre: " + nombre, 
                "Busqueda", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void verTiposConjuntos() {
        String reporte = "LOS 3 TIPOS DE CONJUNTOS\n";
        reporte += "------------------------\n\n";
        
        reporte += "1. TREESET (ordenado por horario de ida):\n";
        reporte += "   Tamaño: " + alumnosTreeSet.size() + " alumnos\n";
        reporte += "   Primeros 5:\n";
        int count = 0;
        for (Alumno a : alumnosTreeSet) {
            if (count++ >= 5) break;
            reporte += "   - " + a.getNombre() + " (" + a.getHorarioIda() + ")\n";
        }
        reporte += "\n";
        
        reporte += "2. HASHSET (sin orden especifico):\n";
        reporte += "   Tamaño: " + alumnosHashSet.size() + " alumnos\n";
        reporte += "   Primeros 5:\n";
        count = 0;
        for (Alumno a : alumnosHashSet) {
            if (count++ >= 5) break;
            reporte += "   - " + a.getNombre() + " (" + a.getHorarioIda() + ")\n";
        }
        reporte += "\n";
        
        reporte += "3. LINKEDHASHSET (orden de insercion):\n";
        reporte += "   Tamaño: " + alumnosLinkedHashSet.size() + " alumnos\n";
        reporte += "   Primeros 5:\n";
        count = 0;
        for (Alumno a : alumnosLinkedHashSet) {
            if (count++ >= 5) break;
            reporte += "   - " + a.getNombre() + " (" + a.getHorarioIda() + ")\n";
        }
        
        mostrarReporte(reporte);
    }
    
    private void verificarDuplicados() {
        String reporte = "VERIFICACION DE DUPLICADOS\n";
        reporte += "--------------------------\n\n";
        
        reporte += "Tamaño de los conjuntos:\n";
        reporte += "  TreeSet: " + alumnosTreeSet.size() + " alumnos\n";
        reporte += "  HashSet: " + alumnosHashSet.size() + " alumnos\n";
        reporte += "  LinkedHashSet: " + alumnosLinkedHashSet.size() + " alumnos\n\n";
        
        if (alumnosTreeSet.size() == alumnosHashSet.size() && 
            alumnosHashSet.size() == alumnosLinkedHashSet.size()) {
            reporte += "Los tres conjuntos tienen el mismo tamaño.\n";
            reporte += "No hay alumnos duplicados.\n";
        } else {
            reporte += "Los conjuntos tienen diferente tamaño.\n";
            reporte += "Podria haber duplicados.\n";
        }
        
        reporte += "\nLos Sets en Java no permiten elementos duplicados.";
        
        mostrarReporte(reporte);
    }
    
    private String generarBarra(int valor) {
        int max = 10;
        int numAsteriscos = Math.min(valor, max);
        StringBuilder barra = new StringBuilder("[");
        for (int i = 0; i < numAsteriscos; i++) {
            barra.append("-");
        }
        for (int i = numAsteriscos; i < max; i++) {
            barra.append(" ");
        }
        barra.append("]");
        return barra.toString();
    }
    
    private void mostrarReporte(String contenido) {
        JTextArea textArea = new JTextArea(20, 60);
        textArea.setText(contenido);
        textArea.setEditable(false);
        textArea.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);
        
        JOptionPane.showMessageDialog(null, scrollPane, "Reporte LOBUS", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        trabajo_clase programa = new trabajo_clase();
        programa.iniciar();
    }
}