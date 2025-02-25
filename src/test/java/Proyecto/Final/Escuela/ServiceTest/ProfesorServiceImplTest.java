package Proyecto.Final.Escuela.ServiceTest;

import Proyecto.Final.Escuela.Dtos.ProfesorDTO;
import Proyecto.Final.Escuela.Dtos.ProfesorMapper;
import Proyecto.Final.Escuela.Exception.InvalidDataException;
import Proyecto.Final.Escuela.Exception.ProfesorExistException;
import Proyecto.Final.Escuela.Exception.ProfesorNotFoundException;
import Proyecto.Final.Escuela.Model.Profesor;
import Proyecto.Final.Escuela.Persistance.ProfesorDao;
import Proyecto.Final.Escuela.Service.Implementation.ProfesorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfesorServiceImplTest {

    @Mock
    private ProfesorDao profesorDao;

    @InjectMocks
    private ProfesorServiceImpl profesorService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testGetAllProfesores_ListaVacia() {
        when(profesorDao.findAll()).thenReturn(Collections.emptyList());

        List<ProfesorDTO> resultado = profesorService.getAllProfesores();

        assertTrue(resultado.isEmpty());
        verify(profesorDao, times(1)).findAll();
    }

    @Test
    void testGetAllProfesores() {
        ProfesorDTO profesorDTO = new ProfesorDTO(1, "Lionel", "Scaloni", List.of(101, 102));
        Profesor profesor = ProfesorMapper.toEntity(profesorDTO);

        when(profesorDao.findAll()).thenReturn(List.of(profesor));

        List<ProfesorDTO> resultado = profesorService.getAllProfesores();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Lionel", resultado.get(0).getNombre());
        assertEquals("Scaloni", resultado.get(0).getApellido());
        verify(profesorDao, times(1)).findAll();
    }

    @Test
    void testGetProfesorById() {
        ProfesorDTO profesorDTO = new ProfesorDTO(1, "Lionel", "Scaloni", List.of(101, 102));
        Profesor profesor = ProfesorMapper.toEntity(profesorDTO);

        when(profesorDao.findById(1)).thenReturn(Optional.of(profesor));

        Optional<ProfesorDTO> resultado = profesorService.getProfesorById(1);

        assertTrue(resultado.isPresent());
        assertEquals("Lionel", resultado.get().getNombre());
        assertEquals("Scaloni", resultado.get().getApellido());
        verify(profesorDao, times(1)).findById(1);
    }

    @Test
    void testCreateProfesor() throws ProfesorExistException {
        ProfesorDTO profesorDTO = new ProfesorDTO(1, "Juan", "Perez", List.of(101, 102));
        Profesor profesor = ProfesorMapper.toEntity(profesorDTO);

        when(profesorDao.findAll()).thenReturn(Collections.emptyList());
        when(profesorDao.save(any(Profesor.class))).thenReturn(profesor);

        ProfesorDTO resultado = profesorService.createProfesor(profesorDTO);

        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombre());
        assertEquals("Perez", resultado.getApellido());
        verify(profesorDao, times(1)).save(any(Profesor.class));
    }

    @Test
    void testCreateProfesor_NombreVacio() {
        ProfesorDTO profesorDTO = new ProfesorDTO(1, "", "Perez", List.of(101, 102));

        assertThrows(InvalidDataException.class, () -> profesorService.createProfesor(profesorDTO));
    }

    @Test
    void testCreateProfesor_ApellidoVacio() {
        ProfesorDTO profesorDTO = new ProfesorDTO(1, "Juan", "", List.of(101, 102));

        assertThrows(InvalidDataException.class, () -> profesorService.createProfesor(profesorDTO));
    }

    @Test
    void testCreateProfesor_ProfesorExistException() {
        ProfesorDTO profesorDTO = new ProfesorDTO(1, "Juan", "Perez", List.of(101, 102));
        Profesor profesor = ProfesorMapper.toEntity(profesorDTO);

        when(profesorDao.findAll()).thenReturn(List.of(profesor));

        assertThrows(ProfesorExistException.class, () -> profesorService.createProfesor(profesorDTO));
    }

    @Test
    void testUpdateProfesor() throws ProfesorExistException {
        ProfesorDTO profesorDTO = new ProfesorDTO(1, "Carlos", "Lopez", List.of(101, 102));
        Profesor profesorExistente = new Profesor();
        profesorExistente.setId(1);
        profesorExistente.setNombre("Juan");
        profesorExistente.setApellido("Perez");

        when(profesorDao.findById(1)).thenReturn(Optional.of(profesorExistente));
        when(profesorDao.findAll()).thenReturn(List.of(profesorExistente));
        when(profesorDao.updateProfesor(eq(1), any(Profesor.class))).thenReturn(profesorExistente);

        ProfesorDTO resultado = profesorService.updateProfesor(1, profesorDTO);

        assertNotNull(resultado);
        assertEquals("Carlos", resultado.getNombre());
        assertEquals("Lopez", resultado.getApellido());
        verify(profesorDao, times(1)).updateProfesor(eq(1), any(Profesor.class));
    }

    @Test
    void testUpdateProfesor_ProfesorNotFound() {
        ProfesorDTO profesorDTO = new ProfesorDTO(1, "Carlos", "Lopez", List.of(101, 102));

        when(profesorDao.findById(1)).thenReturn(Optional.empty());

        assertThrows(ProfesorNotFoundException.class, () -> profesorService.updateProfesor(1, profesorDTO));
    }

    @Test
    void testUpdateProfesor_ProfesorExistException() {
        ProfesorDTO profesorDTO = new ProfesorDTO(1, "Juan", "Perez", null);
        Profesor profesorExistente = new Profesor();
        profesorExistente.setId(1);
        profesorExistente.setNombre("Carlos");
        profesorExistente.setApellido("Lopez");

        Profesor profesorDuplicado = new Profesor();
        profesorDuplicado.setId(2);
        profesorDuplicado.setNombre("Juan");
        profesorDuplicado.setApellido("Perez");

        when(profesorDao.findById(1)).thenReturn(Optional.of(profesorExistente));
        when(profesorDao.findAll()).thenReturn(List.of(profesorDuplicado));

        assertThrows(ProfesorExistException.class, () -> profesorService.updateProfesor(1, profesorDTO));
    }

    @Test
    void testDeleteProfesor_Success() {
        when(profesorDao.delete(1)).thenReturn(true);

        boolean resultado = profesorService.deleteProfesor(1);

        assertTrue(resultado);
        verify(profesorDao, times(1)).delete(1);
    }

    @Test
    void testDeleteProfesor_ProfesorNotFound() {
        when(profesorDao.delete(1)).thenReturn(false);

        assertThrows(ProfesorNotFoundException.class, () -> profesorService.deleteProfesor(1));
        verify(profesorDao, times(1)).delete(1);
    }
}
