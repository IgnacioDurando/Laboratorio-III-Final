package Proyecto.Final.Escuela.Exception;

public class AlumnoNotFoundException extends RuntimeException {
    public AlumnoNotFoundException(String message) {
        super(message);
    }
}