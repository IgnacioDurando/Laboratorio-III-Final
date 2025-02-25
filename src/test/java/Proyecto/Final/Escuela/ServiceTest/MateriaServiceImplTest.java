package Proyecto.Final.Escuela.ServiceTest;

import Proyecto.Final.Escuela.Dtos.MateriaDTO;
import Proyecto.Final.Escuela.Persistance.AlumnoDao;
import Proyecto.Final.Escuela.Persistance.CarreraDao;
import Proyecto.Final.Escuela.Persistance.MateriaDao;
import Proyecto.Final.Escuela.Persistance.ProfesorDao;
import Proyecto.Final.Escuela.Service.Implementation.MateriaServiceImpl;
import Proyecto.Final.Escuela.Exception.*;
import Proyecto.Final.Escuela.Dtos.MateriaMapper;
import Proyecto.Final.Escuela.Model.Materia;
import Proyecto.Final.Escuela.Model.Profesor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class MateriaServiceImplTest {

    @Mock
    private MateriaDao materiaDao;
    @Mock
    private ProfesorDao profesorDao;
    @Mock
    private CarreraDao carreraDao;
    @Mock
    private AlumnoDao alumnoDao;
    @InjectMocks
    private MateriaServiceImpl materiaService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testGetAllMaterias_ListaVacia() {
        when(materiaDao.findAll()).thenReturn(Collections.emptyList());

        List<MateriaDTO> resultado = materiaService.getAllMaterias();

        assertTrue(resultado.isEmpty());
        verify(materiaDao, times(1)).findAll();
    }

    @Test
    void testGetAllMaterias() {
        MateriaDTO materiaDTO = new MateriaDTO(1, "Matematicas", "1", "1", 10, List.of(2, 3));
        when(materiaDao.findAll()).thenReturn(List.of(MateriaMapper.toEntity(materiaDTO)));

        List<MateriaDTO> resultado = materiaService.getAllMaterias();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Matematicas", resultado.get(0).getNombre());
        verify(materiaDao, times(1)).findAll();
    }

    @Test
    void testGetMateriaById() {
        MateriaDTO materiaDTO = new MateriaDTO(1, "Matematicas", "1", "1", 10, List.of(2, 3));
        when(materiaDao.findById(1)).thenReturn(Optional.of(MateriaMapper.toEntity(materiaDTO)));

        Optional<MateriaDTO> resultado = materiaService.getMateriaById(1);

        assertTrue(resultado.isPresent());
        assertEquals("Matematicas", resultado.get().getNombre());
        verify(materiaDao, times(1)).findById(1);
    }

    @Test
    void testCreateMateria() throws MateriaExistException {
        MateriaDTO materiaDTO = new MateriaDTO(1,"Matematicas", "1", "1", 10, List.of(2, 3));
        Materia materia = MateriaMapper.toEntity(materiaDTO);
        Profesor profesor = new Profesor();
        profesor.setId(10);

        when(materiaDao.findAll()).thenReturn(Collections.emptyList());
        when(profesorDao.findById(10)).thenReturn(Optional.of(profesor));
        when(materiaDao.save(any(Materia.class))).thenReturn(materia);
        when(materiaDao.findById(2)).thenReturn(Optional.of(new Materia()));
        when(materiaDao.findById(3)).thenReturn(Optional.of(new Materia()));

        MateriaDTO resultado = materiaService.createMateria(materiaDTO);

        assertNotNull(resultado);
        assertEquals("Matematicas", resultado.getNombre());
        verify(materiaDao, times(1)).save(any(Materia.class));
        verify(profesorDao, times(1)).updateProfesor(eq(10), any(Profesor.class));
    }


    @Test
    void testCreateMateria_MateriaExistException() {
        MateriaDTO materiaDTO = new MateriaDTO(1, "Matematicas", "1", "1", 10, List.of(2, 3));
        Materia materia = MateriaMapper.toEntity(materiaDTO);

        when(materiaDao.findAll()).thenReturn(List.of(materia));

        assertThrows(MateriaExistException.class, () -> materiaService.createMateria(materiaDTO));
    }

    @Test
    void testCreateMateria_ProfesorNotFoundException() {
        MateriaDTO materiaDTO = new MateriaDTO(1, "Matematicas", "1", "1", 10, List.of(2, 3));

        when(materiaDao.findAll()).thenReturn(Collections.emptyList());
        when(profesorDao.findById(10)).thenReturn(Optional.empty());

        assertThrows(ProfesorNotFoundException.class, () -> materiaService.createMateria(materiaDTO));
    }

    @Test
    void testCreateMateria_InvalidDataException_NombreVacio() {
        MateriaDTO materiaDTO = new MateriaDTO(1, "", "1", "1", 10, List.of(2, 3));

        assertThrows(InvalidDataException.class, () -> materiaService.createMateria(materiaDTO));
    }

    @Test
    void testCreateMateria_InvalidDataException_SinProfesor() {
        MateriaDTO materiaDTO = new MateriaDTO(1, "Matematicas", "1", "1", null, List.of(2, 3));

        assertThrows(InvalidDataException.class, () -> materiaService.createMateria(materiaDTO));
    }

    @Test
    void testUpdateMateria() throws MateriaExistException {
        MateriaDTO materiaDTO = new MateriaDTO(1,"Matematicas II", "2025", "2", 10, List.of(2, 3));
        Materia materiaExistente = new Materia();
        materiaExistente.setNombre("Matematicas");

        when(materiaDao.findById(1)).thenReturn(Optional.of(materiaExistente));
        when(materiaDao.findById(2)).thenReturn(Optional.of(new Materia()));
        when(materiaDao.findById(3)).thenReturn(Optional.of(new Materia()));
        when(profesorDao.findById(10)).thenReturn(Optional.of(new Profesor()));
        when(materiaDao.updateMateria(eq(1), any(Materia.class))).thenReturn(materiaExistente);

        MateriaDTO resultado = materiaService.updateMateria(1, materiaDTO);

        assertNotNull(resultado);
        assertEquals("Matematicas II", resultado.getNombre());
        verify(materiaDao, times(1)).updateMateria(eq(1), any(Materia.class));
    }

    @Test
    void testUpdateMateria_MateriaNotFoundException() {
        MateriaDTO materiaDTO = new MateriaDTO(1, "Matematicas II", "2", "2", 10, List.of(2, 3));
        when(materiaDao.findById(1)).thenReturn(Optional.empty());

        assertThrows(MateriaNotFoundException.class, () -> materiaService.updateMateria(1, materiaDTO));
    }

    @Test
    void testUpdateMateria_MateriaExistException() {
        MateriaDTO materiaDTO = new MateriaDTO(1, "Matematicas II", "2", "2", 10, List.of(2, 3));
        Materia materiaExistente = new Materia();
        materiaExistente.setId(1);
        materiaExistente.setNombre("Matemáticas");

        when(materiaDao.findById(1)).thenReturn(Optional.of(materiaExistente));
        when(materiaDao.findAll()).thenReturn(List.of(new Materia(2, "Matematicas II", "2", "2", 10, List.of(2, 3))));

        assertThrows(MateriaExistException.class, () -> materiaService.updateMateria(1, materiaDTO));
    }

    @Test
    void testUpdateMateria_InvalidDataException_CorrelativaMisma() {
        MateriaDTO materiaDTO = new MateriaDTO(1, "Matematicas", "2", "2", 10, List.of(1));
        Materia materiaExistente = new Materia();
        materiaExistente.setNombre("Matematicas");

        when(materiaDao.findById(1)).thenReturn(Optional.of(materiaExistente));

        assertThrows(InvalidDataException.class, () -> materiaService.updateMateria(1, materiaDTO));
    }

    @Test
    void testUpdateMateria_MateriaNotFound_Correlativa() {
        MateriaDTO materiaDTO = new MateriaDTO(1, "Matematicas", "2", "2", 10, List.of(99));
        Materia materiaExistente = new Materia();
        materiaExistente.setNombre("Matematicas");

        when(materiaDao.findById(1)).thenReturn(Optional.of(materiaExistente));
        when(materiaDao.findById(99)).thenReturn(Optional.empty());

        assertThrows(MateriaNotFoundException.class, () -> materiaService.updateMateria(1, materiaDTO));
    }


    @Test
    void testRemoveProfesorFromMateria() {
        Materia materia = new Materia();
        materia.setProfesorId(10);
        Profesor profesor = new Profesor();
        profesor.setId(10);

        when(materiaDao.findById(1)).thenReturn(Optional.of(materia));
        when(profesorDao.findById(10)).thenReturn(Optional.of(profesor));

        materiaService.removeProfesorFromMateria(1, 10);

        assertNull(materia.getProfesorId());
        verify(materiaDao, times(1)).updateMateria(eq(1), any(Materia.class));
        verify(profesorDao, times(1)).updateProfesor(eq(10), any(Profesor.class));
    }

    @Test
    void testRemoveProfesorFromMateria_MateriaNotFoundException() {
        when(materiaDao.findById(1)).thenReturn(Optional.empty());

        assertThrows(MateriaNotFoundException.class, () -> materiaService.removeProfesorFromMateria(1, 10));
    }

    @Test
    void testRemoveProfesorFromMateria_ProfesorNotFoundException() {
        Materia materia = new Materia();
        materia.setProfesorId(10);
        when(materiaDao.findById(1)).thenReturn(Optional.of(materia));
        when(profesorDao.findById(10)).thenReturn(Optional.empty());

        assertThrows(ProfesorNotFoundException.class, () -> materiaService.removeProfesorFromMateria(1, 10));
    }

    @Test
    void testRemoveProfesorFromMateria_ProfesorNoAsignado() {
        Materia materia = new Materia();
        materia.setProfesorId(20);
        Profesor profesor = new Profesor();
        profesor.setId(10);
        when(materiaDao.findById(1)).thenReturn(Optional.of(materia));
        when(profesorDao.findById(10)).thenReturn(Optional.of(profesor));

        assertThrows(ProfesorNotFoundException.class, () -> materiaService.removeProfesorFromMateria(1, 10));
    }

    @Test
    void testDeleteMateria() {
        Materia materia = new Materia();
        materia.setId(1);
        materia.setProfesorId(10);

        Profesor profesor = new Profesor();
        profesor.setId(10);

        when(materiaDao.findById(1)).thenReturn(Optional.of(materia));
        when(profesorDao.findById(10)).thenReturn(Optional.of(profesor));
        when(materiaDao.delete(1)).thenReturn(true);

        boolean resultado = materiaService.deleteMateria(1);

        assertTrue(resultado);
        verify(materiaDao, times(1)).delete(1);
        verify(profesorDao, times(1)).updateProfesor(eq(10), any(Profesor.class));
        verify(carreraDao, times(1)).findAll();
        verify(alumnoDao, times(1)).findAll();
        verify(materiaDao, times(1)).findAll();
    }

    @Test
    void testDeleteMateria_MateriaNotFoundException() {
        when(materiaDao.findById(1)).thenReturn(Optional.empty());

        assertThrows(MateriaNotFoundException.class, () -> materiaService.deleteMateria(1));
    }

}