package Proyecto.Final.Escuela.Dtos;

import java.util.List;

public class MateriaDTO {
    private int id;
    private String nombre;
    private String anio;
    private String cuatrimestre;
    private Integer profesorId;
    private List<Integer> correlatividades;

    public MateriaDTO() {}

    public MateriaDTO(int id, String nombre, String anio, String cuatrimestre, Integer profesorId, List<Integer> correlatividades) {
        this.id = id;
        this.nombre = nombre;
        this.anio = anio;
        this.cuatrimestre = cuatrimestre;
        this.profesorId = profesorId;
        this.correlatividades = correlatividades;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getAnio() { return anio; }
    public void setAnio(String anio) { this.anio = anio; }

    public String getCuatrimestre() { return cuatrimestre; }
    public void setCuatrimestre(String cuatrimestre) { this.cuatrimestre = cuatrimestre; }

    public Integer getProfesorId() { return profesorId; }
    public void setProfesorId(Integer profesorId) { this.profesorId = profesorId; }

    public List<Integer> getCorrelatividades() { return correlatividades; }
    public void setCorrelatividades(List<Integer> correlatividades) { this.correlatividades = correlatividades; }
}
