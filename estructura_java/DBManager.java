import java.util.ArrayList;
import java.util.List;

import com.db4o.*;

public class DBManager {
    private ObjectContainer db;

    public DBManager(String filename) {
        db = Db4o.openFile(filename);
    }

    public void close() {
        db.close();
    }

    // Agregar estudiante
    public void agregarEstudiante(Estudiante e) {
        db.store(e);
        db.commit();
    }

    // Modificar estudiante
    public void modificarEstudiante(Estudiante e) {
        ObjectSet<Estudiante> result = db.queryByExample(new Estudiante(e.getMatricula(), null, null, 0));
        if (result.hasNext()) {
            Estudiante existente = result.next();
            existente.setNombre(e.getNombre());
            existente.setCarrera(e.getCarrera());
            existente.setSemestre(e.getSemestre());
            db.store(existente);
            db.commit();
        }
    }

    // Eliminar estudiante
    public void eliminarEstudiante(String matricula) {
        ObjectSet<Estudiante> result = db.queryByExample(new Estudiante(matricula, null, null, 0));
        if (result.hasNext()) {
            Estudiante e = result.next();
            db.delete(e);
            db.commit();
        }
    }

    // Buscar por matrícula
    public Estudiante buscarPorMatricula(String matricula) {
        ObjectSet<Estudiante> result = db.queryByExample(new Estudiante(matricula, null, null, 0));
        if (result.hasNext()) {
            return result.next();
        }
        return null;
    }

    // Consultar todos
    public List<Estudiante> consultarTodos() {
        List<Estudiante> lista = new ArrayList<>();
        ObjectSet<Estudiante> result = db.queryByExample(Estudiante.class);
        while (result.hasNext()) {
            lista.add(result.next());
        }
        return lista;
    }
}


