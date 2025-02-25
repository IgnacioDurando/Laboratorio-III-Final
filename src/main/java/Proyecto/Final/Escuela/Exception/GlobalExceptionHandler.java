package Proyecto.Final.Escuela.Exception;

import Proyecto.Final.Escuela.Util.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AlumnoExistException.class)
    public ResponseEntity<Object> handleAlumnoExistException(AlumnoExistException e) {
        return ResponseHandler.response(HttpStatus.BAD_REQUEST, null, e.getMessage());
    }

    @ExceptionHandler(AlumnoNotFoundException.class)
    public ResponseEntity<Object> handleAlumnoNotFoundException(AlumnoNotFoundException e) {
        return ResponseHandler.response(HttpStatus.NOT_FOUND, null, e.getMessage());
    }

    @ExceptionHandler(MateriaExistException.class)
    public ResponseEntity<Object> handleMateriaExistException(MateriaExistException e) {
        return ResponseHandler.response(HttpStatus.BAD_REQUEST, null, e.getMessage());
    }

    @ExceptionHandler(MateriaNotFoundException.class)
    public ResponseEntity<Object> handleMateriaNotFoundException(MateriaNotFoundException e) {
        return ResponseHandler.response(HttpStatus.NOT_FOUND, null, e.getMessage());
    }

    @ExceptionHandler(CarreraExistException.class)
    public ResponseEntity<Object> handleCarreraExistException(CarreraExistException e) {
        return ResponseHandler.response(HttpStatus.BAD_REQUEST, null, e.getMessage());
    }

    @ExceptionHandler(CarreraNotFoundException.class)
    public ResponseEntity<Object> handleCarreraNotFoundException(CarreraNotFoundException e) {
        return ResponseHandler.response(HttpStatus.NOT_FOUND, null, e.getMessage());
    }

    @ExceptionHandler(ProfesorExistException.class)
    public ResponseEntity<Object> handleProfesorExistException(ProfesorExistException e) {
        return ResponseHandler.response(HttpStatus.BAD_REQUEST, null, e.getMessage());
    }

    @ExceptionHandler(ProfesorNotFoundException.class)
    public ResponseEntity<Object> handleProfesorNotFoundException(ProfesorNotFoundException e) {
        return ResponseHandler.response(HttpStatus.NOT_FOUND, null, e.getMessage());
    }
    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<Object> handleInvalidDataException(InvalidDataException e) {
        return ResponseHandler.response(HttpStatus.BAD_REQUEST, null, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String message = "Error en el tipo de dato del parámetro: " + e.getName();
        return ResponseHandler.response(HttpStatus.BAD_REQUEST, null, message);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception e) {
        return ResponseHandler.response(HttpStatus.INTERNAL_SERVER_ERROR, null, "Error interno del servidor.");
    }
}
