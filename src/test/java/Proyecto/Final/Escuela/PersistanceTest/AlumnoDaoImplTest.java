package Proyecto.Final.Escuela.PersistanceTest;

import Proyecto.Final.Escuela.Model.Alumno;
import Proyecto.Final.Escuela.Persistance.Implementation.AlumnoDaoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class AlumnoDaoImplTest {

    private AlumnoDaoImpl alumnoDao;

    @BeforeEach
    public void setUp() {
        alumnoDao = new AlumnoDaoImpl();
    }


    @Test
    public void testFindAll() {
        List<Alumno> alumnos = alumnoDao.findAll();
        assertTrue(alumnos.isEmpty());

        Alumno alumno = new Alumno();
        alumno.setNombre("Roberto");
        alumno.setApellido("Carlos");
        alumno.setDni("33333333");
        alumno.setEdad("24");
        alumnoDao.save(alumno);

        alumnos = alumnoDao.findAll();
        assertEquals(1, alumnos.size());
    }

    @Test
    public void testFindById() {
        Alumno alumno = new Alumno();
        alumno.setNombre("Papu");
        alumno.setApellido("Gomez");
        alumno.setDni("87654321");
        alumno.setEdad("22");
        Alumno savedAlumno = alumnoDao.save(alumno);

        Optional<Alumno> found = alumnoDao.findById(savedAlumno.getId());
        assertTrue(found.isPresent());
        assertEquals("Papu", found.get().getNombre());
    }

    @Test
    public void testFindById_AlumnoNotFound() {
        Optional<Alumno> found = alumnoDao.findById(999);
        assertFalse(found.isPresent());
    }

    @Test
    public void testSaveNewAlumno(){
        Alumno alumno = new Alumno();
        alumno.setNombre("Emiliano");
        alumno.setApellido("Martinez");
        alumno.setDni("12345678");
        alumno.setEdad("20");
        Alumno savedAlumno = alumnoDao.save(alumno);
        assertNotEquals(0, savedAlumno.getId());
        assertEquals("Emiliano", savedAlumno.getNombre());
    }

    @Test
    public void testUpdateAlumno() {
        Alumno alumno = new Alumno();
        alumno.setNombre("Julian");
        alumno.setApellido("Alvarez");
        alumno.setDni("11111111");
        alumno.setEdad("25");
        Alumno savedAlumno = alumnoDao.save(alumno);

        Alumno updatedAlumno = new Alumno();
        updatedAlumno.setId(savedAlumno.getId());
        updatedAlumno.setNombre("Enzo");
        updatedAlumno.setApellido("Fernandez");
        updatedAlumno.setDni("11111111");
        updatedAlumno.setEdad("26");

        Alumno result = alumnoDao.updateAlumno(savedAlumno.getId(), updatedAlumno);
        assertNotNull(result);
        assertEquals("Enzo", result.getNombre());
        assertEquals("26", result.getEdad());
    }

    @Test
    public void testUpdateAlumno_AlumnoNotFound() {
        Alumno alumno = new Alumno();
        alumno.setId(999);
        alumno.setNombre("Inexistente");
        alumno.setApellido("Test");
        alumno.setDni("00000000");
        alumno.setEdad("0");

        Alumno result = alumnoDao.updateAlumno(999, alumno);
        assertNull(result);
    }

    @Test
    public void testDeleteAlumno() {
        Alumno alumno = new Alumno();
        alumno.setNombre("Lautaro");
        alumno.setApellido("Martinez");
        alumno.setDni("22222222");
        alumno.setEdad("23");
        Alumno savedAlumno = alumnoDao.save(alumno);

        boolean deleted = alumnoDao.delete(savedAlumno.getId());
        assertTrue(deleted);
        Optional<Alumno> found = alumnoDao.findById(savedAlumno.getId());
        assertFalse(found.isPresent());
    }

    @Test
    public void testDeleteAlumno_AlumnoNotFound() {
        boolean deleted = alumnoDao.delete(999);
        assertFalse(deleted);
    }

}
