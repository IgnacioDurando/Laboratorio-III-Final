package Proyecto.Final.Escuela.Model;

import java.util.ArrayList;
import java.util.List;

public class Profesor {
    private int id;
    private String nombre;
    private String apellido;
    private List<Integer> materiasDictadas = new ArrayList<>();

    public Profesor(int id, String nombre, String apellido) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public Profesor() {
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


    public void agregarMateria(int materiaId) {
        if (!materiasDictadas.contains(materiaId)){
            this.materiasDictadas.add(materiaId);
        }
    }

    public List<Integer> getMateriasDictadas() {
        return materiasDictadas;
    }

    @Override
    public String toString() {
        return "Profesor{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", materiasDictadas=" + materiasDictadas +
                '}';
    }

}
