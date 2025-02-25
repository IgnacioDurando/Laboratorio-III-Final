package Proyecto.Final.Escuela.ControllerTest;

import Proyecto.Final.Escuela.Controller.ProfesorController;
import Proyecto.Final.Escuela.Dtos.ProfesorDTO;
import Proyecto.Final.Escuela.Exception.InvalidDataException;
import Proyecto.Final.Escuela.Exception.ProfesorExistException;
import Proyecto.Final.Escuela.Exception.ProfesorNotFoundException;
import Proyecto.Final.Escuela.Service.ProfesorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProfesorControllerTest {

    @Mock
    private ProfesorService profesorService;

    @InjectMocks
    private ProfesorController profesorController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllProfesores_ListaVacia() {
        when(profesorService.getAllProfesores()).thenReturn(Collections.emptyList());

        ResponseEntity<Object> responseEntity = profesorController.getAllProfesores();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("No hay ningun profesor registrado en el sistema.", responseBody.get("message"));
        assertNull(responseBody.get("data"));
    }

    @Test
    public void testGetAllProfesores() {
        List<ProfesorDTO> profesores = Arrays.asList(new ProfesorDTO(1, "Carlos", "Gomez", List.of(101, 102)));
        when(profesorService.getAllProfesores()).thenReturn(profesores);

        ResponseEntity<Object> responseEntity = profesorController.getAllProfesores();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("Lista de profesores obtenida exitosamente.", responseBody.get("message"));
        assertEquals(profesores, responseBody.get("data"));
    }

    @Test
    public void testGetProfesorById() {
        ProfesorDTO profesor = new ProfesorDTO(1, "Carlos", "Gomez", List.of(101, 102));
        when(profesorService.getProfesorById(1)).thenReturn(Optional.of(profesor));

        ResponseEntity<Object> responseEntity = profesorController.getProfesorById(1);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("El profesor fue encontrado.", responseBody.get("message"));
        assertEquals(profesor, responseBody.get("data"));
    }

    @Test
    public void testGetProfesorById_ProfesorNotFound() {
        when(profesorService.getProfesorById(1)).thenReturn(Optional.empty());

        assertThrows(ProfesorNotFoundException.class, () -> profesorController.getProfesorById(1));
    }

    @Test
    public void testCreateProfesor() throws ProfesorExistException {
        ProfesorDTO profesorDTO = new ProfesorDTO(1, "Carlos", "Gomez", List.of(101, 102));
        when(profesorService.createProfesor(profesorDTO)).thenReturn(profesorDTO);

        ResponseEntity<Object> responseEntity = profesorController.createProfesor(profesorDTO);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("El profesor fue creado exitosamente.", responseBody.get("message"));
        assertEquals(profesorDTO, responseBody.get("data"));
    }

    @Test
    public void testCreateProfesor_ProfesorExistException() throws ProfesorExistException {
        ProfesorDTO profesorDTO = new ProfesorDTO(1, "Carlos", "Gomez", List.of(101, 102));
        when(profesorService.createProfesor(profesorDTO)).thenThrow(new ProfesorExistException("El profesor ya existe."));

        ResponseEntity<Object> responseEntity = profesorController.createProfesor(profesorDTO);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("El profesor ya existe.", responseBody.get("message"));
    }

    @Test
    public void testCreateProfesor_InvalidDataException() throws ProfesorExistException {
        ProfesorDTO profesorDTO = new ProfesorDTO(1, "Carlos", "Gomez", List.of(101, 102));
        when(profesorService.createProfesor(profesorDTO)).thenThrow(new InvalidDataException("El nombre y el apellido del profesor no pueden quedar vacios."));

        ResponseEntity<Object> responseEntity = profesorController.createProfesor(profesorDTO);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("El nombre y el apellido del profesor no pueden quedar vacios.", responseBody.get("message"));
    }

    @Test
    public void testUpdateProfesor() throws ProfesorExistException {
        ProfesorDTO profesorDTO = new ProfesorDTO(1, "Carlos", "Gomez", List.of(101, 102));
        when(profesorService.updateProfesor(1, profesorDTO)).thenReturn(profesorDTO);

        ResponseEntity<Object> responseEntity = profesorController.updateProfesor(1, profesorDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("El profesor fue actualizado exitosamente.", responseBody.get("message"));
        assertEquals(profesorDTO, responseBody.get("data"));
    }

    @Test
    public void testDeleteProfesor() {
        doAnswer(invocation -> null).when(profesorService).deleteProfesor(1);

        ResponseEntity<Object> responseEntity = profesorController.deleteProfesor(1);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("El profesor con el ID 1 fue eliminado exitosamente.", responseBody.get("message"));
    }
}