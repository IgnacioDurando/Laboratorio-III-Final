package Proyecto.Final.Escuela.Dtos;

import java.util.Map;
import java.util.HashMap;
import Proyecto.Final.Escuela.Model.Alumno;
import Proyecto.Final.Escuela.Model.EstadoMateria;

public class AlumnoMapper {

    public static AlumnoDTO toDTO(Alumno alumno) {

        Map<Integer, String> materiasDTO = new HashMap<>();
        if (alumno.getMaterias() != null) {
            alumno.getMaterias().forEach((id, estado) -> materiasDTO.put(id, estado.name()));
        }

        return new AlumnoDTO(
                alumno.getId(),
                alumno.getNombre(),
                alumno.getApellido(),
                alumno.getDni(),
                alumno.getEdad(),
                alumno.getCarreraId(),
                materiasDTO
        );
    }

    public static Alumno toEntity(AlumnoDTO dto) {

        Map<Integer, EstadoMateria> materiasEntity = new HashMap<>();
        if (dto.getMaterias() != null) {
            dto.getMaterias().forEach((id, estado) -> {
                try {
                    materiasEntity.put(id, EstadoMateria.valueOf(estado));
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("EstadoMateria inválido: " + estado);
                }
            });
        }

        Alumno alumno = new Alumno();
        alumno.setId(dto.getId());
        alumno.setNombre(dto.getNombre());
        alumno.setApellido(dto.getApellido());
        alumno.setDni(dto.getDni());
        alumno.setEdad(dto.getEdad());
        alumno.setCarreraId(dto.getCarreraId());
        alumno.setMaterias(materiasEntity);
        return alumno;
    }
}
