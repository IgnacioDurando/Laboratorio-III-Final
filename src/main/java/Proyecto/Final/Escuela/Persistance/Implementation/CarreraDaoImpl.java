package Proyecto.Final.Escuela.Persistance.Implementation;

import Proyecto.Final.Escuela.Model.Carrera;
import Proyecto.Final.Escuela.Persistance.CarreraDao;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CarreraDaoImpl implements CarreraDao {
    private final List<Carrera> carreras = new ArrayList<>();

    @Override
    public List<Carrera> findAll() {
        return new ArrayList<>(carreras);
    }

    @Override
    public Optional<Carrera> findById(int id) {
        return carreras.stream().filter(c -> c.getId() == id).findFirst();
    }

    private int generateId() {
        return carreras.isEmpty() ? 1 : carreras.get(carreras.size() - 1).getId() + 1;
    }

    @Override
    public Carrera save(Carrera carrera) {
        if (carrera.getId() == 0) {
            carrera.setId(generateId());
        }
        carreras.add(carrera);
        return carrera;
    }

    @Override
    public Carrera updateCarrera(int id, Carrera carrera) {
        for (int i = 0; i < carreras.size(); i++) {
            if (carreras.get(i).getId() == id) {
                carreras.set(i, carrera);
                return carrera;
            }
        }
        return null;
    }

    @Override
    public boolean delete(int id) {

        return carreras.removeIf(c -> c.getId() == id);
    }
}
