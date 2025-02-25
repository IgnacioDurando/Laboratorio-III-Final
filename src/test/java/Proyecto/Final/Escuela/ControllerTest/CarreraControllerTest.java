package Proyecto.Final.Escuela.ControllerTest;

import Proyecto.Final.Escuela.Controller.CarreraController;
import Proyecto.Final.Escuela.Dtos.CarreraDTO;
import Proyecto.Final.Escuela.Exception.*;
import Proyecto.Final.Escuela.Service.CarreraService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CarreraControllerTest {

    @Mock
    private CarreraService carreraService;

    @InjectMocks
    private CarreraController carreraController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllCarreras_ListaVacia() {
        when(carreraService.getAllCarreras()).thenReturn(Collections.emptyList());

        ResponseEntity<Object> responseEntity = carreraController.getAllCarreras();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("No hay carreras existentes.", responseBody.get("message"));
        assertNull(responseBody.get("data"));
    }

    @Test
    public void testGetAllCarreras() {
        List<CarreraDTO> carreras = List.of(new CarreraDTO(1, "Tecnicatura en Programacion", List.of(101, 102)));
        when(carreraService.getAllCarreras()).thenReturn(carreras);

        ResponseEntity<Object> responseEntity = carreraController.getAllCarreras();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("Lista de carreras obtenida exitosamente.", responseBody.get("message"));
        assertEquals(carreras, responseBody.get("data"));
    }

    @Test
    public void testGetCarreraById() {
        CarreraDTO carreraDTO = new CarreraDTO(1, "Tecnicatura en Programacion", List.of(101, 102));
        when(carreraService.getCarreraById(1)).thenReturn(Optional.of(carreraDTO));

        ResponseEntity<Object> responseEntity = carreraController.getCarreraById(1);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("Carrera encontrada.", responseBody.get("message"));
        assertEquals(carreraDTO, responseBody.get("data"));
    }

    @Test
    public void testGetCarreraById_NotFound() {
        when(carreraService.getCarreraById(1)).thenReturn(Optional.empty());
        assertThrows(CarreraNotFoundException.class, () -> carreraController.getCarreraById(1));
    }

    @Test
    public void testCreateCarrera() throws CarreraExistException {
        CarreraDTO carreraDTO = new CarreraDTO(1, "Tecnicatura en Programacion", List.of(101, 102));
        when(carreraService.createCarrera(carreraDTO)).thenReturn(carreraDTO);

        ResponseEntity<Object> responseEntity = carreraController.createCarrera(carreraDTO);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("Carrera creada exitosamente.", responseBody.get("message"));
        assertEquals(carreraDTO, responseBody.get("data"));
    }

    @Test
    public void testCreateCarrera_CarreraExistException() throws CarreraExistException {
        CarreraDTO carreraDTO = new CarreraDTO(1, "Tecnicatura en Programacion", List.of(101, 102));
        when(carreraService.createCarrera(carreraDTO)).thenThrow(new CarreraExistException("La carrera ya existe."));

        ResponseEntity<Object> responseEntity = carreraController.createCarrera(carreraDTO);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("La carrera ya existe.", responseBody.get("message"));
    }

    @Test
    public void testCreateCarrera_InvalidDataException() throws CarreraExistException {
        CarreraDTO carreraDTO = new CarreraDTO(1, "", List.of(101, 102));
        when(carreraService.createCarrera(carreraDTO)).thenThrow(new InvalidDataException("El nombre de la carrera no puede quedar vacio."));

        ResponseEntity<Object> responseEntity = carreraController.createCarrera(carreraDTO);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("El nombre de la carrera no puede quedar vacio.", responseBody.get("message"));
    }

    @Test
    public void testCreateCarrera_MateriaNotFoundException() throws CarreraExistException {
        CarreraDTO carreraDTO = new CarreraDTO(1, "Tecnicatura en Programacion", List.of(101, 102));
        when(carreraService.createCarrera(carreraDTO)).thenThrow(new MateriaNotFoundException("Materia no encontrada."));

        ResponseEntity<Object> responseEntity = carreraController.createCarrera(carreraDTO);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("Materia no encontrada.", responseBody.get("message"));
    }

    @Test
    public void testUpdateCarrera() throws CarreraExistException, MateriaExistException {
        CarreraDTO carreraDTO = new CarreraDTO(1, "Tecnicatura en Programacion", List.of(101, 102));
        when(carreraService.updateCarrera(1, carreraDTO)).thenReturn(carreraDTO);

        ResponseEntity<Object> responseEntity = carreraController.updateCarrera(1, carreraDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("Carrera actualizada exitosamente.", responseBody.get("message"));
        assertEquals(carreraDTO, responseBody.get("data"));
    }

    @Test
    public void testUpdateCarrera_CarreraExistException() throws CarreraExistException, MateriaExistException {
        CarreraDTO carreraDTO = new CarreraDTO(1, "Tecnicatura en Programacion", List.of(101, 102));
        when(carreraService.updateCarrera(1, carreraDTO)).thenThrow(new CarreraExistException("La carrera ya existe."));

        ResponseEntity<Object> responseEntity = carreraController.updateCarrera(1, carreraDTO);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("La carrera ya existe.", responseBody.get("message"));
    }

    @Test
    public void testUpdateCarrera_CarreraNotFoundException() throws CarreraExistException, MateriaExistException {
        CarreraDTO carreraDTO = new CarreraDTO(1, "Tecnicatura en Programacion", List.of(101, 102));
        when(carreraService.updateCarrera(1, carreraDTO)).thenThrow(new CarreraNotFoundException("La carrera no fue encontrada."));

        ResponseEntity<Object> responseEntity = carreraController.updateCarrera(1, carreraDTO);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("La carrera no fue encontrada.", responseBody.get("message"));
    }

    @Test
    public void testUpdateCarrera_MateriaNotFoundException() throws CarreraExistException, MateriaExistException {
        CarreraDTO carreraDTO = new CarreraDTO(1, "Tecnicatura en Programacion", List.of(101, 102));
        when(carreraService.updateCarrera(1, carreraDTO)).thenThrow(new MateriaNotFoundException("Materia no encontrada."));

        ResponseEntity<Object> responseEntity = carreraController.updateCarrera(1, carreraDTO);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("Materia no encontrada.", responseBody.get("message"));
    }

    @Test
    public void testRemoveMateriaFromCarrera() {
        doNothing().when(carreraService).removeMateriaFromCarrera(1, 101);

        ResponseEntity<Object> responseEntity = carreraController.removeMateriaFromCarrera(1, 101);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("Materia eliminada de la carrera exitosamente.", responseBody.get("message"));
    }

    @Test
    public void testRemoveMateriaFromCarrera_CarreraNotFoundException() {
        doThrow(new CarreraNotFoundException("La carrera no fue encontrada.")).when(carreraService).removeMateriaFromCarrera(1, 101);

        ResponseEntity<Object> responseEntity = carreraController.removeMateriaFromCarrera(1, 101);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("La carrera no fue encontrada.", responseBody.get("message"));
    }

    @Test
    public void testRemoveMateriaFromCarrera_MateriaNotFoundException() {
        doThrow(new MateriaNotFoundException("La materia no fue encontrada.")).when(carreraService).removeMateriaFromCarrera(1, 101);

        ResponseEntity<Object> responseEntity = carreraController.removeMateriaFromCarrera(1, 101);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("La materia no fue encontrada.", responseBody.get("message"));
    }

    @Test
    public void testDeleteCarrera() {
        doAnswer(invocation -> null).when(carreraService).deleteCarrera(1);

        ResponseEntity<Object> responseEntity = carreraController.deleteCarrera(1);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("Carrera eliminada exitosamente.", responseBody.get("message"));
    }

    @Test
    public void testDeleteCarrera_CarreraNotFoundException() {
        doThrow(new CarreraNotFoundException("La carrera no fue encontrada.")).when(carreraService).deleteCarrera(1);

        ResponseEntity<Object> responseEntity = carreraController.deleteCarrera(1);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("La carrera no fue encontrada.", responseBody.get("message"));
    }
}
