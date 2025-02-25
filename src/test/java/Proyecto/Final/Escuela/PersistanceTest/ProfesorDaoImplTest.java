package Proyecto.Final.Escuela.PersistanceTest;

import Proyecto.Final.Escuela.Model.Profesor;
import Proyecto.Final.Escuela.Persistance.Implementation.ProfesorDaoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ProfesorDaoImplTest {

    private ProfesorDaoImpl profesorDao;

    @BeforeEach
    public void setUp() {
        profesorDao = new ProfesorDaoImpl();
    }

    @Test
    public void testFindAll() {
        List<Profesor> profesores = profesorDao.findAll();
        assertTrue(profesores.isEmpty());

        Profesor profesor = new Profesor();
        profesor.setNombre("Lionel");
        profesor.setApellido("Scaloni");
        profesorDao.save(profesor);

        profesores = profesorDao.findAll();
        assertEquals(1, profesores.size());
    }

    @Test
    public void testFindById() {
        Profesor profesor = new Profesor();
        profesor.setNombre("Fernando");
        profesor.setApellido("Durando");
        Profesor savedProfesor = profesorDao.save(profesor);

        Optional<Profesor> found = profesorDao.findById(savedProfesor.getId());
        assertTrue(found.isPresent());
        assertEquals("Fernando", found.get().getNombre());
    }

    @Test
    public void testFindById_ProfesorNotFound() {
        Optional<Profesor> found = profesorDao.findById(999);
        assertFalse(found.isPresent());
    }

    @Test
    public void testSaveNewProfesor() {
        Profesor profesor = new Profesor();
        profesor.setNombre("Juan");
        profesor.setApellido("Perez");
        Profesor savedProfesor = profesorDao.save(profesor);
        assertNotEquals(0, savedProfesor.getId());
        assertEquals("Juan", savedProfesor.getNombre());
    }

    @Test
    public void testUpdateProfesor() {
        Profesor profesor = new Profesor();
        profesor.setNombre("Carlos");
        profesor.setApellido("Gonzalez");
        Profesor savedProfesor = profesorDao.save(profesor);

        Profesor updatedProfesor = new Profesor();
        updatedProfesor.setId(savedProfesor.getId());
        updatedProfesor.setNombre("Facundo");
        updatedProfesor.setApellido("Gonzalez");

        Profesor result = profesorDao.updateProfesor(savedProfesor.getId(), updatedProfesor);
        assertNotNull(result);
        assertEquals("Facundo", result.getNombre());
    }

    @Test
    public void testUpdateProfesor_ProfesorNotFound() {
        Profesor profesor = new Profesor();
        profesor.setId(999);
        profesor.setNombre("Inexistente");

        Profesor result = profesorDao.updateProfesor(999, profesor);
        assertNull(result);
    }

    @Test
    public void testDeleteProfesor() {
        Profesor profesor = new Profesor();
        profesor.setNombre("Lucia");
        profesor.setApellido("Martinez");
        Profesor savedProfesor = profesorDao.save(profesor);

        boolean deleted = profesorDao.delete(savedProfesor.getId());
        assertTrue(deleted);
        Optional<Profesor> found = profesorDao.findById(savedProfesor.getId());
        assertFalse(found.isPresent());
    }

    @Test
    public void testDeleteProfesor_ProfesorNotFound() {
        boolean deleted = profesorDao.delete(999);
        assertFalse(deleted);
    }

}
