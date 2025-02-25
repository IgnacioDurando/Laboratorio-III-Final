package Proyecto.Final.Escuela.Persistance;

import Proyecto.Final.Escuela.Model.Profesor;
import java.util.List;
import java.util.Optional;

public interface ProfesorDao {
    List<Profesor> findAll();
    Optional<Profesor> findById(int id);
    Profesor save(Profesor profesor);
    Profesor updateProfesor(int id, Profesor profesor);
    boolean delete(int id);
}
