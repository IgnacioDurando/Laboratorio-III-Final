package Proyecto.Final.Escuela.Service;

import Proyecto.Final.Escuela.Dtos.ProfesorDTO;
import Proyecto.Final.Escuela.Exception.ProfesorExistException;
import Proyecto.Final.Escuela.Model.Profesor;
import java.util.List;
import java.util.Optional;

public interface ProfesorService {
    List<ProfesorDTO> getAllProfesores();
    Optional<ProfesorDTO> getProfesorById(int id);
    ProfesorDTO createProfesor(ProfesorDTO profesorDTO) throws ProfesorExistException;

    ProfesorDTO updateProfesor(int id, ProfesorDTO profesorDTO) throws ProfesorExistException;

    boolean deleteProfesor(int id);
}
