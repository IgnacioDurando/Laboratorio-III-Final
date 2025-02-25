package Proyecto.Final.Escuela.Dtos;

import java.util.List;

public class ProfesorDTO {
    private int id;
    private String nombre;
    private String apellido;
    private List<Integer> materiasDictadas;

    public ProfesorDTO() {}

    public ProfesorDTO(int id, String nombre, String apellido, List<Integer> materiasDictadas) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.materiasDictadas = materiasDictadas;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public List<Integer> getMateriasDictadas() { return materiasDictadas; }
    public void setMateriasDictadas(List<Integer> materiasDictadas) { this.materiasDictadas = materiasDictadas; }
}
