package Proyecto.Final.Escuela.Dtos;

import Proyecto.Final.Escuela.Model.Profesor;

public class ProfesorMapper {

    public static ProfesorDTO toDTO(Profesor profesor) {
        return new ProfesorDTO(
                profesor.getId(),
                profesor.getNombre(),
                profesor.getApellido(),
                profesor.getMateriasDictadas()
        );
    }

    public static Profesor toEntity(ProfesorDTO dto) {
        Profesor profesor = new Profesor();
        profesor.setId(dto.getId());
        profesor.setNombre(dto.getNombre());
        profesor.setApellido(dto.getApellido());
        return profesor;
    }
}
