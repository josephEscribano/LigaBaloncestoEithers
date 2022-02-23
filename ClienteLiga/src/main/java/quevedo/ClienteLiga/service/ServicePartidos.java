package quevedo.ClienteLiga.service;

import io.vavr.control.Either;
import quevedo.ClienteLiga.dao.DAOPartidos;
import quevedo.common.modelos.Partido;

import javax.inject.Inject;
import java.util.List;

public class ServicePartidos {
    private final DAOPartidos daoPartidos;

    @Inject
    public ServicePartidos(DAOPartidos daoPartidos) {
        this.daoPartidos = daoPartidos;
    }

    public Either<String, List<Partido>> getAll() {
        return daoPartidos.getAll();
    }

    public Either<String, List<Partido>> filtroEquipos(String equipo) {
        return daoPartidos.filtroEquipos(equipo);
    }

    public Either<String, List<Partido>> filtrosJornadas(String jornada) {
        return daoPartidos.filtrosJornadas(jornada);
    }

    public Either<String, Partido> savePartido(Partido partido) {
        return daoPartidos.savePartido(partido);
    }

    public Either<String, Partido> updatePartido(Partido partido) {
        return daoPartidos.updatePartido(partido);
    }

    public Either<String, String> deletePartido(String id) {
        return daoPartidos.deletePartido(id);
    }
}
