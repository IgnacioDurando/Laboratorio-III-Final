package Proyecto.Final.Escuela.Util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {
    public static ResponseEntity<Object> response(HttpStatus status, Object data, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status.value());
        response.put("data", data);
        response.put("message", message);
        return new ResponseEntity<>(response, status);
    }
}