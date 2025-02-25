package Proyecto.Final.Escuela.Dtos;

import java.util.List;

public class CarreraDTO {
    private int id;
    private String nombre;
    private List<Integer> materias;

    public CarreraDTO() {}

    public CarreraDTO(int id, String nombre, List<Integer> materias) {
        this.id = id;
        this.nombre = nombre;
        this.materias = materias;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public List<Integer> getMaterias() { return materias; }
    public void setMaterias(List<Integer> materias) { this.materias = materias; }
}
