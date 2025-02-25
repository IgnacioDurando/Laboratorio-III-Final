package Proyecto.Final.Escuela.Service;

import Proyecto.Final.Escuela.Dtos.MateriaDTO;
import Proyecto.Final.Escuela.Exception.MateriaExistException;

import java.util.List;
import java.util.Optional;

public interface MateriaService {
    List<MateriaDTO> getAllMaterias();
    Optional<MateriaDTO> getMateriaById(int id);
    MateriaDTO createMateria(MateriaDTO materiaDTO) throws MateriaExistException;
    MateriaDTO updateMateria(int id, MateriaDTO materiaDTO) throws MateriaExistException;
    void removeProfesorFromMateria(int idMateria, int idProfesor);
    boolean deleteMateria(int id);
}
