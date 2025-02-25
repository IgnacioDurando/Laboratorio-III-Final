package Proyecto.Final.Escuela.Exception;

public class MateriaNotFoundException extends RuntimeException {
    public MateriaNotFoundException(String message) {
        super(message);
    }
}