package Proyecto.Final.Escuela.ControllerTest;

import Proyecto.Final.Escuela.Controller.AlumnoController;
import Proyecto.Final.Escuela.Dtos.AlumnoDTO;
import Proyecto.Final.Escuela.Exception.*;
import Proyecto.Final.Escuela.Service.AlumnoService;
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

public class AlumnoControllerTest {

    @Mock
    private AlumnoService alumnoService;

    @InjectMocks
    private AlumnoController alumnoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllAlumnos_ListaVacia() {
        when(alumnoService.getAllAlumnos()).thenReturn(Collections.emptyList());

        ResponseEntity<Object> responseEntity = alumnoController.getAllAlumnos();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("No hay alumnos existentes.", responseBody.get("message"));
        assertNull(responseBody.get("data"));
    }

    @Test
    public void testGetAllAlumnos() {
        List<AlumnoDTO> alumnos = Arrays.asList(new AlumnoDTO(1, "Juan Perez", "Perez", "12345678", "20", 1, new HashMap<>()));
        when(alumnoService.getAllAlumnos()).thenReturn(alumnos);

        ResponseEntity<Object> responseEntity = alumnoController.getAllAlumnos();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("Lista de alumnos obtenida exitosamente.", responseBody.get("message"));
        assertEquals(alumnos, responseBody.get("data"));
    }

    @Test
    public void testGetAlumnoById() {
        AlumnoDTO alumno = new AlumnoDTO(1, "Juan Perez", "Perez", "12345678", "20", 1, new HashMap<>());
        when(alumnoService.getAlumnoById(1)).thenReturn(Optional.of(alumno));

        ResponseEntity<Object> responseEntity = alumnoController.getAlumnoById(1);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("Alumno encontrado.", responseBody.get("message"));
        assertEquals(alumno, responseBody.get("data"));
    }

    @Test
    public void testGetAlumnoById_AlumnoNotFound() {
        when(alumnoService.getAlumnoById(1)).thenReturn(Optional.empty());
        assertThrows(AlumnoNotFoundException.class, () -> alumnoController.getAlumnoById(1));
    }

    @Test
    public void testCreateAlumno() throws AlumnoExistException {
        AlumnoDTO alumnoDTO = new AlumnoDTO(1, "Juan", "Perez", "12345678", "20", 1, new HashMap<>());
        when(alumnoService.createAlumno(alumnoDTO)).thenReturn(alumnoDTO);

        ResponseEntity<Object> responseEntity = alumnoController.createAlumno(alumnoDTO);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("Alumno creado exitosamente.", responseBody.get("message"));
        assertEquals(alumnoDTO, responseBody.get("data"));
    }

    @Test
    public void testCreateAlumno_AlumnoExistException() throws AlumnoExistException {
        AlumnoDTO alumnoDTO = new AlumnoDTO(1, "Juan", "Perez", "12345678", "20", 1, new HashMap<>());
        when(alumnoService.createAlumno(alumnoDTO)).thenThrow(new AlumnoExistException("El alumno ya existe."));

        ResponseEntity<Object> responseEntity = alumnoController.createAlumno(alumnoDTO);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("El alumno ya existe.", responseBody.get("message"));
    }

    @Test
    public void testCreateAlumno_CarreraNotFoundException() throws AlumnoExistException {
        AlumnoDTO alumnoDTO = new AlumnoDTO(1, "Juan", "Perez", "12345678", "20", 1, new HashMap<>());
        when(alumnoService.createAlumno(alumnoDTO)).thenThrow(new CarreraNotFoundException("Carrera no encontrada."));

        ResponseEntity<Object> responseEntity = alumnoController.createAlumno(alumnoDTO);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("Carrera no encontrada.", responseBody.get("message"));
    }

    @Test
    public void testUpdateAlumno() throws AlumnoExistException{
        AlumnoDTO alumnoDTO = new AlumnoDTO(1, "Juan", "Perez", "12345678", "20", 1, new HashMap<>());
        when(alumnoService.updateAlumno(1, alumnoDTO)).thenReturn(alumnoDTO);

        ResponseEntity<Object> responseEntity = alumnoController.updateAlumno(1, alumnoDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("Alumno actualizado exitosamente.", responseBody.get("message"));
        assertEquals(alumnoDTO, responseBody.get("data"));
    }

    @Test
    public void testUpdateAlumno_AlumnoNotFoundException() throws AlumnoExistException{
        AlumnoDTO alumnoDTO = new AlumnoDTO(1, "Juan", "Perez", "12345678", "20", 1, new HashMap<>());
        when(alumnoService.updateAlumno(1, alumnoDTO)).thenThrow(new AlumnoNotFoundException("El alumno no fue encontrado."));

        ResponseEntity<Object> responseEntity = alumnoController.updateAlumno(1, alumnoDTO);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("El alumno no fue encontrado.", responseBody.get("message"));
    }

    @Test
    public void testUpdateAlumno_AlumnoExistException() throws AlumnoExistException {
        AlumnoDTO alumnoDTO = new AlumnoDTO(1, "Juan", "Perez", "12345678", "20", 1, new HashMap<>());
        when(alumnoService.updateAlumno(1, alumnoDTO)).thenThrow(new AlumnoExistException("El alumno ya existe."));

        ResponseEntity<Object> responseEntity = alumnoController.updateAlumno(1, alumnoDTO);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("El alumno ya existe.", responseBody.get("message"));
    }

    @Test
    public void testUpdateAlumno_MateriaNotFoundException() throws AlumnoExistException {
        AlumnoDTO alumnoDTO = new AlumnoDTO(1, "Juan", "Perez", "12345678", "20", 1, new HashMap<>());
        when(alumnoService.updateAlumno(1, alumnoDTO)).thenThrow(new MateriaNotFoundException("La materia no fue encontrada."));

        ResponseEntity<Object> responseEntity = alumnoController.updateAlumno(1, alumnoDTO);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("La materia no fue encontrada.", responseBody.get("message"));
    }

    @Test
    public void testUpdateAlumno_CarreraNotFoundException() throws AlumnoExistException {
        AlumnoDTO alumnoDTO = new AlumnoDTO(1, "Juan", "Perez", "12345678", "20", 1, new HashMap<>());
        when(alumnoService.updateAlumno(1, alumnoDTO)).thenThrow(new CarreraNotFoundException("La carrera no fue encontrada."));

        ResponseEntity<Object> responseEntity = alumnoController.updateAlumno(1, alumnoDTO);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("La carrera no fue encontrada.", responseBody.get("message"));
    }

    @Test
    public void testRemoveMateriaFromAlumno() {
        doNothing().when(alumnoService).removeMateriaFromAlumno(1, 101);

        ResponseEntity<Object> responseEntity = alumnoController.removeMateriaFromAlumno(1, 101);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("Materia eliminada del alumno exitosamente.", responseBody.get("message"));
    }

    @Test
    public void testRemoveMateriaFromAlumno_AlumnoNotFoundException() {
        doThrow(new AlumnoNotFoundException("El alumno no fue encontrado.")).when(alumnoService).removeMateriaFromAlumno(1, 101);

        ResponseEntity<Object> responseEntity = alumnoController.removeMateriaFromAlumno(1, 101);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("El alumno no fue encontrado.", responseBody.get("message"));
    }

    @Test
    public void testRemoveMateriaFromAlumno_MateriaNotFoundException() {
        doThrow(new MateriaNotFoundException("La materia no fue encontrada.")).when(alumnoService).removeMateriaFromAlumno(1, 101);

        ResponseEntity<Object> responseEntity = alumnoController.removeMateriaFromAlumno(1, 101);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("La materia no fue encontrada.", responseBody.get("message"));
    }

    @Test
    public void testDeleteAlumno() {
        doAnswer(invocation -> null).when(alumnoService).deleteAlumno(1);

        ResponseEntity<Object> responseEntity = alumnoController.deleteAlumno(1);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("Alumno eliminado exitosamente.", responseBody.get("message"));
    }

    @Test
    public void testDeleteAlumno_AlumnoNotFoundException() {
        doThrow(new AlumnoNotFoundException("El alumno no fue encontrado.")).when(alumnoService).deleteAlumno(1);

        ResponseEntity<Object> responseEntity = alumnoController.deleteAlumno(1);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("El alumno no fue encontrado.", responseBody.get("message"));
    }
}
