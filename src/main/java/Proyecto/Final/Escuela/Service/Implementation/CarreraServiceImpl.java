package Proyecto.Final.Escuela.Service.Implementation;

import Proyecto.Final.Escuela.Dtos.CarreraDTO;
import Proyecto.Final.Escuela.Dtos.CarreraMapper;
import Proyecto.Final.Escuela.Exception.*;
import Proyecto.Final.Escuela.Model.Carrera;
import Proyecto.Final.Escuela.Model.Alumno;
import Proyecto.Final.Escuela.Persistance.AlumnoDao;
import Proyecto.Final.Escuela.Persistance.CarreraDao;
import Proyecto.Final.Escuela.Persistance.MateriaDao;
import Proyecto.Final.Escuela.Service.CarreraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class CarreraServiceImpl implements CarreraService {
    private final CarreraDao carreraDao;
    private final MateriaDao materiaDao;
    private final AlumnoDao alumnoDao;

    @Autowired
    public CarreraServiceImpl(CarreraDao carreraDao, MateriaDao materiaDao, AlumnoDao alumnoDao) {
        this.carreraDao = carreraDao;
        this.materiaDao = materiaDao;
        this.alumnoDao = alumnoDao;
    }

    @Override
    public List<CarreraDTO> getAllCarreras() {

        return carreraDao.findAll().stream().map(CarreraMapper::toDTO).toList();
    }

    @Override
    public Optional<CarreraDTO> getCarreraById(int id) {

        return carreraDao.findById(id).map(CarreraMapper::toDTO);
    }

    @Override
    public CarreraDTO createCarrera(CarreraDTO carreraDTO) throws CarreraExistException {
        if(carreraDTO == null || carreraDTO.getNombre() == null || carreraDTO.getNombre().trim().isEmpty()){
            throw new InvalidDataException("El nombre de la carrera no puede quedar vacio.");
        }

        boolean carreraExistente = carreraDao.findAll()
                .stream()
                .anyMatch(c -> c.getNombre().trim().equalsIgnoreCase(carreraDTO.getNombre()));

        if(carreraExistente){
            throw new CarreraExistException("La carrera " + carreraDTO.getNombre() + " ya existe");
        }

        if(carreraDTO.getMaterias() != null && !carreraDTO.getMaterias().isEmpty()){
            List<Integer> materiasNoExistentes = new ArrayList<>();
            for (Integer idMateria : carreraDTO.getMaterias()){
                if(materiaDao.findById(idMateria).isEmpty()){
                    materiasNoExistentes.add(idMateria);
                }
            }
            if(!materiasNoExistentes.isEmpty()){
                throw new MateriaNotFoundException("Las materias con los siguientes IDs no existen: " + materiasNoExistentes);
            }
        }

        Carrera carrera = CarreraMapper.toEntity(carreraDTO);
        carrera = carreraDao.save(carrera);
        return CarreraMapper.toDTO(carrera);
    }

    @Override
    public CarreraDTO updateCarrera(int id, CarreraDTO carreraDTO) throws CarreraExistException, MateriaExistException {
        Carrera carreraExistente = carreraDao.findById(id)
                .orElseThrow(() -> new CarreraNotFoundException("La carrera con el ID " + id + " no fue encontrada."));

        if (carreraDTO.getNombre() != null && !carreraDTO.getNombre().equalsIgnoreCase(carreraExistente.getNombre())){
            boolean existe = carreraDao.findAll().stream()
                    .anyMatch(c -> c.getId() != id && c.getNombre().equalsIgnoreCase(carreraDTO.getNombre()));

            if (existe){
                throw new CarreraExistException("Ya existe una carrera con el mismo nombre.");
            }

            carreraExistente.setNombre(carreraDTO.getNombre());
        }

        if (carreraDTO.getMaterias() != null && !carreraDTO.getMaterias().isEmpty()) {
            List<Integer> materiasExistentes = carreraExistente.getMaterias();
            if (materiasExistentes == null) {
                materiasExistentes = new ArrayList<>();
            }

            List<Integer> materiasNoExistentes = new ArrayList<>();
            List<Integer> materiasExistentesEnCarrera = new ArrayList<>();
            for (Integer idMateria : carreraDTO.getMaterias()) {
                if (materiaDao.findById(idMateria).isEmpty()) {
                    materiasNoExistentes.add(idMateria);
                } else {
                    if (!materiasExistentes.contains(idMateria)) {
                        materiasExistentes.add(idMateria);
                    }else{
                        materiasExistentesEnCarrera.add(idMateria);
                    }
                }
            }

            if (!materiasNoExistentes.isEmpty()) {
                throw new MateriaNotFoundException("Las materias con los siguientes IDs no existen: " + materiasNoExistentes);
            }

            if (!materiasExistentesEnCarrera.isEmpty()) {
                throw new MateriaExistException("Las siguientes materias ya están en la carrera: " + materiasExistentesEnCarrera);
            }

            carreraExistente.setMaterias(materiasExistentes);
        }

        Carrera carreraActualizada = carreraDao.updateCarrera(id, carreraExistente);
        return CarreraMapper.toDTO(carreraActualizada);
    }

    @Override
    public void removeMateriaFromCarrera(int idCarrera, int idMateria) {
        Carrera carrera = carreraDao.findById(idCarrera)
                .orElseThrow(() -> new CarreraNotFoundException("La carrera con el ID " + idCarrera + " no fue encontrada."));

        if (carrera.getMaterias() == null || !carrera.getMaterias().contains(idMateria)) {
            throw new MateriaNotFoundException("La materia con el ID " + idMateria + " no se encuentra asignada a esta carrera.");
        }

        List<Alumno> alumnosEnCarrera = alumnoDao.findAll().stream()
                .filter(alumno -> idCarrera == alumno.getCarreraId() && alumno.getMaterias() != null && alumno.getMaterias().containsKey(idMateria))
                .toList();

        for (Alumno alumno : alumnosEnCarrera) {
            alumno.getMaterias().remove(idMateria);
            alumnoDao.updateAlumno(alumno.getId(), alumno);
        }

        carrera.getMaterias().removeIf(m -> m == idMateria);
        carreraDao.updateCarrera(idCarrera, carrera);
    }

    @Override
    public boolean deleteCarrera(int id) {
        Carrera carrera = carreraDao.findById(id)
                .orElseThrow(() -> new CarreraNotFoundException("La carrera con el ID " + id + " no fue encontrada."));

        List<Alumno> alumnosEnCarrera = alumnoDao.findAll().stream()
                .filter(alumno -> id == alumno.getCarreraId())
                .toList();

        for (Alumno alumno : alumnosEnCarrera) {
            alumno.setCarreraId(null);
            alumno.setMaterias(new HashMap<>());
            alumnoDao.updateAlumno(alumno.getId(), alumno);
        }
        return carreraDao.delete(id);
    }

}
