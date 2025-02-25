package Proyecto.Final.Escuela.ServiceTest;

import Proyecto.Final.Escuela.Dtos.CarreraDTO;
import Proyecto.Final.Escuela.Dtos.CarreraMapper;
import Proyecto.Final.Escuela.Exception.*;
import Proyecto.Final.Escuela.Model.Carrera;
import Proyecto.Final.Escuela.Model.Materia;
import Proyecto.Final.Escuela.Persistance.AlumnoDao;
import Proyecto.Final.Escuela.Persistance.CarreraDao;
import Proyecto.Final.Escuela.Persistance.MateriaDao;
import Proyecto.Final.Escuela.Service.Implementation.CarreraServiceImpl;
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
class CarreraServiceImplTest {

    @Mock
    private CarreraDao carreraDao;
    @Mock
    private MateriaDao materiaDao;

    @Mock
    private AlumnoDao alumnoDao;

    @InjectMocks
    private CarreraServiceImpl carreraService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testGetAllCarreras_ListaVacia() {
        when(carreraDao.findAll()).thenReturn(Collections.emptyList());

        List<CarreraDTO> resultado = carreraService.getAllCarreras();

        assertTrue(resultado.isEmpty());
        verify(carreraDao, times(1)).findAll();
    }

    @Test
    void testGetAllCarreras() {
        CarreraDTO carreraDTO = new CarreraDTO(1, "Tecnicatura en Programacion", List.of(101, 102));
        Carrera carrera = CarreraMapper.toEntity(carreraDTO);

        when(carreraDao.findAll()).thenReturn(List.of(carrera));

        List<CarreraDTO> resultado = carreraService.getAllCarreras();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Tecnicatura en Programacion", resultado.get(0).getNombre());
        verify(carreraDao, times(1)).findAll();
    }

    @Test
    void testGetCarreraById() {
        CarreraDTO carreraDTO = new CarreraDTO(1, "Tecnicatura en Programacion", List.of(101, 102));
        Carrera carrera = CarreraMapper.toEntity(carreraDTO);

        when(carreraDao.findById(1)).thenReturn(Optional.of(carrera));

        Optional<CarreraDTO> resultado = carreraService.getCarreraById(1);

        assertTrue(resultado.isPresent());
        assertEquals("Tecnicatura en Programacion", resultado.get().getNombre());
        verify(carreraDao, times(1)).findById(1);
    }


    @Test
    void testCreateCarrera() throws CarreraExistException {
        CarreraDTO carreraDTO = new CarreraDTO(1, "Tecnicatura en Programacion", List.of(101, 102));
        Carrera carrera = CarreraMapper.toEntity(carreraDTO);

        when(carreraDao.findAll()).thenReturn(Collections.emptyList());
        when(materiaDao.findById(101)).thenReturn(Optional.of(new Materia()));
        when(materiaDao.findById(102)).thenReturn(Optional.of(new Materia()));
        when(carreraDao.save(any(Carrera.class))).thenReturn(carrera);

        CarreraDTO resultado = carreraService.createCarrera(carreraDTO);

        assertNotNull(resultado);
        assertEquals("Tecnicatura en Programacion", resultado.getNombre());
        verify(carreraDao, times(1)).save(any(Carrera.class));
    }

    @Test
    void testCreateCarrera_NombreVacio() {
        CarreraDTO carreraDTO = new CarreraDTO(1, "", List.of(101, 102));

        assertThrows(InvalidDataException.class, () -> carreraService.createCarrera(carreraDTO));
    }

    @Test
    void testCreateCarrera_CarreraExistException() {
        CarreraDTO carreraDTO = new CarreraDTO(1, "Tecnicatura en Programacion", List.of(101, 102));
        Carrera carrera = CarreraMapper.toEntity(carreraDTO);

        when(carreraDao.findAll()).thenReturn(List.of(carrera));

        assertThrows(CarreraExistException.class, () -> carreraService.createCarrera(carreraDTO));
    }

    @Test
    void testCreateCarrera_MateriaNotFoundException() {
        CarreraDTO carreraDTO = new CarreraDTO(1, "Tecnicatura en Programacion", List.of(101, 102));

        when(carreraDao.findAll()).thenReturn(Collections.emptyList());
        when(materiaDao.findById(101)).thenReturn(Optional.empty());
        when(materiaDao.findById(102)).thenReturn(Optional.empty());

        assertThrows(MateriaNotFoundException.class, () -> carreraService.createCarrera(carreraDTO));
    }

    @Test
    void testUpdateCarrera() throws CarreraExistException, MateriaExistException {
        CarreraDTO carreraDTO = new CarreraDTO(1, "Ing Civil", List.of(103));
        Carrera carreraExistente = new Carrera(1, "Ing", new ArrayList<>(List.of(101, 102)));

        when(carreraDao.findById(1)).thenReturn(Optional.of(carreraExistente));
        when(materiaDao.findById(103)).thenReturn(Optional.of(new Materia()));
        when(carreraDao.updateCarrera(eq(1), any(Carrera.class))).thenReturn(carreraExistente);

        CarreraDTO resultado = carreraService.updateCarrera(1, carreraDTO);

        assertNotNull(resultado);
        assertEquals("Ing Civil", resultado.getNombre());
        assertTrue(resultado.getMaterias().contains(103));
        verify(carreraDao, times(1)).updateCarrera(eq(1), any(Carrera.class));
    }


    @Test
    void testUpdateCarrera_CarreraNotFound() {
        when(carreraDao.findById(1)).thenReturn(Optional.empty());

        CarreraDTO carreraDTO = new CarreraDTO(1, "Medicina", List.of(201));

        assertThrows(CarreraNotFoundException.class, () -> carreraService.updateCarrera(1, carreraDTO));
    }

    @Test
    void testUpdateCarrera_NombreDuplicado() {
        CarreraDTO carreraDTO = new CarreraDTO(1, "Arquitectura", List.of(201));
        Carrera carreraExistente = new Carrera(1, "Ing", List.of(101, 102));

        when(carreraDao.findById(1)).thenReturn(Optional.of(carreraExistente));
        when(carreraDao.findAll()).thenReturn(List.of(new Carrera(2, "Arquitectura", List.of(201))));

        assertThrows(CarreraExistException.class, () -> carreraService.updateCarrera(1, carreraDTO));
    }

    @Test
    void testUpdateCarrera_MateriaNotFound() {
        CarreraDTO carreraDTO = new CarreraDTO(1, "Ing Civil", List.of(101, 999));
        Carrera carreraExistente = new Carrera(1, "Ing", List.of(101));

        when(carreraDao.findById(1)).thenReturn(Optional.of(carreraExistente));
        when(materiaDao.findById(101)).thenReturn(Optional.of(new Materia()));
        when(materiaDao.findById(999)).thenReturn(Optional.empty());

        Exception ex = assertThrows(MateriaNotFoundException.class, () -> carreraService.updateCarrera(1, carreraDTO));
        assertTrue(ex.getMessage().contains("999"));
    }


    @Test
    void testUpdateCarrera_MateriaExistException() {
        CarreraDTO carreraDTO = new CarreraDTO(1, "Ing", List.of(101));
        Carrera carreraExistente = new Carrera(1, "Ing", List.of(101));

        when(carreraDao.findById(1)).thenReturn(Optional.of(carreraExistente));
        when(materiaDao.findById(101)).thenReturn(Optional.of(new Materia()));

        assertThrows(MateriaExistException.class, () -> carreraService.updateCarrera(1, carreraDTO));
    }

    @Test
    void testRemoveMateriaFromCarrera() {
        int idCarrera = 1;
        int idMateria = 101;

        Carrera carreraExistente = new Carrera(idCarrera, "Ing", new ArrayList<>(List.of(101, 102)));

        when(carreraDao.findById(idCarrera)).thenReturn(Optional.of(carreraExistente));
        when(carreraDao.updateCarrera(eq(idCarrera), any(Carrera.class))).thenReturn(carreraExistente);

        carreraService.removeMateriaFromCarrera(idCarrera, idMateria);

        assertFalse(carreraExistente.getMaterias().contains(idMateria));
        verify(carreraDao, times(1)).updateCarrera(eq(idCarrera), any(Carrera.class));
    }

    @Test
    void testRemoveMateriaFromCarrera_CarreraNotFound() {
        int idCarrera = 99;
        int idMateria = 101;

        when(carreraDao.findById(idCarrera)).thenReturn(Optional.empty());

        Exception ex = assertThrows(CarreraNotFoundException.class, () -> carreraService.removeMateriaFromCarrera(idCarrera, idMateria));
        assertEquals("La carrera con el ID 99 no fue encontrada.", ex.getMessage());
    }

    @Test
    void testRemoveMateriaFromCarrera_MateriaNotFound() {
        int idCarrera = 2;
        int idMateria = 999;

        Carrera carreraExistente = new Carrera(idCarrera, "Medicina", new ArrayList<>(List.of(201, 202)));

        when(carreraDao.findById(idCarrera)).thenReturn(Optional.of(carreraExistente));

        Exception ex = assertThrows(MateriaNotFoundException.class, () -> carreraService.removeMateriaFromCarrera(idCarrera, idMateria));
        assertEquals("La materia con el ID 999 no se encuentra asignada a esta carrera.", ex.getMessage());
    }

    @Test
    void testDeleteCarrera() {
        int idCarrera = 1;
        Carrera carreraExistente = new Carrera(idCarrera, "Ing", new ArrayList<>(List.of(101, 102)));

        when(carreraDao.findById(idCarrera)).thenReturn(Optional.of(carreraExistente));
        when(carreraDao.delete(idCarrera)).thenReturn(true);

        boolean resultado = carreraService.deleteCarrera(idCarrera);

        assertTrue(resultado);
        verify(carreraDao, times(1)).delete(idCarrera);
    }

    @Test
    void testDeleteCarrera_CarreraNotFound() {
        int idCarrera = 99;

        when(carreraDao.findById(idCarrera)).thenReturn(Optional.empty());

        Exception ex = assertThrows(CarreraNotFoundException.class, () -> carreraService.deleteCarrera(idCarrera));
        assertEquals("La carrera con el ID 99 no fue encontrada.", ex.getMessage());
    }
}
