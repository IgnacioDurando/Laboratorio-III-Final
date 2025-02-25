package Proyecto.Final.Escuela.Persistance;

import Proyecto.Final.Escuela.Model.Alumno;
import java.util.List;
import java.util.Optional;

public interface AlumnoDao {
    List<Alumno> findAll();
    Optional<Alumno> findById(int id);
    Alumno save(Alumno alumno);
    Alumno updateAlumno(int id, Alumno alumno);
    boolean delete(int id);
}