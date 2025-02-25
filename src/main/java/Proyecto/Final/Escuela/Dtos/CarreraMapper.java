package Proyecto.Final.Escuela.Dtos;

import Proyecto.Final.Escuela.Model.Carrera;

public class CarreraMapper {

    public static CarreraDTO toDTO(Carrera carrera) {
        return new CarreraDTO(
                carrera.getId(),
                carrera.getNombre(),
                carrera.getMaterias()
        );
    }

    public static Carrera toEntity(CarreraDTO dto) {
        Carrera carrera = new Carrera();
        carrera.setId(dto.getId());
        carrera.setNombre(dto.getNombre());
        carrera.setMaterias(dto.getMaterias());
        return carrera;
    }
}
