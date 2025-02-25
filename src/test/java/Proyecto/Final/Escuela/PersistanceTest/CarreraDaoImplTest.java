package Proyecto.Final.Escuela.PersistanceTest;

import Proyecto.Final.Escuela.Model.Carrera;
import Proyecto.Final.Escuela.Persistance.Implementation.CarreraDaoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class CarreraDaoImplTest {

    private CarreraDaoImpl carreraDao;

    @BeforeEach
    public void setUp() {
        carreraDao = new CarreraDaoImpl();
    }

    @Test
    public void testFindAll() {
        List<Carrera> carreras = carreraDao.findAll();
        assertTrue(carreras.isEmpty());

        Carrera carrera = new Carrera();
        carrera.setNombre("Tecnicatura en Programacion");
        carreraDao.save(carrera);

        carreras = carreraDao.findAll();
        assertEquals(1, carreras.size());
    }

    @Test
    public void testFindById() {
        Carrera carrera = new Carrera();
        carrera.setNombre("Ingenieria Civil");
        Carrera savedCarrera = carreraDao.save(carrera);

        Optional<Carrera> found = carreraDao.findById(savedCarrera.getId());
        assertTrue(found.isPresent());
        assertEquals("Ingenieria Civil", found.get().getNombre());
    }

    @Test
    public void testFindById_CarreraNotFound() {
        Optional<Carrera> found = carreraDao.findById(999);
        assertFalse(found.isPresent());
    }

    @Test
    public void testSaveNewCarrera() {
        Carrera carrera = new Carrera();
        carrera.setNombre("Arquitectura");
        Carrera savedCarrera = carreraDao.save(carrera);
        assertNotEquals(0, savedCarrera.getId());
        assertEquals("Arquitectura", savedCarrera.getNombre());
    }

    @Test
    public void testUpdateCarrera_CarreraExist() {
        Carrera carrera = new Carrera();
        carrera.setNombre("Economia");
        Carrera savedCarrera = carreraDao.save(carrera);

        Carrera updatedCarrera = new Carrera();
        updatedCarrera.setId(savedCarrera.getId());
        updatedCarrera.setNombre("Economia II");

        Carrera result = carreraDao.updateCarrera(savedCarrera.getId(), updatedCarrera);
        assertNotNull(result);
        assertEquals("Economia II", result.getNombre());
    }

    @Test
    public void testUpdateCarrera_CarreraNotFound() {
        Carrera carrera = new Carrera();
        carrera.setId(999);
        carrera.setNombre("Psicologia");

        Carrera result = carreraDao.updateCarrera(999, carrera);
        assertNull(result);
    }

    @Test
    public void testDeleteCarrera() {
        Carrera carrera = new Carrera();
        carrera.setNombre("Medicina");
        Carrera savedCarrera = carreraDao.save(carrera);

        boolean deleted = carreraDao.delete(savedCarrera.getId());
        assertTrue(deleted);
        Optional<Carrera> found = carreraDao.findById(savedCarrera.getId());
        assertFalse(found.isPresent());
    }

    @Test
    public void testDeleteCarrera_CarreraNotFound() {
        boolean deleted = carreraDao.delete(999);
        assertFalse(deleted);
    }
}
