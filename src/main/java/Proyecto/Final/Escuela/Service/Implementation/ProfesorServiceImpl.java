package Proyecto.Final.Escuela.Service.Implementation;

import Proyecto.Final.Escuela.Dtos.ProfesorDTO;
import Proyecto.Final.Escuela.Dtos.ProfesorMapper;
import Proyecto.Final.Escuela.Exception.InvalidDataException;
import Proyecto.Final.Escuela.Exception.ProfesorNotFoundException;
import Proyecto.Final.Escuela.Model.Profesor;
import Proyecto.Final.Escuela.Persistance.ProfesorDao;
import Proyecto.Final.Escuela.Exception.ProfesorExistException;
import Proyecto.Final.Escuela.Service.ProfesorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfesorServiceImpl implements ProfesorService {
    private final ProfesorDao profesorDao;

    @Autowired
    public ProfesorServiceImpl(ProfesorDao profesorDao) {

        this.profesorDao = profesorDao;
    }

    @Override
    public List<ProfesorDTO> getAllProfesores() {

        return profesorDao.findAll().stream().map(ProfesorMapper::toDTO).toList();
    }

    @Override
    public Optional<ProfesorDTO> getProfesorById(int id) {

        return profesorDao.findById(id).map(ProfesorMapper::toDTO);
    }

    @Override
    public ProfesorDTO createProfesor(ProfesorDTO profesorDTO) throws ProfesorExistException{

        if (profesorDTO == null || profesorDTO.getNombre() == null || profesorDTO.getNombre().trim().isEmpty() || profesorDTO.getApellido() == null || profesorDTO.getApellido().trim().isEmpty()) {
            throw new InvalidDataException("El nombre y apellido del profesor no pueden quedar vacios.");
        }

        boolean ProfesorExistente = profesorDao.findAll()
                .stream()
                .anyMatch(p -> p.getNombre().equalsIgnoreCase(profesorDTO.getNombre()) && p.getApellido().equalsIgnoreCase(profesorDTO.getApellido()));
        if (ProfesorExistente) {
            throw new ProfesorExistException("El profesor " + profesorDTO.getNombre() + " " + profesorDTO.getApellido() + " ya esta registrado en el sistema.");
        }

        Profesor profesor = ProfesorMapper.toEntity(profesorDTO);
        profesor = profesorDao.save(profesor);
        return ProfesorMapper.toDTO(profesor);
    }

    @Override
    public ProfesorDTO updateProfesor(int id, ProfesorDTO profesorDTO) throws ProfesorExistException{
        Profesor profesorExistente = profesorDao.findById(id)
                .orElseThrow(() -> new ProfesorNotFoundException("El profesor con el ID " + id + " no fue encontrado."));

        String nuevoNombre = profesorDTO.getNombre() != null ? profesorDTO.getNombre() : profesorExistente.getNombre();
        String nuevoApellido = profesorDTO.getApellido() != null ? profesorDTO.getApellido() : profesorExistente.getApellido();

        boolean existe = profesorDao.findAll().stream()
                .anyMatch(p -> p.getId() != id &&
                        p.getNombre().equalsIgnoreCase(nuevoNombre) &&
                        p.getApellido().equalsIgnoreCase(nuevoApellido));

        if (existe) {
            throw new ProfesorExistException("Ya existe un profesor con el mismo nombre y apellido.");
        }

        if(profesorDTO.getNombre() != null){
            profesorExistente.setNombre(profesorDTO.getNombre());
        }

        if(profesorDTO.getApellido() != null){
            profesorExistente.setApellido(profesorDTO.getApellido());
        }

        Profesor profesorActualizado = profesorDao.updateProfesor(id, profesorExistente);
        return ProfesorMapper.toDTO(profesorActualizado);
    }

    @Override
    public boolean deleteProfesor(int id) {
        if(!profesorDao.delete(id)){
            throw new ProfesorNotFoundException("No se encontro ningun profesor con el ID: " + id + ".");
        }
        return true;
    }
}
