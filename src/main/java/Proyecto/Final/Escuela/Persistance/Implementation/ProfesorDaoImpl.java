package Proyecto.Final.Escuela.Persistance.Implementation;

import Proyecto.Final.Escuela.Model.Profesor;
import Proyecto.Final.Escuela.Persistance.ProfesorDao;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ProfesorDaoImpl implements ProfesorDao {
    private final List<Profesor> profesores = new ArrayList<>();

    @Override
    public List<Profesor> findAll() {

        return new ArrayList<>(profesores);
    }

    @Override
    public Optional<Profesor> findById(int id) {

        return profesores.stream().filter(p -> p.getId() == id).findFirst();
    }

    private int generateId() {
        return profesores.isEmpty() ? 1 : profesores.get(profesores.size() - 1).getId() + 1;
    }

    @Override
    public Profesor save(Profesor profesor) {
        if (profesor.getId() == 0) {
            profesor.setId(generateId());
        }
        profesores.add(profesor);
        return profesor;
    }

    @Override
    public Profesor updateProfesor(int id, Profesor profesor) {
        for (int i = 0; i < profesores.size(); i++) {
            if (profesores.get(i).getId() == id) {
                profesores.set(i, profesor);
                return profesor;
            }
        }
        return null;
    }

    @Override
    public boolean delete(int id) {

        return profesores.removeIf(p -> p.getId() == id);
    }
}
