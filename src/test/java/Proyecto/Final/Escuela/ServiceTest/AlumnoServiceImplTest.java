package Proyecto.Final.Escuela.ServiceTest;

import Proyecto.Final.Escuela.Dtos.AlumnoDTO;
import Proyecto.Final.Escuela.Exception.*;
import Proyecto.Final.Escuela.Model.Alumno;
import Proyecto.Final.Escuela.Model.Carrera;
import Proyecto.Final.Escuela.Model.EstadoMateria;
import Proyecto.Final.Escuela.Persistance.AlumnoDao;
import Proyecto.Final.Escuela.Persistance.CarreraDao;
import Proyecto.Final.Escuela.Service.Implementation.AlumnoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlumnoServiceImplTest {

    @Mock
    private AlumnoDao alumnoDao;

    @Mock
    private CarreraDao carreraDao;

    @InjectMocks
    private AlumnoServiceImpl alumnoService;

    private Alumno alumno;
    private AlumnoDTO alumnoDTO;

    @BeforeEach
    void setUp() {
        alumno = new Alumno();
        alumno.setId(1);
        alumno.setNombre("Juan");
        alumno.setApellido("Perez");
        alumno.setDni("12345678");
        alumno.setCarreraId(1);
        alumno.setMaterias(new HashMap<>());

        alumnoDTO = new AlumnoDTO();
        alumnoDTO.setNombre("Juan");
        alumnoDTO.setApellido("Perez");
        alumnoDTO.setDni("12345678");
        alumnoDTO.setCarreraId(1);
    }

    @Test
    void getAllAlumnos() {
        when(alumnoDao.findAll()).thenReturn(Collections.singletonList(alumno));
        List<AlumnoDTO> resultado = alumnoService.getAllAlumnos();
        assertFalse(resultado.isEmpty());
        verify(alumnoDao, times(1)).findAll();
    }

    @Test
    void getAlumnoById() {
        when(alumnoDao.findById(1)).thenReturn(Optional.of(alumno));
        Optional<AlumnoDTO> resultado = alumnoService.getAlumnoById(1);
        assertTrue(resultado.isPresent());
        assertEquals("Juan", resultado.get().getNombre());
    }

    @Test
    void createAlumno() throws AlumnoExistException {
        when(alumnoDao.findAll()).thenReturn(Collections.emptyList());

        Carrera carrera = new Carrera();
        carrera.setId(1);
        when(carreraDao.findById(1)).thenReturn(Optional.of(carrera));

        when(alumnoDao.save(any(Alumno.class))).thenReturn(alumno);

        AlumnoDTO resultado = alumnoService.createAlumno(alumnoDTO);

        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombre());
    }

    @Test
    void createAlumno_AlumnoExistException() {
        when(alumnoDao.findAll()).thenReturn(Collections.singletonList(alumno));
        assertThrows(AlumnoExistException.class, () -> alumnoService.createAlumno(alumnoDTO));
    }

    @Test
    void createAlumno_CarreraNotFoundException() {
        when(alumnoDao.findAll()).thenReturn(Collections.emptyList());
        when(carreraDao.findById(1)).thenReturn(Optional.empty());

        assertThrows(CarreraNotFoundException.class, () -> alumnoService.createAlumno(alumnoDTO));
    }

    @Test
    void updateAlumno() throws AlumnoExistException, MateriaExistException {
        when(alumnoDao.findById(1)).thenReturn(Optional.of(alumno));
        when(alumnoDao.updateAlumno(eq(1), any(Alumno.class))).thenReturn(alumno);

        AlumnoDTO result = alumnoService.updateAlumno(1, alumnoDTO);
        assertNotNull(result);
        assertEquals("Juan", result.getNombre());
    }

    @Test
    void updateAlumno_AlumnoNotFoundException() {
        when(alumnoDao.findById(1)).thenReturn(Optional.empty());
        assertThrows(AlumnoNotFoundException.class, () -> alumnoService.updateAlumno(1, alumnoDTO));
    }

    @Test
    void updateAlumno_CarreraNotFoundException() {
        AlumnoDTO alumnoDTO = new AlumnoDTO();
        alumnoDTO.setCarreraId(2);

        when(alumnoDao.findById(1)).thenReturn(Optional.of(alumno));
        when(carreraDao.findById(2)).thenReturn(Optional.empty());

        assertThrows(CarreraNotFoundException.class, () -> alumnoService.updateAlumno(1, alumnoDTO));
    }

    @Test
    void updateAlumno_AgregarMateriaExistosamente() throws Exception {
        AlumnoDTO alumnoDTO = new AlumnoDTO();
        Map<Integer, String> materias = new HashMap<>();
        materias.put(101, "APROBADA");
        alumnoDTO.setMaterias(materias);

        Carrera carrera = new Carrera();
        carrera.setId(1);
        carrera.setMaterias(Collections.singletonList(101));

        when(alumnoDao.findById(1)).thenReturn(Optional.of(alumno));
        when(carreraDao.findById(1)).thenReturn(Optional.of(carrera));
        when(alumnoDao.updateAlumno(1, alumno)).thenReturn(alumno);

        AlumnoDTO result = alumnoService.updateAlumno(1, alumnoDTO);

        assertEquals(1, result.getMaterias().size());
        assertEquals("APROBADA", result.getMaterias().get(101));
    }

    @Test
    void updateAlumno_MateriaNotFoundException() {
        AlumnoDTO alumnoDTO = new AlumnoDTO();
        Map<Integer, String> materias = new HashMap<>();
        materias.put(102, "CURSANDO");
        alumnoDTO.setMaterias(materias);

        Carrera carrera = new Carrera();
        carrera.setId(1);
        carrera.setMaterias(Collections.singletonList(101));

        when(alumnoDao.findById(1)).thenReturn(Optional.of(alumno));
        when(carreraDao.findById(1)).thenReturn(Optional.of(carrera));

        assertThrows(MateriaNotFoundException.class, () -> alumnoService.updateAlumno(1, alumnoDTO));
    }

    @Test
    void removeMateriaFromAlumno() {
        Map<Integer, EstadoMateria> materias = new HashMap<>();
        materias.put(101, EstadoMateria.APROBADA);
        alumno.setMaterias(materias);

        when(alumnoDao.findById(1)).thenReturn(Optional.of(alumno));
        when(alumnoDao.updateAlumno(eq(1), any(Alumno.class))).thenReturn(alumno);

        alumnoService.removeMateriaFromAlumno(1, 101);
        assertEquals(0, alumno.getMaterias().size());
    }

    @Test
    void removeMateriaFromAlumno_AlumnoNotFoundException() {
        when(alumnoDao.findById(1)).thenReturn(Optional.empty());
        assertThrows(AlumnoNotFoundException.class, () -> alumnoService.removeMateriaFromAlumno(1, 101));
    }

    @Test
    void removeMateriaFromAlumno_MateriaNotFoundException() {
        when(alumnoDao.findById(1)).thenReturn(Optional.of(alumno));
        assertThrows(MateriaNotFoundException.class, () -> alumnoService.removeMateriaFromAlumno(1, 102));
    }

    @Test
    void deleteAlumno() {
        when(alumnoDao.delete(1)).thenReturn(true);
        boolean result = alumnoService.deleteAlumno(1);
        assertTrue(result);
    }

    @Test
    void deleteAlumno_AlumnoNotFoundException() {
        when(alumnoDao.delete(1)).thenReturn(false);
        assertThrows(AlumnoNotFoundException.class, () -> alumnoService.deleteAlumno(1));
    }

}
