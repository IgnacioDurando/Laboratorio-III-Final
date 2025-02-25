package Proyecto.Final.Escuela.Service;

import Proyecto.Final.Escuela.Dtos.AlumnoDTO;
import Proyecto.Final.Escuela.Exception.AlumnoExistException;

import java.util.List;
import java.util.Optional;

public interface AlumnoService {
    List<AlumnoDTO> getAllAlumnos();
    Optional<AlumnoDTO> getAlumnoById(int id);
    AlumnoDTO createAlumno(AlumnoDTO alumnoDTO) throws AlumnoExistException;
    AlumnoDTO updateAlumno(int id, AlumnoDTO alumnoDTO) throws AlumnoExistException;
    void removeMateriaFromAlumno(int idAlumno, int idMateria);
    boolean deleteAlumno(int id);
}
