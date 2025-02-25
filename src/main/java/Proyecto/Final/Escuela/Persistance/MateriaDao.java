package Proyecto.Final.Escuela.Persistance;

import Proyecto.Final.Escuela.Model.Materia;
import java.util.List;
import java.util.Optional;

public interface MateriaDao {
    List<Materia> findAll();
    Optional<Materia> findById(int id);
    Materia save(Materia materia);
    Materia updateMateria(int id, Materia materia);
    boolean delete(int id);
}