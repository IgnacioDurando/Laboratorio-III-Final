package Proyecto.Final.Escuela.Service.Implementation;

import Proyecto.Final.Escuela.Dtos.AlumnoDTO;
import Proyecto.Final.Escuela.Dtos.AlumnoMapper;
import Proyecto.Final.Escuela.Exception.*;
import Proyecto.Final.Escuela.Model.Alumno;
import Proyecto.Final.Escuela.Model.Carrera;
import Proyecto.Final.Escuela.Model.EstadoMateria;
import Proyecto.Final.Escuela.Persistance.AlumnoDao;
import Proyecto.Final.Escuela.Persistance.CarreraDao;
import Proyecto.Final.Escuela.Service.AlumnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AlumnoServiceImpl implements AlumnoService {
    private final AlumnoDao alumnoDao;
    private final CarreraDao carreraDao;

    @Autowired
    public AlumnoServiceImpl(AlumnoDao alumnoDao, CarreraDao carreraDao) {

        this.alumnoDao = alumnoDao;
        this.carreraDao = carreraDao;
    }

    @Override
    public List<AlumnoDTO> getAllAlumnos() {
        return alumnoDao.findAll().stream().map(AlumnoMapper::toDTO).toList();
    }

    @Override
    public Optional<AlumnoDTO> getAlumnoById(int id) {
        return alumnoDao.findById(id).map(AlumnoMapper::toDTO);
    }

    @Override
    public AlumnoDTO createAlumno(AlumnoDTO alumnoDTO) throws AlumnoExistException{
        if (alumnoDTO.getNombre() == null || alumnoDTO.getNombre().trim().isEmpty() || alumnoDTO.getApellido() == null || alumnoDTO.getApellido().trim().isEmpty() || alumnoDTO.getDni() == null || alumnoDTO.getDni().trim().isEmpty()) {
            throw new InvalidDataException("El nombre, apellido y el DNI del alumno no pueden quedar vacíos.");
        }

        boolean dniExistente = alumnoDao.findAll()
                .stream()
                .anyMatch(a -> a.getDni().equals(alumnoDTO.getDni()));

        if (dniExistente) {
            throw new AlumnoExistException("El DNI " + alumnoDTO.getDni() + " ya pertenece a uno de nuestros alumnos.");
        }


        if (alumnoDTO.getCarreraId() != null) {
            boolean carreraExiste = carreraDao.findById(alumnoDTO.getCarreraId()).isPresent();
            if (!carreraExiste) {
                throw new CarreraNotFoundException("La carrera con el ID " + alumnoDTO.getCarreraId() + " no existe.");
            }
        }

        if (alumnoDTO.getMaterias() != null && !alumnoDTO.getMaterias().isEmpty()) {
            throw new InvalidDataException("No se pueden asignar materias al momento de la creacion del alumno. Primero debe ser creado.");
        }

        Alumno alumno = AlumnoMapper.toEntity(alumnoDTO);
        alumno = alumnoDao.save(alumno);
        return AlumnoMapper.toDTO(alumno);
    }


    @Override
    public AlumnoDTO updateAlumno(int id, AlumnoDTO alumnoDTO) throws AlumnoExistException{

        Alumno alumnoExistente = alumnoDao.findById(id)
                .orElseThrow(() -> new AlumnoNotFoundException("El alumno con el ID " + id + " no fue encontrado."));



        if(alumnoDTO.getNombre() != null){
            alumnoExistente.setNombre(alumnoDTO.getNombre());
        }
        if(alumnoDTO.getApellido() != null){
            alumnoExistente.setApellido(alumnoDTO.getApellido());
        }
        if(alumnoDTO.getEdad() != null){
            alumnoExistente.setEdad(alumnoDTO.getEdad());
        }

        if (alumnoDTO.getDni() != null && !alumnoDTO.getDni().equals(alumnoExistente.getDni())) {
            boolean dniExistente = alumnoDao.findAll().stream()
                    .anyMatch(a -> a.getId() != id && a.getDni().equals(alumnoDTO.getDni()));

            if (dniExistente) {
                throw new AlumnoExistException("Ya existe un alumno con el DNI: " + alumnoDTO.getDni() + ".");
            }

            alumnoExistente.setDni(alumnoDTO.getDni());
        }

        if (alumnoDTO.getCarreraId() != null && !alumnoDTO.getCarreraId().equals(alumnoExistente.getCarreraId())) {
            carreraDao.findById(alumnoDTO.getCarreraId())
                    .orElseThrow(() -> new CarreraNotFoundException("La carrera con el ID " + alumnoDTO.getCarreraId() + " no existe."));


            alumnoExistente.setMaterias(new HashMap<>());
            alumnoExistente.setCarreraId(alumnoDTO.getCarreraId());
        }

        if (alumnoDTO.getMaterias() != null && !alumnoDTO.getMaterias().isEmpty()) {
            if (alumnoExistente.getCarreraId() == null) {
                throw new CarreraNotFoundException("El alumno no tiene una carrera asignada.");
            }

            Carrera carrera = carreraDao.findById(alumnoExistente.getCarreraId())
                    .orElseThrow(() -> new CarreraNotFoundException("La carrera con el ID " + alumnoExistente.getCarreraId() + " no existe."));


            List<Integer> materiasDeCarrera = carrera.getMaterias();
            Map<Integer, EstadoMateria> materiasDelAlumno = alumnoExistente.getMaterias();
            if (materiasDelAlumno == null) {
                materiasDelAlumno = new HashMap<>();
            }

            List<Integer> materiasNoEncontradasEnCarrera = new ArrayList<>();

            for (Map.Entry<Integer, String> entry : alumnoDTO.getMaterias().entrySet()) {
                Integer idMateria = entry.getKey();
                String estadoMateriaStr = entry.getValue();
                if (estadoMateriaStr == null || estadoMateriaStr.trim().isEmpty()) {
                    throw new InvalidDataException("El estado de la materia con ID " + idMateria + " debe estar completado.");
                }

                EstadoMateria estadoMateria = Arrays.stream(EstadoMateria.values())
                        .filter(e -> e.name().equalsIgnoreCase(estadoMateriaStr))
                        .findFirst()
                        .orElseThrow(() -> new InvalidDataException(
                                "Estado no aceptable para la materia con ID " + idMateria + ". Tiene que ser APROBADA, DESAPROBADA o CURSANDO."
                        ));

                if (!materiasDeCarrera.contains(idMateria)) {
                    materiasNoEncontradasEnCarrera.add(idMateria);
                }else {
                    materiasDelAlumno.put(idMateria, estadoMateria);
                }
            }


            if (!materiasNoEncontradasEnCarrera.isEmpty()) {
                throw new MateriaNotFoundException("Las siguientes materias no pertenecen a la carrera asignada: " + materiasNoEncontradasEnCarrera);
            }

            alumnoExistente.setMaterias(materiasDelAlumno);
        }

        Alumno alumnoActualizado = alumnoDao.updateAlumno(id, alumnoExistente);
        return AlumnoMapper.toDTO(alumnoActualizado);
    }

    @Override
    public void removeMateriaFromAlumno(int idAlumno, int idMateria) {
        Alumno alumno = alumnoDao.findById(idAlumno)
                .orElseThrow(() -> new AlumnoNotFoundException("El alumno con ID " + idAlumno + " no fue encontrado."));

        if (alumno.getMaterias() == null || !alumno.getMaterias().containsKey(idMateria)) {
            throw new MateriaNotFoundException("La materia con ID " + idMateria + " no está asignada a este alumno.");
        }

        alumno.getMaterias().remove(idMateria);
        alumnoDao.updateAlumno(idAlumno, alumno);
    }


    @Override
    public boolean deleteAlumno(int id) {
        if (!alumnoDao.delete(id)) {
            throw new AlumnoNotFoundException("No se encontro ningun alumno con el ID: " + id + ".");
        }
        return true;
    }

}