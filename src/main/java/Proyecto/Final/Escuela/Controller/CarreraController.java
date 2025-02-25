package Proyecto.Final.Escuela.Controller;

import Proyecto.Final.Escuela.Dtos.CarreraDTO;
import Proyecto.Final.Escuela.Exception.*;
import Proyecto.Final.Escuela.Service.CarreraService;
import Proyecto.Final.Escuela.Util.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carrera")
public class CarreraController {
    private final CarreraService carreraService;

    @Autowired
    public CarreraController(CarreraService carreraService) {
        this.carreraService = carreraService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllCarreras() {
        List<CarreraDTO> carreras = carreraService.getAllCarreras();
        if (carreras.isEmpty()) {
            return ResponseHandler.response(HttpStatus.OK, null, "No hay carreras existentes.");
        }
        return ResponseHandler.response(HttpStatus.OK, carreras, "Lista de carreras obtenida exitosamente.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getCarreraById(@PathVariable int id) {
        CarreraDTO carrera = carreraService.getCarreraById(id)
                .orElseThrow(() -> new CarreraNotFoundException("La carrera con el ID " + id + " no fue encontrada."));
        return ResponseHandler.response(HttpStatus.OK, carrera, "Carrera encontrada.");
    }

    @PostMapping
    public ResponseEntity<Object> createCarrera(@RequestBody CarreraDTO carreraDTO) {
        try {
            CarreraDTO nuevaCarrera = carreraService.createCarrera(carreraDTO);
            return ResponseHandler.response(HttpStatus.CREATED, nuevaCarrera, "Carrera creada exitosamente.");
        } catch (CarreraExistException | InvalidDataException e) {
            return ResponseHandler.response(HttpStatus.BAD_REQUEST, null, e.getMessage());
        }catch (MateriaNotFoundException e){
            return ResponseHandler.response(HttpStatus.NOT_FOUND, null, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateCarrera(@PathVariable int id, @RequestBody CarreraDTO carreraDTO) {
        try {
            CarreraDTO actualizada = carreraService.updateCarrera(id, carreraDTO);
            return ResponseHandler.response(HttpStatus.OK, actualizada, "Carrera actualizada exitosamente.");
        } catch (MateriaExistException | CarreraExistException e) {
            return ResponseHandler.response(HttpStatus.BAD_REQUEST, null, e.getMessage());
        }catch (CarreraNotFoundException | MateriaNotFoundException e){
            return ResponseHandler.response(HttpStatus.NOT_FOUND, null, e.getMessage());
        }
    }


    @DeleteMapping("/{idCarrera}/materia/{idMateria}")
    public ResponseEntity<Object> removeMateriaFromCarrera(@PathVariable int idCarrera, @PathVariable int idMateria) {
        try {
            carreraService.removeMateriaFromCarrera(idCarrera, idMateria);
            return ResponseHandler.response(HttpStatus.OK, null, "Materia eliminada de la carrera exitosamente.");
        } catch (CarreraNotFoundException | MateriaNotFoundException e) {
            return ResponseHandler.response(HttpStatus.NOT_FOUND, null, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCarrera(@PathVariable int id) {
        try {
            carreraService.deleteCarrera(id);
            return ResponseHandler.response(HttpStatus.OK, null, "Carrera eliminada exitosamente.");
        } catch (CarreraNotFoundException e) {
            return ResponseHandler.response(HttpStatus.NOT_FOUND, null, e.getMessage());
        }
    }

}
