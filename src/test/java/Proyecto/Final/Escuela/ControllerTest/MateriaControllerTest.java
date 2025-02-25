package Proyecto.Final.Escuela.ControllerTest;

import Proyecto.Final.Escuela.Controller.MateriaController;
import Proyecto.Final.Escuela.Dtos.MateriaDTO;
import Proyecto.Final.Escuela.Exception.MateriaExistException;
import Proyecto.Final.Escuela.Exception.MateriaNotFoundException;
import Proyecto.Final.Escuela.Exception.ProfesorNotFoundException;
import Proyecto.Final.Escuela.Service.MateriaService;
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

public class MateriaControllerTest {

    @Mock
    private MateriaService materiaService;

    @InjectMocks
    private MateriaController materiaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllMaterias_ListaVacia() {
        when(materiaService.getAllMaterias()).thenReturn(Collections.emptyList());

        ResponseEntity<Object> responseEntity = materiaController.getAllMaterias();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("No hay materias existentes.", responseBody.get("message"));
        assertNull(responseBody.get("data"));
    }

    @Test
    public void testGetAllMaterias() {
        List<MateriaDTO> materias = Arrays.asList(new MateriaDTO(1, "Matemáticas", "1", "1", 10, Arrays.asList(2, 3)));
        when(materiaService.getAllMaterias()).thenReturn(materias);

        ResponseEntity<Object> responseEntity = materiaController.getAllMaterias();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("Lista de materias obtenida exitosamente.", responseBody.get("message"));
        assertEquals(materias, responseBody.get("data"));
    }

    @Test
    public void testGetMateriaById() {
        MateriaDTO materia = new MateriaDTO(1, "Matematicas", "1", "1", 10, Arrays.asList(2, 3));
        when(materiaService.getMateriaById(1)).thenReturn(Optional.of(materia));

        ResponseEntity<Object> responseEntity = materiaController.getAlumnoById(1);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("La materia fue encontrada.", responseBody.get("message"));
        assertEquals(materia, responseBody.get("data"));
    }

    @Test
    public void testGetMateriaById_MateriaNotFound() {
        when(materiaService.getMateriaById(1)).thenReturn(Optional.empty());

        assertThrows(MateriaNotFoundException.class, () -> materiaController.getAlumnoById(1));
    }

    @Test
    public void testCreateMateria() throws MateriaExistException {
        MateriaDTO materiaDTO = new MateriaDTO(1, "Matematicas", "1", "1", 10, Arrays.asList(2, 3));
        when(materiaService.createMateria(materiaDTO)).thenReturn(materiaDTO);

        ResponseEntity<Object> responseEntity = materiaController.createMateria(materiaDTO);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("La materia fue creada exitosamente.", responseBody.get("message"));
        assertEquals(materiaDTO, responseBody.get("data"));
    }

    @Test
    public void testCreateMateria_MateriaExistException() throws MateriaExistException {
        MateriaDTO materiaDTO = new MateriaDTO(1, "Matematicas", "1", "1", 10, Arrays.asList(2, 3));
        when(materiaService.createMateria(materiaDTO)).thenThrow(new MateriaExistException("La materia ya existe."));

        ResponseEntity<Object> responseEntity = materiaController.createMateria(materiaDTO);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("La materia ya existe.", responseBody.get("message"));
    }

    @Test
    public void testCreateMateria_ProfesorNotFoundException() throws MateriaExistException {
        MateriaDTO materiaDTO = new MateriaDTO(1, "Matematicas", "1", "1", 10, Arrays.asList(2, 3));
        when(materiaService.createMateria(materiaDTO)).thenThrow(new ProfesorNotFoundException("El profesor no fue encontrado."));

        ResponseEntity<Object> responseEntity = materiaController.createMateria(materiaDTO);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("El profesor no fue encontrado.", responseBody.get("message"));
    }

    @Test
    public void testUpdateMateria() throws MateriaExistException {
        MateriaDTO materiaDTO = new MateriaDTO(1, "Matematicas", "1", "1", 10, Arrays.asList(2, 3));
        when(materiaService.updateMateria(1, materiaDTO)).thenReturn(materiaDTO);

        ResponseEntity<Object> responseEntity = materiaController.updateMateria(1, materiaDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("La materia fue actualizada exitosamente.", responseBody.get("message"));
        assertEquals(materiaDTO, responseBody.get("data"));
    }

    @Test
    public void testUpdateMateria_MateriaExistException() throws MateriaExistException {
        MateriaDTO materiaDTO = new MateriaDTO(1, "Matematicas", "1", "1", 10, Arrays.asList(2, 3));
        when(materiaService.updateMateria(1, materiaDTO)).thenThrow(new MateriaExistException("La materia ya existe."));

        ResponseEntity<Object> responseEntity = materiaController.updateMateria(1, materiaDTO);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("La materia ya existe.", responseBody.get("message"));
    }

    @Test
    public void testUpdateMateria_ProfesorNotFoundException() throws MateriaExistException {
        MateriaDTO materiaDTO = new MateriaDTO(1, "Matemáticas", "1", "1", 10, Arrays.asList(2, 3));
        when(materiaService.updateMateria(1, materiaDTO)).thenThrow(new ProfesorNotFoundException("El profesor no fue encontrado."));

        ResponseEntity<Object> responseEntity = materiaController.updateMateria(1, materiaDTO);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("El profesor no fue encontrado.", responseBody.get("message"));
    }

    @Test
    public void testUpdateMateria_MateriaNotFoundException() throws MateriaExistException {
        MateriaDTO materiaDTO = new MateriaDTO(1, "Matemáticas", "1", "1", 10, Arrays.asList(2, 3));
        when(materiaService.updateMateria(1, materiaDTO)).thenThrow(new MateriaNotFoundException("La materia no fue encontrada."));

        ResponseEntity<Object> responseEntity = materiaController.updateMateria(1, materiaDTO);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("La materia no fue encontrada.", responseBody.get("message"));
    }

    @Test
    public void testRemoveProfesorFromMateria() {
        doNothing().when(materiaService).removeProfesorFromMateria(1, 10);

        ResponseEntity<Object> responseEntity = materiaController.removeProfesorFromMateria(1, 10);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("El profesor eliminado de la materia exitosamente.", responseBody.get("message"));
    }

    @Test
    public void testRemoveProfesorFromMateria_MateriaNotFoundException() {
        doThrow(new MateriaNotFoundException("La materia no fue encontrada.")).when(materiaService).removeProfesorFromMateria(1, 10);

        ResponseEntity<Object> responseEntity = materiaController.removeProfesorFromMateria(1, 10);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("La materia no fue encontrada.", responseBody.get("message"));
    }

    @Test
    public void testRemoveProfesorFromMateria_ProfesorNotFoundException() {
        doThrow(new ProfesorNotFoundException("El profesor no fue encontrado.")).when(materiaService).removeProfesorFromMateria(1, 10);

        ResponseEntity<Object> responseEntity = materiaController.removeProfesorFromMateria(1, 10);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("El profesor no fue encontrado.", responseBody.get("message"));
    }

    @Test
    public void testDeleteMateria() {
        doAnswer(invocation -> null).when(materiaService).deleteMateria(1);

        ResponseEntity<Object> responseEntity = materiaController.deleteMateria(1);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("La materia con el ID 1 fue eliminada exitosamente.", responseBody.get("message"));
    }
}
