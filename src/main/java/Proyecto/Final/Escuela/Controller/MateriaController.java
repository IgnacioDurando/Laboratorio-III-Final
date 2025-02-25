package Proyecto.Final.Escuela.Controller;

import Proyecto.Final.Escuela.Dtos.MateriaDTO;
import Proyecto.Final.Escuela.Exception.InvalidDataException;
import Proyecto.Final.Escuela.Exception.MateriaNotFoundException;
import Proyecto.Final.Escuela.Exception.ProfesorNotFoundException;
import Proyecto.Final.Escuela.Service.MateriaService;
import Proyecto.Final.Escuela.Exception.MateriaExistException;
import Proyecto.Final.Escuela.Util.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/materia")
public class MateriaController {
    private final MateriaService materiaService;

    @Autowired
    public MateriaController(MateriaService materiaService) {
        this.materiaService = materiaService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllMaterias() {
        List<MateriaDTO> materias = materiaService.getAllMaterias();
        if (materias.isEmpty()) {
            return ResponseHandler.response(HttpStatus.OK, null, "No hay materias existentes.");
        }
        return ResponseHandler.response(HttpStatus.OK, materias, "Lista de materias obtenida exitosamente.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getAlumnoById(@PathVariable int id) {
        MateriaDTO materia = materiaService.getMateriaById(id).orElseThrow(() ->
                new MateriaNotFoundException("La materia con el ID " + id + " no fue encontrada."));
        return ResponseHandler.response(HttpStatus.OK, materia, "La materia fue encontrada.");
    }

    @PostMapping
    public ResponseEntity<Object> createMateria(@RequestBody MateriaDTO materiaDTO) {
        try {
            MateriaDTO nuevaMateria = materiaService.createMateria(materiaDTO);
            return ResponseHandler.response(HttpStatus.CREATED, nuevaMateria, "La materia fue creada exitosamente.");
        } catch (MateriaExistException | InvalidDataException e) {
            return ResponseHandler.response(HttpStatus.BAD_REQUEST, null, e.getMessage());
        } catch (ProfesorNotFoundException e){
            return ResponseHandler.response(HttpStatus.NOT_FOUND, null, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateMateria(@PathVariable int id, @RequestBody MateriaDTO materiaDTO) {
        try {
            MateriaDTO actualizada = materiaService.updateMateria(id, materiaDTO);
            return ResponseHandler.response(HttpStatus.OK, actualizada, "La materia fue actualizada exitosamente.");
        } catch (MateriaExistException | InvalidDataException e) {
            return ResponseHandler.response(HttpStatus.BAD_REQUEST, null, e.getMessage());
        } catch (MateriaNotFoundException | ProfesorNotFoundException e) {
            return ResponseHandler.response(HttpStatus.NOT_FOUND, null, e.getMessage());
        }
    }


    @DeleteMapping("/{idMateria}/profesor/{idProfesor}")
    public ResponseEntity<Object> removeProfesorFromMateria(@PathVariable int idMateria, @PathVariable int idProfesor) {
        try {
            materiaService.removeProfesorFromMateria(idMateria, idProfesor);
            return ResponseHandler.response(HttpStatus.OK, null, "El profesor eliminado de la materia exitosamente.");
        } catch (MateriaNotFoundException | ProfesorNotFoundException e) {
            return ResponseHandler.response(HttpStatus.NOT_FOUND, null, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMateria(@PathVariable int id) {
        materiaService.deleteMateria(id);
        return ResponseHandler.response(HttpStatus.OK, null, "La materia con el ID " + id + " fue eliminada exitosamente.");
    }
}
