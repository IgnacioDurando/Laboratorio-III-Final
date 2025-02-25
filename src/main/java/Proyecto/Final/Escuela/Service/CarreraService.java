package Proyecto.Final.Escuela.Service;

import Proyecto.Final.Escuela.Dtos.CarreraDTO;
import Proyecto.Final.Escuela.Exception.CarreraExistException;
import Proyecto.Final.Escuela.Exception.MateriaExistException;

import java.util.List;
import java.util.Optional;

public interface CarreraService {
    List<CarreraDTO> getAllCarreras();
    Optional<CarreraDTO> getCarreraById(int id);
    CarreraDTO createCarrera(CarreraDTO carreraDTO) throws CarreraExistException;
    CarreraDTO updateCarrera(int id, CarreraDTO carreraDTO) throws CarreraExistException, MateriaExistException;
    void removeMateriaFromCarrera(int idCarrera, int idMateria);
    boolean deleteCarrera(int id);

}