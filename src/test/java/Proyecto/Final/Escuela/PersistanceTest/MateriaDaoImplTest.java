package Proyecto.Final.Escuela.PersistanceTest;

import Proyecto.Final.Escuela.Model.Materia;
import Proyecto.Final.Escuela.Persistance.Implementation.MateriaDaoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class MateriaDaoImplTest {

    private MateriaDaoImpl materiaDao;

    @BeforeEach
    public void setUp() {
        materiaDao = new MateriaDaoImpl();
    }

    @Test
    public void testFindAll() {
        List<Materia> materias = materiaDao.findAll();
        assertTrue(materias.isEmpty());

        Materia materia = new Materia();
        materia.setNombre("Biologia");
        materia.setAnio("1");
        materia.setCuatrimestre("2");
        materiaDao.save(materia);

        materias = materiaDao.findAll();
        assertEquals(1, materias.size());
    }

    @Test
    public void testFindById() {
        Materia materia = new Materia();
        materia.setNombre("Historia");
        materia.setAnio("1");
        materia.setCuatrimestre("2");
        Materia savedMateria = materiaDao.save(materia);

        Optional<Materia> found = materiaDao.findById(savedMateria.getId());
        assertTrue(found.isPresent());
        assertEquals("Historia", found.get().getNombre());
    }

    @Test
    public void testFindById_MateriaNotFound() {
        Optional<Materia> found = materiaDao.findById(999);
        assertFalse(found.isPresent());
    }

    @Test
    public void testSaveNewMateria() {
        Materia materia = new Materia();
        materia.setNombre("Matematicas");
        materia.setAnio("1");
        materia.setCuatrimestre("1");
        Materia savedMateria = materiaDao.save(materia);
        assertNotEquals(0, savedMateria.getId());
        assertEquals("Matematicas", savedMateria.getNombre());
    }

    @Test
    public void testUpdateMateria_MateriaExist() {
        Materia materia = new Materia();
        materia.setNombre("Fisica");
        materia.setAnio("1");
        materia.setCuatrimestre("1");
        Materia savedMateria = materiaDao.save(materia);

        Materia updatedMateria = new Materia();
        updatedMateria.setId(savedMateria.getId());
        updatedMateria.setNombre("Fisica Avanzada");
        updatedMateria.setAnio("1");
        updatedMateria.setCuatrimestre("2");

        Materia result = materiaDao.updateMateria(savedMateria.getId(), updatedMateria);
        assertNotNull(result);
        assertEquals("Fisica Avanzada", result.getNombre());
        assertEquals("1", result.getAnio());
    }

    @Test
    public void testUpdateMateria_MateriaNotFound() {
        Materia materia = new Materia();
        materia.setId(999);
        materia.setNombre("Inexistente");
        materia.setAnio("2");
        materia.setCuatrimestre("1");

        Materia result = materiaDao.updateMateria(999, materia);
        assertNull(result);
    }

    @Test
    public void testDeleteMateria() {
        Materia materia = new Materia();
        materia.setNombre("Quimica");
        materia.setAnio("1");
        materia.setCuatrimestre("1");
        Materia savedMateria = materiaDao.save(materia);

        boolean deleted = materiaDao.delete(savedMateria.getId());
        assertTrue(deleted);
        Optional<Materia> found = materiaDao.findById(savedMateria.getId());
        assertFalse(found.isPresent());
    }

    @Test
    public void testDeleteMateria_MateriaNotFound() {
        boolean deleted = materiaDao.delete(999);
        assertFalse(deleted);
    }

}
