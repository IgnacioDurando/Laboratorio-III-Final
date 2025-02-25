package Proyecto.Final.Escuela.Dtos;

import Proyecto.Final.Escuela.Model.Materia;

public class MateriaMapper {

    public static MateriaDTO toDTO(Materia materia) {
        return new MateriaDTO(
                materia.getId(),
                materia.getNombre(),
                materia.getAnio(),
                materia.getCuatrimestre(),
                materia.getProfesorId(),
                materia.getCorrelatividades()
        );
    }

    public static Materia toEntity(MateriaDTO dto) {
        Materia materia = new Materia();
        materia.setId(dto.getId());
        materia.setNombre(dto.getNombre());
        materia.setAnio(dto.getAnio());
        materia.setCuatrimestre(dto.getCuatrimestre());
        materia.setProfesorId(dto.getProfesorId());
        materia.setCorrelatividades(dto.getCorrelatividades());
        return materia;
    }
}
