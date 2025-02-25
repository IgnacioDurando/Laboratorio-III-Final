package Proyecto.Final.Escuela.Dtos;

import java.util.Map;

public class AlumnoDTO {
    private int id;
    private String nombre;
    private String apellido;
    private String dni;
    private String edad;

    private Integer carreraId;
    private Map<Integer, String> materias;

    public AlumnoDTO() {}

    public AlumnoDTO(int id, String nombre, String apellido, String dni, String edad, Integer carreraId, Map<Integer, String> materias) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.edad = edad;
        this.carreraId = carreraId;
        this.materias = materias;
    }

    public AlumnoDTO(int id, String nombre, Integer carreraId) {
        this.id = id;
        this.nombre = nombre;
        this.carreraId = carreraId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public Integer getCarreraId() { return carreraId; }
    public void setCarreraId(Integer carreraId) { this.carreraId = carreraId; }

    public Map<Integer, String> getMaterias() {
        return materias;
    }

    public void setMaterias(Map<Integer, String> materias) {
        this.materias = materias;
    }
}

