package Proyecto.Final.Escuela.Persistance;

import Proyecto.Final.Escuela.Model.Carrera;
import java.util.List;
import java.util.Optional;

public interface CarreraDao {
    List<Carrera> findAll();
    Optional<Carrera> findById(int id);
    Carrera save(Carrera carrera);
    Carrera updateCarrera(int id, Carrera carrera);
    boolean delete(int id);
}