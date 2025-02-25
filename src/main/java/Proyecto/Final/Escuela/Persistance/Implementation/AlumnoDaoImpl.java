package Proyecto.Final.Escuela.Persistance.Implementation;

import Proyecto.Final.Escuela.Model.Alumno;
import Proyecto.Final.Escuela.Persistance.AlumnoDao;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class AlumnoDaoImpl implements AlumnoDao {
    private final List<Alumno> alumnos = new ArrayList<>();

    @Override
    public List<Alumno> findAll() {
        return new ArrayList<>(alumnos);
    }

    @Override
    public Optional<Alumno> findById(int id) {
        return alumnos.stream().filter(a -> a.getId() == id).findFirst();
    }


    private int generateId() {
        return alumnos.isEmpty() ? 1 : alumnos.get(alumnos.size() - 1).getId() + 1;
    }
    @Override
    public Alumno save(Alumno alumno) {
        if (alumno.getId() == 0) {
            alumno.setId(generateId());
        }
        alumnos.add(alumno);
        return alumno;
    }
    @Override
    public Alumno updateAlumno(int id, Alumno alumno){
        for(int i = 0; i < alumnos.size(); i++){
            if (alumnos.get(i).getId() == id){
                alumnos.set(i, alumno);
                return alumno;
            }
        }
        return null;
    }

    @Override
    public boolean delete(int id) {
        return alumnos.removeIf(a -> a.getId() == id);
    }
}