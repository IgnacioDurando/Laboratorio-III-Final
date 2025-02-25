package Proyecto.Final.Escuela.Model;

import java.util.HashMap;
import java.util.Map;

public class Alumno {
    private int id;
    private String nombre;
    private String apellido;
    private String edad;
    private String dni;
    private Integer carreraId;
    private Map<Integer, EstadoMateria> materias = new HashMap<>();

    public Alumno(int id, String nombre, String apellido, String edad, String dni, Map<Integer, EstadoMateria> materias) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.dni = dni;
        this.materias = materias;
    }

    public Alumno() {
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public String getNombre() {

        return nombre;
    }

    public void setNombre(String nombre) {

        this.nombre = nombre;
    }

    public String getApellido() {

        return apellido;
    }

    public void setApellido(String apellido) {

        this.apellido = apellido;
    }

    public String getEdad() {

        return edad;
    }

    public void setEdad(String edad) {

        this.edad = edad;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {

        this.dni = dni;
    }

    public Integer getCarreraId() {
        return carreraId;
    }

    public void setCarreraId(Integer carreraId) {
        this.carreraId = carreraId;
    }

    public Map<Integer, EstadoMateria> getMaterias() {
        return materias;
    }

    public void setMaterias(Map<Integer, EstadoMateria> materias) {
        this.materias = materias;
    }

    @Override
    public String toString() {
        return "Alumno{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", edad=" + edad +
                ", dni='" + dni + '\'' +
                ", materias=" + materias +
                '}';
    }
}
