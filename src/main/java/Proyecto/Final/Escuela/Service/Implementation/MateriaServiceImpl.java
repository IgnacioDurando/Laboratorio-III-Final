package Proyecto.Final.Escuela.Service.Implementation;

import Proyecto.Final.Escuela.Dtos.MateriaDTO;
import Proyecto.Final.Escuela.Dtos.MateriaMapper;
import Proyecto.Final.Escuela.Exception.InvalidDataException;
import Proyecto.Final.Escuela.Exception.MateriaNotFoundException;
import Proyecto.Final.Escuela.Exception.ProfesorNotFoundException;
import Proyecto.Final.Escuela.Model.Alumno;
import Proyecto.Final.Escuela.Model.Carrera;
import Proyecto.Final.Escuela.Model.Materia;
import Proyecto.Final.Escuela.Persistance.AlumnoDao;
import Proyecto.Final.Escuela.Persistance.CarreraDao;
import Proyecto.Final.Escuela.Persistance.MateriaDao;
import Proyecto.Final.Escuela.Persistance.ProfesorDao;
import Proyecto.Final.Escuela.Service.MateriaService;
import Proyecto.Final.Escuela.Model.Profesor;
import Proyecto.Final.Escuela.Exception.MateriaExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MateriaServiceImpl implements MateriaService {
    private final MateriaDao materiaDao;
    private final ProfesorDao profesorDao;
    private final CarreraDao carreraDao;
    private final AlumnoDao alumnoDao;

    @Autowired
    public MateriaServiceImpl(MateriaDao materiaDao, ProfesorDao profesorDao, CarreraDao carreraDao, AlumnoDao alumnoDao) {

        this.materiaDao = materiaDao;
        this.profesorDao = profesorDao;
        this.carreraDao = carreraDao;
        this.alumnoDao = alumnoDao;
    }

    @Override
    public List<MateriaDTO> getAllMaterias() {

        return materiaDao.findAll().stream().map(MateriaMapper::toDTO).toList();
    }

    @Override
    public Optional<MateriaDTO> getMateriaById(int id) {

        return materiaDao.findById(id).map(MateriaMapper::toDTO);
    }

    @Override
    public MateriaDTO createMateria(MateriaDTO materiaDTO) throws MateriaExistException{

        if (materiaDTO == null || materiaDTO.getNombre() == null || materiaDTO.getNombre().trim().isEmpty()) {
            throw new InvalidDataException("El nombre de la materia no puede quedar vacío.");
        }

        if (materiaDTO.getProfesorId() == null) {
            throw new InvalidDataException("Hay que asignarle un profesor a la materia.");
        }

        boolean materiaExistente = materiaDao.findAll()
                .stream()
                .anyMatch(m -> m.getNombre().trim().equalsIgnoreCase(materiaDTO.getNombre()));

        if (materiaExistente) {
            throw new MateriaExistException("La materia " + materiaDTO.getNombre() + " ya existe.");
        }


        Profesor profesor = profesorDao.findById(materiaDTO.getProfesorId())
                .orElseThrow(() -> new ProfesorNotFoundException("El profesor con el ID " + materiaDTO.getProfesorId() + " no fue encontrado."));


        if (materiaDTO.getCorrelatividades() != null && !materiaDTO.getCorrelatividades().isEmpty()) {
            List<Integer> materiasNoExistentes = new ArrayList<>();

            for (Integer idCorrelativa : materiaDTO.getCorrelatividades()) {
                if (materiaDao.findById(idCorrelativa).isEmpty()) {
                    materiasNoExistentes.add(idCorrelativa);
                }
            }
            if (!materiasNoExistentes.isEmpty()) {
                throw new MateriaNotFoundException("Las materias con los siguientes id no existen: " + materiasNoExistentes);
            }
        }

        Materia nuevaMateria = materiaDao.save(MateriaMapper.toEntity(materiaDTO));

        profesor.agregarMateria(nuevaMateria.getId());
        profesorDao.updateProfesor(profesor.getId(), profesor);

        return MateriaMapper.toDTO(nuevaMateria);
    }

    @Override
    public MateriaDTO updateMateria(int id, MateriaDTO materiaDTO) throws MateriaExistException{
        Materia materiaExistente = materiaDao.findById(id)
                .orElseThrow(() -> new MateriaNotFoundException("La materia con el ID " + id + " no fue encontrada."));

        if (materiaDTO.getNombre() != null && !materiaDTO.getNombre().equalsIgnoreCase(materiaExistente.getNombre())) {
            boolean existe = materiaDao.findAll().stream()
                    .anyMatch(m -> m.getId() != id && m.getNombre().equalsIgnoreCase(materiaDTO.getNombre()));

            if (existe) {
                throw new MateriaExistException("Ya existe una materia con el mismo nombre.");
            }
        }

        if (materiaDTO.getCorrelatividades() != null) {
            List<Integer> materiasNoExistentes = new ArrayList<>();

            for (Integer idCorrelativa : materiaDTO.getCorrelatividades()) {
                if (idCorrelativa.equals(id)) {
                    throw new InvalidDataException("Una materia no puede ser correlativa de si misma.");
                }
                if (materiaDao.findById(idCorrelativa).isEmpty()) {
                    materiasNoExistentes.add(idCorrelativa);
                }
            }

            if (!materiasNoExistentes.isEmpty()) {
                throw new MateriaNotFoundException("Las materias con los siguientes id no existen: " + materiasNoExistentes);
            }

        }

        if (materiaDTO.getNombre() != null) {
            materiaExistente.setNombre(materiaDTO.getNombre());
        }
        if (materiaDTO.getCorrelatividades() != null) {
            materiaExistente.setCorrelatividades(materiaDTO.getCorrelatividades());
        }

        if(materiaDTO.getAnio() != null){
            materiaExistente.setAnio(materiaDTO.getAnio());
        }

        if(materiaDTO.getCuatrimestre() != null){
            materiaExistente.setCuatrimestre(materiaDTO.getCuatrimestre());
        }

        if (materiaDTO.getProfesorId() != null && materiaDTO.getProfesorId() != materiaExistente.getProfesorId()) {
            Profesor nuevoProfesor = profesorDao.findById(materiaDTO.getProfesorId())
                    .orElseThrow(() -> new ProfesorNotFoundException("El profesor con el ID " + materiaDTO.getProfesorId() + " no fue encontrado."));

            if (materiaExistente.getProfesorId() != null) {
                Profesor profesorAnterior = profesorDao.findById(materiaExistente.getProfesorId()).orElse(null);
                if (profesorAnterior != null) {
                    profesorAnterior.getMateriasDictadas().removeIf(m -> m == materiaExistente.getId());
                    profesorDao.updateProfesor(profesorAnterior.getId(), profesorAnterior);
                }
            }

            nuevoProfesor.agregarMateria(materiaExistente.getId());
            profesorDao.updateProfesor(nuevoProfesor.getId(), nuevoProfesor);

            materiaExistente.setProfesorId(materiaDTO.getProfesorId());
        }


        Materia materiaActualizada = materiaDao.updateMateria(id, materiaExistente);
        return MateriaMapper.toDTO(materiaActualizada);
    }


    @Override
    public void removeProfesorFromMateria(int idMateria, int idProfesor) {
        Materia materia = materiaDao.findById(idMateria)
                .orElseThrow(() -> new MateriaNotFoundException("La materia con el ID " + idMateria + " no fue encontrada."));

        Profesor profesor = profesorDao.findById(idProfesor)
                .orElseThrow(() -> new ProfesorNotFoundException("El profesor con el ID " + idProfesor + " no fue encontrado."));

        if (materia.getProfesorId() == null || !materia.getProfesorId().equals(idProfesor)) {
            throw new ProfesorNotFoundException("El profesor con el ID " + idProfesor + " no está asignado a esta materia.");
        }

        materia.setProfesorId(null);
        materiaDao.updateMateria(idMateria, materia);

        profesor.getMateriasDictadas().removeIf(m -> m == idMateria);
        profesorDao.updateProfesor(idProfesor, profesor);
    }


    @Override
    public boolean deleteMateria(int id) {
        Materia materia = materiaDao.findById(id)
                .orElseThrow(() -> new MateriaNotFoundException("No se encontro ninguna materia con el ID " + id + "."));


        if (materia.getProfesorId() != null) {
            profesorDao.findById(materia.getProfesorId()).ifPresent(profesor -> {
                profesor.getMateriasDictadas().removeIf(m -> m == id);
                profesorDao.updateProfesor(profesor.getId(), profesor);
            });
        }

        List<Carrera> carreras = carreraDao.findAll();
        for (Carrera carrera : carreras) {
            if (carrera.getMaterias().contains(id)) {
                carrera.getMaterias().removeIf(m -> m == id);
                carreraDao.updateCarrera(carrera.getId(), carrera);
            }
        }

        List<Alumno> alumnos = alumnoDao.findAll();
        for (Alumno alumno : alumnos) {
            if (alumno.getMaterias().containsKey(id)) {
                alumno.getMaterias().remove(id);
                alumnoDao.updateAlumno(alumno.getId(), alumno);
            }
        }

        List<Materia> todasMaterias = materiaDao.findAll();
        for (Materia m : todasMaterias) {
            if (m.getCorrelatividades() != null && m.getCorrelatividades().contains(id)) {
                m.getCorrelatividades().removeIf(correlativa -> correlativa == id);
                materiaDao.updateMateria(m.getId(), m);
            }
        }

        return materiaDao.delete(id);
    }

}
