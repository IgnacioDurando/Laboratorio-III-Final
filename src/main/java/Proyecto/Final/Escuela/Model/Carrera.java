package Proyecto.Final.Escuela.Model;

import java.util.ArrayList;
import java.util.List;

public class Carrera {
    private int id;
    private String nombre;
    private List<Integer> materias;

    public Carrera(int id, String nombre, List<Integer> materias) {
        this.id = id;
        this.nombre = nombre;
        this.materias = materias != null ? materias : new ArrayList<>();
    }

    public Carrera() {

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

    public List<Integer> getMaterias() {
        return materias;
    }

    public void setMaterias(List<Integer> materias) {
        this.materias = materias;
    }

    public void agregarMateria(int materiaId) {
        if (!materias.contains(materiaId)) {
            materias.add(materiaId);
        }
    }


    @Override
    public String toString() {
        return "Carrera{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", materias=" + materias +
                '}';
    }
}
