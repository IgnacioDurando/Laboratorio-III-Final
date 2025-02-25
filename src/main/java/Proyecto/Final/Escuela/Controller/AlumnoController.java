package Proyecto.Final.Escuela.Controller;

import Proyecto.Final.Escuela.Dtos.AlumnoDTO;
import Proyecto.Final.Escuela.Exception.*;
import Proyecto.Final.Escuela.Service.AlumnoService;
import Proyecto.Final.Escuela.Util.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alumno")
public class AlumnoController {
    private final AlumnoService alumnoService;

    @Autowired
    public AlumnoController(AlumnoService alumnoService) {
        this.alumnoService = alumnoService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllAlumnos() {
        List<AlumnoDTO> alumnos = alumnoService.getAllAlumnos();
        if (alumnos.isEmpty()) {
            return ResponseHandler.response(HttpStatus.OK, null, "No hay alumnos existentes.");
        }
        return ResponseHandler.response(HttpStatus.OK, alumnos, "Lista de alumnos obtenida exitosamente.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getAlumnoById(@PathVariable int id) {
        AlumnoDTO alumno = alumnoService.getAlumnoById(id)
                .orElseThrow(() -> new AlumnoNotFoundException("El alumno con el ID " + id + " no fue encontrado."));
        return ResponseHandler.response(HttpStatus.OK, alumno, "Alumno encontrado.");
    }

    @PostMapping
    public ResponseEntity<Object> createAlumno(@RequestBody AlumnoDTO alumnoDTO) {
        try {
            AlumnoDTO nuevoAlumno = alumnoService.createAlumno(alumnoDTO);
            return ResponseHandler.response(HttpStatus.CREATED, nuevoAlumno, "Alumno creado exitosamente.");
        } catch (AlumnoExistException | InvalidDataException e) {
            return ResponseHandler.response(HttpStatus.BAD_REQUEST, null, e.getMessage());
        }catch (CarreraNotFoundException e){
            return ResponseHandler.response(HttpStatus.NOT_FOUND, null, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateAlumno(@PathVariable int id, @RequestBody AlumnoDTO alumnoDTO) {
        try {
            AlumnoDTO actualizado = alumnoService.updateAlumno(id, alumnoDTO);
            return ResponseHandler.response(HttpStatus.OK, actualizado, "Alumno actualizado exitosamente.");
        } catch (AlumnoExistException e ) {
            return ResponseHandler.response(HttpStatus.BAD_REQUEST, null, e.getMessage());
        }catch (AlumnoNotFoundException | CarreraNotFoundException | MateriaNotFoundException e){
            return ResponseHandler.response(HttpStatus.NOT_FOUND, null, e.getMessage());
        }
    }

    @DeleteMapping("/{idAlumno}/materia/{idMateria}")
    public ResponseEntity<Object> removeMateriaFromAlumno(@PathVariable int idAlumno, @PathVariable int idMateria) {
        try {
            alumnoService.removeMateriaFromAlumno(idAlumno, idMateria);
            return ResponseHandler.response(HttpStatus.OK, null, "Materia eliminada del alumno exitosamente.");
        } catch (AlumnoNotFoundException | MateriaNotFoundException e) {
            return ResponseHandler.response(HttpStatus.NOT_FOUND, null, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAlumno(@PathVariable int id) {
        try {
            alumnoService.deleteAlumno(id);
            return ResponseHandler.response(HttpStatus.OK, null, "Alumno eliminado exitosamente.");
        } catch (AlumnoNotFoundException e) {
            return ResponseHandler.response(HttpStatus.NOT_FOUND, null, e.getMessage());
        }
    }


}
