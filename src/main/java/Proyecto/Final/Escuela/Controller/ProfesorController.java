package Proyecto.Final.Escuela.Controller;

import Proyecto.Final.Escuela.Dtos.ProfesorDTO;
import Proyecto.Final.Escuela.Exception.InvalidDataException;
import Proyecto.Final.Escuela.Exception.ProfesorExistException;
import Proyecto.Final.Escuela.Exception.ProfesorNotFoundException;
import Proyecto.Final.Escuela.Service.ProfesorService;
import Proyecto.Final.Escuela.Util.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profesor")
public class ProfesorController {
    private final ProfesorService profesorService;

    @Autowired
    public ProfesorController(ProfesorService profesorService){

        this.profesorService = profesorService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllProfesores(){
        List<ProfesorDTO> profesor = profesorService.getAllProfesores();
        if(profesor.isEmpty()){
            return ResponseHandler.response(HttpStatus.OK, null, "No hay ningun profesor registrado en el sistema.");
        }
        return ResponseHandler.response(HttpStatus.OK, profesor, "Lista de profesores obtenida exitosamente.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getProfesorById(@PathVariable int id){
        ProfesorDTO profesor = profesorService.getProfesorById(id).orElseThrow(() ->
                new ProfesorNotFoundException("El profesor con el ID " + id + " no fue encontrado"));
        return ResponseHandler.response(HttpStatus.OK, profesor, "El profesor fue encontrado.");
    }

    @PostMapping
    public ResponseEntity<Object> createProfesor(@RequestBody ProfesorDTO profesorDTO) {
        try {
            ProfesorDTO nuevoProfesor = profesorService.createProfesor(profesorDTO);
            return ResponseHandler.response(HttpStatus.CREATED, nuevoProfesor, "El profesor fue creado exitosamente.");
        } catch (ProfesorExistException | InvalidDataException e) {
            return ResponseHandler.response(HttpStatus.BAD_REQUEST, null, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateProfesor(@PathVariable int id, @RequestBody ProfesorDTO profesorDTO) {
        try {
            ProfesorDTO actualizado = profesorService.updateProfesor(id, profesorDTO);
            return ResponseHandler.response(HttpStatus.OK, actualizado, "El profesor fue actualizado exitosamente.");
        } catch (ProfesorExistException e) {
            return ResponseHandler.response(HttpStatus.BAD_REQUEST, null, e.getMessage());
        }catch (ProfesorNotFoundException e){
            return ResponseHandler.response(HttpStatus.NOT_FOUND, null, e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public  ResponseEntity<Object> deleteProfesor(@PathVariable int id){
        profesorService.deleteProfesor(id);
        return ResponseHandler.response(HttpStatus.OK, null, "El profesor con el ID " + id + " fue eliminado exitosamente.");
    }
}
