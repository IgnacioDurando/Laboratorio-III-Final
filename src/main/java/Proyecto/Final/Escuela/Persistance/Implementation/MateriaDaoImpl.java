package Proyecto.Final.Escuela.Persistance.Implementation;

import Proyecto.Final.Escuela.Model.Materia;
import Proyecto.Final.Escuela.Persistance.MateriaDao;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class MateriaDaoImpl implements MateriaDao {
    private final List<Materia> materias = new ArrayList<>();

    @Override
    public List<Materia> findAll() {
        return new ArrayList<>(materias);
    }

    @Override
    public Optional<Materia> findById(int id) {
        return materias.stream().filter(m -> m.getId() == id).findFirst();
    }


    private int generateId() {
        return materias.isEmpty() ? 1 : materias.get(materias.size() - 1).getId() + 1;
    }
    @Override
    public Materia save(Materia materia) {
        if (materia.getId() == 0) {
            materia.setId(generateId());
        }
        materias.add(materia);
        return materia;
    }

    @Override
    public Materia updateMateria(int id, Materia materia) {
        for (int i = 0; i < materias.size(); i++) {
            if (materias.get(i).getId() == id) {
                materias.set(i, materia);
                return materia;
            }
        }
        return null;
    }

    @Override
    public boolean delete(int id) {
        return materias.removeIf(m -> m.getId() == id);
    }
}
